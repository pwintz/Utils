package paul.wintz.canvas;

import paul.wintz.utils.logging.Lg;

public final class LayerRangeSetter {
    private static final String TAG = Lg.makeTAG(LayerRangeSetter.class);
    private Layer<?> layer;

    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    public LayerRangeSetter(Layer<?> layer) {
        this.layer = layer;
    }

    public final void setXRange(float minX, float maxX) {
        this.minX = minX;
        this.maxX = maxX;
        updateXRange();
    }

    public final void setYRange(float minY, float maxY) {
        this.minY = minY;
        this.maxY = maxY;
        updateYRange();
        Lg.d(TAG, "Set range to (" + minY + ", " + maxY + ")");
    }

    public final void setMinX(float minX) {
        this.minX = minX;
        updateXRange();
    }

    public final void setMaxX(float maxX) {
        this.maxX = maxX;
        updateXRange();
    }

    public final void setMinY(float minY) {
        this.minY = minY;
        updateYRange();
    }

    public final void setMaxY(float maxY) {
        this.maxY = maxY;
        updateYRange();
    }

    private void updateXRange() {
        float rangeX = maxX - minX;
        float scaleX = rangeX / layer.getWidth();
        float centerX = rangeX / 2.0f;

        layer.setCenterX(centerX / layer.getWidth());
        layer.setScaleX(scaleX / layer.getWidth());
    }

    private void updateYRange() {
        float yRange = maxY - minY;
        float scaleY = -yRange / layer.getHeight();
        float centerY = -yRange / 2.0f;

        layer.setCenterY(centerY);
        layer.setScaleY(scaleY);
    }
}
