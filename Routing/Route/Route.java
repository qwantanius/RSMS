package RSMS.Routing.Route;

import RSMS.RollingStocks.types.RollingStockTypesEnum;
import RSMS.Routing.Station.Station;
import RSMS.Routing.Station.StationCreationDto;
import RSMS.Routing.Station.StationSortingService;
import RSMS.common.helpers.CollectionHelper;
import RSMS.common.types.MessageFormats;
import java.util.ArrayList;
import java.util.Iterator;

public class Route {

    private String routeName;
    private Station START_STATION;
    private Station DESTINATION_STATION;
    private ArrayList<Station> stations;
    private int MAX_AMOUNT_OF_ROLLING_STOCKS;
    private int MAX_SPEED_IN_METERS_PER_HOUR;
    private int MAX_UNITS_AMOUNT_PER_ROLLING_STOCK;
    private int MAX_WEIGHT_PER_UNIT_IN_KG;
    private double SUMMARY_DISTANCE_IN_METERS;
    private RollingStockTypesEnum[] BANNED_ROLLING_STOCKS_TYPES;

    public Route(RouteCreationDto routeCreationDto) {
        this.stations = new ArrayList<>();
        this.routeName = routeCreationDto.routeName;
        this.MAX_AMOUNT_OF_ROLLING_STOCKS = routeCreationDto.MAX_AMOUNT_OF_ROLLING_STOCKS;
        this.MAX_SPEED_IN_METERS_PER_HOUR = routeCreationDto.MAX_SPEED_IN_METERS_PER_HOUR;
        this.MAX_UNITS_AMOUNT_PER_ROLLING_STOCK = routeCreationDto.MAX_UNITS_AMOUNT_PER_ROLLING_STOCK;
        this.MAX_WEIGHT_PER_UNIT_IN_KG = routeCreationDto.MAX_WEIGHT_PER_UNIT_IN_KG;
        this.BANNED_ROLLING_STOCKS_TYPES = routeCreationDto.BANNED_ROLLING_STOCKS_TYPES;
        this.createStations(routeCreationDto.stations);
        this.START_STATION = this.getStations().get(0);
        this.sortStationRoutes();
    }

    public static Route createReturnRoute(Route route) {
        ArrayList<Station> shuffledReturnRouteStations = CollectionHelper.swapBordersAndShuffleBody(route.getStations());

        RouteCreationDto newRouteCreationDto = new RouteCreationDto();
        newRouteCreationDto.stations = Station.getStationDtosFromStations(shuffledReturnRouteStations);
        newRouteCreationDto.routeName = route.getRouteName();
        newRouteCreationDto.BANNED_ROLLING_STOCKS_TYPES = route.getBannedRollingStocksTypes();
        newRouteCreationDto.MAX_SPEED_IN_METERS_PER_HOUR = route.getMaxSpeedInMetersPerHour();
        newRouteCreationDto.MAX_AMOUNT_OF_ROLLING_STOCKS = route.getMaxAmountOfRollingStocks();
        newRouteCreationDto.MAX_UNITS_AMOUNT_PER_ROLLING_STOCK = route.getMaxUnitsAmountPerRollingStock();
        newRouteCreationDto.MAX_WEIGHT_PER_UNIT_IN_KG = route.getMaxWeighPerUnitInKg();
        return new Route(newRouteCreationDto).assignNewDestination(route.getStart());
    }

    public String getRouteName() {
        return routeName;
    }

    public Station getDestination() {
        return DESTINATION_STATION;
    }

    public Station getStart() {
        return START_STATION;
    }

    public int getMaxAmountOfRollingStocks() {
        return MAX_AMOUNT_OF_ROLLING_STOCKS;
    }

    public int getMaxSpeedInMetersPerHour() {
        return MAX_SPEED_IN_METERS_PER_HOUR;
    }

    public int getMaxUnitsAmountPerRollingStock() {
        return MAX_UNITS_AMOUNT_PER_ROLLING_STOCK;
    }

    public int getMaxWeighPerUnitInKg() {
        return MAX_WEIGHT_PER_UNIT_IN_KG;
    }

    public RollingStockTypesEnum[] getBannedRollingStocksTypes() {
        return BANNED_ROLLING_STOCKS_TYPES;
    }
    public ArrayList<Station> getStations() {
        return stations;
    }

    public String toString() {
        return String.format(
            MessageFormats.ROUTE_TO_STRING_TMP,
            this.routeName,
            this.getStations().size()-1,
            this.START_STATION,
            this.DESTINATION_STATION,
            (int)Route.calcSummaryDistance(this.getStations()) / Station.METERS_IN_KM
        );
    }

    public static double calcSummaryDistance(ArrayList<Station> stations) {
        double distance = 0;
        Iterator<Station> stationIterator = stations.iterator();
        Station previous = stationIterator.next();
        while (stationIterator.hasNext()) {
            Station current = stationIterator.next();
            distance += previous.getDistanceInKmFrom(current);
            previous = current;
        }
        return distance;
    }

    public double getRouteLength() {
        return Route.calcSummaryDistance(this.getStations());
    }

    public void sortStationRoutes() {
        this.getStations().sort(StationSortingService.ASC_EVERY_FROM_START(this));
        this.refreshRouteHead();
    }

    public void refreshRouteHead() {
        this.START_STATION = this.stations.get(0);
        this.DESTINATION_STATION = this.getStations().get(this.getStations().size()-1);
        this.SUMMARY_DISTANCE_IN_METERS = Route.calcSummaryDistance(this.getStations());
    }

    public Route assignNewDestination(Station station) {
        this.stations.add(station);
        this.sortStationRoutes();
        int routeCuttingIndex = this.stations.indexOf(station)+1;
        this.stations.subList(routeCuttingIndex, this.stations.size()).clear();
        this.sortStationRoutes();
        return this;
    }

    private void createStations(ArrayList<StationCreationDto> stations) {
        stations.forEach(stationCreationDto -> this.getStations().add(new Station(stationCreationDto)));
    }
}
