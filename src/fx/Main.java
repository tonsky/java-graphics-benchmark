package fx;

import java.util.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.effect.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;

import fx.*;

public class Main extends Application {
    int width = 1440;
    int height = 810;
    long frames = 0;

    Demo[] demos = new Demo[] {
        new CirclesDemo(),
        // new EverythingDemo(),
        new ShadowsDemo(),
    };
    int demoIdx = 1;

    public static void main(String[] args) throws Exception {
        launch();
    }

    public void repaint(GraphicsContext gc) {
        gc.clearRect(0, 0, width, height);
        demos[demoIdx].draw(gc, width, height, 1f, 0, 0);
        ++frames;
    }

    public void updateTitle(Stage stage) {
        Demo demo = demos[demoIdx];
        String title = demo.getClass().getSimpleName() + " - " + demo.variants[demo.variantIdx] + " - " + frames + " fps";
        stage.setTitle(title);
        if (frames > 0)
            System.out.println(title);
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene s = new Scene(root, width, height);

        final Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        repaint(gc);
        root.getChildren().add(canvas);

        s.setOnKeyPressed((e) -> {
            Demo demo = demos[demoIdx];
            if (KeyCode.LEFT == e.getCode()) {
                demoIdx = (demoIdx + demos.length - 1) % demos.length;
                frames = 0;
                updateTitle(stage);
            } else if (KeyCode.RIGHT == e.getCode()) {
                demoIdx = (demoIdx + 1) % demos.length;
                frames = 0;
                updateTitle(stage);
            } else if (KeyCode.UP == e.getCode()) {
                if (demo.variants.length > 1) {
                    demo.variantIdx = (demo.variantIdx + demo.variants.length - 1) % demo.variants.length;
                    frames = 0;
                    updateTitle(stage);
                }
            } else if (KeyCode.DOWN == e.getCode()) {
                if (demo.variants.length > 1) {
                    demo.variantIdx = (demo.variantIdx + 1) % demo.variants.length;
                    frames = 0;
                    updateTitle(stage);
                }
            }
        });
        updateTitle(stage);

        stage.setScene(s);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Platform.runLater(() -> 
                    repaint(canvas.getGraphicsContext2D());
                // );
            }
        };
        timer.start();

        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(() -> { updateTitle(stage); frames = 0; });
            }
        }, 1000, 1000);
    }
}