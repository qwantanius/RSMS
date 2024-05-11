package RSMS.common.exceptions;

public class RollingStockNotFoundException extends Exception {
    public RollingStockNotFoundException(String searchByUUID) {
        super("Rolling stock " + searchByUUID + " not found");
    }
}
