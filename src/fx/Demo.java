package fx;

import javafx.scene.canvas.*;

public abstract class Demo {

    public String[] variants = new String[] { "Default" };
    public int variantIdx = 0;
    
    public String variantTitle() {
        return variants[variantIdx];
    }

    public void changeVariant(int delta) {
        variantIdx = (variantIdx + variants.length + delta) % variants.length;
    }

    public abstract void draw(GraphicsContext gc, int width, int height, float dpi);
}