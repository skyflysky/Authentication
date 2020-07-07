package sky.tool.Authentication.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogginAccountRedis
{
	private static final String KEY_PREFIX = "loggin:account:";
	
	Long timestamp;
	
	String account;
	
	public static String keyGenerate(String account)
	{
		return KEY_PREFIX.concat(account);
	}
}
