package fx;

import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;

public class VSyncDemo extends Demo {
    private int frame = 0;
    private Font font = Font.loadFont("file:fonts/FiraCode-Regular.otf", 128);

    // public VSyncDemo() {
    //     variants = new String[] { "vsync", "no vsync" };
    // }

    @Override
    public void draw(GraphicsContext gc, int width, int height, float dpi, float dt, float oscillation) {
        // if ("vsync".equals(variants[variantIdx]))
        //     com.sun.scenario.Settings.set("javafx.animation.fullspeed", "false");
        // else
        //     com.sun.scenario.Settings.set("javafx.animation.fullspeed", "true");

        // vsync label
        var text = new Text("VSYNC");
        text.setFont(font);
        var bounds = text.getLayoutBounds();
        gc.setFill(Color.web("#e0e0e0"));
        gc.fillRect((width - bounds.getWidth() - 100) / 2, (height - bounds.getHeight() - 100) / 2, bounds.getWidth() + 100, bounds.getHeight() + 100);
        gc.setFont(font);
        gc.setFill(frame % 2 == 0 ? Color.web("#ef8784") : Color.web("#a1fcfe"));
        gc.fillText("VSYNC", (width - bounds.getWidth()) / 2, (height + bounds.getHeight()) / 2);

        // frame
        for (int x = 0; x < 20; ++x) {
            for (int y = 0; y < 2; ++y) {
                gc.setStroke(Color.web("#CCCCCC"));
                gc.strokeRect((width - 400) / 2 + x * 20, (height + bounds.getHeight()) / 2 + 50 + 20 + y * 20, 20, 20);
                if (frame % 40 == x * 2 + y) {
                    gc.setFill(Color.web("#ef8784"));
                    gc.fillRect((width - 400) / 2 + x * 20 + 2, (height + bounds.getHeight()) / 2 + 50 + 20 + y * 20 + 2, 16, 16);
                }
            }
        }

        // tear
        gc.setFill(Color.web("#ef8784"));
        gc.fillRect((frame % (width / 20)) * 20, 0, 20, height);

        frame++;
    }
}