package main;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.awt.FontFormatException;

public class FontLoader {
    public static Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            return font.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, (int) size); // fallback font
        }
    }
}
