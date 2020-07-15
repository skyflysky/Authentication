package sky.tool.Authentication.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import jodd.util.StringUtil;

public class SignUtil
{
	private static final Long GAP = 10000l;
	
	private static final String SIGN_KEY= "sign" , TIME_KEY = "timestamp";
	
	private static Logger logger = Logger.getLogger(SignUtil.class);
	
	public static boolean checkSign(Map<String, String[]> params)
	{
		String sign = params.get(SIGN_KEY)[0] , timeStamp = params.get(TIME_KEY)[0];
		if(StringUtil.isAllBlank(timeStamp))
		{
			try
			{
				if(Math.abs(System.currentTimeMillis() - Long.valueOf(timeStamp)) > GAP)
				{
					return false;
				}
			}
			catch (NumberFormatException e)
			{
				return false;
			}
			
			if(StringUtil.isNotBlank(sign))
			{
				List<String> keyList = new LinkedList<String>(params.keySet());
				keyList.remove(SIGN_KEY);
				keyList.sort(String.CASE_INSENSITIVE_ORDER);
				String calSign = sign(params , keyList);
				if(calSign.equals(sign))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static String sign(Map<String , String> params)
	{
		List<String> keyList = new LinkedList<>(params.keySet());
		keyList.sort(String.CASE_INSENSITIVE_ORDER);
		Map<String, String[]> param = new TreeMap<>();
		for(String key : keyList)
		{
			String[] sArray = new String[1];
			sArray[0] = params.get(key);
			param.put(key, sArray);
		}
		return sign(param, keyList);
	}
	
	public static String sign(Map<String, String[]> params , List<String> keyList)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			for(String key : keyList)
			{
				md.update(key.getBytes(Charset.forName("UTF-8")));
				for(String value : params.get(key))
				{
					md.update(value.getBytes(Charset.forName("UTF-8")));
				}
			}
			return Hex.encodeHexString(md.digest());
		}
		catch (NoSuchAlgorithmException e)
		{
			logger.error(e);
		}
		return null;
	}
	
}
