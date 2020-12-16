package skija;

import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class TypographyDemo extends Demo {
    public Typeface face = Typeface.makeFromFile("fonts/InterHinted-Regular.ttf");

    public void drawLine(Canvas canvas, Shaper shaper, String text, Font font, FontFeature[] features, Paint paint) {
        try (var blob = shaper.shape(text, font, features, true, Float.POSITIVE_INFINITY, Point.ZERO);) {
            var bounds = blob.getBounds();
            canvas.drawTextBlob(blob, 0, 0, font, paint);
            canvas.translate(0, bounds.getHeight());
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, float dt, float oscillation) {
        try (var fill = new Paint().setColor(0xFF172e7c);
             var shaper = Shaper.makeShapeThenWrap();
             var font = new Font(face, 32 * dpi);)
        {
            canvas.save();
            canvas.translate(20 * dpi, 20 * dpi);
            
            drawLine(canvas, shaper, "hello дружба 😀🧑🏿👮‍♀️🕵️‍♀️👩‍❤️‍👨👩‍👩‍👧‍👧 one ثلاثة12👂34خمسة", font, null, fill);
            drawLine(canvas, shaper, "6x9 13:34 2+5*7 Illegal ?!", font, null, fill);
            drawLine(canvas, shaper, "6x9 13:34 2+5*7 Illegal ?! -calt", font, FontFeature.parse("-calt"), fill);
            drawLine(canvas, shaper, "6x9 13:34 2+5*7 Illegal ?! +dlig +ss01 +ss02 +cv01", font, FontFeature.parse("dlig ss01 ss02 cv01"), fill);
            canvas.translate(0, 20 * dpi);

            font.setSize(11 * dpi);
            font.setEdging(FontEdging.ALIAS);
            drawLine(canvas, shaper, "1006 Inter Illegal Component Fix Position Scrolling // ALIAS", font, null, fill);
            font.setEdging(FontEdging.ANTI_ALIAS);
            drawLine(canvas, shaper, "1006 Inter Illegal Component Fix Position Scrolling // ANTI_ALIAS", font, null, fill);
            font.setEdging(FontEdging.SUBPIXEL_ANTI_ALIAS);
            drawLine(canvas, shaper, "1006 Inter Illegal Component Fix Position Scrolling // SUBPIXEL_ANTI_ALIAS", font, null, fill);

            canvas.restore();
        }
    }

    @Override
    public void scale(Canvas canvas, float dpi) {
        // noop
    }
}
