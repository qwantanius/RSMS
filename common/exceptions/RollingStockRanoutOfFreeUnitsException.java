package RSMS.common.exceptions;

import RSMS.RollingStocks.BasicRollingStock;
import RSMS.RollingStocks.types.TransportationLoadDto;

public class RollingStockRanoutOfFreeUnitsException extends RuntimeException {
    public RollingStockRanoutOfFreeUnitsException(BasicRollingStock rollingStock, TransportationLoadDto TransportationLoadDto) {
        super(String.format(
            "[Failed to load %s rolling stock %s:%s ran out of free units, free units: %d, units to be loaded: %d]",
            TransportationLoadDto.loadName,
            rollingStock.getClass().getSimpleName(),
            rollingStock.getUuid(),
            rollingStock.getUnitsManager().getAvailableUnitsForLoading(),
            TransportationLoadDto.unitsAmountRequiredForLoading
        ));
    }
}
