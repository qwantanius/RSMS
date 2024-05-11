package RSMS.common.exceptions;

import RSMS.RollingStocks.BasicRollingStock;

public class RollingStockIsNotAttachedException extends RuntimeException {
    public RollingStockIsNotAttachedException(BasicRollingStock car) {
        super("Cannot detach " + car + ", it is not attached");
    }
}
