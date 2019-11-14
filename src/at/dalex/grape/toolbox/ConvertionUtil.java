package at.dalex.grape.toolbox;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class was written by dalex on 13.09.2017.
 * You are not permitted to edit this file.
 *
 * @author dalex
 */

public class ConvertionUtil {

    public static String convertInputStreamToString(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line + "\n");
            }
            return out.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static float[] normalizeColors(Color color) {
        return normalizeColors(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static float[] normalizeColors(int r, int g, int b, int a) {
        float[] color = new float[4];
        color[0] = (r / 255);
        color[1] = (g / 255);
        color[2] = (b / 255);
        color[3] = (a / 255);
        return color;
    }
}
