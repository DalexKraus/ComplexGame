package at.dalex.grape.graphics.font;

import at.dalex.grape.graphics.BatchRenderer;
import at.dalex.grape.graphics.graphicsutil.Graphics;
import at.dalex.grape.graphics.graphicsutil.Image;
import at.dalex.grape.graphics.graphicsutil.ImageUtils;
import org.joml.Matrix4f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;

public class BitmapFont {

    private final Map<Character, Glyph> glyphs;
    private Image atlasImage;
    private int fontHeight;
    private BatchRenderer glyphRenderer;

    public BitmapFont(int size, boolean antiAlias) {
        this(new Font(MONOSPACED, PLAIN, size), antiAlias);
    }

    public BitmapFont(Font font, boolean antiAlias) {
        glyphs = new HashMap<>();
        atlasImage = createFontTexture(font, antiAlias);
        glyphRenderer = new BatchRenderer(atlasImage);
    }

    private Image createFontTexture(Font font, boolean antiAlias) {
        fontHeight = font.getSize();

        BufferedImage image = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        int x = 0;
        int y = 0;
        for (int i = 32; i < 256; i++) {

            char character = (char) i;
            BufferedImage charImage = createCharImage(font, character, antiAlias);
            //Unable to create char image, continue
            if (charImage == null) continue;

            int charWidth = charImage.getWidth();
            int charHeight = charImage.getHeight();

            //Create glyph and draw char on image
            Glyph glyph = new Glyph(x, y, charWidth, charHeight, 0f);
            glyphs.put(character, glyph);

            g.drawImage(charImage, x, y, null);

            x += glyph.width;
            if (x + glyph.width >= 1024) {
                x = 0;
                y += charHeight;
            }
        }

        return ImageUtils.convertBufferedImage(image);
    }

    //Works
    private BufferedImage createCharImage(Font font, char c, boolean antiAlias) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.dispose();

        int charWidth = metrics.charWidth(c);
        int charHeight = metrics.getHeight();

        if (charWidth == 0) return null;

        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setFont(font);
        g.setPaint(Color.WHITE);
        g.drawString(String.valueOf(c), 0, metrics.getAscent());
        g.dispose();
        return image;
    }

    public void drawText(String text, int x, int y, Matrix4f projectionAndViewMatrix) {

        for (int i = 0; i < text.length(); i++) {
            Glyph glyph = glyphs.get(text.charAt(i));
            float normalizedWidth = 1.0f / atlasImage.getWidth();
            float normalizedHeight = 1.0f / atlasImage.getHeight();

            float uvX = glyph.x * normalizedWidth;
            float uvY = glyph.y * normalizedHeight;

            float uvW = glyph.width  * normalizedWidth * 2f;
            float uvH = glyph.height * normalizedHeight / 2.2f;

            glyphRenderer.queueRender(x + i * (glyph.width / 2), y, glyph.width / 2, glyph.height / 2,
                    uvX, uvY, uvW, uvH);
        }

        glyphRenderer.drawQueue(projectionAndViewMatrix);
        glyphRenderer.flush();
    }
}
