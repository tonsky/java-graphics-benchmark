package fx;

import java.util.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.effect.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class EverythingDemo extends Demo {
    Font uiFont = Font.font(".AppleSystemUIFont", 24);
    // Font fileFont = Font.loadFont("file:fonts/Inter-Regular.ttf", 11);
    // Font codeFont = Font.font("Fira Code", 11);
    Font codeFont = Font.loadFont("file:fonts/FiraCode-Regular.otf", 11);
    Font fileFont = codeFont;

    void paintBg(GraphicsContext gc, int width, int height) {
        gc.setFill(new Color(0.6, 0.8, 0.6, 1.0));
        // DropShadow dropShadow = new DropShadow();
        // dropShadow.setRadius(5.0);
        // dropShadow.setOffsetX(3.0);
        // dropShadow.setOffsetY(3.0);
        // dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        // gc.setEffect(dropShadow);

        // Shadow shadow = new Shadow();
        // shadow.setRadius(5.0);
        // shadow.setColor(Color.color(0.4, 0.5, 0.5));
        // gc.setEffect(shadow);

        // BoxBlur boxBlur = new BoxBlur();
        // boxBlur.setWidth(10);
        // boxBlur.setHeight(3);
        // boxBlur.setIterations(3);
        // gc.setEffect(boxBlur);

        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++ y) {
                if ((x + y) % 2 == 0) {
                    gc.fillRect​(width * x / 8.0,
                                height * y / 8.0,
                                width / 8.0,
                                height / 8.0);
                }
            }
        }

        gc.setEffect(null);
    }

    void paintTimer(GraphicsContext gc) {
        var angle = (System.currentTimeMillis() % 10000) / 10000.0 * 2.0 * Math.PI;
        gc.setFont(uiFont);
        gc.setFill(Color.BLUE);
        gc.fillText("" + System.currentTimeMillis(), 
            Math.round(200.0 + Math.sin(angle) * 100.0),
            Math.round(150.0 + Math.cos(angle) * 100.0));
    }

    long[] times = new long[10];
    int idx = 0;
    long lastT = System.nanoTime();

    void paintFPS(GraphicsContext gc) {
        var t = System.nanoTime();
        times[idx] = t - lastT;
        idx = (idx + 1) % times.length;
        lastT = t;

        long min = Arrays.stream(times).reduce(Long::min).getAsLong();
        long max = Arrays.stream(times).reduce(Long::max).getAsLong();
        if (min > 0) {
            gc.setFont(uiFont);
            gc.setFill(Color.BLACK);
            gc.fillText("FPS min " + 1_000_000_000 / max, 1000, 24);
            gc.fillText("FPS max " + 1_000_000_000 / max, 1000, 48);
        }
    }

    void paintFiles(GraphicsContext gc) {
        gc.setFont(fileFont);
        var y = 30;
        for (var file: files) {
            var lb = measureText(fileFont, file);
            gc.setFill(new Color(1, 0, 0, 0.1));
            gc.fillRect(0 + lb.getMinX(), y + lb.getMinY(), lb.getWidth(), lb.getHeight());
            gc.setFill(Color.BLACK);
            gc.fillText(file, 0, y);
            y += 17;
        }
    }

    Bounds measureText(Font font, String s) {
        var text = new Text(s);
        text.setFont(font);
        return text.getLayoutBounds();
    }

    void paintCode(GraphicsContext gc) {
        gc.setFont(codeFont);
        
        var y = 30;
        for (var line: code) {
            var lb = measureText(codeFont, line);
            gc.setFill(new Color(0, 0, 1, 0.1));
            gc.fillRect(200 + lb.getMinX(), y + lb.getMinY(), lb.getWidth(), lb.getHeight());
            gc.setFill(Color.BLACK);
            gc.fillText(line, 200, y);
            y += 17;
        }
    }

    int offset = 0;

    void paintColumn(GraphicsContext gc, int width, int height) {
        gc.setFill(new Color(0.0, 0.0, 0.0, 0.2));
        for (int i = 0; i < 10; ++i) {
            gc.fillRect(offset * width / 60, height / 10 * i, width / 5, height / 10);
        }
        offset = (offset + 1) % 60;
    }

    void paintToggle(GraphicsContext gc, boolean on) {
        gc.save();

        gc.beginPath();
        
        // gc.moveTo(0, 15.5);
        // gc.arcTo(0, 0, 15.5, 0, 15.5);
        // gc.lineTo(35.5, 0);
        // gc.arcTo(51, 0, 51, 15.5, 15.5);
        // gc.arcTo(51, 31, 31.5, 31, 15.5);
        // gc.lineTo(15.5, 31);
        // gc.arcTo(0, 31, 0, 15.5, 15.5);

        gc.moveTo(15.5, 31);
        gc.arc(15.5, 15.5, 15.5, 15.5, 90, 180);
        gc.lineTo(35.5, 31);
        gc.arc(35.5, 15.5, 15.5, 15.5, 270, 180);
        gc.lineTo(15.5, 0);

        // gc.rect(0, 0, 51, 31);

        gc.closePath();
        gc.clip();

        var bg = on ? new Color(53/256.0, 199/256.0, 89/256.0, 1) : new Color(233/256.0, 233/256.0, 235/256.0, 1);
        gc.setFill(bg);
        gc.fillRect(0, 0, 51, 31);
        // gc.fillRoundRect(0, 0, 51, 31, 31, 31);

        // gc.beginPath();
        // gc.moveTo(15.5, 31);
        // gc.arc(15.5, 15.5, 15.5, 15.5, 90, 180);
        // gc.lineTo(35.5, 31);
        // gc.arc(35.5, 15.5, 15.5, 15.5, 270, 180);
        // gc.lineTo(15.5, 0);
        // gc.closePath();
        // gc.fill();

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(8.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(4.0);
        dropShadow.setColor(Color.color(0, 0, 0, 0.1));
        gc.setEffect(dropShadow);

        gc.setFill(Color.WHITE);
        gc.fillOval(on ? 22 : 2, 2, 27, 27);
        gc.setEffect(null);
        gc.restore();
    }

    @Override
    public void draw(GraphicsContext gc, int width, int height, float dpi, int xpos, int ypos) {
        var t = System.nanoTime();
        
        paintBg(gc, width, height);
        paintTimer(gc);
        paintFPS(gc);
        paintFiles(gc);
        paintCode(gc);

        gc.save();
        gc.translate(500, 100);
        for (int i = 0; i < 20; ++i) {
            paintToggle(gc, false);
            gc.translate(69, 0);
            paintToggle(gc, true);
            gc.translate(-69, 33);
        }
        gc.restore();

        paintColumn(gc, width, height);
    }

    static String[] files = new String[] {
        "src",
        "├ Awt.java",
        "└ main",
        "  ├ resources",
        "  │ ├ black.png",
        "  │ └ image.png",
        "  └ java",
        "    └ benchmark",
        "      └ Benchmark.java",
        "target",
        "└ classes",
        "  ├ Foreground.class",
        "  ├ Awt.class",
        "  ├ Compositor.class",
        "  ├ Background.class",
        "  ├ Comp.class",
        "  ├ Layer.class",
        "  └ Screen.class",
        "fonts",
        "├ Inter-Medium.ttf",
        "├ Inter-Light.ttf",
        "├ Inter-ExtraLightItalic.ttf",
        "├ Inter-SemiBoldItalic.ttf",
        "├ Inter-Thin.ttf",
        "├ Inter-Bold.ttf",
        "├ Inter-Regular.ttf",
        "├ Inter-LightItalic.ttf",
        "├ Inter-ExtraBold.ttf",
        "├ Inter-ThinItalic.ttf",
        "├ Inter-ExtraLight.ttf",
        "├ Inter-BoldItalic.ttf",
        "├ Inter-ExtraBoldItalic.ttf",
        "├ Inter-MediumItalic.ttf",
        "├ Inter-Black.ttf",
        "├ Inter-Italic.ttf",
        "├ Inter-BlackItalic.ttf",
        "└ Inter-SemiBold.ttf",
        "shmonts",
        "├ PTRootUI-Medium.ttf",
        "├ PTRootUI-Light.ttf",
        "├ PTRootUI-ExtraLightItalic.ttf",
        "├ PTRootUI-SemiBoldItalic.ttf",
        "├ PTRootUI-Thin.ttf",
        "├ PTRootUI-Bold.ttf",
        "├ PTRootUI-Regular.ttf",
        "├ PTRootUI-LightItalic.ttf",
        "├ PTRootUI-ExtraBold.ttf",
        "├ PTRootUI-ThinItalic.ttf",
        "├ PTRootUI-ExtraLight.ttf",
        "├ PTRootUI-BoldItalic.ttf",
        "├ PTRootUI-ExtraBoldItalic.ttf",
        "├ PTRootUI-MediumItalic.ttf",
        "├ PTRootUI-Black.ttf",
        "├ PTRootUI-Italic.ttf",
        "├ PTRootUI-BlackItalic.ttf",
        "└ PTRootUI-SemiBold.ttf",
        "pom.xml",
        "README.md",
        ".gitignore",
        "nbactions.xml"};

    static String[] code = new String[] {
        "--> =>> *** +++ <!--",
        "import java.awt.*;",
        "import java.awt.event.*;",
        "import java.awt.font.*;",
        "import java.awt.geom.*;",
        "import java.awt.image.*;",
        "import java.io.*;",
        "import java.util.*;",
        "import java.util.concurrent.*;",
        "import javax.swing.*;",
        "",
        "public class Awt {",
        "    static Font inter;",
        "    static Font firacode;",
        "    static double DPI = 2.0;",
        "",
        "    public static Font loadFont(String path, int type) throws Exception {",
        "        FileInputStream is = new FileInputStream(path);",
        "        try {",
        "            return Font.createFont(type, is);",
        "        } finally {",
        "            is.close();",
        "        }",
        "    }",
        "",
        "    public static void main(String[] args) throws Exception {",
        "        var frame = new JFrame(\"AWT testbed\");",
        "        frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);",
        "        var panel = new JPanel(false);",
        "        panel.setPreferredSize(new java.awt.Dimension(1300, 800));",
        "        frame.setContentPane(panel);",
        "        frame.pack();",
        "        frame.setVisible(true);",
        "        ",
        "        var vsync = Class.forName(\"sun.java2d.pipe.hw.ExtendedBufferCapabilities$VSyncType\");",
        "        var ebc = Class.forName(\"sun.java2d.pipe.hw.ExtendedBufferCapabilities\");",
        "        var ctor = ebc.getConstructor(ImageCapabilities.class, ImageCapabilities.class, BufferCapabilities.FlipContents.class, vsync);",
        "        BufferCapabilities bc = (BufferCapabilities) ctor.newInstance(new ImageCapabilities(true), new ImageCapabilities(true), BufferCapabilities.FlipContents);",
        "        frame.createBufferStrategy(2, bc);",
        "",
        "        inter = loadFont(\"fonts/Inter-Regular.ttf\", Font.TRUETYPE_FONT);",
        "        firacode = loadFont(\"fonts/FiraCode-Regular.otf\", Font.TRUETYPE_FONT);",
        "        Background bg = new Background();",
        "        Foreground fg = new Foreground();",
        "        Screen sc = new Screen();",
        "        FPS fps = new FPS();",
        "        Compositor compositor = new Compositor(bg, sc, fg, fps);",
        "",
        "        while (true) {",
        "            compositor.paint(frame);",
        "        }",
        "        ",
        "        // ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);",
        "        // Runnable tick = () -> { ",
        "        //     // var angle = (System.currentTimeMillis() % 10000) / 10000.0 * 2.0 * Math.PI;",
        "        //     // fg.pos = new Point((int) (200.0 + Math.sin(angle) * 100.0),",
        "        //     //                    (int) (150.0 + Math.cos(angle) * 100.0));",
        "        //     // bg.needsRepaint = true;",
        "        //     // sc.needsRepaint = true;",
        "        //     fg.needsRepaint = true;",
        "        //     compositor.paint(frame);",
        "        // };",
        "        // pool.scheduleAtFixedRate​(tick, 0, 5000, TimeUnit.MICROSECONDS);",
        "    }",
        "}"
    };
}