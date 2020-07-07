package sky.tool.Authentication.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.transaction.Transactional;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.stereotype.Service;

import sky.tool.Authentication.common.DefualtAuthenticationLogginStatus;
import sky.tool.Authentication.config.RedisUtils;
import sky.tool.Authentication.dao.DefaultAuthenticationLogginDao;
import sky.tool.Authentication.entity.AuthenticationUser;
import sky.tool.Authentication.entity.DefaultAuthenticationLoggin;
import sky.tool.Authentication.entity.abstractt.AbstractLoggin;
import sky.tool.Authentication.entity.abstractt.AbstractUser;
import sky.tool.Authentication.entity.redis.LogginAccountRedis;
import sky.tool.Authentication.entity.redis.LogginTokenReids;
import sky.tool.Authentication.service.LogginService;

@Service("defaultAuthLoggin")
public class DefaultAuthenticationLogginServiceImpl implements LogginService
{
	/**
	 * 登录验证的时间延迟，只要是account对了，就会启用
	 */
	@Value("${auth.loggin.delay}")
	private Long logginDelay;
	
	/**
	 * token有效期
	 */
	@Value("${auth.token.delay}")
	private Long tokenDelay;
	
	/**
	 * 被除数， 选一个小质数来担任
	 */
	@Value("${auth.dividend}")
	private String dividend; 
	
	/**
	 * 用户账号前缀
	 */
	@Value("${auth.accountPrefix}")
	private String preFix;
	
	/**
	 * 固定大数盐
	 */
	@Value("${auth.salt}")
	private String salt;
	
	/**
	 * 核心变换字典，58进制，所以字典58位，代表从0-57。
	 */
	@Value("${auth.dictionary}")
	private String dictionary;
	
	/**
	 * 异或用的数，可变
	 */
	@Value("${auth.xor}")
	public Integer xor;

	/**
	 * 加减用的数，可变
	 */
	@Value("${auth.add}")
	public Long add;
	
	/**
	 * 核心变换数组
	 */
	@Value("${auth.changeArray}")
	public Integer[] changeArray;
	
	/**
	 * 随机数的基础
	 */
	@Value("${auth.base}")
	public String base;
	
	/**
	 * 随机数的取值范围
	 */
	@Value("${auth.random}")
	public Integer randomInt;
	
	/**
	 * 密码字节长度
	 */
	@Value("${auth.password.size}")
	public Integer passwordSize;

	/**
	 * 生成一个授权用户对象。
	 * 对象的account由前缀+随机大合数的Base加密构成。
	 * 对象的password由一个随机字节数组
	 * 对象的secret是password用固定大数加盐SHA256一次再与随机大合数加盐SHA256一次存在数据库
	 */
	private Logger logger = Logger.getLogger(DefaultAuthenticationLogginServiceImpl.class);
	
	@Autowired
	private DefaultAuthenticationLogginDao logginDao;
	
	@Autowired
	private RedisUtils redisUtil;
	
	@Override
	@Transactional(rollbackOn = {RuntimeException.class})
	public AbstractLoggin generateLoggin(AbstractUser user)
	{
		if(user instanceof AuthenticationUser)
		{
			AuthenticationUser actuser = (AuthenticationUser) user;
			disableToken(logginDao.findByStatusAndUser(DefualtAuthenticationLogginStatus.ACTIVE, actuser));
			logginDao.batchUpdateStatusByUser(DefualtAuthenticationLogginStatus.DEACTIVE, actuser);
			Random random = new Random();
			
			BigInteger bigNum;
			String account;
			do
			{
				bigNum = new BigInteger(base).add(new BigInteger(String.valueOf(random.nextInt(randomInt)))).multiply(new BigInteger(dividend));
				account = encode(bigNum);
			}
			while (logginDao.countByAccessAccount(account) != 0);
			
			byte[] rowPassword = new byte[passwordSize];
			random.nextBytes(rowPassword);
			String password = Hex.encodeHexString(rowPassword);
			String secret = getSecret(rowPassword , bigNum.toByteArray());
			DefaultAuthenticationLoggin loggin = logginDao.save(new DefaultAuthenticationLoggin(account, secret, password , actuser));
			return loggin;
		}
		else
		{
			throw new ClassCastException("generate loggin error : wrong generator");
		}
	}
	
	@Override
	public void disableToken(List<AbstractLoggin> loggins)
	{
		for(AbstractLoggin loggin : loggins)
		{
			String oldkey = LogginTokenReids.keyGenerate(loggin.getToken());
			redisUtil.remove(oldkey);
		}
	}

	private String getSecret(byte[] rowPassword, byte[] bigNumByte)
	{
		try
		{
			MessageDigest md1 = MessageDigest.getInstance("SHA-256") , md2 = MessageDigest.getInstance("SHA-256");
			byte[] first = md1.digest(ByteUtils.concat(rowPassword, Hex.decodeHex(salt)));
			byte[] result = md2.digest(ByteUtils.concat(first, bigNumByte));
			return Hex.encodeHexString(result);
		}
		catch (NoSuchAlgorithmException e)
		{
			logger.error(e);
			return null;
		}
		catch (DecoderException e)
		{
			logger.error(e);
			return null;
		}
	}

