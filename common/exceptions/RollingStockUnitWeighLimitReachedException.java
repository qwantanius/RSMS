package RSMS.common.exceptions;

import RSMS.RollingStocks.BasicRollingStock;
import RSMS.RollingStocks.types.TransportationLoadDto;

public class RollingStockUnitWeighLimitReachedException extends RuntimeException {
    public RollingStockUnitWeighLimitReachedException(BasicRollingStock basicRollingStock, TransportationLoadDto TransportationLoadDto) {
        super(String.format(
            "[Failed to load %s into rolling stock %s:%s unit weight limit reached, expected: %f, got: %f]",
            TransportationLoadDto.loadName,
            basicRollingStock.getClass().getSimpleName(),
            basicRollingStock.getUuid(),
            basicRollingStock.getUnitsManager().getMaxWeighPerUnitKg(),
            TransportationLoadDto.weightPerUnitInKg
        ));
    }
}
