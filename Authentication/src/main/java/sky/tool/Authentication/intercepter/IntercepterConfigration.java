package sky.tool.Authentication.intercepter;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 拦截器的配置层
 */
@Configuration
public class IntercepterConfigration extends WebMvcConfigurationSupport 
{
	@Value("${auth.intercepter.path.all}")
	private String[] allPath; 
	
	@Value("${auth.intercepter.path.auth}")
	private String[] authPath;
	
	@Value("${auth.intercepter.path.sign}")
	private String[] signPath;
	
	@Value("${auth.intercepter.path.token}")
	private String[] tokenPath;
	
	@Resource(name = "AuthIntercepter")
	HandlerInterceptor auth;
	
	@Resource(name = "LogIntercepter")
	HandlerInterceptor log;
	
	@Resource(name = "SignIntercepter")
	HandlerInterceptor sign;
	
	@Resource(name = "TokenIntercepter")
	HandlerInterceptor token;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		registry.addInterceptor(log).addPathPatterns(allPath);
		registry.addInterceptor(sign).addPathPatterns(signPath);
		registry.addInterceptor(token).addPathPatterns(tokenPath);
		registry.addInterceptor(auth).addPathPatterns(authPath);
		super.addInterceptors(registry);
	}
}
