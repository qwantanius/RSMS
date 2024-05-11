package RSMS.RollingStocks;

import RSMS.RollingStocks.managers.RollingStockCouplingPolicyManager;
import RSMS.RollingStocks.types.RollingStockTypesEnum;

import java.util.ArrayList;

public final class RollingStockConfig {
    public String name;
    public int availableUnits;
    public float weighKgPerUnits;
    public RollingStockCouplingPolicyManager rollingStockCouplingPolicyManager;
    public ArrayList<RollingStockTypesEnum> rollingStockTypes;

    public RollingStockConfig(
            String name,
            int availableUnits,
            float weighKgPerUnits,
            ArrayList<RollingStockTypesEnum> rollingStockTypesEnum
    ) {
        this.name = name;
        this.availableUnits = availableUnits;
        this.weighKgPerUnits = weighKgPerUnits;
        this.rollingStockTypes = rollingStockTypesEnum;
        this.rollingStockCouplingPolicyManager = new RollingStockCouplingPolicyManager();
    }
}
