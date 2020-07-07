package sky.tool.Authentication.entity;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sky.tool.Authentication.common.DefualtAuthenticationLogginStatus;
import sky.tool.Authentication.entity.abstractt.AbstractLoggin;
import sky.tool.Authentication.entity.abstractt.AbstractUser;

@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name="authenticaion_loggin")
public class DefaultAuthenticationLoggin extends AbstractLoggin
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	DefualtAuthenticationLogginStatus status;
	
	@ManyToOne(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	AuthenticationUser user;
	
	public DefaultAuthenticationLoggin(String accessAccount , String secret , String clearPassword , AbstractUser user)
	{
		super();
		this.user = (AuthenticationUser) user;
		this.status = DefualtAuthenticationLogginStatus.ACTIVE;
		this.accessAccount = accessAccount;
		this.clearPassword = clearPassword;
		this.generateTime = Calendar.getInstance();
		this.secret = secret;
	}
}
