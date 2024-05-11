package RSMS.common.files;

import RSMS.Locomotive.Locomotive;
import RSMS.Locomotive.SpeedSeeder;
import RSMS.RSMSControlCenter;
import RSMS.RollingStocks.BasicRollingStock;
import RSMS.RollingStocks.managers.RollingStockCouplingManager;
import RSMS.RollingStocks.managers.RollingStockLoadingManager;
import RSMS.RollingStocks.managers.RollingStockUnitsManager;
import RSMS.Routing.Route.Route;
import RSMS.Routing.Station.Station;
import RSMS.common.helpers.TableBuilder;
import RSMS.common.logger.Logger;
import RSMS.common.logger.LoggerLevelEnum;
import RSMS.common.types.MessageFormats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StateFile {

    public static Logger errLogger = new Logger(LoggerLevelEnum.ERROR, StateFile.class.getSimpleName());
    public static String fileName = "AppState.txt";
    public static String outDir = System.getProperty("user.dir") + "/storage/";
    private static boolean runReporting = true;

    public static void startReporting() {
        try {
            int counter = 0;
            while (StateFile.runReporting) {
                boolean doNotOverwriteFile = false;
                String report = StateFile.getReport(counter);
                FileService.writeTo(StateFile.getReportFileAbsolutePath(), report, doNotOverwriteFile);
                Thread.sleep(SpeedSeeder.ONE_SECOND_IN_MILLIS*5);
                counter++;
            }
        } catch (Exception err) {
            StateFile.errLogger.log(err.toString());
        }
    }

    public static String getReportFileAbsolutePath() {
        return StateFile.outDir + StateFile.fileName;
    }
    
    public static void stopReporting() {
        StateFile.runReporting = false;
    }

    public static void startReportingPool() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                StateFile.startReporting();
            }
        });
    }

    private static String getReport(int counter) {
        String header = "Update number " + counter  + " at " + new Date().toString();
        List<List<String>> locomotiveTable = new ArrayList<>();
        locomotiveTable.add(Arrays.asList(
            "LOCOMOTIVE",
            "LOCOMOTIVE_HEAD",
            "CURRENT_SPEED",
            "STATUS",
            "HOME_STATION",
            "ROUTE",
            "ROUTE_START",
            "ROUTE_DESTINATION",
            "TRIP_PROGRESS",
            "CURRENT_STATION",
            "NEXT_STATION",
            "STATION_TO_STATION_PROGRESS",
            "MAX_AMOUNT_OF_ROLLING_STOCKS",
            "CURRENT_AMOUNT_OF_ROLLING_STOCKS",
            "AMOUNT_OF_JOURNEYS",
            "ROUTE_TOTAL_LENGTH"
        ));
        List<List<String>> rollingStocksTable = new ArrayList<>();
        rollingStocksTable.add(Arrays.asList(
            "RAILROAD_CAR_NAME",
            "RAILROAD_CAR_AVAILABLE_UNITS",
            "RAILROAD_CAR_LOADED_UNITS",
            "LOADING_UNIT_NAME",
            "EXPECTED_TO_BE_UNLOADED",
            "CONNECTED_TO",
            "BELONGS_TO_LOCOMOTIVE",
            "UUID",
            "FEATURES",
            "LOAD_WEIGHT_KG",
            "ROLLING_STOCK_WEIGHT_KG",
            "TOTAL_WEIGHT_KG"
        ));
        List<List<String>> stationsTable = new ArrayList<>();
        stationsTable.add(Arrays.asList(
            "STATION_NAME",
            "LOCATION_X",
            "LOCATION_Y",
            "BELONGS_TO_ROUTE",
            "DISTANCE_FROM_START",
            "DISTANCE_FROM_DESTINATION"
        ));
        ArrayList<Route> availableRoutes = new ArrayList<>();
        RSMSControlCenter.getRunningLocomotives().forEach(locomotiveMovable -> {
            Locomotive locomotive = locomotiveMovable.getLocomotive();
            Route route = locomotiveMovable.getAssignedRoute();
            if (!availableRoutes.contains(route)) {
                availableRoutes.add(route);
            }
            locomotiveTable.add(Arrays.asList(
                locomotive.getName(),
                locomotive.getHead().getName(),
                String.valueOf(locomotive.getHead().getEngine().getCurrentSpeedInKmPerHour()),
                locomotiveMovable.getLocomotiveStatus().name(),
                locomotive.getHomeStation().getStationName(),
                route.getRouteName(),
                route.getDestination().getStationName(),
                route.getStart().getStationName(),
                String.valueOf(locomotiveMovable.getTripPercentage()),
                locomotiveMovable.getCurrentStation().getStationName(),
                locomotiveMovable.getNextStation().getStationName(),
                String.valueOf(locomotiveMovable.getSpeedSeeder().getStationToStationCompletionPercentage()),
                String.valueOf(locomotive.getMaxAmountOfRollingStocks()),
                String.valueOf(locomotive.getBody().size()),
                String.valueOf(locomotiveMovable.getJourneyCounter()),
                (route.getRouteLength()/ Station.METERS_IN_KM)  + "km"
            ));

            // in order to do not affect the origin array
            ArrayList<BasicRollingStock> rollingStocks = (ArrayList<BasicRollingStock>)locomotive.getBody().clone();
            rollingStocks.sort((lhsrs, rhsrs) -> (int)(lhsrs.getSummaryWeight() - rhsrs.getSummaryWeight()));
            rollingStocks.forEach(rollingStock -> {
                RollingStockLoadingManager loadingManager = rollingStock.getLoadingManager();
                RollingStockCouplingManager couplingManager = rollingStock.getCouplingManager();
                RollingStockUnitsManager unitsManager = rollingStock.getUnitsManager();
                rollingStocksTable.add(Arrays.asList(
                    rollingStock.getName(),
                    String.valueOf(rollingStock.getUnitsManager().getAvailableUnitsForLoading()),
                    String.valueOf(unitsManager.getLoadedUnits()),
                    rollingStock.getUnitLoadingName(locomotive),
                    loadingManager.getTransportationLoadDto().unloadAt.getStationName(),
                    couplingManager.getConnectedRollingStock() != null ? couplingManager.getConnectedRollingStock().getName() :  "CAR CONNECTED TO HEAD (" + locomotive.getHead().getName() +")",
                    locomotive.getName(),
                    rollingStock.getUuid().toString(),
                    rollingStock.getFeatures().toString(),
                    String.valueOf(rollingStock.getCurrentLoadWeight()),
                    String.valueOf(rollingStock.getRollingStockWeight()),
                    String.valueOf(rollingStock.getSummaryWeight())
                ));
            });
            rollingStocksTable.add(Arrays.asList(
                MessageFormats.DEFAULT_ROW_SEPARATOR,
                MessageFormats.DEFAULT_ROW_SEPARATOR,
                MessageFormats.DEFAULT_ROW_SEPARATOR,
                MessageFormats.DEFAULT_ROW_SEPARATOR,
                MessageFormats.DEFAULT_ROW_SEPARATOR,
                MessageFormats.DEFAULT_ROW_SEPARATOR,
                MessageFormats.DEFAULT_ROW_SEPARATOR,
                MessageFormats.DEFAULT_ROW_SEPARATOR,
                MessageFormats.DEFAULT_ROW_SEPARATOR,
                MessageFormats.DEFAULT_ROW_SEPARATOR
            ));
        });
        availableRoutes.forEach(route -> {
            route.getStations().forEach(station -> {
                stationsTable.add(Arrays.asList(
                    station.getStationName(),
                    String.valueOf(station.getLOCATION_X()),
                    String.valueOf(station.getLOCATION_Y()),
                    route.getRouteName(),
                    // convert from coordinates system to human readable kms
                    station.getDistanceInKmFrom(route.getStart()) / 1000 + " km",
                    station.getDistanceInKmFrom(route.getDestination()) / 1000 + " km"
                ));
            });
        });
        return String.format(
            MessageFormats.getReportTemplate(),
            MessageFormats.getWaterMark(),
            header,
            TableBuilder.getTable(locomotiveTable),
            TableBuilder.getTable(rollingStocksTable),
            TableBuilder.getTable(stationsTable)
        );
    }
}
