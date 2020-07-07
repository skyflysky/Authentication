package sky.tool.Authentication.common;

public enum DefualtAuthenticationLogginStatus
{
	CREATED("已创建"),
	ACTIVE("激活"),
	DEACTIVE("停用"),
	WRONG("错误");
	
	private String discrption;
	
	private DefualtAuthenticationLogginStatus (String discrption)
	{
		this.discrption = discrption;
	}
	
	public String getDiscrption()
	{
		return this.discrption;
	}
}
