package sky.tool.Authentication.intercepter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 拦截器的配置层
 * @author sky
 */
@Configuration
public class IntercepterConfigration extends WebMvcConfigurationSupport 
{
	private static final String ALL = "/**";
	
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		registry.addInterceptor(new LogIntercepter()).addPathPatterns(ALL);
		super.addInterceptors(registry);
	}
}
