import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Awt {
    static Font uiFont;
    static Font firacode;
    static double DPI = 2.0;

    public static Font loadFont(String path) throws Exception {
        FileInputStream is = new FileInputStream(path);
        try {
            return Font.createFont(Font.TRUETYPE_FONT, is);
        } finally {
            is.close();
        }
    }

    public static void main(String[] args) throws Exception {
        // var frame = new JFrame("AWT testbed");
        // frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        // var panel = new JPanel(false);
        // panel.setPreferredSize(new java.awt.Dimension(1800, 900));
        // frame.setContentPane(panel);
        // frame.pack();

        var frame = new Frame("AWT testbed");
        frame.setSize(1300, 800);
        frame.addWindowListener(new WindowAdapter() { //doesn't compile
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
        
        var vsync = Class.forName("sun.java2d.pipe.hw.ExtendedBufferCapabilities$VSyncType");
        var ebc = Class.forName("sun.java2d.pipe.hw.ExtendedBufferCapabilities");
        // var ctor = ebc.getConstructor(ImageCapabilities.class, ImageCapabilities.class, BufferCapabilities.FlipContents.class, vsync);
        // BufferCapabilities bc = (BufferCapabilities) ctor.newInstance(new ImageCapabilities(true), new ImageCapabilities(true), BufferCapabilities.FlipContents.BACKGROUND, vsync.getField("VSYNC_ON").get(null));
        var ctor = ebc.getConstructor(ImageCapabilities.class, ImageCapabilities.class, BufferCapabilities.FlipContents.class);
        BufferCapabilities bc = (BufferCapabilities) ctor.newInstance(new ImageCapabilities(true), new ImageCapabilities(true), BufferCapabilities.FlipContents.UNDEFINED);
        frame.createBufferStrategy(2, bc);
        // frame.createBufferStrategy(2);

        uiFont = Font.decode(".AppleSystemUIFont 13"); // loadFont("fonts/Inter-Regular.ttf");
        firacode = loadFont("fonts/FiraCode-regular.otf");
        // firacode = loadFont("fonts/SF-Pro-Text-Regular.otf");

        Background bg = new Background();
        Foreground fg = new Foreground();
        Screen sc = new Screen();
        FPS fps = new FPS();
        Compositor compositor = new Compositor(bg, sc, fg, new Toggles(), fps);

        while (true) {
            compositor.paint(frame);
        }
        
        // ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);
        // Runnable tick = () -> { 
        //     // var angle = (System.currentTimeMillis() % 10000) / 10000.0 * 2.0 * Math.PI;
        //     // fg.pos = new Point((int) (200.0 + Math.sin(angle) * 100.0),
        //     //                    (int) (150.0 + Math.cos(angle) * 100.0));
        //     // bg.needsRepaint = true;
        //     // sc.needsRepaint = true;
        //     fg.needsRepaint = true;
        //     compositor.paint(frame);
        // };
        // pool.scheduleAtFixedRate​(tick, 0, 5000, TimeUnit.MICROSECONDS);
    }
}

abstract class Layer {
    public Dimension size;
    public Point pos = new Point(0, 0);
    public Image buffer;
    public boolean needsRepaint = true;

    abstract void paintImpl(Graphics2D g);
}

class Compositor {
    Collection<Layer> layers;

    public Compositor(Layer... ls) {
        layers = Arrays.asList(ls);
    }

    public void paintLayer(Layer layer, Graphics2D g) {
        if (layer.buffer == null) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

            layer.buffer = new BufferedImage(layer.size.width, layer.size.height, BufferedImage.TYPE_INT_ARGB);
            // layer.buffer = comp.createVolatileImage(layer.size.width, layer.size.height);
            // layer.buffer = gc.createCompatibleVolatileImage(layer.size.width, layer.size.height, Transparency.TRANSLUCENT);
            
            // try {
            //     layer.buffer = gc.createCompatibleVolatileImage(layer.size.width, layer.size.height, new ImageCapabilities(true), Transparency.TRANSLUCENT);
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
        }

        if (layer.needsRepaint) {
            layer.needsRepaint = false;

            var t0 = System.nanoTime();
            var layerG = ((BufferedImage) layer.buffer).createGraphics();
            // var transform = new AffineTransform();
            // transform.setToScale(Awt.DPI, Awt.DPI);
            // layerG.setTransform(transform);
            layerG.setRenderingHint​(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            layerG.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC, 0f);
            // layerG.setComposite(alcom);
            // layerG.setColor(new Color(0, 0, 0, 0));
            // layerG.fillRect(0, 0, (int) (layer.size.width * Awt.DPI), (int) (layer.size.height * Awt.DPI));

            layerG.setBackground(new Color(0, 0, 0, 0));
            layerG.clearRect(0, 0, (int) (layer.size.width * Awt.DPI), (int) (layer.size.height * Awt.DPI));
            // layerG.clearRect(0, 0, 10, 10);

            // layerG.setComposite(AlphaComposite.Src);
            // layerG.setColor(new Color(0, 0, 0, 0));
            // layerG.fillRect(0, 0, (int) (layer.size.width * Awt.DPI), (int) (layer.size.height * Awt.DPI));

            // layerG.setComposite(AlphaComposite.SrcOver);

            layer.paintImpl(layerG);
            layerG.dispose();

            var dt = (System.nanoTime() - t0) / 1_000_000.0;
            System.out.println("  ┌ " + layer.getClass().getName() + " took " + dt + " ms");
        }

        g.drawImage(layer.buffer, layer.pos.x, layer.pos.y, layer.size.width, layer.size.height, null);
    }

    public void paintDirect(Layer layer, Graphics2D g) {
        var t0 = System.nanoTime();
        var g2 = (Graphics2D) g.create();
        g2.translate(layer.pos.x, layer.pos.y);
        layer.paintImpl(g2);
        g2.dispose();
        var dt = (System.nanoTime() - t0) / 1_000_000.0;
        System.out.println("  ┌ " + layer.getClass().getName() + " took " + dt + " ms");
    }

    public void paint(Frame frame) {
        var t0 = System.nanoTime();
        BufferStrategy strategy = frame.getBufferStrategy();
        do {
            do {
                var t1 = System.nanoTime();

                Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                g.setBackground(Color.WHITE);
                g.clearRect(0, 0, 1300, 800);
                g.setRenderingHint​(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                // var transform = new AffineTransform();
                // transform.setToScale(1.0, 1.0);
                // g.setTransform(transform);
                for (var layer: layers) {
                    paintDirect(layer, g);
                    // paintLayer(layer, g);
                }
                var t3 = System.nanoTime();
                Toolkit.getDefaultToolkit().sync();
                var ddt = (System.nanoTime() - t3) / 1_000_000.0;
                System.out.println("┌ sync took " + ddt + " ms");
                g.dispose();

                var dt = (System.nanoTime() - t1) / 1_000_000.0;
                System.out.println("┌ Compositor took " + dt + " ms");
            } while (strategy.contentsRestored());
            var t2 = System.nanoTime();
            strategy.show();
            var dt = (System.nanoTime() - t2) / 1_000_000.0;
            System.out.println("┌ show took " + dt + " ms");
        } while (strategy.contentsLost());

        var dt = (System.nanoTime() - t0) / 1_000_000.0;
        System.out.println("Frame took " + dt + " ms\n");
    }
}

class FPS extends Layer {
    FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, false);
    Font font = Awt.uiFont.deriveFont(24f);
    long lastT = System.nanoTime();

    long[] times = new long[10];
    int idx = 0;

    public FPS() {
        size = new Dimension(200, 100);
        pos = new Point(1100, 30);
    }

    Rectangle2D drawLine(Graphics2D g, String line, int x, int y) {
        TextLayout layout = new TextLayout(line, font, frc);
        // var bounds = layout.getBounds();
        // g.setColor(new Color(255, 0, 0, 50));
        // g.fillRect(x, y, (int) layout.getAdvance(), (int) (layout.getAscent() + layout.getDescent()));

        g.setColor(Color.BLACK);
        layout.draw(g, x - (int) (layout.getLeading() + layout.getAdvance()), y + layout.getAscent());
        return new Rectangle2D.Double(x, y, layout.getLeading() + layout.getAdvance(), layout.getAscent() + layout.getDescent());
    }

    public void paintImpl(Graphics2D g) {
        needsRepaint = true;
        var t = System.nanoTime();
        times[idx] = t - lastT;
        idx = (idx + 1) % times.length;
        lastT = t;

        long min = Arrays.stream(times).reduce(Long::min).getAsLong();
        long max = Arrays.stream(times).reduce(Long::max).getAsLong();
        if (min > 0) {
            var b1 = drawLine(g, "min " + 1_000_000_000 / max, 0, 24);
            drawLine(g, "max " + 1_000_000_000 / min, 0, (int) (b1.getY() + b1.getHeight()));
        }
    }
}


class Foreground extends Layer {
    FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
    Font font = Awt.uiFont.deriveFont(24f);

    public Foreground() {
        size = new Dimension(200, 100);
    }

    public void paintImpl(Graphics2D g) {
        needsRepaint = true;
        var angle = (System.currentTimeMillis() % 10000) / 10000.0 * 2.0 * Math.PI;
        pos = new Point((int) (200.0 + Math.sin(angle) * 100.0),
                        (int) (150.0 + Math.cos(angle) * 100.0));

        g.setColor(Color.BLUE);
        TextLayout layout = new TextLayout("" + System.currentTimeMillis(), font, frc);
        layout.draw(g, 0, layout.getAscent());
    }
}


class Background extends Layer {
    public Background() {
        size = new Dimension(1300, 800);
    }

    public void paintImpl(Graphics2D g) {
        g.setColor(new Color(150, 200, 150));

        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++ y) {
                if ((x + y) % 2 == 0) {
                    g.fillRect​((int) (size.width * x / 8.0),
                               (int) (size.height * y / 8.0),
                               (int) (size.width / 8.0),
                               (int) (size.height / 8.0));
                }
            }
        }

        g.setColor(Color.RED);
        g.drawLine(0, 0, size.width, size.height);
    }
}

