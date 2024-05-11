package RSMS.Locomotive;

import RSMS.RSMSControlCenter;
import RSMS.RollingStocks.BasicRollingStock;
import RSMS.RollingStocks.managers.RollingStockEngineManager;
import RSMS.RollingStocks.types.TransportationLoadDto;
import RSMS.Routing.Route.Route;
import RSMS.Routing.Station.Station;
import RSMS.common.exceptions.RollingStockIsNotLoadedException;
import RSMS.common.exceptions.RollingStockUnloadingFailed;
import RSMS.common.helpers.MathHelper;
import RSMS.common.logger.Logger;
import RSMS.common.logger.LoggerLevelEnum;

import java.util.ArrayList;
import java.util.Random;

public class LocomotiveMovable implements Runnable {

    public static final int percentageMinimalChangeUnit = 3;
    public static final float kmMinimalChangeUnit = 10;
    public static final int maxAllowedSpeedInKm = 200;
    public static float speedThatRequiresCruiseMode = 100;
    public static float engineWarmUpDistanceInKm = 0.5f;
    public static int delayInSecondsForSlowingDownWhenTesting = 0;


    private Route assignedRoute;
    private Station currentStation;
    private boolean stoppedAtStation;
    private Station nextStation;
    private final Locomotive locomotive;
    private final Logger logger;
    private final Logger errLogger;
    private long currentTimeEstimation;
    // p.s.: change to double, sometimes you will see in logs nice periodical fractions e.g. 46.46464646 so on
    private int tripPercentage;
    private boolean isBlocked = false;
    private SpeedSeeder speedSeeder;
    private LocomotiveStatusEnum locomotiveStatus;
    private int journeyCounter = 0;

    public LocomotiveMovable(Route moveThrough, Locomotive locomotive) {
        this.assignedRoute = moveThrough;
        this.locomotive = locomotive;
        this.logger = locomotive.getLogger();
        this.errLogger = new Logger(LoggerLevelEnum.ERROR, this.getClass().getSimpleName());
        this.currentStation = this.assignedRoute.getStations().get(0);
    }

    public double getTripPercentage() {
        return tripPercentage;
    }

    public void assignNewRoute(Route route) {
        this.assignedRoute = route;
    }

    public Locomotive getLocomotive() {
        return locomotive;
    }

    public Route getAssignedRoute() {
        return assignedRoute;
    }

