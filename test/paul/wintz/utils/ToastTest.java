package paul.wintz.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import paul.wintz.stringids.StringId;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ToastTest {

    @Mock Toast.Toaster toaster;

    @Mock StringId stringId;

    @Test
    public void toasterTest() {
        Toast.setToaster(toaster);

        Toast.show("A message");
        verify(toaster).show("A message");

        Toast.show(stringId);
        verify(toaster).show(stringId);

        Toast.show(stringId, 1, "string", stringId);
        verify(toaster).show(stringId, 1, "string", stringId);

        Toast.show("<string format>", 1, "string", stringId);
        verify(toaster).show("<string format>", 1, "string", stringId);

        verifyNoMoreInteractions(toaster);
    }
}