package sky.tool.Authentication.intercepter;

import javax.annotation.Resource;

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
	private static final String ALL = "/**",
								AUTH = "/loggin/token";
	
	@Resource(name = "AuthIntercepter")
	HandlerInterceptor auth;
	
	@Resource(name = "LogIntercepter")
	HandlerInterceptor log;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		registry.addInterceptor(log).addPathPatterns(ALL);
		registry.addInterceptor(auth).addPathPatterns(AUTH);
		super.addInterceptors(registry);
	}
}
