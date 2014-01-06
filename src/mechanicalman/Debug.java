package mechanicalman;

public class Debug
{
	private static boolean debugOn = false;
	
	public static void turnOn()
	{
		debugOn = true;
	}

	public static void turnOff()
	{
		debugOn = false;
	}
	
	public static boolean isDebugOn()
	{
		return debugOn;
	}
		
	public static void println(String message)
	{
		if (debugOn)
			System.out.println(message);
	}

	public static void print(String message)
	{
		if (debugOn)
			System.out.print(message);
	}
}