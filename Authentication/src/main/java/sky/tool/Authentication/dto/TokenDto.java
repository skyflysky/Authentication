package sky.tool.Authentication.dto;

import lombok.Data;

@Data
public class TokenDto
{
	private String token;
	
	private Long expirationTimeStamp;

	public TokenDto(String token , Long expirationTimeStamp)
	{
		super();
		this.token = token;
		this.expirationTimeStamp = System.currentTimeMillis() + expirationTimeStamp;
	}
}
