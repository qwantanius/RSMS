package RSMS.common.exceptions;

import RSMS.RollingStocks.BasicRollingStock;

public class RollingStockTargetMustNotBeNullException extends Exception {
    public RollingStockTargetMustNotBeNullException(BasicRollingStock car) {
        super("Attaching target for " + car + " must not be null ");
    }
}
