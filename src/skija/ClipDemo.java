package skija;

import org.jetbrains.skija.*;

public class ClipDemo extends Demo {
    public ClipDemo() {
        variants = new String[] { "AntiAlias ON", "AntiAlias OFF" };
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, float dt, float oscillation) {
        try (var fill  = new Paint();
             var outer = new Path().addCircle(10, 10, 10);
             var inner = new Path().addCircle(10, 10, 5);)
        {
            boolean aa = "AntiAlias ON".equals(variants[variantIdx]);
            for (int x = 5; x <= width - 25; x += 25) {
                for (int y = 5; y <= height - 25; y += 25) {
                    canvas.save();
                    canvas.translate(x, y);
                    canvas.clipPath(outer, ClipMode.INTERSECT, aa);
                    canvas.clipPath(inner, ClipMode.DIFFERENCE, aa);
                    fill.setColor(new Color4f((float) x / width, 0.5f * (1 - x / width), (float) y / height).toColor());
                    canvas.drawRect(Rect.makeXYWH(0, 0, 20, 20), fill);
                    canvas.restore();
                }
            }
        }
    }
}
