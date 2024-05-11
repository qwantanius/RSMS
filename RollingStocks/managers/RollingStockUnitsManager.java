package RSMS.RollingStocks.managers;

import RSMS.RollingStocks.BasicRollingStock;

public class RollingStockUnitsManager {

    private BasicRollingStock rollingStock;

    public RollingStockUnitsManager(BasicRollingStock rollingStock) {
        this.rollingStock = rollingStock;
        this.loadUnits(0);
    }

    private int maximumAmountUnitsInStock;
    private float maximumWeightPerLoadingUnit;
    private int loadedUnits = 0;

    public int getLoadedUnits() { return this.loadedUnits; }

    public void loadUnits(int amountOfUnitsToBeLoaded) {
        this.loadedUnits += amountOfUnitsToBeLoaded;
    }

    public void unloadUnits(int amountOfUnitsToBeUnloaded) { this.loadedUnits -= amountOfUnitsToBeUnloaded;}

    public int getAvailableUnitsForLoading() {return this.getMaxAmountOfUnits() - this.getLoadedUnits(); }
    public int getMaxAmountOfUnits() { return this.maximumAmountUnitsInStock;}
    public float getMaxWeighPerUnitKg() { return this.maximumWeightPerLoadingUnit; }

    public void setMaxWeightPerUnitKg(float maximumWeightPerLoadingUnit) { this.maximumWeightPerLoadingUnit = maximumWeightPerLoadingUnit; }
    public void setMaxAmountOfUnits(int newAmount) { this.maximumAmountUnitsInStock = newAmount; }

}
