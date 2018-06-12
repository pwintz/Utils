package paul.wintz.canvas;

import static com.google.common.base.Preconditions.checkArgument;
import static paul.wintz.utils.logging.Lg.makeTAG;

import java.util.*;

import paul.wintz.utils.logging.Lg;

public class LayeredCanvas<L> {
    private static final String TAG = makeTAG(LayeredCanvas.class);

    protected List<Layer<L>> layers; // the bottom layer is layer 0.

    private float scale = 0.2f;
    private float rotation = 0.0f;
    private float centerX = 0.5f;
    private float centerY = 0.5f;
    private boolean preserveGraph = false;

    protected LayeredCanvas(Layer<L>... layers) {
        checkArgument(layers.length > 0);
        this.layers = Arrays.asList(layers);
    }

    public void handleNewFrame() {
        for (final Layer<L> layer : layers) {

            layer.setCenter(getCenterX(), getCenterY());
            layer.setRotation(getRotation());
            layer.setScale(scale);
            layer.handleNewFrame();
        }
    }

    public void setSize(int width, int height) {
        Lg.v(TAG, "setSize(width=%d, height=%d), preserveGraph=%b", width, height, preserveGraph);
        if (preserveGraph)
            return;

        for (final Layer<L> layer : layers) {
            layer.setSize(width, height);
        }

    }

    public void setScaleToFit(float maxRadiusToFit) {
        if (preserveGraph)
            return;
        scale = /*options.zoom.getDecimal() * */getWidth() / (2 * maxRadiusToFit);
    }

    public int getWidth() {
        return layers.get(0).getWidth();
    }

    public int getHeight() {
        return layers.get(0).getHeight();
    }

    public double getScale() {
        return layers.get(0).getAverageScale();
    }

    protected final Layer<L> getLayer(int i){
        return layers.get(i);
    }

    //  public void saveImage(File f) {
    //      layers.saveImage(f);
    //      System.out.println("Image saved to '" + f.getAbsolutePath() + "'. Its size is: " + Utils.fileSize(f));
    //  }

    public void clearAll() {
        Lg.v(TAG, "clearAll(), preserveGraph=%b", preserveGraph);
        if (preserveGraph)
            return;

        for (final Layer<L> layer : layers) {
            layer.clear();
        }
    }

    public List<Layer<L>> getLayers() {
        return layers;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        Lg.v(TAG, "setCenterX(%.2f)", centerX);
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        Lg.v(TAG, "setCenterY(%.2f)", centerY);
        this.centerY = centerY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public String toString() {
        return String.format("LayeredCanvas (%dx%d)", getWidth(), getHeight());
    }
}
