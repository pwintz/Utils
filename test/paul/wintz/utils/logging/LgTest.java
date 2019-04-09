package paul.wintz.utils.logging;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class LgTest {

    @Mock Logger logger;

    @Before
    public void setUp() throws Exception {
        Lg.setLogger(logger);
    }

    private class MyClass {}

    @Test
    public void makeTAG() {
        assertThat(Lg.makeTAG(MyClass.class), is(equalTo("MyClass")));
    }

    @Test
    public void testVerbose() {
        Exception exception = new Exception();
        Lg.v("TAG", "message");
        Lg.v("TAG", "message with exception", exception);
        Lg.v("TAG", "formatted message: %d %.2f", 1, 2.0);

        Mockito.verify(logger).logVerbose("TAG", "message");
        Mockito.verify(logger).logVerbose("TAG", "message with exception", exception);
        Mockito.verify(logger).logVerbose("TAG", "formatted message: 1 2.00");
    }

    @Test
    public void testDebug() {
        Exception exception = new Exception();
        Lg.d("TAG", "message");
        Lg.d("TAG", "message with exception", exception);
        Lg.d("TAG", "formatted message: %d %.2f", 1, 2.0);

        Mockito.verify(logger).logDebug("TAG", "message");
        Mockito.verify(logger).logDebug("TAG", "message with exception", exception);
        Mockito.verify(logger).logDebug("TAG", "formatted message: 1 2.00");
    }

    @Test
    public void testInfo() {
        Exception exception = new Exception();
        Lg.i("TAG", "message");
        Lg.i("TAG", "message with exception", exception);
        Lg.i("TAG", "formatted message: %d %.2f", 1, 2.0);

        Mockito.verify(logger).logInfo("TAG", "message");
        Mockito.verify(logger).logInfo("TAG", "message with exception", exception);
        Mockito.verify(logger).logInfo("TAG", "formatted message: 1 2.00");
    }

    @Test
    public void testWarning() {
        Exception exception = new Exception();
        Lg.w("TAG", "message");
        Lg.w("TAG", "message with exception", exception);
        Lg.w("TAG", "formatted message: %d %.2f", 1, 2.0);

        Mockito.verify(logger).logWarning("TAG", "message");
        Mockito.verify(logger).logWarning("TAG", "message with exception", exception);
        Mockito.verify(logger).logWarning("TAG", "formatted message: 1 2.00");
    }

    @Test
    public void testError() {
        Exception exception = new Exception();
        Lg.e("TAG", "message");
        Lg.e("TAG", "message with exception", exception);
        Lg.e("TAG", exception);
        Lg.e("TAG", "formatted message: %d %.2f", 1, 2.0);

        Mockito.verify(logger).logError("TAG", "message");
        Mockito.verify(logger).logError("TAG", "message with exception", exception);
        Mockito.verify(logger).logError("TAG", "", exception);
        Mockito.verify(logger).logError("TAG", "formatted message: 1 2.00");
    }
}