package sky.tool.Authentication.service;

import sky.tool.Authentication.entity.abstractt.AbstractUser;

public interface UserService
{
	AbstractUser getOne(Integer id);
}
