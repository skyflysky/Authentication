package sky.tool.Authentication.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jodd.util.StringUtil;
import sky.tool.Authentication.config.RedisUtils;

@Component("TokenIntercepter")
public class TokenIntercepter implements HandlerInterceptor
{
	@Autowired
	private RedisUtils redisUtil;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		String token = request.getParameter("token");
		if(StringUtil.isNotBlank(token))
		{
			Object rawData = redisUtil.get(token);
			if(rawData != null) 
			{
				request.setAttribute("tokenEntity", rawData);
				return true;
			}
		}
		response.sendError(403, "token access blocked");
		return false;
	}
}
