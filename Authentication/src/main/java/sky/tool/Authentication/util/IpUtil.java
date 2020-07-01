package sky.tool.Authentication.util;

import javax.servlet.http.HttpServletRequest;

/**
 * ip工具类
 */
public class IpUtil
{
	/**
	 * 返回请求的来源Ip地址
	 * @param request 请求类
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request)
	{
		String unknown = "unknown";
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip))
		{
			ip = request.getHeader("CLIENTIP");
		}
		if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip))
		{
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
