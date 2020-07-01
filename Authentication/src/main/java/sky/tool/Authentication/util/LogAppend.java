package sky.tool.Authentication.util;

public class LogAppend
{
	public static String appender(String... strings)
	{
		StringBuilder sb = new StringBuilder();
		for(String s : strings)
		{
			sb.append(s);
		}
		return sb.toString();
	}
}
