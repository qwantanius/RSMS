package RSMS.Locomotive;

import RSMS.Routing.Station.Station;

public class LocomotiveCreationDto {
    public String locomotiveName;
    public float MAX_WEIGHT_PER_ROLLING_STOCK_IN_KG;
    public int MAX_AMOUNT_OF_ROLLING_STOCKS;
    public int MAX_AMOUNT_OF_ELECTRIC_GRID_CONNECTIONS;
    public LocomotiveHead LOCOMOTIVES_HEAD;
    public Station startStation;
    public Station destinationStation;
}
