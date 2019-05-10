package paul.wintz.canvas;

import com.google.common.collect.ImmutableList;
import paul.wintz.utils.logging.Lg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static paul.wintz.utils.logging.Lg.makeTAG;

public class LayeredCanvas<L> {
    private static final String TAG = makeTAG(LayeredCanvas.class);

    protected final ImmutableList<Layer<L>> layers; // the bottom layer is layer 0.

    // zoom is a user controlled parameter to adjust the scaling.
    private float zoom = 1.0f;
    private float rotation = 0.0f;
    private float centerX = 0.5f;
    private float centerY = 0.5f;
    private boolean preserveGraph = false;
    private float drawingWidth;
    private float maxRadiusToFit;

    protected LayeredCanvas(Layer<L> layer) {
        this(Collections.singletonList(layer));
    }

    protected LayeredCanvas(Layer<L> bottomLayer, Layer<L> topLayer) {
        this(Arrays.asList(bottomLayer, topLayer));
    }

    protected  LayeredCanvas(List<Layer<L>> layers){
        checkArgument(!layers.isEmpty());
        this.layers = ImmutableList.copyOf(layers);
    }

    public void handleNewFrame() {
        // scale is the amount that the image is scaled in order to make the drawing fit in the available area
        float scale = zoom * getWidth() / (2 * maxRadiusToFit);
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
        this.maxRadiusToFit = maxRadiusToFit;
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
        Lg.v(TAG, "setRotation(%.2f)", rotation);
        this.rotation = rotation;
    }

    public void setPreserveGraph(boolean preserveGraph){
        Lg.v(TAG, "setPreserveGraph(%b)", preserveGraph);
        this.preserveGraph = preserveGraph;
    }

    public boolean getPreserveGraph() {
        return preserveGraph;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getZoom() {
        return zoom;
    }

    @Override
    public String toString() {
        return String.format("LayeredCanvas (%dx%d)", getWidth(), getHeight());
    }
}
