package skija;

import java.util.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class Main {
    int width = 1440;
    int height = 810;
    float dpi = 2;
    long frames = 0;
    String pendingTitle;

    Demo[] demos = new Demo[] {
        new CirclesDemo(),
        new ShadowsDemo(),
    };
    int demoIdx = 1;

    public static void main(String [] args) throws Exception {
        new Main().run();
    }

    public void updateTitle() {
        Demo demo = demos[demoIdx];
        String title = demo.getClass().getSimpleName() + " - " + demo.variants[demo.variantIdx] + " - " + frames + " fps";
        pendingTitle = title;
        if (frames > 0)
            System.out.println(title);
    }

    public void run() {
        GLFWErrorCallback.createPrint(System.err).set();
        glfwInit();

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        long window = glfwCreateWindow(width, height, "Skija LWJGL Demo", NULL, NULL);

        glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(win, true);
        });

        glfwSetWindowPos(window, 0, 0);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(0); // Disable v-sync
        glfwShowWindow(window);

        GL.createCapabilities();
        var context = DirectContext.makeGL();

        int fbId = GL11.glGetInteger(0x8CA6); // GL_FRAMEBUFFER_BINDING
        var renderTarget = BackendRenderTarget.makeGL((int) (width * dpi), (int) (height * dpi), /*samples*/0, /*stencil*/8, fbId, FramebufferFormat.GR_GL_RGBA8);
        var surface = Surface.makeFromBackendRenderTarget(context, renderTarget, SurfaceOrigin.BOTTOM_LEFT, SurfaceColorFormat.RGBA_8888, ColorSpace.getSRGB()); // TODO load monitor profile
        var canvas = surface.getCanvas();
 
        glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                Demo demo = demos[demoIdx];
                if (GLFW_KEY_LEFT == key) {
                    demoIdx = (demoIdx + demos.length - 1) % demos.length;
                    frames = 0;
                    updateTitle();
                } else if (GLFW_KEY_RIGHT == key) {
                    demoIdx = (demoIdx + 1) % demos.length;
                    frames = 0;
                    updateTitle();
                } else if (GLFW_KEY_UP == key) {
                    if (demo.variants.length > 1) {
                        demo.variantIdx = (demo.variantIdx + demo.variants.length - 1) % demo.variants.length;
                        frames = 0;
                        updateTitle();
                    }
                } else if (GLFW_KEY_DOWN == key) {
                    if (demo.variants.length > 1) {
                        demo.variantIdx = (demo.variantIdx + 1) % demo.variants.length;
                        frames = 0;
                        updateTitle();
                    }
                }
            }
        });

        updateTitle();

        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            public void run() {
                updateTitle();
                frames = 0;
            }
        }, 1000, 1000);

        while (!glfwWindowShouldClose(window)) {
            if (pendingTitle != null) {
                glfwSetWindowTitle(window, pendingTitle);
                pendingTitle = null;
            }
            canvas.clear(0xFFFFFFFF);
            int count = canvas.save();
            canvas.scale(dpi, dpi);
            demos[demoIdx].draw(canvas, width, height, dpi);
            canvas.restoreToCount(count);
            context.flush();
            glfwSwapBuffers(window);
            glfwPollEvents();
            ++frames;
        }
    }
}