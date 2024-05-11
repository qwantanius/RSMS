package RSMS.common.mockers;

import RSMS.Locomotive.*;
import RSMS.RollingStocks.BasicRollingStock;
import RSMS.RollingStocks.RollingStockFactory;
import RSMS.RollingStocks.managers.RollingStockCouplingPolicyManager;
import RSMS.RollingStocks.types.RollingStockTypesEnum;
import RSMS.Routing.Route.Route;
import RSMS.Routing.Route.RouteCreationDto;
import RSMS.Routing.Route.RoutingManager;
import RSMS.Routing.Station.Station;

import java.util.ArrayList;

public class LocomotiveMockingService {

    private static int counter = 0;


    public static Locomotive genLocomotiveForPeopleTransport(
        RollingStockFactory rollingStockFactory,
        Station start,
        Station destination,
        int amountOfRollingStocks
    ) {
        Locomotive locomotive = new Locomotive(LocomotiveMockingService.genRandomDTO(start, destination));
        ArrayList<RollingStockTypesEnum> peopleTransport = new ArrayList<>();

        peopleTransport.add(RollingStockTypesEnum.passenger_railroad_car);
        peopleTransport.add(RollingStockTypesEnum.electrical_grid_provider);
        ArrayList<BasicRollingStock> cars = RollingStockMockingService.createSpecificCars(
            rollingStockFactory,
            peopleTransport,
            amountOfRollingStocks
        );
        cars.forEach(car -> locomotive.getLocomotiveRSManager().addRollingStock(car));
        return locomotive;
    }

    public static Locomotive genLocomotiveForAllPossibleTransport(
        RollingStockFactory rollingStockFactory,
        Station start,
        Station destination
    ) {
        int amountOfPeopleLocomotives = 5;
        Locomotive locomotive = LocomotiveMockingService.genLocomotiveForPeopleTransport(
            rollingStockFactory,
            start,
            destination,
            amountOfPeopleLocomotives
        );
        LocomotiveRollingStocksManager rollingStocksManager = locomotive.getLocomotiveRSManager();
        RollingStockTypesEnum[] rollingStockTypesEnums = RollingStockTypesEnum.values();
        for (int i=0; i<rollingStockTypesEnums.length; i++) {
            ArrayList<RollingStockTypesEnum> transportationLoadTypes = new ArrayList<>();

            if (rollingStockTypesEnums[i] == RollingStockTypesEnum.passenger_railroad_car) {
                transportationLoadTypes.add(RollingStockTypesEnum.electrical_grid_provider);
            }
            transportationLoadTypes.add(rollingStockTypesEnums[i]);
            BasicRollingStock car = RollingStockMockingService.createSpecificCars(rollingStockFactory, transportationLoadTypes, 1).get(0);
            rollingStocksManager.addRollingStock(car);
        }
        return locomotive;
    }

    public static LocomotiveCreationDto genRandomDTO(Station start, Station destination) {
        LocomotiveCreationDto randomDTO = new LocomotiveCreationDto();
        randomDTO.MAX_AMOUNT_OF_ROLLING_STOCKS = 50;
        randomDTO.startStation = start;
        randomDTO.destinationStation = destination;
        randomDTO.LOCOMOTIVES_HEAD = getHead();
        randomDTO.MAX_AMOUNT_OF_ELECTRIC_GRID_CONNECTIONS = (int)(Math.random()*10);
        randomDTO.locomotiveName = String.format(
            "LOC_%s_%s_%d",
            start.getStationName(),
            destination.getStationName(),
            LocomotiveMockingService.counter
        );
        LocomotiveMockingService.counter++;
        return randomDTO;
    }

    public static LocomotiveHead getHead() {
        LocomotiveHead head = new LocomotiveHead(new RollingStockCouplingPolicyManager());
        head.setName(String.format("ROLLING_STOCK_HEAD_%s", Math.floor(Math.random()*1000)));
        return head;
    }

    public static ArrayList<LocomotiveMovable> genMovableLocomotives(int amount) {
        ArrayList<RouteCreationDto> routeCreationDtos = RoutesMockingService.generateRandomRoutesDtos(1);
        RoutingManager rm = new RoutingManager(routeCreationDtos);

        RollingStockFactory rollingStockFactory = new RollingStockFactory();
        Route testRoute = rm.getRoutes().get(0);
        ArrayList<LocomotiveMovable> movableLocomotives = new ArrayList<>();
        for (int i=0; i<amount; i++) {
            LocomotiveMovable locomotiveMovable = new LocomotiveMovable(testRoute, LocomotiveMockingService.genLocomotiveForAllPossibleTransport(
                rollingStockFactory,
                testRoute.getStart(),
                testRoute.getDestination()
            ));
            RollingStockMockingService.genRandomLoads(locomotiveMovable);
            movableLocomotives.add(locomotiveMovable);
        }
        return movableLocomotives;
    }
}
