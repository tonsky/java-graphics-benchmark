package skija;

import java.util.*;
import org.jetbrains.skija.*;

public class GradientsDemo extends Demo {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, float dt, float oscillation) {
        var padding = 10;
        var bw = 40;
        var bh = 20;

        var bottomColor  = Color.makeLerp(0xFF3792DF, 0xFF99D0FF, oscillation);
        // var bottomColor  = Color.makeLerp(0xFFCC0080, 0xFF00CC80, oscillation);

        try (var bgShader     = Shader.makeLinearGradient(0, 0, 0, bh, new int[] { 0xFF2A65BC, 0xFF053A8A });
             var bg           = new Paint().setShader(bgShader);
             var topShader    = Shader.makeLinearGradient(0, 1, 0, 1 + bh * 0.5f, new int[] { 0xFF9CBCDE, 0x00000000 });
             var top          = new Paint().setShader(topShader);
             var bottomShader = Shader.makeLinearGradient(0, bh * 0.3f, 0, bh, new int[] { 0x00000000, bottomColor });
             var bottom       = new Paint().setShader(bottomShader);)
        {
            for (var x = padding; x <= width - padding; x += bw + padding) {
                for (var y = padding; y <= height - padding; y += bh + padding) {
                    canvas.save();
                    canvas.translate(x, y);

                    canvas.drawRRect(RRect.makeXYWH(0, 0, bw, bh, 10), bg);
                    canvas.drawRRect(RRect.makeXYWH(4, 1, bw - 8, bh * 0.5f, 5), top);
                    canvas.drawRRect(RRect.makeXYWH(2, bh * 0.3f, bw - 4, bh * 0.7f, 7), bottom);

                    canvas.restore();
                } 
            }
        }
    }
}