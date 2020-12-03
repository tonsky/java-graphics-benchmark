package fx;

import javafx.scene.canvas.*;

public abstract class Demo {

    public String[] _variants = new String[] { "Default" };
    public int _variantIdx = 0;
    
    public String variantTitle() {
        return _variants[_variantIdx];
    }

    public void changeVariant(int delta) {
        _variantIdx = (_variantIdx + _variants.length + delta) % _variants.length;
    }

    public abstract void draw(GraphicsContext gc, int width, int height, float dpi, int xpos, int ypos);
}