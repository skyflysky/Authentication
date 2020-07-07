
package sky.tool.Authentication.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * redis工具类
 **/
@Component
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RedisUtils
{
	private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

	@Autowired
	RedisTemplate redisTemplate;

	/**
	 * 批量删除对应的value
	 *
	 * @param keys
	 */
	public void remove(final String... keys)
	{
		for (String key : keys)
		{
			remove(key);
		}
	}

	/**
	 * 批量删除key
	 *
	 * @param pattern
	 */
	public void removePattern(final String pattern)
	{
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
		{
			redisTemplate.delete(keys);
		}
	}

	/**
	 * 删除对应的value
	 *
	 * @param key
	 */
	public void remove(final String key)
	{
		if (exists(key))
		{
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 *
	 * @param key
	 * @return
	 */
	public boolean exists(final String key)
	{
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 *
	 * @param key
	 * @return
	 */
	public Object get(final String key)
	{
		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
		result = operations.get(key);
		return result;
	}

	/**
	 * 写入缓存
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value)
	{
		boolean result = false;
		try
		{
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		}
		catch (Exception e)
		{
			logger.error("set(String, Object)", e);
		}
		return result;
	}

	/**
	 * 写入缓存
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime)
	{
		boolean result = false;
		try
		{
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
			result = true;
		}
		catch (Exception e)
		{
			logger.error("set(String, Object, Long)", e);
		}
		return result;
	}

	/**
	 * 自定义 redis scan 操作
	 */
	private Cursor<String> scan(RedisTemplate redisTemplate, String pattern, Long limit)
	{
		ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
		RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
		return (Cursor) redisTemplate.executeWithStickyConnection(new RedisCallback()
		{
			@Override
			public Object doInRedis(RedisConnection redisConnection) throws org.springframework.dao.DataAccessException
			{
				return new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize);
			}
		});
	}

	/**
	 * 组装 scan 的结果集
	 */
	public List<String> assembleScanKeys(String pattern, Long limit)
	{
		HashSet<String> set = new HashSet<>();
		Cursor<String> cursor = scan(redisTemplate, pattern, limit);
		while (cursor.hasNext())
		{
			set.add(cursor.next());
		}
		try
		{
			cursor.close();
		}
		catch (Exception e)
		{
			logger.error("关闭 redis connection 失败");
		}
		return new ArrayList<>(set);
	}
}
