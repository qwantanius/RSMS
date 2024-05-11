package RSMS.Locomotive;

import RSMS.RollingStocks.BasicRollingStock;
import RSMS.RollingStocks.managers.RollingStockCouplingPolicyManager;
import RSMS.RollingStocks.managers.RollingStockEngineManager;

public class LocomotiveHead extends BasicRollingStock {

    private float locationX = 0f;
    private float locationY = 0f;
    private RollingStockEngineManager rollingStockEngineManager;

    public LocomotiveHead(RollingStockCouplingPolicyManager rollingStockCouplingPolicyManager) {
        super(rollingStockCouplingPolicyManager);
        this.rollingStockEngineManager = new RollingStockEngineManager(this);
    }

    public float getLocationX() {
        return locationX;
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public RollingStockEngineManager getEngine() {
        return this.rollingStockEngineManager;
    }

}
