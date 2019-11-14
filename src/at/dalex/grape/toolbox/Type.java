package at.dalex.grape.toolbox;

public class Type {
	
	/**
	 * Create a new <code>Integer</code> using a <code>String</code> as source.
	 * @param str Source String
	 * @return Created Integer from String
	 */
	public static int parseInt(String str) {
		try {
			return Integer.parseInt(str);
		}
		catch (NumberFormatException ex) {
			System.err.println("[TypeUtils] " + str + " is not a valid Number!");
			ex.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Create a new <code>Float</code> using a <code>String</code> as source.
	 * @param str Source String
	 * @return Created Float from String
	 */
	public static float parseFloat(String str) {
		try {
			return Float.parseFloat(str);
		}
		catch (NumberFormatException ex) {
			System.err.println("[TypeUtils] " + str + " is not a valid Number!");
			ex.printStackTrace();
		}
		return 0f;
	}
}
