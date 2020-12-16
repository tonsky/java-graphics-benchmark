package fx;

import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class ClipDemo extends Demo {
    @Override
    public void draw(GraphicsContext gc, int width, int height, float dpi, float dt, float oscillation) {
        for (int x = 5; x <= width - 25; x += 25) {
            for (int y = 5; y <= height - 25; y += 25) {
                gc.save();
                gc.translate(x, y);
        
                gc.beginPath();
                gc.setFillRule(FillRule.EVEN_ODD);
                gc.arc(10, 10, 10, 10, 0, 360);
                gc.arc(10, 10, 5, 5, 0, 360);
                gc.clip();
                
                gc.setFill(Color.color((double) x / width, 0.5 * (1 - x / width), (double) y / height));
                gc.fillRect(0, 0, 20, 20);
                // gc.fill();
                
                gc.restore();
            }
        }
    }
}