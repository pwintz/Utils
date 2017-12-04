package paul.wintz.canvas;

public interface LayerFactory<L> {

	Layer<L> makeLayer(int width, int height);

}
