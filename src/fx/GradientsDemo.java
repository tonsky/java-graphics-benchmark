package fx;

import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.effect.*;
import javafx.scene.paint.*;

public class GradientsDemo extends Demo {
    @Override
    public void draw(GraphicsContext gc, int width, int height, float dpi, float dt, float oscillation) {
        var padding = 10;
        var bw = 40;
        var bh = 20;

        var bg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[] { new Stop(0, Color.web("#2A65BC")), new Stop(1, Color.web("#053A8A")) });
        var top = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[] { new Stop(0, Color.web("#9CBCDE")), new Stop(1, Color.web("#000000", 0.0)) });
        var bottomColor = Color.web("#3792DF").interpolate(Color.web("#99D0FF"), oscillation);
        var bottom = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[] { new Stop(0, Color.web("#000000", 0.0)), new Stop(1, bottomColor) });


        for (var x = padding; x <= width - padding; x += bw + padding) {
            for (var y = padding; y <= height - padding; y += bh + padding) {
                // gc.beginPath();        
                // gc.moveTo(x + bh / 2, y + 0);
                // gc.arc(x + bh / 2, y + bh / 2, bh / 2, bh / 2, 90, 180);
                // gc.lineTo(x + bw - bh / 2, y + bh);
                // gc.arc(x + bw - bh / 2, y + bh / 2, bh / 2, bh / 2, 270, 180);
                // gc.lineTo(x + bh / 2, y + 0);
                // gc.closePath();
                
                gc.save();
                // gc.clip();
                gc.translate(x, y);

                gc.setFill(bg);
                gc.fillRoundRect(0, 0, bw, bh, bh, bh);

                gc.setFill(top);
                gc.fillRoundRect(4, 1, bw - 8, bh * 0.5f, bh * 0.5f, bh * 0.5f);

                gc.setFill(bottom);
                gc.fillRoundRect(2, bh * 0.3f, bw - 4, bh * 0.7f, bh * 0.7f, bh * 0.7f);

                gc.restore();
            }
        }
    }
}