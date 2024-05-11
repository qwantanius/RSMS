package RSMS.common.exceptions;

import RSMS.RollingStocks.BasicRollingStock;

public class RollingStockRecursiveConnectionException extends Exception {
    public RollingStockRecursiveConnectionException(BasicRollingStock car, BasicRollingStock target) {
        super("Cannot attach railroad car " + car.getUuid() + " to target " + target.getUuid() + " as this cause recursive connection");
    }
}
