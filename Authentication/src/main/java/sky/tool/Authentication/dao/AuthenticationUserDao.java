package sky.tool.Authentication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sky.tool.Authentication.entity.AuthenticationUser;

public interface AuthenticationUserDao extends JpaRepository<AuthenticationUser, Integer> , JpaSpecificationExecutor<AuthenticationUser>
{

}
