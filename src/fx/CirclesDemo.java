package fx;

import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class CirclesDemo extends Demo {
    public float[] arr; // x, y, dx, dy
    public Color[] colors;
    public float radius = 4;

    public CirclesDemo() {
        variants = new String[] { "10000", "1000", "100000" };
    }

    @Override
    public void draw(GraphicsContext gc, int width, int height, float dpi, int xpos, int ypos) {
        if (arr == null) {
            var random = new Random();
            arr = new float[100000 * 4];
            colors = new Color[100000];
            for (int i = 0; i < 100000; ++i) {
                arr[i * 4] = radius + random.nextFloat() * (width - radius);
                arr[i * 4 + 1] = radius + random.nextFloat() * (height - radius);
                arr[i * 4 + 2] = random.nextFloat() - 0.5f;
                arr[i * 4 + 3] = random.nextFloat() - 0.5f;
                colors[i] = new Color(
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat(),
                    1.0);
            }
        }

        int circles = Integer.parseInt(variantTitle());

        gc.setFill(Color.web("#C33"));

        for (int i = 0; i < circles; ++i) {
            var x = arr[i * 4];
            var dx = arr[i * 4 + 2];
            if ((x + dx > width - radius && dx > 0) || (x + dx < radius && dx < 0)) {
                dx = -dx;
                arr[i * 4 + 2] = dx;
            }
            arr[i * 4] = x + dx;

            var y = arr[i * 4 +1];
            var dy = arr[i * 4 + 3];
            if ((y + dy > height - radius && dy > 0) || (y + dy < radius && dy < 0)) {
                dy = -dy;
                arr[i * 4 + 3] = dy;
            }
            arr[i * 4 + 1] = y + dy;

            gc.setFill(colors[i]);
            gc.fillOval(arr[i * 4] - radius, arr[i * 4 + 1] - radius, radius * 2, radius * 2);
        }
    }
}