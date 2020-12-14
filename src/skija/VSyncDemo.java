package skija;

import java.util.*;
import org.jetbrains.skija.*;
import org.lwjgl.glfw.*;

public class VSyncDemo extends Demo {
    private int frame = 0;

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, float dt, float oscillation) {
        try (var font = new Font().setSize(128);
             var bgFill = new Paint().setColor(0xFFe0e0e0);
             var textFill = new Paint().setColor(frame % 2 == 0 ? 0xFFef8784 : 0xFFa1fcfe);
             var frameStroke = new Paint().setColor(0xFFCCCCCC).setMode(PaintMode.STROKE).setStrokeWidth(1);
             var frameFill = new Paint().setColor(0xFFef8784);
             var tearFill = new Paint().setColor(0xFFef8784);)
        {
            // vsync label
            var bounds = font.measureText("VSYNC");
            canvas.drawRect(Rect.makeXYWH((width - bounds.getWidth() - 100) / 2, (height - bounds.getHeight() - 100) / 2, bounds.getWidth() + 100, bounds.getHeight() + 100), bgFill);
            canvas.drawString("VSYNC", (width - bounds.getWidth()) / 2, (height + bounds.getHeight()) / 2, font, textFill);

            // frame
            for (int x = 0; x < 20; ++x) {
                for (int y = 0; y < 2; ++y) {
                    canvas.drawRect(Rect.makeXYWH((width - 400) / 2 + x * 20, (height + bounds.getHeight()) / 2 + 50 + 20 + y * 20, 20, 20), frameStroke);
                    if (frame % 40 == x * 2 + y)
                        canvas.drawRect(Rect.makeXYWH((width - 400) / 2 + x * 20 + 2, (height + bounds.getHeight()) / 2 + 50 + 20 + y * 20 + 2, 16, 16), frameFill);
                }
            }

            // tear
            canvas.drawRect(Rect.makeXYWH((frame % (width / 20)) * 20, 0, 20, height), tearFill);

            frame++;
        }
    }

    @Override
    public void onEnter() {
        GLFW.glfwSwapInterval(1);
    }

    @Override
    public void onExit() {
        GLFW.glfwSwapInterval(0);
    }
}