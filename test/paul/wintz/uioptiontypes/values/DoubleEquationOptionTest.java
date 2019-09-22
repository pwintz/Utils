package paul.wintz.uioptiontypes.values;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import paul.wintz.uioptiontypes.ValuesSuppliers;

@RunWith(MockitoJUnitRunner.class)
public class DoubleEquationOptionTest {
    //TODO: Add tests

    DoubleEquationOption.Builder builder = DoubleEquationOption.builder();

    @Mock ValueOption.ValueChangeCallback<EquationDoubleSupplierPair> callback;

    @Test
    public void canInitialize() {
        DoubleEquationOption equationOption = builder.addViewValueChangeCallback(callback).setValuesSupplier(ValuesSuppliers.builder().build())
                .initial("2+3").build();

    }
}