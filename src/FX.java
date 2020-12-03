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

import fx.*;

public class FX extends Application {
    int width = 1400;
    int height = 810;
    double[] times = new double[100];
    long lastTime = 0;
    int timeIdx = 0;

    Demo[] scenes = new Demo[] {
        new BenchCirclesDemo(),
        new EverythingDemo(),
    };
    int sceneIdx = 0;

    public static void main(String[] args) throws Exception {
        launch();
    }

    public void repaint(GraphicsContext gc) {
        long now = System.nanoTime();
        times[timeIdx] = (now - lastTime) / 1000000.0;
        lastTime = now;
        timeIdx = (timeIdx + 1) % times.length;
        if (timeIdx == 0)
            System.out.println(String.format("%.0f fps", 1000.0 / Arrays.stream(times).takeWhile(t -> t > 0).average().getAsDouble()));

        gc.clearRect(0, 0, width, height);
        scenes[sceneIdx].draw(gc, width, height, 1f, 0, 0);
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene s = new Scene(root, width, height);

        final Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        repaint(gc);
        root.getChildren().add(canvas);

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
    }
}