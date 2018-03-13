package paul.wintz.uioptiontypes;

import java.util.List;

import com.google.common.collect.ImmutableList;

import paul.wintz.uioptiontypes.integers.IntegerOption;
import paul.wintz.utils.color.ColorUtils;

public class ColorOption extends OptionGroup {

	private static final int COLOR_RANGE = 256;

	public interface OnColorChangeListener {
		void onColorChange(int color);
	}

	private final List<IntegerOption> rgba = ImmutableList.of(
			IntegerOption.builder().description("Red").min(0).max(COLOR_RANGE - 1).build(),
			IntegerOption.builder().description("Green").min(0).max(COLOR_RANGE - 1).build(),
			IntegerOption.builder().description("Blue").min(0).max(COLOR_RANGE - 1).build(),
			IntegerOption.builder().description("Alpha").min(0).max(COLOR_RANGE - 1).build());

	private OnColorChangeListener onColorChangeListener = color -> {
		// do nothing by default
	};

	public ColorOption() {
		this("Color");
		setColor(0, 0, 0, COLOR_RANGE);
	}

	public ColorOption(String description) {
		super(description);
		addAll(rgba);
		for(IntegerOption channelOption : rgba) {
			channelOption.addOnValueChangedListener(value -> onColorChangeListener.onColorChange(getColor()));
		}
	}

	public ColorOption(String description, int red, int green, int blue, int alpha) {
		this(description);
		setColor(red, green, blue, alpha);
	}

	public ColorOption(int red, int green, int blue, int alpha){
		this("Color", red, green, blue, alpha);
	}

	public int getColor() {
		return ColorUtils.rgba(getRed(), getGreen(), getBlue(), getAlpha());
	}

	public void setColor(int r, int g, int b, int a) {
		setRed(r);
		setGreen(g);
		setBlue(b);
		setAlpha(a);
	}

	public void setOnColorChangeListner(OnColorChangeListener onColorChangeListener) {
		this.onColorChangeListener = onColorChangeListener;
	}

	@Override
	public String toString() {
		return String.format("RGBA: [%3d, %3d, %3d, %3d]", getRed(), getGreen(), getBlue(), getAlpha());
	}

	private int getRed() {
		return rgba.get(0).getValue();
	}

	private void setRed(int r) {
		rgba.get(0).setValue(r);
	}

	private int getGreen() {
		return rgba.get(1).getValue();
	}

	private void setGreen(int g) {
		rgba.get(1).setValue(g);
	}

	private int getBlue() {
		return rgba.get(2).getValue();
	}

	private void setBlue(int b) {
		rgba.get(2).setValue(b);
	}

	private int getAlpha() {
		return rgba.get(3).getValue();
	}

	private void setAlpha(int a) {
		rgba.get(3).setValue(a);
	}
}