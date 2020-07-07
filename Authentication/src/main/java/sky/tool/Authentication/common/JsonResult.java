
package sky.tool.Authentication.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * json结果返回封装类
 **/
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult implements Serializable
{

	private static final long serialVersionUID = -29181603214347990L;

	private int code;
	
	private String msg;
	
	private Object response;

	public static JsonResult success(Object response)
	{
		return JsonResult.builder().code(HttpResultConstant.SUCCESS.getCode()).msg(HttpResultConstant.SUCCESS.getMessage()).response(response).build();
	}

	public static JsonResult success()
	{
		return JsonResult.builder().code(HttpResultConstant.SUCCESS.getCode()).msg(HttpResultConstant.SUCCESS.getMessage()).build();
	}
	
	public static JsonResult build(HttpResultConstant constant)
	{
		return JsonResult.builder().code(constant.getCode()).msg(constant.getMessage()).build();
	}

	public static JsonResult build(int code, String msg)
	{
		return JsonResult.builder().code(code).msg(msg).build();
	}

	public static JsonResult build(int code, String msg, Object response)
	{
		return JsonResult.builder().code(code).msg(msg).response(response).build();
	}

}
