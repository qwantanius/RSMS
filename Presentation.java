package RSMS;

import RSMS.Locomotive.Locomotive;
import RSMS.Locomotive.LocomotiveMovable;
import RSMS.RollingStocks.BasicRollingStock;
import RSMS.RollingStocks.RollingStockFactory;
import RSMS.RollingStocks.managers.RollingStockCouplingPolicyManager;
import RSMS.RollingStocks.types.RollingStockTypesEnum;
import RSMS.Routing.Route.Route;
import RSMS.Routing.Route.RoutingManager;
import RSMS.Routing.Station.Station;
import RSMS.common.files.StateFile;
import RSMS.common.logger.Logger;
import RSMS.common.logger.LoggerLevelEnum;
import RSMS.common.logger.LoggerOutputEnum;
import RSMS.common.mockers.LocomotiveMockingService;
import RSMS.common.mockers.RollingStockMockingService;
import RSMS.common.mockers.RoutesMockingService;

import java.util.ArrayList;
import java.util.Random;

public class Presentation {

    public static void main(String[] args) {
        Random random = new Random();
        Logger logger = new Logger(LoggerLevelEnum.INFO, Presentation.class.getSimpleName());
        Logger errLogger = new Logger(LoggerLevelEnum.ERROR, Presentation.class.getSimpleName());
        Logger.disableAll();
        Logger.setOutputForAll(LoggerOutputEnum.SYSTEM_OUT);

        // Core functionalities demo
        {
            // using rolling stock factory to create random amount of rolling stocks
            // of different types
            Logger.disableAll();
            {
                RollingStockFactory rollingStockFactory = new RollingStockFactory();
                ArrayList<BasicRollingStock> rollingStocks = RollingStockMockingService.createRandomRollingStocks(
                    rollingStockFactory,
                    random.nextInt(50)
                );
                for (BasicRollingStock rollingStock : rollingStocks) {
                    logger.log(rollingStock.getRollingStockTypes().toString());
                }
            }

            // creating stations with the same destination but different starting stations
            // this is needed for generating random routes
            Logger.disableAll();
            {
                RoutingManager rm = new RoutingManager(RoutesMockingService.generateRandomRoutesDtos(2));
                rm.printRoutes();
                rm.getRoutes().get(0).assignNewDestination(rm.getRoutes().get(1).getDestination());
                rm.printRoutes();
            }

            Logger.enableAll();
            // simply policy connecting management
            {
                RollingStockFactory rollingStockFactory = new RollingStockFactory();
                BasicRollingStock rollingStock1 = rollingStockFactory.create(RollingStockMockingService.generateRandomDto());
                BasicRollingStock rollingStock2 = rollingStockFactory.create(RollingStockMockingService.generateRandomDto());
                RollingStockCouplingPolicyManager rollingStockCouplingPolicyManager = rollingStock1.getCouplingManager().getRollingStockCouplingPolicyManager();
                logger.log(rollingStock1.getRollingStockTypes().toString());
                // comment this row of code to see the result of case, when we try to connect
                // some rolling stocks that require AC, but connecting side does not provide
                // any of those
                // ---->
                rollingStock2.addRollingStockType(RollingStockTypesEnum.electrical_grid_provider);
                // <----
                logger.log(rollingStock2.getRollingStockTypes().toString());
                logger.log(rollingStockCouplingPolicyManager.getCouplingPolicies().toString());
                logger.log(rollingStockCouplingPolicyManager.isCouplingValidAllowed(rollingStock1, rollingStock2).toString());
            }
        }

        // Locomotive pure functionality demo
        Logger.disableAll();
        {
            // Listed below logic handled automatically
            // and shown just to illustrate how attaching works
            RoutingManager rm = new RoutingManager(RoutesMockingService.generateRandomRoutesDtos(2));
            RollingStockFactory rollingStockFactory = new RollingStockFactory();
            Logger.disableAll();

            // assigning mock start and destination points
            Route testRoute = rm.getRoutes().get(0);
            Station startA = testRoute.getStart();
            Station destA = testRoute.getDestination();

            // Creating mock transportation with people
            ArrayList<RollingStockTypesEnum> peopleTransport = new ArrayList<>();
            peopleTransport.add(RollingStockTypesEnum.passenger_railroad_car);
            peopleTransport.add(RollingStockTypesEnum.electrical_grid_provider);

            // initiating locomotive instance
            Locomotive demoLocomotive = new Locomotive(LocomotiveMockingService.genRandomDTO(startA, destA));
            demoLocomotive.info();

            // Creating array of railroad cars for passengers
            ArrayList<BasicRollingStock> testCars = RollingStockMockingService.createSpecificCars(rollingStockFactory, peopleTransport, 20);
            testCars.forEach(testcar -> demoLocomotive.getLocomotiveRSManager().addRollingStock(testcar));

            // Testing connections
            demoLocomotive.info();
            demoLocomotive.getBody().forEach(car -> {
                BasicRollingStock connectedTo = car.getCouplingManager().getConnectedRollingStock();
                logger.log(car.getName() + " connected to " + (connectedTo != null ? connectedTo.getName() : "null"));
            });
        }

        // Locomotive 1 direction route demo, no circling
        // logic presented here used in implementation of method
        // that perform circling between start and destination
        Logger.disableAll();
        {
            RoutingManager rm = new RoutingManager(RoutesMockingService.generateRandomRoutesDtos(2));
            RollingStockFactory rollingStockFactory = new RollingStockFactory();
            Route testRoute = rm.getRoutes().get(0);
            Station startA = testRoute.getStart();
            Station destA = testRoute.getDestination();
            Locomotive demoLocomotive = new Locomotive(LocomotiveMockingService.genRandomDTO(startA, destA));
            ArrayList<RollingStockTypesEnum> peopleTransport = new ArrayList<>();

            peopleTransport.add(RollingStockTypesEnum.passenger_railroad_car);
            peopleTransport.add(RollingStockTypesEnum.electrical_grid_provider);
            ArrayList<BasicRollingStock> testCars = RollingStockMockingService.createSpecificCars(rollingStockFactory, peopleTransport, 20);
            testCars.forEach(testcar -> demoLocomotive.getLocomotiveRSManager().addRollingStock(testcar));

            LocomotiveMovable locomotiveMovable = new LocomotiveMovable(testRoute, demoLocomotive);
            // RSMSControlCenter.startJourney(locomotiveMovable);
        }

        // WHAT WAS REQUESTED IN PDF :
        Logger.enableAll();
        {
            Logger.setOutputForAll(LoggerOutputEnum.SYSTEM_OUT);
            // for seeing better slowing down process
            // LocomotiveMovable.delayInSecondsForSlowingDownWhenTesting = 1;
            BasicRollingStock.enableCyclingLoading();
            StateFile.startReportingPool();
            // p.s. this will create 25 locomotives, 100 stations
            // and this 100 stations are hold by 1 route
            // you may create route intersections by creating new route
            // based on old, but with reassigned destination and/or start
            // this approach for me is much more efficient than DFS
            // also one of the approaches I considered but did not managed
            // to test well : Turing Machine circling through routes and cars
            // (basically here we will need few of machines managed by one)
            // and assigning them for given in instruction stations, where route
            // is chunked and each chunk has different types , regular, blocked, intersection right, left, lr
            // (representing types of rails) as a piece of path in route
            // and so on. it would be great option if I can implement it in future projects
            // this idea sounds wierd but should be a nice practical excersize
            RSMSControlCenter.executeLocomotivesForRoute(LocomotiveMockingService.genMovableLocomotives(25));
        }
    }
}
