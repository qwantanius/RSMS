package RSMS.Locomotive;

import RSMS.RollingStocks.BasicRollingStock;

import java.util.ArrayList;

public class LocomotiveRollingStocksManager {

    private Locomotive locomotive;

    public LocomotiveRollingStocksManager(Locomotive locomotive) {
        this.locomotive = locomotive;
    }

    public void addRollingStock(BasicRollingStock rollingStock) {
        ArrayList<BasicRollingStock> rollingStocks = this.locomotive.getBody();
        if (!(rollingStocks.size() > this.locomotive.getMaxAmountOfRollingStocks())) {
            if (!rollingStocks.contains(rollingStock)) {
                rollingStocks.add(0, rollingStock);
                if (rollingStocks.size() > 1) {
                    rollingStock.getCouplingManager().attachTo(rollingStocks.get(1));
                }
            }
        }
    }

    public float getLength() {
        // + 1 time ROLLING_STOCK_LENGTH_IN_METERS to include head
        // this operation performed separately in order to avoid wiping
        // length by rollingStocks.size() = 0
        return Math.abs(this.getTailX() - BasicRollingStock.ROLLING_STOCK_LENGTH_IN_METERS);
    }

    public float getTailX() {
        ArrayList<BasicRollingStock> rollingStocks = this.locomotive.getBody();
        LocomotiveHead head = this.locomotive.getHead();
        return head.getLocationX() - (BasicRollingStock.ROLLING_STOCK_LENGTH_IN_METERS * rollingStocks.size());
    }

}
