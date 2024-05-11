package RSMS.common.exceptions;

import RSMS.RollingStocks.BasicRollingStock;

public class RollingStockAlreadyConnectedException extends Exception {
    public RollingStockAlreadyConnectedException(BasicRollingStock car) {
        super(
            "Cannot attach " + car + ", its already attached: " +
            car.getCouplingManager().getConnectedRollingStock()
        );
    }
}
