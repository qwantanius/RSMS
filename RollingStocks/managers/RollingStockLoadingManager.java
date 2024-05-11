package RSMS.RollingStocks.managers;

import RSMS.RollingStocks.BasicRollingStock;
import RSMS.RollingStocks.types.RollingStockTypesEnum;
import RSMS.RollingStocks.types.TransportationLoadDto;
import RSMS.common.exceptions.*;

public class RollingStockLoadingManager {
    private TransportationLoadDto transportationLoadDto;
    private boolean isStockLoaded;
    private BasicRollingStock rollingStock;

    public RollingStockLoadingManager(BasicRollingStock rollingStock) {
        this.rollingStock = rollingStock;
    }

    public boolean isLoaded() {
        return this.isStockLoaded;
    }

    public TransportationLoadDto getTransportationLoadDto() {
        return this.transportationLoadDto;
    }

    public void setTransportationLoadDto(TransportationLoadDto transportationLoadDto) {
        this.transportationLoadDto = transportationLoadDto;
    }

    public void load(TransportationLoadDto transportationLoadDto)
        throws
            RollingStockRanoutOfFreeUnitsException,
            RollingStockUnitWeighLimitReachedException,
            RollingStockTypeDoesNotMatchWithTransportationLoadingException {
        this.validateBeforeLoading(transportationLoadDto);
        this.isStockLoaded = true;
        this.setTransportationLoadDto(transportationLoadDto);
        this.rollingStock.getUnitsManager().loadUnits(transportationLoadDto.unitsAmountRequiredForLoading);
    }

    public void unload(int amountOfUnits) throws RollingStockIsNotLoadedException, RollingStockUnloadingFailed {
        this.validateBeforeUnloading(amountOfUnits);
        this.rollingStock.getUnitsManager().unloadUnits(amountOfUnits);
        this.transportationLoadDto.unitsAmountRequiredForLoading -= amountOfUnits;
        if (transportationLoadDto.unitsAmountRequiredForLoading == 0) {
            this.isStockLoaded = false;
        }
    }

    private void validateBeforeLoading(TransportationLoadDto transportationLoadDto)
        throws
            RollingStockRanoutOfFreeUnitsException,
            RollingStockUnitWeighLimitReachedException,
            RollingStockTypeDoesNotMatchWithTransportationLoadingException {
        this.validateLoadingType(transportationLoadDto);
        this.validateUnitsAmount(transportationLoadDto);
        this.validateUnitWeight(transportationLoadDto);
    }

    private void validateLoadingType(TransportationLoadDto transportationLoadDto)
        throws
            RollingStockTypeDoesNotMatchWithTransportationLoadingException {
        RollingStockTypesEnum typeToCheck = transportationLoadDto.requiredRollingStockType;
        boolean areStockAndLoadTypesMatched = this.rollingStock.getRollingStockTypes().contains(typeToCheck);
        if (!areStockAndLoadTypesMatched) {
            throw new RollingStockTypeDoesNotMatchWithTransportationLoadingException(this.rollingStock);
        }
    }

    private void validateUnitWeight(TransportationLoadDto transportationLoadDto)
        throws
            RollingStockUnitWeighLimitReachedException {
        float stockMaxKgPerUnit = this.rollingStock.getUnitsManager().getMaxWeighPerUnitKg();
        boolean isWeightPerUnitMatchedLimit = transportationLoadDto.weightPerUnitInKg > stockMaxKgPerUnit;
        if (isWeightPerUnitMatchedLimit) {
            throw new RollingStockUnitWeighLimitReachedException(this.rollingStock, transportationLoadDto);
        }
    }

    private void validateUnitsAmount(TransportationLoadDto transportationLoadDto)
        throws
            RollingStockRanoutOfFreeUnitsException {
        int unitsAmountToValidate = transportationLoadDto.unitsAmountRequiredForLoading;
        int stockAvailableUnits =  this.rollingStock.getUnitsManager().getAvailableUnitsForLoading();
        boolean isUnitsAmountEnoughForLoading = unitsAmountToValidate <= stockAvailableUnits;
        if (!isUnitsAmountEnoughForLoading) {
            throw new RollingStockRanoutOfFreeUnitsException(this.rollingStock, transportationLoadDto);
        }
    }

    private void validateBeforeUnloading(int amountOfUnitsToUnload)
        throws
            RollingStockIsNotLoadedException,
            RollingStockUnloadingFailed {
        if (amountOfUnitsToUnload > this.rollingStock.getUnitsManager().getLoadedUnits()) {
            int amountOfLoadedUnits = this.rollingStock.getUnitsManager().getLoadedUnits();
            throw new RollingStockUnloadingFailed(amountOfLoadedUnits, amountOfUnitsToUnload);
        }
        if (!this.isLoaded()) {
            throw new RollingStockIsNotLoadedException(this);
        }
    }

}
