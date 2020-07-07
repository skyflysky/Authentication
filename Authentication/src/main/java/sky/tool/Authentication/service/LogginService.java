package sky.tool.Authentication.service;

import java.util.List;

import sky.tool.Authentication.entity.abstractt.AbstractLoggin;
import sky.tool.Authentication.entity.abstractt.AbstractUser;

public interface LogginService
{
	AbstractLoggin generateLoggin(AbstractUser user);
	
	String generateToken(AbstractLoggin loggin);
	
	void disableToken(List<AbstractLoggin> loggins);
	
	boolean isLegal(String account);
	
	AbstractLoggin authLoggin(String account , String password);
}
