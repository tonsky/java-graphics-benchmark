package fx;

import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.effect.*;
import javafx.scene.paint.*;

public class ShadowsDemo extends Demo {
    public float[] arr; // x, y, dx, dy
    public float size = 100;
    public final int squares = 100;
    public DropShadow shadow;

    public ShadowsDemo() {
        shadow = new DropShadow();
        shadow.setRadius(20.0);
        shadow.setOffsetX(4.0);
        shadow.setOffsetY(4.0);
        shadow.setColor(Color.color(1.0, 0, 0, 0.1));
    }

    @Override
    public void draw(GraphicsContext gc, int width, int height, float dpi, int xpos, int ypos) {
        if (arr == null) {
            var random = new Random();
            arr = new float[squares * 4];
            for (int i = 0; i < squares; ++i) {
                arr[i * 4] = size / 2 + random.nextFloat() * (width - size / 2);
                arr[i * 4 + 1] = size / 2 + random.nextFloat() * (height - size / 2);
                arr[i * 4 + 2] = random.nextFloat() * 10f - 5f;
                arr[i * 4 + 3] = random.nextFloat() * 10f - 5f;
            }
        }

        gc.setFill(Color.web("#EEE"));
        gc.setEffect(shadow);

        for (int i = 0; i < squares; ++i) {
            var x = arr[i * 4];
            var dx = arr[i * 4 + 2];
            if ((x + dx > width - size / 2 && dx > 0) || (x + dx < size / 2 && dx < 0)) {
                dx = -dx;
                arr[i * 4 + 2] = dx;
            }
            arr[i * 4] = x + dx;

            var y = arr[i * 4 +1];
            var dy = arr[i * 4 + 3];
            if ((y + dy > height - size / 2 && dy > 0) || (y + dy < size / 2 && dy < 0)) {
                dy = -dy;
                arr[i * 4 + 3] = dy;
            }
            arr[i * 4 + 1] = y + dy;

            gc.fillRoundRect(arr[i * 4] - size / 2, arr[i * 4 + 1] - size / 2, size, size, 10, 10);
        }

        gc.setEffect(null);
    }
}