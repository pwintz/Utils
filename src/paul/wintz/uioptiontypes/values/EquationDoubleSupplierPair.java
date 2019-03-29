package paul.wintz.uioptiontypes.values;

import java.util.function.DoubleSupplier;

public class EquationDoubleSupplierPair {
    public final String equation;
    public final DoubleSupplier valueSupplier;

    EquationDoubleSupplierPair(String equation, DoubleSupplier valueSupplier) {
        this.equation = equation;
        this.valueSupplier = valueSupplier;
    }

}
