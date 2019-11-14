package at.dalex.grape.resource;

import at.dalex.grape.GrapeEngine;

import javax.swing.*;
import java.io.*;

public class FileContentReader {

    /**
     * Reads the content of a file  and returns it as a string.
     * @param file The file which should be read
     * @return The file's content
     */
    public static String readFile(String file) {
        //Create Reader and Buffer
        InputStreamReader inputStreamReader = null;

        //Add game's location as prefix
        file = GrapeEngine.getEngine().getGameLocation() + "/" + file;

        //Try to open file
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(new File(file)));
        } catch (FileNotFoundException e) {
            //Could not locate file
            System.err.println("[FATAL] Could not locate file '" + file + "'!");
            e.printStackTrace();

            JOptionPane.showConfirmDialog(null, "Could not locate file '" + file + "'!\nThe Program will now terminate.",
                    "Fatal Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

            System.exit(-1);
        }

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();

        //Try to read the file
        try {
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                //Append the read line to the buffer
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            //Close the reader
            bufferedReader.close();
        } catch(IOException e) {
            //Could not read file
            System.err.println("[FATAL] Could not read file '" + file + "'!");
            e.printStackTrace();

            JOptionPane.showConfirmDialog(null, "Could not read file '" + file + "'!\nThe Program will now terminate.",
                    "Fatal Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

            System.exit(-1);
        }
        //Return the buffer's content
        return sb.toString();
    }
}
