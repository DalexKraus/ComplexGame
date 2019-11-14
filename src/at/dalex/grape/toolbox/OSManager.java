package at.dalex.grape.toolbox;

import java.awt.Toolkit;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * OsManager can be used to set certain settings if the engine is running on certain operating systems.
 * @author dalex
 *
 */
public class OSManager {

	/**
	 * Sets the default <code>Swing Look-And-Feel</code> based on the current operating system.
	 */
	public static void setLook() {
		if (runningOnWindows()) {
			setWindowsLook();
		}
	}
	
	/**
	 * Check if the Engine is running on an windows operating system
	 * @return True, if the Engine does run on windows
	 */
	public static boolean runningOnWindows() {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			return true;
		}
		return false;
	}
	
	private static void setWindowsLook() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Plays the default error sound, depending on the os.
	 */
	public static void playErrorSound() {
		if (runningOnWindows()) {
			final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.hand");
			if (runnable != null) runnable.run();
		}
	}

	/**
	 * Plays the default exclamation sound, depending on the os.
	 */
	public static void playExclamationSound() {
		if (runningOnWindows()) {
			final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
			if (runnable != null) runnable.run();
		}
	}
}
