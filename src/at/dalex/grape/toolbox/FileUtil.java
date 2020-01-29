package at.dalex.grape.toolbox;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {

	public static boolean isFile(File file) {
		return (file.exists() && !file.isDirectory());
	}

	public static String md5sum(InputStream input) throws IOException {
		BufferedInputStream in = new BufferedInputStream(input);
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			DigestInputStream digestInputStream = new DigestInputStream(in, digest);
			for (; digestInputStream.read() >= 0;) { }

			ByteArrayOutputStream md5out = new ByteArrayOutputStream();
			md5out.write(digest.digest());
			return md5out.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("MD5 algorithm is not available: " + e);
		} finally {
			in.close();
		}
	}
}
