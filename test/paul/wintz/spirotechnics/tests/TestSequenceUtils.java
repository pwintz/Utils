package paul.wintz.spirotechnics.tests;

import org.junit.Test;

import static org.junit.Assert.*;

import paul.wintz.utils.SequenceUtils;

public class TestSequenceUtils {

    @Test
    public void testDoLengthsMatch() {

        assertTrue(SequenceUtils.doLengthsMatch(new boolean[3], new int[3], new String[3]));
        assertTrue(SequenceUtils.doLengthsMatch(new boolean[12], new int[12], new double[12], new double[12]));
        assertTrue(SequenceUtils.doLengthsMatch(new boolean[0], new Boolean[0]));

        assertFalse(SequenceUtils.doLengthsMatch(new boolean[0], new int[1]));
        assertFalse(SequenceUtils.doLengthsMatch(new boolean[3], new int[4], new Object[5]));

        try{
            SequenceUtils.doLengthsMatch(new Object());
            fail();
        } catch(final IllegalArgumentException e){
            //good.
        }

    }
    
}
