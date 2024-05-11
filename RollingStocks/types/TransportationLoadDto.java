package RSMS.RollingStocks.types;

import RSMS.Routing.Station.Station;
import RSMS.common.types.MessageFormats;

public class TransportationLoadDto {

    public TransportationLoadDto(
            String name,
            int unitsAmountRequiredForLoading,
            float weightPerUnitInKg,
            RollingStockTypesEnum rsLoadingType,
            Station unloadAt
    ) {
        this.unitsAmountRequiredForLoading = unitsAmountRequiredForLoading;
        this.loadName = name;
        this.requiredRollingStockType = rsLoadingType;
        this.weightPerUnitInKg = weightPerUnitInKg;
        this.unloadAt = unloadAt;
    }

    public String loadName;
    public int unitsAmountRequiredForLoading;
    public float weightPerUnitInKg;
    public Station unloadAt;

    public RollingStockTypesEnum requiredRollingStockType;

    @Override
    public String toString() {
        return String.format(
            MessageFormats.TRANSPORTATION_LOADING_TO_STRING_TEMPLATE,
            this.loadName,
            this.unitsAmountRequiredForLoading,
            this.weightPerUnitInKg
        );
    }
}