package sky.tool.Authentication.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import sky.tool.Authentication.util.SignUtil;

@Component("SignIntercepter")
public class SignIntercepter implements HandlerInterceptor
{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		if(SignUtil.checkSign(request.getParameterMap()))
		{
			return true;
		}
		response.sendError(403, "sign access blocked");
		return false;
	}
}