class Toggles extends Layer {
    public Toggles() {
        pos = new Point(500, 100);
    }

    public void paintImpl(Graphics2D g) {
        var g2 = g.create();
        for (int i = 0; i < 20; ++i) {
            paintToggle(g, false);
            g.translate(69, 0);
            paintToggle(g, true);
            g.translate(-69, 33);
        }
        g2.dispose();
    }

    public void paintToggle(Graphics2D g, boolean on) {
        var g2 = (Graphics2D) g.create();
        var bg = on ? new Color(53/256f, 199/256f, 89/256f) : new Color(233/256f, 233/256f, 235/256f);
        g2.setColor(bg);
        
        g2.clip(new RoundRectangle2D.Double(0.0, 0.0, 51.0, 31.0, 31.0, 31.0));
        g2.fillRect(0, 0, 51, 31);

        // g2.fillRoundRect(0, 0, 51, 31, 31, 31);
        
        g2.setColor(Color.WHITE);
        g2.fillOval(on ? 22 : 2, 2, 27, 27);
        g2.dispose();
    }
}

class Screen extends Layer {
    FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
    // Map<TextAttribute, ?> attrs = Map.of(TextAttribute.LIGATURES, Integer.valueOf(0), TextAttribute.KERNING, Integer.valueOf(0));
    Font font = Awt.firacode.deriveFont(11f); // .deriveFont(attrs);
    Map<String, TextLayout> layoutCache = new HashMap<>();

