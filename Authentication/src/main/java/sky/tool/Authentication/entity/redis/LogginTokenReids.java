package sky.tool.Authentication.entity.redis;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogginTokenReids
{
	private static final String KEY_PREFIX = "loggin:token:";
	
	private Long timestamp;
	
	private Long LogginId;
	
	public static String keyGenerate(String token)
	{
		if(token == null)
		{
			token = "";
		}
		return KEY_PREFIX.concat(token);
	}
}
