package skija;

import java.util.*;
import org.jetbrains.skija.*;

public class ShadowsDemo extends Demo {
    float[] arr; // x, y, dx, dy, size
    final int squares = 200;
    Paint fill = new Paint().setColor(0xFFFFFFFF);
    ImageFilter shadow = ImageFilter.makeDropShadow(4, 4, 10, 10, 0x20FF0000);

    public ShadowsDemo() {
        variants = new String[] { "ShadowUtils", "ImageFilter" };
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi) {
        if (arr == null) {
            var random = new Random();
            arr = new float[squares * 5];
            for (int i = 0; i < squares; ++i) {
                var size = 50 + random.nextFloat() * 50;
                arr[i * 5] = size / 2 + random.nextFloat() * (width - size / 2);
                arr[i * 5 + 1] = size / 2 + random.nextFloat() * (height - size / 2);
                arr[i * 5 + 2] = random.nextFloat() * 10f - 5f;
                arr[i * 5 + 3] = random.nextFloat() * 10f - 5f;
                arr[i * 5 + 4] = size;
            }
        }

        if ("ImageFilter".equals(variantTitle()))
            fill.setImageFilter(shadow);
        else
            fill.setImageFilter(null);

        for (int i = 0; i < squares; ++i) {
            var size = arr[i * 5 + 4];
            var x = arr[i * 5];
            var dx = arr[i * 5 + 2];
            if ((x + dx > width - size / 2 && dx > 0) || (x + dx < size / 2 && dx < 0)) {
                dx = -dx;
                arr[i * 5 + 2] = dx;
            }
            arr[i * 5] = x + dx;

            var y = arr[i * 5 +1];
            var dy = arr[i * 5 + 3];
            if ((y + dy > height - size / 2 && dy > 0) || (y + dy < size / 2 && dy < 0)) {
                dy = -dy;
                arr[i * 5 + 3] = dy;
            }
            arr[i * 5 + 1] = y + dy;

            var rect = Rect.makeXYWH(arr[i * 5] - size / 2, arr[i * 5 + 1] - size / 2, size, size);
            if ("ShadowUtils".equals(variantTitle())) {
                var path = new Path().addRect(rect);
                var zPlaneParams = new Point3(0, 0, 8 * dpi);
                var lightPos = new Point3(width / 2 * dpi, 0, 600 * dpi);
                var lightRadius = 800 * dpi;
                var ambientColor = 0x0A0000FF;
                var spotColor = 0x30FF0000;
                ShadowUtils.drawShadow(canvas, path, zPlaneParams, lightPos, lightRadius, ambientColor, spotColor, false, false);
                path.close();
            }
            canvas.drawRect(rect, fill);
        }
    }
}