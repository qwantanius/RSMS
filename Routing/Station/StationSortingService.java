package RSMS.Routing.Station;

import RSMS.Routing.Route.Route;

import java.util.Comparator;

public final class StationSortingService {

    public final static Comparator<Station> ASC_EVERY_FROM_START(Route route) {
        return (lhsStation, rhsStation) -> {
            double lhsDistFromStart = lhsStation.getDistanceInKmFrom(route.getStart());
            double rhsDistFromStart = rhsStation.getDistanceInKmFrom(route.getStart());
            return (int) (lhsDistFromStart - rhsDistFromStart);
        };
    }

}
