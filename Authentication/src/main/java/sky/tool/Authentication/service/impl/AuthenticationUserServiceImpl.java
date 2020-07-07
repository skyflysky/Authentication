package sky.tool.Authentication.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sky.tool.Authentication.dao.AuthenticationUserDao;
import sky.tool.Authentication.entity.abstractt.AbstractUser;
import sky.tool.Authentication.service.UserService;

@Service("authUser")
public class AuthenticationUserServiceImpl implements UserService
{
	
	@Autowired
	AuthenticationUserDao userDao;

	@Override
	public AbstractUser getOne(Integer id)
	{
		return userDao.getOne(id);
	}

}
