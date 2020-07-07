package sky.tool.Authentication.intercepter;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSONObject;

import sky.tool.Authentication.util.IpUtil;
import sky.tool.Authentication.util.LogAppend;

/**
 * 所有请求的log 记录层
 */
@Configuration("LogIntercepter")
public class LogIntercepter	implements HandlerInterceptor
{
	private Logger logger = Logger.getLogger(getClass());
	
	private static final String QUERY_STRING = "QueryString" ,
								REMOTE_HOST = "RemoteHost",
								LOG_STRING1 = "控制层请求日志 请求接口:'",
								LOG_STRING2 = "' 内容:'",
								LOG_STRING3 = "'";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception
	{
		logRequestLog(request);
		return true;
	}
	
	private void logRequestLog(HttpServletRequest request)
	{
		Map<String, String[]> origeinalMap = request.getParameterMap();
		Map<String, Object> targetMap = new LinkedHashMap<>();
		for (String key : origeinalMap.keySet())
		{
			String[] array = origeinalMap.get(key);
			if(array.length > 1)
			{
				targetMap.put(key, origeinalMap.get(key));
			}
			else
			{
				targetMap.put(key, array[0]);
			}
		}
		JSONObject ob = new JSONObject(targetMap);
		ob.put(QUERY_STRING, request.getQueryString());
		ob.put(REMOTE_HOST, IpUtil.getIpAddr(request));
		logger.info(LogAppend.appender(LOG_STRING1 , request.getServletPath() , LOG_STRING2 , ob.toJSONString() , LOG_STRING3));
	}
}
