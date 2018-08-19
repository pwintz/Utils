package paul.wintz.canvas;

import org.junit.Test;
import paul.wintz.utils.color.ColorUtils;

import java.awt.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static paul.wintz.utils.color.ColorUtils.*;

public class PainterTest {

    Painter painter = new Painter();

    @Test
    public void defaultValues() {
        assertThat(painter.getFill(), is(equalTo(TRANSPARENT)));
        assertThat(painter.getStroke(), is(equalTo(TRANSPARENT)));
        assertThat(painter.getStrokeWeight(), is(equalTo(0.0f)));
        assertTrue(painter.isFilled());
        assertFalse(painter.isStroked());
    }

    @Test
    public void canChainSetters() {
        //noinspection unused
        Painter p = this.painter.setFill(0x00ff00)
                .setStroke(0xff0000)
                .setStrokeWeight(1)
                .setStroked(true)
                .setFilled(true)
                .setOnlyFilled()
                .setOnlyStroked();
    }

    @Test
    public void setStroke() {
        painter.setStroke(GREEN);
        assertThat(painter.getStroke(), is(equalTo(GREEN)));
    }

    @Test
    public void setFill() {
        painter.setFill(CYAN);
        assertThat(painter.getFill(), is(equalTo(CYAN)));
    }

    @Test
    public void setStrokeWeight() {
        painter.setStrokeWeight(3.14f);
        assertThat(painter.getStrokeWeight(), is(equalTo(3.14f)));
    }

    @Test
    public void setStrokeWeightClipsUpToZero() {
        painter.setStrokeWeight(-0.01f);
        assertThat(painter.getStrokeWeight(), is(equalTo(0f)));
    }

    @Test
    public void setStrokeAndWeight() {
        painter.setStrokeAndWeight(YELLOW, 2.17f);

        assertThat(painter.getStroke(), is(equalTo(YELLOW)));
        assertThat(painter.getStrokeWeight(), is(equalTo(2.17f)));
    }

    @Test
    public void setOnlyStroke() {
        painter.setOnlyStroked();

        assertTrue(painter.isStroked());
        assertFalse(painter.isFilled());
    }

    @Test
    public void setOnlyFilled() {
        painter.setOnlyFilled();

        assertTrue(painter.isFilled());
        assertFalse(painter.isStroked());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void toStringDoesNotChoke() {
        painter.toString();
        painter.setStroked(true).toString();
        painter.setFilled(false).toString();
    }

    @Test
    public void hashCodeAndEqualsForEqualPainters() {
        Painter p0 = new Painter();
        Painter p1 = new Painter();

        assertThat(p0, is(equalTo(p1)));
        assertThat(p0.hashCode(), is(equalTo(p1.hashCode())));
    }

    @Test
    public void hashCodeAndEqualsForUnequalPainters() {
        Painter p0 = new Painter().setFill(1234);
        Painter p1 = new Painter();

        assertThat(p0, is(not(equalTo(p1))));
        assertThat(p0.hashCode(), is(not(equalTo(p1.hashCode()))));
    }
}