    public Screen() {
        size = new Dimension(1300, 800);
    }

    public Rectangle2D paintLine(Graphics2D g, String line, int x, int y) {
        if (line.length() > 0) {
            var layout = layoutCache.get(line);
            if (layout == null) {
                layout = new TextLayout(line, font, frc);
                layoutCache.put(line, layout);
            }   
            var bounds = layout.getBounds();
            layout.draw(g, x, (int) (y + layout.getAscent()));
            return bounds;
        }
        return null;
    }

    public void paintImpl(Graphics2D g) {
        g.setColor(Color.BLACK);
        var x = 0;
        var y = 30;

        for (var file: files) {
            paintLine(g, file, 0, y);
            y += 17;
        }

        y = 60;
        for (var line: code) {
            paintLine(g, line, 200, y);
            y += 17;
        }

        // for (var path: paths) {
        //     if (y > 800) { y = 34; x += 400; }
        //     var idx = path.lastIndexOf("/");
        //     g.setColor(Color.BLACK);
        //     var bounds = paintLine(g, path.substring(idx + 1, path.length()), x, y);
        //     g.setColor(Color.GRAY);
        //     paintLine(g, path.substring(0, idx), (int) (x + bounds.getX() + bounds.getWidth() + 10), y);
        //     y += 17;
        // }
    }   

