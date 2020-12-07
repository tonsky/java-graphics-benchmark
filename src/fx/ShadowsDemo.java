package fx;

import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.effect.*;
import javafx.scene.paint.*;

public class ShadowsDemo extends Demo {
    public float[] arr; // x, y, dx, dy, size
    public final int squares = 200;
    public DropShadow shadow;

    public ShadowsDemo() {
        shadow = new DropShadow();
        shadow.setRadius(20.0);
        shadow.setOffsetX(4.0);
        shadow.setOffsetY(4.0);
        shadow.setColor(Color.color(1.0, 0, 0, 0.1));
    }

    @Override
    public void draw(GraphicsContext gc, int width, int height, float dpi, float dt, float oscillation) {
        if (arr == null) {
            var random = new Random();
            arr = new float[squares * 5];
            for (int i = 0; i < squares; ++i) {
                var size = 50 + random.nextFloat() * 50;
                arr[i * 5] = size / 2 + random.nextFloat() * (width - size / 2);
                arr[i * 5 + 1] = size / 2 + random.nextFloat() * (height - size / 2);
                arr[i * 5 + 2] = random.nextFloat() * 0.2f - 0.1f;
                arr[i * 5 + 3] = random.nextFloat() * 0.2f - 0.1f;
                arr[i * 5 + 4] = size;
            }
        }

        gc.setFill(Color.web("#FFF"));
        gc.setEffect(shadow);

        for (int i = 0; i < squares; ++i) {
            var size = arr[i * 5 + 4];
            var x = arr[i * 5];
            var dx = arr[i * 5 + 2];
            if ((x + dx * dt > width - size / 2 && dx > 0) || (x + dx * dt < size / 2 && dx < 0)) {
                dx = -dx;
                arr[i * 5 + 2] = dx;
            }
            arr[i * 5] = x + dx * dt;

            var y = arr[i * 5 +1];
            var dy = arr[i * 5 + 3];
            if ((y + dy * dt > height - size / 2 && dy > 0) || (y + dy * dt < size / 2 && dy < 0)) {
                dy = -dy;
                arr[i * 5 + 3] = dy;
            }
            arr[i * 5 + 1] = y + dy * dt;

            gc.fillRect(arr[i * 5] - size / 2, arr[i * 5 + 1] - size / 2, size, size);
        }

        gc.setEffect(null);
    }
}