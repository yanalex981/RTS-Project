/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description: Loads native libraries based on operating system.
 */

import java.lang.reflect.Field;
import java.util.Arrays;

public class LibraryLoader 
{
	
	/**
	 * Finds out the operating system of the system and loads the appropriate natives.
	 * 
	 * @throws Exception: Exception pertaining to a failure to load files.
	 */
	public static void loadNativeLibraries()throws Exception
	{
		
		if(System.getProperty("os.name").equals("Mac OS X"))
		{
			addLibraryPath("natives/macosx");
		}
		else if(System.getProperty("os.name").equals("Linux"))
		{
			addLibraryPath("natives/linux");
		}
		else if(System.getProperty("os.name").split(" ")[0].equals("Windows"))
		{
			addLibraryPath("natives/windows");
			if(System.getProperty("os.arch").equals("amd64") || System.getProperty("os.arch").equals("x86_64")){
				System.loadLibrary("OpenAL64");
			}
			else
			{
				System.loadLibrary("OpenAL32");
			}
		}
	}
	
	/**
	 * Adds a library to the path based on the given string.
	 * 
	 * @param s: The string that identifies what library path to load.
	 * @throws Exception: Exception pertaining to failure to find files.
	 */
	private static void addLibraryPath(String s)throws Exception
	{
		final Field usr_paths_field = ClassLoader.class.getDeclaredField("usr_paths");
		usr_paths_field.setAccessible(true);
		
		final String[] paths = (String[]) usr_paths_field.get(null);
		for(String path : paths)
		{
			if(path.equals(s))
			{
				return;
			}
		}
		final String[] new_paths = Arrays.copyOf(paths, paths.length + 1);
		new_paths[paths.length - 1] = s;
		usr_paths_field.set(null, new_paths);
	}
}
