package at.dalex.grape.toolbox;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontUtil {

    public static Font loadFrontFromFile(File fontFile, int fontSize) {
        Font loadedFont = null;
        try {
            loadedFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            loadedFont = loadedFont.deriveFont((float) fontSize);
        } catch (FontFormatException | IOException e) {
            System.err.println("[FontUtil] Unable to load font from ttf file.");
            e.printStackTrace();
        }

        return loadedFont;
    }
}
