package skija;

import java.util.*;
import org.jetbrains.skija.*;

public class CirclesDemo extends Demo {
    public float[] arr; // x, y, dx, dy, radius
    public int[] colors;
    public Paint fill = new Paint();

    public CirclesDemo() {
        variants = new String[] { "10000", "1000", "100000" };
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, float dt, float oscillation) {
        if (arr == null) {
            var random = new Random();
            arr = new float[100000 * 5];
            colors = new int[100000];
            for (int i = 0; i < 100000; ++i) {
                var radius = 2 + random.nextFloat() * 8;
                arr[i * 5] = radius + random.nextFloat() * (width - radius);
                arr[i * 5 + 1] = radius + random.nextFloat() * (height - radius);
                arr[i * 5 + 2] = random.nextFloat() * 0.2f - 0.1f;
                arr[i * 5 + 3] = random.nextFloat() * 0.2f - 0.1f;
                arr[i * 5 + 4] = radius;
                colors[i] = new Color4f(
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat()).toColor();
            }
        }

        int circles = Integer.parseInt(variantTitle());
        for (int i = 0; i < circles; ++i) {
            var radius = arr[i * 5 + 4];

            var x = arr[i * 5];
            var dx = arr[i * 5 + 2];
            if ((x + dx * dt > width - radius && dx > 0) || (x + dx * dt < radius && dx < 0)) {
                dx = -dx;
                arr[i * 5 + 2] = dx;
            }
            arr[i * 5] = x + dx * dt;

            var y = arr[i * 5 +1];
            var dy = arr[i * 5 + 3];
            if ((y + dy * dt > height - radius && dy > 0) || (y + dy * dt < radius && dy < 0)) {
                dy = -dy;
                arr[i * 5 + 3] = dy;
            }
            arr[i * 5 + 1] = y + dy * dt;

            fill.setColor(colors[i]);
            canvas.drawCircle(arr[i * 5], arr[i * 5 + 1], radius, fill);
        }
    }
}