	@Override
	public boolean isLegal(String account)
	{
		if(account.startsWith(preFix))
		{
			BigInteger bigNum = null;
			try
			{
				bigNum = decode(account);
			}
			catch (IllegalArgumentException e)
			{
				logger.warn(e);
				return false;
			}
			int i = bigNum.mod(new BigInteger(dividend)).intValue();
			String redisKey = LogginAccountRedis.keyGenerate(account);
			if(i == 0 && (!redisUtil.exists(redisKey)))
			{
				redisUtil.set(redisKey, new LogginAccountRedis(System.currentTimeMillis(), account), logginDelay);
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional(rollbackOn = {RuntimeException.class})
	public AbstractLoggin authLoggin(String account, String password)
	{
		try
		{
			DefaultAuthenticationLoggin loggin = logginDao.getByAccessAccount(account);
			String secret = getSecret(Hex.decodeHex(password) , decode(account).toByteArray());
			if(secret.equals(loggin.getSecret()))
			{
				String newToken = generateToken(loggin);
				String oldKey = LogginTokenReids.keyGenerate(loggin.getToken());
				loggin.setTokenTime(Calendar.getInstance());
				loggin.setToken(newToken);
				logginDao.save(loggin);
				redisUtil.remove(oldKey);
				redisUtil.set(LogginTokenReids.keyGenerate(newToken), new LogginTokenReids(System.currentTimeMillis(), loggin.getId()), tokenDelay);
			}
			return loggin;
		}
		catch (IllegalArgumentException | DecoderException e)
		{
			logger.warn(password,e);
			return null;
		}
	}
	
	/**
	 * 生成token
	 * @param loggin
	 * @return
	 */
	@Override 
	public String generateToken(AbstractLoggin loggin)
	{
		byte[] buffer = new byte[24];
		new Random().nextBytes(buffer);
		return Hex.encodeHexString(buffer);
	}

	/**
	 * 建立字典中字符与字典值之间的对应关系。
	 * @return
	 */
	public Map<Character, Integer> getWorkingMap()
	{
		char[] cArray = dictionary.toCharArray();
		Map<Character, Integer> map = new HashMap<>(dictionary.length());
		for(int i = 0 ; i < dictionary.length() ; i++)
		{
			map.put(cArray[i], i);
		}
		return map;
	}
	
	/**
	 * 获取整形数组中元素的最大/最小值
	 * @param array 整形数组
	 * @param max true获取最大值， false获取最小值
	 * @return 最大、最小值
	 */
	public int getExtremum(Integer[] array , boolean max)
	{
		int k = (max ? Integer.MIN_VALUE : Integer.MAX_VALUE);
		for(int l : array)
		{
			k = (max ? Integer.max(k, l) : Integer.min(k, l));
		}
		return k;
	}
	
	/**
	 * 解码
	 */
	public BigInteger decode(String str) throws IllegalArgumentException
	{
		BigInteger b = new BigInteger("0");
		char[] intputArray = str.toCharArray();
		for(int i = 0 ; i < changeArray.length ; i ++)
		{
			Integer input = getWorkingMap().get(intputArray[changeArray[i]]);
			if(input != null)
			{
				BigDecimal b1 = new BigDecimal(input);
				BigDecimal b2 = new BigDecimal(dictionary.length()).pow(i);
				b = b.add(b1.multiply(b2).toBigInteger());
			}
			else
			{
				throw new IllegalArgumentException(intputArray[changeArray[i]] + " is unreachable char");
			}
		}
		return b.subtract(new BigInteger(String.valueOf(add))).xor(new BigInteger(String.valueOf(xor)));
	}
	
	public String encode(BigInteger num)
	{
		BigDecimal b = new BigDecimal(num.xor(new BigInteger(String.valueOf(xor))).toString()).add(new BigDecimal(add));
		char[] resultArray = new char[changeArray.length];
		char[] dictionaryArray = dictionary.toCharArray();
		for(int i = 0 ;  i < changeArray.length ;  i++)
		{
			/*
			 * 在生成的过程中，不需要考虑前缀，所以把重排数组中，前缀占用空间减去。要求：
			 * assert getExtremum(changeArray, false) == preFix.length();
			 */
			resultArray[changeArray[i] - getExtremum(changeArray, false)] = 
					dictionaryArray[
					                //(b / (进制的i次幂))对进制取模
					                b.divide(new BigDecimal(Math.pow(dictionary.length(), i)) , RoundingMode.DOWN)
					                .remainder(new BigDecimal(dictionary.length()))
					                .intValue()];
		}
		//将前缀加回。
		StringBuilder sb = new StringBuilder(preFix);
		for(char i : resultArray)
		{
			//拼接字符串。
			sb.append(i);
		}
		return sb.toString();
	}
}