    public long getCurrentTimeEstimation() {
        return currentTimeEstimation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public SpeedSeeder getSpeedSeeder() {
        return this.speedSeeder;
    }

    public LocomotiveStatusEnum getLocomotiveStatus() {
        return this.locomotiveStatus;
    }

    public void setLocomotiveStatus(LocomotiveStatusEnum locomotiveStatus) {
        this.locomotiveStatus = locomotiveStatus;
    }

    public int getJourneyCounter() {
        return journeyCounter;
    }

    @Override
    public void run() {
        this.journeyCounter++;
        try {
            this.setLocomotiveStatus(LocomotiveStatusEnum.BLOCKED);
            ArrayList<Station> stations = this.assignedRoute.getStations();
            RollingStockEngineManager engineManager = this.locomotive.getHead().getEngine();
            this.speedSeeder = new SpeedSeeder(engineManager, this);
            Thread speedSeederThread = new Thread(speedSeeder);
            speedSeederThread.start();
            for (int i = 0; i < stations.size() - 1; i++) {
                if (speedSeeder.isErrored()) {
                    break;
                }
                if (stations.get(i).getStationName().equals(this.getAssignedRoute().getStart().getStationName())) {
                    RSMSControlCenter.consumeStation(this.currentStation);
                    RSMSControlCenter.consumeStation(this.nextStation);
                    continue;
                }
                this.tripPercentage = (int) MathHelper.calculatePercentage(i, stations.size()-1);
                this.reassignStationsIteratively(stations, i);
                this.blockIfNeeded(speedSeeder, stations, i);
                RSMSControlCenter.reserveStation(this.nextStation, this);
                double distance = this.calculateJourneyAndWarmingUpDistance();
                this.currentTimeEstimation = this.calculateArrivalTimeFor(distance);
                this.printArrivalInfo();
                this.printSpeedDetails(engineManager);
                this.driveToAndSlowDownBeforeStation(engineManager, speedSeeder);
                this.stopAtStation(RSMSControlCenter.quickStopAtStationDurationInMillis);
                speedSeeder.resetTic();
                this.proceedJourney(speedSeeder);
            }
            this.tripPercentage = (int)MathHelper.calculatePercentage(stations.size()-1, stations.size()-1);
            this.printJourneyCompletionInfo();
            this.locomotiveStatus = LocomotiveStatusEnum.STOPPED_AT_STATION;
            speedSeederThread.interrupt();
        } catch (Exception err) {
            this.errLogger.log(err.getMessage());
        }
    }

    private void blockIfNeeded(SpeedSeeder speedSeeder, ArrayList<Station> stations, int iterator) throws InterruptedException {
        this.logger.log(this.getLocomotive().getName() + " : BLOCKED");
        this.locomotiveStatus = LocomotiveStatusEnum.BLOCKED;
        speedSeeder.pause();
        while (RSMSControlCenter.isStationBusy(stations.get(iterator + 1))) {
            Thread.sleep(SpeedSeeder.ONE_SECOND_IN_MILLIS);
        }
        this.logger.log(this.getLocomotive().getName() + " : FREE TO GO");
        this.locomotiveStatus = LocomotiveStatusEnum.FREE_TO_GO;
        // check
        RSMSControlCenter.printStartedMessage(this);
        speedSeeder.resume();
    }

    private void driveToAndSlowDownBeforeStation(
        RollingStockEngineManager engineManager,
        SpeedSeeder speedSeeder
    ) throws InterruptedException {
        // calc 10% extra time to slow down
        // P.S.: Yes, in terms of very short travelling
        // time like we have in our simulation we will
        // have very quick slowing down, but if you will
        // apply longer distances and longer travelling
        // time, we will achieve +- real slowing down
        // train path so in order to increase a bit slowing
        // down time, to see how it works, you can change
        // value of N in DELAY_FOR_TESTING * {N} where is Integer
        // this calculation stands for showing real live
        // trains slowing down before stations area
        long slowDownTime = ((this.currentTimeEstimation/100)*10);
        Thread.sleep(this.currentTimeEstimation - slowDownTime);
        speedSeeder.pause();
        this.slowDownBeforeStation(slowDownTime, engineManager);
    }

    private void slowDownBeforeStation(long slowDownTime, RollingStockEngineManager engineManager) throws InterruptedException {
        this.logger.log("Decreasing speed before station...");
        while(engineManager.getCurrentSpeedInKmPerHour() >= LocomotiveMovable.kmMinimalChangeUnit) {
            long delay = SpeedSeeder.ONE_SECOND_IN_MILLIS * LocomotiveMovable.delayInSecondsForSlowingDownWhenTesting;
            this.currentTimeEstimation += delay;
            engineManager.decreaseCurrentSpeedByKm(LocomotiveMovable.kmMinimalChangeUnit);
            Thread.sleep(slowDownTime + delay);
        }
        this.idleEngine(engineManager);
    }

    public void stopAtStation(long duration) throws InterruptedException {
        this.stoppedAtStation = true;
        this.logger.log(String.format("\t ( Waiting %d seconds at station for possible loads/unloads )", MathHelper.millisToSeconds(duration)));
        this.getLocomotive().getBody().forEach(rs -> {
            String currStationName = this.currentStation.getStationName();
            String unloadAtStationName = rs.getLoadingManager().getTransportationLoadDto().unloadAt.getStationName();
            if (currStationName.equals(unloadAtStationName)) {
                try {
                    Random random = new Random();
                    rs.getLoadingManager().unload(rs.getUnitsManager().getLoadedUnits());
                    this.setLocomotiveStatus(LocomotiveStatusEnum.UNLOADED);
                    if (BasicRollingStock.isCyclingLoadingAvailable()) {
                        // loading for a cycle load to be in our app
                        TransportationLoadDto transportationLoadDto = rs.getLoadingManager().getTransportationLoadDto();
                        transportationLoadDto.unloadAt = this.assignedRoute.getStations().get(random.nextInt(this.assignedRoute.getStations().size()));
                        transportationLoadDto.unitsAmountRequiredForLoading = rs.getUnitsManager().getAvailableUnitsForLoading();
                        rs.getLoadingManager().load(transportationLoadDto);
                        this.setLocomotiveStatus(LocomotiveStatusEnum.LOADED);
                    }
                } catch (RollingStockIsNotLoadedException | RollingStockUnloadingFailed e) {
                    this.errLogger.log(e.toString());
                }
            }
        });
        this.setLocomotiveStatus(LocomotiveStatusEnum.STOPPED_AT_STATION);
        Thread.sleep(duration);
    }

    private void idleEngine(RollingStockEngineManager engineManager) {
        float currentSpeed = engineManager.getCurrentSpeedInKmPerHour();
        if (currentSpeed != 0) {
            engineManager.decreaseCurrentSpeedByKm(currentSpeed);
        }
    }

    private void printSpeedDetails(RollingStockEngineManager engineManager) {
        this.logger.log("Moving from " + this.currentStation.getStationName() + " to " + this.nextStation.getStationName());
        this.logger.log("Current speed : " + engineManager.getCurrentSpeedInKmPerHour() + " km/h");
        this.setLocomotiveStatus(this.journeyCounter % 2 == 0 ? LocomotiveStatusEnum.MOVING_THROUGH_RETURN_ROUTE : LocomotiveStatusEnum.MOVING);
        this.printJourneyCompletionInfo();
    }

    private void printArrivalInfo() {
        this.logger.log("Estimated arrival in " + MathHelper.millisToSeconds(this.currentTimeEstimation) + " seconds");
    }

    private void printJourneyCompletionInfo() {
        this.logger.log("Journey completion progress : " + this.getTripPercentage());
    }

    private void proceedJourney(SpeedSeeder speedSeeder) {
        // moving forward if we are not reached destination
        // if we reached destination we should reassign value
        // of stoppedAtStation flag in order to do not log
        // message below after we reached the destination
        this.stoppedAtStation = this.nextStation.getStationName().equals(this.getAssignedRoute().getDestination().getStationName());
        if (!this.stoppedAtStation) {
            RSMSControlCenter.consumeStation(this.currentStation);
            RSMSControlCenter.consumeStation(this.nextStation);
            speedSeeder.resume();
            this.logger.log("\t( Moving forward after station brake at station " + this.nextStation.getStationName() + " )");
        }
    }

    private long calculateArrivalTimeFor(double distance) {
        return (long) (distance / LocomotiveMovable.speedThatRequiresCruiseMode);
    }

    private double calculateJourneyAndWarmingUpDistance() {
        double distanceTillNextStation = this.currentStation.getDistanceInKmFrom(nextStation);
        return distanceTillNextStation - LocomotiveMovable.engineWarmUpDistanceInKm;
    }

    private void reassignStationsIteratively(ArrayList<Station> stations, int iterator) {
        this.currentStation = stations.get(iterator);
        this.nextStation = stations.get(iterator + 1);
    }
}
