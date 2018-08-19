package paul.wintz.math;

import static java.lang.Math.*;
import static paul.wintz.math.Vector2D.angleBetween;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import paul.wintz.math.Vector2D;
import paul.wintz.utils.*;

public final class Vector2DTest {

    private static final double TOLERANCE = 0.0001;

    @Test
    public final void testAngleBetween() {
        for(double angle1 = -Math.PI / 2; angle1 < Math.PI / 2.0; angle1 += 0.1) {
            for(double angle2 = -Math.PI / 2; angle2 < Math.PI / 2.0; angle2 += 0.1) {

                final double angleBetween = angleBetween(makeUnitVectorAtAngle(angle1), makeUnitVectorAtAngle(angle2));
                assertEquals("angle1=" + angle1 + ", angle2=" + angle2,
                        Utils.shiftedModulus(angle2 - angle1, 0, 2 * PI),
                        angleBetween,
                        TOLERANCE);

            }
        }
    }

    @Test
    public final void testCrossProduct() {
        for(double angle = -Math.PI; angle < Math.PI; angle += 0.1) {
            {
                final Vector2D vector1 = makeUnitVectorAtAngle(angle);
                final Vector2D vector2 = makeUnitVectorAtAngle(angle + PI / 2.0);

                final double crossProduct = Vector2D.crossProduct(vector1, vector2);
                assertEquals(1, crossProduct, TOLERANCE);
            }
            {
                final Vector2D vector1 = makeUnitVectorAtAngle(angle);
                final Vector2D vector2 = makeUnitVectorAtAngle(angle + PI / 6.0);

                final double crossProduct = Vector2D.crossProduct(vector1, vector2);
                assertEquals(0.5, crossProduct, TOLERANCE);
            }

        }
    }

    private static Vector2D makeUnitVectorAtAngle(double angle) {
        return new Vector2D(cos(angle), sin(angle));
    }

}
