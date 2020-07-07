
package sky.tool.Authentication.util;

import java.lang.reflect.Method;

/**
 * Bean工具
 * 
 */
public class DtoUtil
{

	/**
	 * 根据属性名，拷贝bean的属性
	 * 
	 * @param dest
	 *            目标对象
	 * @param orig
	 *            源对象
	 * @param ignoreNull
	 *            是否忽略值为null的属性
	 * @param ignoreProperties
	 *            需要忽略的属性
	 */
	public static boolean copyBean(Object dest, final Object orig, boolean ignoreNull, final String... ignoreProperties)
	{
		StringBuffer ignore = new StringBuffer("");
		if (ignoreProperties != null)
		{
			for (String s : ignoreProperties)
			{
				ignore.append(s).append(",");
			}
		}

		if (orig == null || dest == null)
		{
			dest = null;
		}
		else
		{
			Class<?> origClass = orig.getClass();
			Class<?> destClass = dest.getClass();
			Method[] destMethods = destClass.getMethods();
			String destMethodName , fieldName;
			Object fieldValue;
			Method origMethod;
			for (Method destMethod : destMethods)
			{
				destMethodName = destMethod.getName();
				if (destMethodName.matches("^set\\w+"))
				{
					fieldName = destMethodName.replaceAll("^set", "");
					if (ignore.toString().contains(fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1)))
						continue;
					try
					{
						origMethod = origClass.getMethod("get" + fieldName);
					}
					catch (NoSuchMethodException e)
					{
						try
						{
							origMethod = origClass.getMethod("is" + fieldName);
						}
						catch (Exception e1)
						{
							origMethod = null;
						}
					}
					if (origMethod == null)
					{
						// logger.debug("源对象没有属性" + fieldName +
						// "的获取方法,将忽略此属性的拷贝");
					}
					else
					{
						try
						{
							fieldValue = origMethod.invoke(orig);
							if ((!ignoreNull || fieldValue != null)
									&& destMethod.getParameterTypes()[0] == origMethod.getReturnType())
							{
								destMethod.invoke(dest, fieldValue);
							}
						}
						catch (Exception e)
						{
							return false;
						}
					}
				}
			}

		}
		return true;
	}

	/**
	 * 将旧实体的属性拷贝到新实体的属性，属性名需相同
	 * 
	 * @param dest
	 *            新实体
	 * @param orig
	 *            旧实体
	 * @param ignoreNull
	 *            是否忽略旧实体值为null的属性
	 */
	public static void copyBean(Object dest, final Object orig, boolean ignoreNull)
	{
		copyBean(dest, orig, ignoreNull, "");
	}

}
