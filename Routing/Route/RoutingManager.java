package RSMS.Routing.Route;

import RSMS.common.logger.Logger;
import RSMS.common.logger.LoggerLevelEnum;

import java.util.ArrayList;

public class RoutingManager {

    private ArrayList<Route> routes;
    private Logger logger;

    public RoutingManager(ArrayList<RouteCreationDto> routeCreationDtos) {
        this.logger = new Logger(LoggerLevelEnum.INFO, this.getClass().getSimpleName());
        this.routes = new ArrayList<>();
        routeCreationDtos.forEach(routeCreationDto -> {
            Route route = new Route(routeCreationDto);
            this.routes.add(route);
        });
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void printRoutes() {
        for (Route route: this.getRoutes()) {
            this.logger.log(route.toString());
        }
    }
}
