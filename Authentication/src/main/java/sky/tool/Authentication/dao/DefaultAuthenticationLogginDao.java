package sky.tool.Authentication.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sky.tool.Authentication.common.DefualtAuthenticationLogginStatus;
import sky.tool.Authentication.entity.AuthenticationUser;
import sky.tool.Authentication.entity.DefaultAuthenticationLoggin;
import sky.tool.Authentication.entity.abstractt.AbstractLoggin;

public interface DefaultAuthenticationLogginDao extends JpaRepository<DefaultAuthenticationLoggin, Long> , JpaSpecificationExecutor<DefaultAuthenticationLoggin>
{
	@Modifying
	@Query(value = "UPDATE DefaultAuthenticationLoggin SET status=:status WHERE user=:user")
	int batchUpdateStatusByUser(@Param(value = "status") DefualtAuthenticationLogginStatus stauts , @Param(value = "user") AuthenticationUser user);
	
	int countByAccessAccount(String accessAccount);
	
	DefaultAuthenticationLoggin getByAccessAccount(String accessAccount);
	
	List<AbstractLoggin> findByStatusAndUser(DefualtAuthenticationLogginStatus status , AuthenticationUser user);
}
