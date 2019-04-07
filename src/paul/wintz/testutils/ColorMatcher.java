package paul.wintz.testutils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import paul.wintz.utils.color.ColorUtils;

import static org.hamcrest.Matchers.is;
import static paul.wintz.utils.color.ColorUtils.*;

public class ColorMatcher extends BaseMatcher<Integer> {
    private final int color;
    private final boolean isFullyTransparent;

    public static Matcher<Integer> isRed() {
        return is(new ColorMatcher(RED));
    }

    public static Matcher<Integer> isGreen() {
        return is(new ColorMatcher(GREEN));
    }

    public static Matcher<Integer> isBlue() {
        return is(new ColorMatcher(BLUE));
    }

    public static Matcher<Integer> isWhite() {
        return is(new ColorMatcher(WHITE));
    }

    public static Matcher<Integer> isFullyTransparent() {
        return is(new ColorMatcher(-1, true));
    }

    public static Matcher<Integer> isBlack() {
        return is(new ColorMatcher(BLACK));
    }

    private ColorMatcher(int color) {
        this(color, false);
    }

    private ColorMatcher(int color, boolean isFullyTransparent) {
        this.color = color;
        this.isFullyTransparent = isFullyTransparent;
    }

    @Override
    public boolean matches(Object item) {
        if(item == null) {
            return false;
        }
        if (!(item instanceof Integer)) {
            return false;
        }
        int intItem = (Integer) item;
        if(isFullyTransparent) {
            return ColorUtils.alpha(intItem) == 0;
        }
        return intItem == color;
    }

    @Override
    public void describeTo(Description description) {
        if(isFullyTransparent) {
            description.appendText("Fully Transparent");
        } else {
            description.appendText(ColorUtils.toString(color));
        }
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        if(item instanceof Integer){
            int intItem = (Integer) item;
            super.describeMismatch(ColorUtils.toString(intItem), description);
        } else {
            super.describeMismatch(item, description);
        }
    }
}
