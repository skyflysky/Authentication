package sky.tool.Authentication.task;

import org.apache.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultTask implements ApplicationRunner
{
	private Logger logger = Logger.getLogger(DefaultTask.class);
	
	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		logger.info("startup complete");
	}
}
