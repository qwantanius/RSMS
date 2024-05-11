package RSMS.common.exceptions;

import RSMS.RollingStocks.managers.RollingStockLoadingManager;

public class RollingStockIsNotLoadedException extends Exception {
    public RollingStockIsNotLoadedException(RollingStockLoadingManager rollingStock) {
        super(String.format("[Rolling stock %s is not loaded]", rollingStock.toString()));
    }
}
