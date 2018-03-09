package paul.wintz.canvas.testing;

import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.*;

public class MockLayerTest {

	MockLayer layer = new MockLayer(100, 100);

	@Test
	public final void testSimple() {
		layer.clear();
		layer.assertEqualToRecorded("clear");
	}

	@Test
	public void testLineCoords() {
		layer.line(0, 0, 100, 100, null);
		layer.assertEqualToRecorded("line", "0x0", "100x100");
	}

	@Test
	public void testScaled() throws Exception {
		int scale = 2;
		layer.setScale(scale); // setters are not recorded
		layer.line(0, 0, 100, 100, null);
		layer.assertEqualToRecorded("line", "0x0", "200x200");
		assertThat(layer.getAverageScale(), is(equalTo((float) scale)));
	}

	@Test
	public void testShifted() throws Exception {
		layer.setCenter(50, 50); // setters are not recorded
		layer.line(0, 0, 100, 100, null);
		layer.assertEqualToRecorded("line", "50x50", "150x150");
	}

	@Test
	public void testScaledX() throws Exception {
		int scaleX = 2;
		layer.setScaleX(scaleX);
		layer.line(0, 0, 100, 100, null);
		layer.assertEqualToRecorded("line", "0x0", "200x100");
	}

	@Test
	public void testScaledY() throws Exception {
		int scaleY = 2;
		layer.setScaleY(scaleY);
		layer.line(0, 0, 100, 100, null);
		layer.assertEqualToRecorded("line", "0x0", "100x200");
	}

	@Test
	public void testScaledAndShifted() throws Exception {
		int scaleX = 3;
		int scaleY = 2;
		layer.setScaleX(scaleX);
		layer.setScaleY(scaleY);
		layer.setCenter(50, 75);
		layer.line(0, 0, 100, 100, null);
		layer.assertEqualToRecorded("line", "50x75", "350x275");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidRegEx_singleInt() throws Exception {
		layer.assertEqualToRecorded("", "1123");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidRegEx_float() throws Exception {
		layer.assertEqualToRecorded("", "1.2x4.5");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidRegEx_extraDimension() throws Exception {
		layer.assertEqualToRecorded("", "1x2x3");
	}
}
