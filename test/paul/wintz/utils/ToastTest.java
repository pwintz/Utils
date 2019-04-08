package paul.wintz.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ToastTest {

    @Mock Toast.Toaster toaster;

    @Test
    public void setToasterTest() {
        Toast.setToaster(toaster);
        Toast.show("A message");

        verify(toaster).show("A message");
    }
}