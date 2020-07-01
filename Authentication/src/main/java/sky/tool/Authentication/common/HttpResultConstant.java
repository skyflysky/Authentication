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
	INVALID_PARAM(-3 , "invalid param");

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
