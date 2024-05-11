package RSMS.Routing.Route;

import RSMS.RollingStocks.types.RollingStockTypesEnum;
import RSMS.Routing.Station.StationCreationDto;

import java.util.ArrayList;

public final class RouteCreationDto {
    public String routeName;
    public ArrayList<StationCreationDto> stations;
    public int MAX_AMOUNT_OF_ROLLING_STOCKS;
    public int MAX_SPEED_IN_METERS_PER_HOUR;
    public int MAX_UNITS_AMOUNT_PER_ROLLING_STOCK;
    public int MAX_WEIGHT_PER_UNIT_IN_KG;
    public RollingStockTypesEnum[] BANNED_ROLLING_STOCKS_TYPES;
}
