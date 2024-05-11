package RSMS.common.exceptions;

import RSMS.Locomotive.LocomotiveMovable;

public class RailroadHazardException extends Exception {
    public RailroadHazardException(LocomotiveMovable locomotiveMovable) {
        super(
            String.format(
                "[!] RAILROAD HAZARD: SPEED LIMIT IS REACHED by locomotive %s at route %s  RAILROAD HAZARD [!] near station %s",
                locomotiveMovable.getLocomotive().getName(),
                locomotiveMovable.getAssignedRoute().getRouteName(),
                locomotiveMovable.getNextStation().getStationName()
            )
        );
    }
}
