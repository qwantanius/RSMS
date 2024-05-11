package RSMS.common.exceptions;

import RSMS.RollingStocks.BasicRollingStock;

public class RollingStockTypeDoesNotMatchWithTransportationLoadingException extends RuntimeException {
    public RollingStockTypeDoesNotMatchWithTransportationLoadingException(BasicRollingStock rs) {
        super("Cannot load rolling stock " + rs.getUuid() + " loading type mismatch");
    }
}
