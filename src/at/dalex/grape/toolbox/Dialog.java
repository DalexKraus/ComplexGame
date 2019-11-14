package at.dalex.grape.toolbox;

import javax.swing.JOptionPane;

public class Dialog {

	public static void error(String title, String message) {
		JOptionPane.showConfirmDialog(null, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}
}
