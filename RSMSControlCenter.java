package RSMS;

import RSMS.Locomotive.Locomotive;
import RSMS.Locomotive.LocomotiveMovable;
import RSMS.Locomotive.LocomotiveStatusEnum;
import RSMS.Locomotive.SpeedSeeder;
import RSMS.Routing.Route.Route;
import RSMS.Routing.Station.Station;
import RSMS.common.logger.Logger;
import RSMS.common.logger.LoggerLevelEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RSMSControlCenter {

    public static final long quickStopAtStationDurationInMillis = SpeedSeeder.ONE_SECOND_IN_MILLIS*2;
    // not 'long stop' but regular because in future we may scale our code base
    // scale our code base by adding really long stops
    public static final long regularStopAtStationDurationInMillis = SpeedSeeder.ONE_SECOND_IN_MILLIS*30;

    private static double delayBetweenLocomotiveExecuting = SpeedSeeder.ONE_SECOND_IN_MILLIS;
    public static final Logger logger = new Logger(LoggerLevelEnum.INFO, RSMSControlCenter.class.getSimpleName());
    private static final Logger errLoger = new Logger(LoggerLevelEnum.INFO, RSMSControlCenter.class.getSimpleName() + ".err");
    private static final ArrayList<LocomotiveMovable> movableLocomotives = new ArrayList<>();
    private static final HashMap<Station, LocomotiveMovable> busyStations = new HashMap<>();

    public static ArrayList<LocomotiveMovable> getRunningLocomotives() {
        RSMSControlCenter.movableLocomotives.sort((lhsloc, rhsloc) -> (int) (rhsloc.getAssignedRoute().getRouteLength() - lhsloc.getAssignedRoute().getRouteLength()));
        return RSMSControlCenter.movableLocomotives;
    }

    public static boolean isStationBusy(Station station) {
        return RSMSControlCenter.busyStations.containsKey(station);
    }

    public static void reserveStation(Station stationToReserve, LocomotiveMovable reserveBy) {
        RSMSControlCenter.busyStations.put(stationToReserve, reserveBy);
    }

    public static void consumeStation(Station station) {
        RSMSControlCenter.busyStations.remove(station);
    }

    public static LocomotiveMovable getLocomotiveOnBusyStation(Station station) {
        return RSMSControlCenter.busyStations.get(station);
    }

    public static Thread startJourney(LocomotiveMovable locomotiveMovable) {
        if (!RSMSControlCenter.movableLocomotives.contains(locomotiveMovable)) {
            RSMSControlCenter.movableLocomotives.add(locomotiveMovable);
            RSMSControlCenter.reserveStation(locomotiveMovable.getCurrentStation(), locomotiveMovable);
            if (locomotiveMovable.getNextStation() != null) {
                RSMSControlCenter.reserveStation(locomotiveMovable.getNextStation(), locomotiveMovable);
            }
            Thread locomotiveThread = new Thread(locomotiveMovable);
            locomotiveThread.start();
            return locomotiveThread;
        }
        throw new RuntimeException("Attempting to execute already running locomotive : " + locomotiveMovable.getLocomotive().getName());
    }

    public static void printStartedMessage(LocomotiveMovable locomotiveMovable) {
        Locomotive locomotive = locomotiveMovable.getLocomotive();
        Route assignedRoute = locomotiveMovable.getAssignedRoute();
        RSMSControlCenter.logger.log(
            "\nLocomotive: " + locomotive.getName() + " started journey" +
            "\nFrom      : " + assignedRoute.getStart().getStationName() +
            "\nTo        : " + assignedRoute.getDestination().getStationName()
        );
    }

    public static void startRepeatableJourney(LocomotiveMovable locomotiveMovable, int repeatableJourneyLimit) {
        try {
            Route assignedRoute = locomotiveMovable.getAssignedRoute();
            int journeyCounter = 0;
            while (journeyCounter != repeatableJourneyLimit + 1) {
                while (RSMSControlCenter.isStationBusy(locomotiveMovable.getAssignedRoute().getStart())) {
                    Thread.sleep(SpeedSeeder.ONE_SECOND_IN_MILLIS);
                    locomotiveMovable.setLocomotiveStatus(LocomotiveStatusEnum.BLOCKED);
                }
                locomotiveMovable.assignNewRoute(assignedRoute);
                Thread journeyThread = RSMSControlCenter.startJourney(locomotiveMovable);
                journeyThread.join();
                if (!RSMSControlCenter.movableLocomotives.contains(locomotiveMovable)) {
                    RSMSControlCenter.movableLocomotives.add(locomotiveMovable);
                }
                locomotiveMovable.stopAtStation(RSMSControlCenter.regularStopAtStationDurationInMillis);
                RSMSControlCenter.logger.log("Locomotive " + locomotiveMovable.getLocomotive().getName() + " starts journey back");
                assignedRoute = Route.createReturnRoute(assignedRoute);
                journeyCounter++;
                if (RSMSControlCenter.movableLocomotives.contains(locomotiveMovable)) {
                    RSMSControlCenter.movableLocomotives.remove(locomotiveMovable);
                }
                RSMSControlCenter.consumeStation(locomotiveMovable.getCurrentStation());
                if (locomotiveMovable.getNextStation() != null) {
                    RSMSControlCenter.consumeStation(locomotiveMovable.getNextStation());
                }
            }
        } catch (InterruptedException err) {
            RSMSControlCenter.errLoger.log("Unexpected interrupt: " + err.getMessage());
        }
    }

    public static void startRepeatableJourney(LocomotiveMovable locomotiveMovable) {
        RSMSControlCenter.startRepeatableJourney(locomotiveMovable, Integer.MAX_VALUE);
    }

    public static void executeLocomotivesForRoute(ArrayList<LocomotiveMovable> movableLocomotives) {
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            for (LocomotiveMovable locomotiveMovable : movableLocomotives) {
                String assignedRouteName = locomotiveMovable.getAssignedRoute().getRouteName();
                String locomotiveName = locomotiveMovable.getLocomotive().getName();
                locomotiveMovable.setLocomotiveStatus(LocomotiveStatusEnum.FREE_TO_GO);
                RSMSControlCenter.logger.log("Executing locomotive : " + locomotiveName + " for route " + assignedRouteName);
                executorService.execute(() -> RSMSControlCenter.startRepeatableJourney(locomotiveMovable));
                Thread.sleep(SpeedSeeder.ONE_SECOND_IN_MILLIS);
            }
            executorService.shutdown();
        } catch (InterruptedException e) {
            RSMSControlCenter.errLoger.log(e.getMessage());
        }
    }
}