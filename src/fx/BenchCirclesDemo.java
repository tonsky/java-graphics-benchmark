package fx;

import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class BenchCirclesDemo extends Demo {
    public float[] _arr; // x, y, dx, dy
    public Color[] _colors;
    public float radius = 4;

    public BenchCirclesDemo() {
        _variants = new String[] { "10000", "1000", "100000" };
    }

    @Override
    public void draw(GraphicsContext gc, int width, int height, float dpi, int xpos, int ypos) {
        if (_arr == null) {
            var random = new Random();
            _arr = new float[100000 * 4];
            _colors = new Color[100000];
            for (int i = 0; i < 100000; ++i) {
                _arr[i * 4] = radius + random.nextFloat() * (width - radius);
                _arr[i * 4 + 1] = radius + random.nextFloat() * (height - radius);
                _arr[i * 4 + 2] = random.nextFloat() - 0.5f;
                _arr[i * 4 + 3] = random.nextFloat() - 0.5f;
                _colors[i] = new Color(
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat(),
                    1.0);
            }
        }

        int circles = Integer.parseInt(variantTitle());

        gc.setFill(Color.web("#C33"));

        for (int i = 0; i < circles; ++i) {
            var x = _arr[i * 4];
            var dx = _arr[i * 4 + 2];
            if ((x + dx > width - radius && dx > 0) || (x + dx < radius && dx < 0)) {
                dx = -dx;
                _arr[i * 4 + 2] = dx;
            }
            _arr[i * 4] = x + dx;

            var y = _arr[i * 4 +1];
            var dy = _arr[i * 4 + 3];
            if ((y + dy > height - radius && dy > 0) || (y + dy < radius && dy < 0)) {
                dy = -dy;
                _arr[i * 4 + 3] = dy;
            }
            _arr[i * 4 + 1] = y + dy;

            gc.setFill(_colors[i]);
            gc.fillOval(_arr[i * 4] - radius, _arr[i * 4 + 1] - radius, radius * 2, radius * 2);
        }
    }
}