    String[] files = new String[] {
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

    String[] code = new String[] {
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

    String paths[] = new String[] {
        "./deps.edn",
        "./target/classes/onair/frontend/indent/Indent.class",
        "./target/classes/onair/frontend/indent/Indenter.class",
        "./target/classes/onair/frontend/strings/FixingLayoutTypoTolerantMatcher.class",
        "./target/classes/onair/frontend/strings/FList.class",
        "./target/classes/onair/frontend/strings/CharArrayCharSequence.class",
        "./target/classes/onair/frontend/strings/NameUtil$Matcher.class",
        "./target/classes/onair/frontend/strings/TypoTolerantMatcher$Error.class",
        "./target/classes/onair/frontend/strings/TypoTolerantMatcher.class",
        "./target/classes/onair/frontend/strings/NameUtil.class",
        "./target/classes/onair/frontend/strings/CharSequenceWithStringHash.class",
        "./target/classes/onair/frontend/strings/TypoTolerantMatcher$Fragment.class",
        "./target/classes/onair/frontend/strings/MatcherWithFallback.class",
        "./target/classes/onair/frontend/strings/NameUtilCore.class",
        "./target/classes/onair/frontend/strings/NameUtil$MatcherBuilder.class",
        "./target/classes/onair/frontend/strings/TypoTolerantMatcher$ErrorState.class",
        "./target/classes/onair/frontend/strings/TypoTolerantMatcher$Range.class",
        "./target/classes/onair/frontend/strings/StringSearcher.class",
        "./target/classes/onair/frontend/strings/MinusculeMatcherImpl.class",
        "./target/classes/onair/frontend/strings/Segment.class",
        "./target/classes/onair/frontend/strings/MinusculeMatcher.class",
        "./target/classes/onair/frontend/strings/TypoTolerantMatcher$MissError.class",
        "./target/classes/onair/frontend/strings/TypoTolerantMatcher$TypoError.class",
        "./target/classes/onair/frontend/strings/CharSequenceBackedByArray.class",
        "./target/classes/onair/frontend/strings/StringUtil$2.class",
        "./target/classes/onair/frontend/strings/TextRange.class",
        "./target/classes/onair/frontend/strings/Matcher.class",
        "./target/classes/onair/frontend/strings/CharArrayUtil.class",
        "./target/classes/onair/frontend/strings/TypoTolerantMatcher$SwapError.class",
        "./target/classes/onair/frontend/strings/FixingLayoutMatcher.class",
        "./target/classes/onair/frontend/strings/StringUtil$1.class",
        "./target/classes/onair/frontend/strings/StringUtil$BombedCharSequence.class",
        "./target/classes/onair/frontend/strings/TypoTolerantMatcher$Session.class",
        "./target/classes/onair/frontend/strings/FList$1.class",
        "./target/classes/onair/frontend/strings/StringUtilRt.class",
        "./target/classes/onair/frontend/strings/NameUtil$MatchingCaseSensitivity.class",
        "./target/classes/onair/frontend/strings/ProcessCanceledException.class",
        "./target/classes/onair/frontend/strings/StringUtil.class",
        "./target/classes/onair/frontend/ast/ASTContainer$Job.class",
        "./target/classes/onair/frontend/ast/Future.class",
        "./target/classes/onair/frontend/ast/ASTContainer.class",
        "./target/classes/onair/frontend/ast/ParserPool.class",
        "./target/classes/onair/frontend/ast/JSitterText.class",
        "./target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst",
        "./target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst",
        "./target/maven-archiver/pom.properties",
        "./target/frontend-0.1-SNAPSHOT.jar",
        "./resources/.DS_Store",
        "./resources/icons/directory-closed.svg",
        "./resources/icons/directory-open.svg",
        "./resources/Fira Code/variable_ttf/FiraCode-VF.ttf",
        "./resources/Fira Code/ttf/FiraCode-Medium.ttf",
        "./resources/Fira Code/ttf/FiraCode-Regular.ttf",
        "./resources/Fira Code/ttf/FiraCode-Retina.ttf",
        "./resources/Fira Code/ttf/FiraCode-Bold.ttf",
        "./resources/Fira Code/ttf/FiraCode-Light.ttf",
        "./pom.xml",
        "./.gitignore",
        "./frontend.iml",
        "./.cpcache/927574275.libs",
        "./.cpcache/927574275.cp",
        "./src/test/clj/onair/bench/fixtures.clj",
        "./src/test/clj/onair/tests/fsd.clj",
        "./src/test/clj/onair/tests/kernel.clj",
        "./src/main/java/onair/frontend/indent/Indent.java",
        "./src/main/java/onair/frontend/indent/Indenter.java",
        "./src/main/java/onair/frontend/strings/TextRange.java",
        "./src/main/java/onair/frontend/strings/Matcher.java",
        "./src/main/java/onair/frontend/strings/ProcessCanceledException.java",
        "./src/main/java/onair/frontend/strings/TypoTolerantMatcher.java",
        "./src/main/java/onair/frontend/strings/NameUtilCore.java",
        "./src/main/java/onair/frontend/strings/CharArrayCharSequence.java",
        "./src/main/java/onair/frontend/strings/Segment.java",
        "./src/main/java/onair/frontend/strings/FixingLayoutTypoTolerantMatcher.java",
        "./src/main/java/onair/frontend/strings/FixingLayoutMatcher.java",
        "./src/main/java/onair/frontend/strings/CharSequenceBackedByArray.java",
        "./src/main/java/onair/frontend/strings/StringUtil.java",
        "./src/main/java/onair/frontend/strings/CharArrayUtil.java",
        "./src/main/java/onair/frontend/strings/StringSearcher.java",
        "./src/main/java/onair/frontend/strings/MinusculeMatcher.java",
        "./src/main/java/onair/frontend/strings/CharSequenceWithStringHash.java",
        "./src/main/java/onair/frontend/strings/FList.java",
        "./src/main/java/onair/frontend/strings/NameUtil.java",
        "./src/main/java/onair/frontend/strings/MinusculeMatcherImpl.java",
        "./src/main/java/onair/frontend/strings/MatcherWithFallback.java",
        "./src/main/java/onair/frontend/strings/StringUtilRt.java",
        "./src/main/java/onair/frontend/ast/Future.java",
        "./src/main/java/onair/frontend/ast/JSitterText.java",
        "./src/main/java/onair/frontend/ast/ASTContainer.java",
        "./src/main/java/onair/frontend/ast/ParserPool.java",
        "./src/main/clj/onair/frontend/editors.clj",
        "./src/main/clj/onair/frontend/layout.clj",
        "./src/main/clj/onair/frontend/ui/tree.clj",
        "./src/main/clj/onair/frontend/ui/render.clj",
        "./src/main/clj/onair/frontend/ui/core.clj",
        "./src/main/clj/onair/frontend/ui/navigation.clj",
        "./src/main/clj/onair/frontend/ui/keyboard.clj",
        "./src/main/clj/onair/frontend/ui/list.clj",
        "./src/main/clj/onair/frontend/poly.clj",
        "./src/main/clj/onair/frontend/file_tree.clj",
        "./src/main/clj/onair/frontend/blob.clj",
        "./src/main/clj/onair/frontend/reducers.clj",
        "./src/main/clj/onair/frontend/db.clj",
        "./src/main/clj/onair/frontend/workspace.clj",
        "./src/main/clj/onair/frontend/patterns.clj",
        "./src/main/clj/onair/frontend/interop.clj",
        "./src/main/clj/onair/frontend/tags.clj",
        "./src/main/clj/onair/frontend/fsd.clj",
        "./src/main/clj/onair/frontend/layout/window.clj",
        "./src/main/clj/onair/frontend/layout/buffer.clj",
        "./src/main/clj/onair/frontend/layout/stack.clj",
        "./src/main/clj/onair/frontend/layout/sidebar.clj",
        "./src/main/clj/onair/frontend/layout/core.clj",
        "./src/main/clj/onair/frontend/layout/layout-actions.edn",
        "./src/main/clj/onair/frontend/utils.cljc",
        "./src/main/clj/onair/frontend/spec/layout.clj",
        "./src/main/clj/onair/frontend/spec/db.clj",
        "./src/main/clj/onair/frontend/spec/patterns.clj",
        "./src/main/clj/onair/frontend/spec/tags.clj",
        "./src/main/clj/onair/frontend/spec/transition.clj",
        "./src/main/clj/onair/frontend/spec/core.clj",
        "./src/main/clj/onair/frontend/spec/protocol/text_search.clj",
        "./src/main/clj/onair/frontend/spec/protocol/blob.clj",
        "./src/main/clj/onair/frontend/spec/protocol/workspace.clj",
        "./src/main/clj/onair/frontend/spec/protocol/fsd.clj",
        "./src/main/clj/onair/frontend/spec/protocol/tx.clj",
        "./src/main/clj/onair/frontend/spec/protocol/core.clj",
        "./src/main/clj/onair/frontend/spec/protocol/document.clj",
        "./src/main/clj/onair/frontend/spec/protocol/snapshot.clj",
        "./src/main/clj/onair/frontend/spec/document.clj",
        "./src/main/clj/onair/frontend/spec/keymap.clj",
        "./src/main/clj/onair/frontend/spec/triggers.clj",
        "./src/main/clj/onair/frontend/spec/plugin.clj",
        "./src/main/clj/onair/frontend/navigation/navigation-actions.edn",
        "./src/main/clj/onair/frontend/navigation/global_text_search.clj",
        "./src/main/clj/onair/frontend/navigation/core.clj",
        "./src/main/clj/onair/frontend/navigation/README",
        "./src/main/clj/onair/frontend/navigation/palette.clj",
        "./src/main/clj/onair/frontend/navigation/goto_file.clj",
        "./src/main/clj/onair/frontend/core.clj",
        "./src/main/clj/onair/frontend/ot.clj",
        "./src/main/clj/onair/frontend/kernel.clj",
        "./src/main/clj/onair/frontend/extensions.clj",
        "./src/main/clj/onair/frontend/reload.clj",
        "./src/main/clj/onair/frontend/README",
        "./src/main/clj/onair/frontend/lang.clj",
        "./src/main/clj/onair/frontend/document.clj",
        "./src/main/clj/onair/frontend/actions.clj",
        "./src/main/clj/onair/frontend/themes.clj",
        "./src/main/clj/onair/frontend/main.clj",
        "./src/main/clj/onair/frontend/rentity.clj",
        "./src/main/clj/onair/frontend/keymap.clj",
        "./src/main/clj/onair/frontend/triggers.clj",
        "./src/main/clj/onair/frontend/snapshot.clj",
        "./src/main/clj/onair/frontend/user.clj",
        "./src/main/clj/onair/frontend/plugins.clj",
        "./src/main/clj/onair/frontend/editor/text_search.clj",
        "./src/main/clj/onair/frontend/editor/view.clj",
        "./src/main/clj/onair/frontend/editor/highlighting.clj",
        "./src/main/clj/onair/frontend/editor/render.clj",
        "./src/main/clj/onair/frontend/editor/core.clj",
        "./src/main/clj/onair/frontend/editor/controller.clj",
        "./src/main/clj/onair/frontend/editor/instructions.clj",
        "./src/main/clj/onair/frontend/editor/editor-actions.edn",
        "./src/main/clj/onair/frontend/editor/composite.clj"
    };
}