package at.dalex.grape.graphics.font;

import at.dalex.grape.graphics.BatchRenderer;
import at.dalex.grape.graphics.graphicsutil.Image;
import at.dalex.grape.graphics.graphicsutil.ImageUtils;
import org.joml.Matrix4f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;

public class BitmapFont {

    private final Map<Character, Glyph> glyphs;
    private Image texture;
    private int fontHeight;
    private BatchRenderer renderer;

    public BitmapFont() {
        this(new Font(Font.MONOSPACED, PLAIN, 16), true);
    }

    public BitmapFont(boolean antiAlias) {
        this(new Font(Font.MONOSPACED, PLAIN, 16), antiAlias);
    }

    public BitmapFont(int size, boolean antiAlias) {
        this(new Font(MONOSPACED, PLAIN, size), antiAlias);
    }

    public BitmapFont(InputStream in, int size) throws FontFormatException, IOException {
        this(in, size, true);
    }

    public BitmapFont(InputStream in, int size, boolean antiAlias) throws IOException, FontFormatException {
        this(Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(PLAIN, size), antiAlias);
    }

    public BitmapFont(Font font) {
        this(font, true);
    }

    public BitmapFont(Font font, boolean antiAlias) {
        glyphs = new HashMap<>();
        texture = createFontTexture(font, antiAlias);
        renderer = new BatchRenderer(texture);
    }

    private Image createFontTexture(Font font, boolean antiAlias) {
        /* Loop through the characters to get charWidth and charHeight */
        int width = 0;
        int height = 0;

        for (int i = 32; i < 256; i++) {
            if (i == 127) //DEL Control code
                continue;

            char character = (char) i;
            BufferedImage charImage = createCharImage(font, character, antiAlias);
            if (charImage == null) continue;

            width += charImage.getWidth();
            height = Math.max(height, charImage.getHeight());
        }

        fontHeight = height;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        int x = 0;

        for (int i = 32; i < 256; i++) {
            if (i == 127) //DEL Control code
                continue;

            char character = (char) i;
            BufferedImage charImage = createCharImage(font, character, antiAlias);
            if (charImage == null) continue;

            int charWidth = charImage.getWidth();
            int charHeight = charImage.getHeight();

            //Create glyph and draw char on image
            Glyph glyph = new Glyph(charWidth, charHeight, x, image.getHeight() - charHeight, 0f);
            g.drawImage(charImage, x, 0, null);
            x += glyph.width;
            glyphs.put(character, glyph);
        }

        return ImageUtils.convertBufferedImage(image);
    }

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

    public int getWidth(String text) {
        int width = 0;
        int lineWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                width = Math.max(width, lineWidth);
                lineWidth = 0;
                continue;
            }
            if (c == '\r') continue;

            Glyph glyph = glyphs.get(c);
            lineWidth += glyph.width;
        }

        width = Math.max(width, lineWidth);
        return width;
    }

    public int getHeight(String text) {
        int height = 0;
        int lineHeight = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                height += lineHeight;
                lineHeight = 0;
                continue;
            }
            if (c == '\r') continue;

            Glyph glyph = glyphs.get(c);
            lineHeight = Math.max(lineHeight, glyph.height);
        }
        height += lineHeight;
        return height;
    }

    public void drawText(String text, int x, int y, Matrix4f projectionAndViewMatrix) {
        int textHeight = getHeight(text);

        int drawX = x;
        int drawY = y;
        if (textHeight > fontHeight) drawY += textHeight - fontHeight;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                /* Line feed, set x and y to draw at the next line */
                drawY += fontHeight;
                drawX = x;
                continue;
            }
            if (ch == '\r') {
                /* Carriage return, just skip it */
                continue;
            }

            Glyph g = glyphs.get(ch);
            float normalizedWidth = 1.0f / texture.getWidth();
            float normalizedHeight = 1.0f / texture.getHeight();
            float u1 = g.x * normalizedWidth;
            float v1 = g.y * normalizedHeight;
            float u2 = u1 + g.width * normalizedWidth;
            float v2 = v1 + g.height * normalizedHeight;

            renderer.queueRender(drawX, drawY, g.width, g.height, u1, v1, u2, v2);
            drawX += g.width;
        }

        renderer.drawQueue(projectionAndViewMatrix);
        renderer.flush();
    }
}
