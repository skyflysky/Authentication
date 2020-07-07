package sky.tool.Authentication.common;

public enum HttpResultConstant
{

	/**
	 * 成功
	 */
	SUCCESS(0, "success"), 
	
	/**
	 * 失败
	 */
	FAIL(-1, "fail"),
	
	/**
	 *  系统失效
	 */
	SYSTEM_ERROR(-2 , "system error"),
	
	/**
	 * 非法入参
	 */
	INVALID_PARAM(-3 , "invalid param"),
	
	/**
	 * 无此实体
	 */
	UNREACHABLE_ENTITY(-4 , "unreachable_entity"),
	
	/**
	 * 校验失败
	 */
	VERIFICATION_FAIL(-5 , "verification fail");

	private int code;
	private String message;

	public int getCode()
	{
		return code;
	}

	public String getMessage()
	{
		return message;
	}

	HttpResultConstant(int code, String message)
	{
		this.code = code;
		this.message = message;
	}
}
