package at.dalex.grape.toolbox;

import java.io.File;

public class FileUtil {

	public static boolean isFile(File file) {
		return (file.exists() && !file.isDirectory());
	}
}
