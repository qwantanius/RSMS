package RSMS.common.exceptions;

public class RollingStockUnloadingFailed extends Exception {

    public final static String MSG_TEMPLATE = "You are trying unload too much units (loaded: %d, to be unloaded %d)";

    public RollingStockUnloadingFailed(int alreadyLoadedUnitsAmount, int aboutToBeLoadedUnitsAmount) {
        super(
            String.format(
                RollingStockUnloadingFailed.MSG_TEMPLATE,
                alreadyLoadedUnitsAmount,
                aboutToBeLoadedUnitsAmount
            )
        );
    }
}
