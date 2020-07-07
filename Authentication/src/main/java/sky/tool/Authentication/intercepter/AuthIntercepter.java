package sky.tool.Authentication.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import sky.tool.Authentication.service.LogginService;


@Configuration("AuthIntercepter")
public class AuthIntercepter implements HandlerInterceptor
{
	private Logger logger = Logger.getLogger(AuthIntercepter.class);
	
	@Autowired
	LogginService logginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		String account = request.getParameter("account");
		if(account != null && logginService.isLegal(account))
		{
			return true;
		}
		logger.info("verification blocked");
		response.sendError(403, "account access blocked");
		return false;
	}
	
}
