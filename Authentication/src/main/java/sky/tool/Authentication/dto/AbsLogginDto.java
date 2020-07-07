package sky.tool.Authentication.dto;

import java.io.Serializable;

import lombok.Data;
import sky.tool.Authentication.entity.abstractt.AbstractLoggin;
import sky.tool.Authentication.util.DtoUtil;

@Data
public class AbsLogginDto implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4209958433239853947L;
	
	private String accessAccount;
	
	private String clearPassword;
	
	public static AbsLogginDto fromEntity(AbstractLoggin entity)
	{
		AbsLogginDto dto = new AbsLogginDto();
		DtoUtil.copyBean(dto, entity, false);
		return dto;
	}
	
}
