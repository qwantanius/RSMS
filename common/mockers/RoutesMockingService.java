package RSMS.common.mockers;

import RSMS.Routing.Route.RouteCreationDto;

import java.util.ArrayList;


public class RoutesMockingService {
    private static int counter = 0;

    public static ArrayList<RouteCreationDto> generateRandomRoutesDtos(int amount) {
        ArrayList<RouteCreationDto> routes = new ArrayList<>();
        for(int i=0; i<amount; i++) {
            RouteCreationDto randomRoute = RoutesMockingService.generateRandomDto();
            routes.add(randomRoute);
        }
        return routes;
    }

    public static RouteCreationDto generateRandomDto() {
        RouteCreationDto routeCreationDto = new RouteCreationDto();
        routeCreationDto.routeName = "MOCK_ROUTE_" + RoutesMockingService.counter + "_" + Math.round(Math.random()*100);
        routeCreationDto.stations = StationsMockingService.genStationsDtos(100); //(int)(Math.random()*100)
        RoutesMockingService.counter++;
        return routeCreationDto;
    }

}
