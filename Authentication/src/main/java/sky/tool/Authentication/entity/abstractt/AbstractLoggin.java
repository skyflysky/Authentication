package sky.tool.Authentication.entity.abstractt;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import lombok.Data;

@Data
@MappedSuperclass
public class AbstractLoggin
{
	@Column(nullable = false)
	protected String accessAccount;
	
	@Column(nullable = false)
	protected String secret;
	
	@Column(nullable = false)
	protected Calendar generateTime;
	
	@Transient
	protected String clearPassword;
	
	@Column(nullable = true)
	protected String token;
	
	@Column(nullable = true)
	protected Calendar tokenTime;
}
