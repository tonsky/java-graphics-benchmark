package fx;

import java.io.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;

public class TypographyDemo extends Demo {
    public Font inter32;
    public Font inter11;

    public TypographyDemo() {
        try (var is = new FileInputStream("fonts/InterHinted-Regular.ttf"); ) {
            inter32 = Font.loadFont(is, 32);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (var is = new FileInputStream("fonts/InterHinted-Regular.ttf"); ) {
            inter11 = Font.loadFont(is, 11);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Bounds measureText(String s, Font font) {
        var text = new Text(s);
        text.setFont(font);
        return text.getLayoutBounds();
    }

    public void drawLine(GraphicsContext gc, String s, Font font) {
        gc.setFont(font);
        gc.fillText(s, 0, 0);
        gc.translate(0, measureText(s, font).getHeight());
    }

    @Override
    public void draw(GraphicsContext gc, int width, int height, float dpi, float dt, float oscillation) {
        gc.setFill(Color.web("#172e7c"));
        gc.save();
        gc.translate(20, 50);
            
        drawLine(gc, "hello дружба 😀🧑🏿👮‍♀️🕵️‍♀️👩‍❤️‍👨👩‍👩‍👧‍👧 one ثلاثة12👂34خمسة", inter32);
        drawLine(gc, "6x9 13:34 2+5*7 Illegal ?!", inter32);
        gc.translate(0, 20 * dpi);

        gc.setFontSmoothingType(FontSmoothingType.GRAY);
        drawLine(gc, "1006 Inter Illegal Component Fix Position Scrolling // GRAY", inter11);
        gc.setFontSmoothingType(FontSmoothingType.LCD);
        drawLine(gc, "1006 Inter Illegal Component Fix Position Scrolling // LCD", inter11);
        gc.restore();
    }
}
