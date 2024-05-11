package RSMS.Locomotive;

import RSMS.RollingStocks.managers.RollingStockEngineManager;
import RSMS.common.exceptions.RailroadHazardException;
import RSMS.common.helpers.MathHelper;
import RSMS.common.logger.Logger;
import RSMS.common.logger.LoggerLevelEnum;

import java.lang.reflect.Method;
import java.util.Random;

public class SpeedSeeder implements Runnable {

    public static final long ONE_SECOND_IN_MILLIS = 1_000;

    private final LocomotiveMovable locomotiveMovable;
    private final RollingStockEngineManager engineManager;
    private final Logger logger = new Logger(LoggerLevelEnum.INFO, this.getClass().getSimpleName());
    private final Logger errLogger = new Logger(LoggerLevelEnum.ERROR, this.getClass().getSimpleName());
    private final Random random = new Random();
    private volatile boolean isPaused;
    private volatile boolean isErrored = false;
    private int tic = 0;

    public SpeedSeeder(RollingStockEngineManager engineManager, LocomotiveMovable locomotiveMovable) {
        this.engineManager = engineManager;
        this.locomotiveMovable = locomotiveMovable;
    }

    public void pause() {
        this.isPaused = true;
    }

    public void resume() {
        this.isPaused = false;
    }

    public boolean isErrored() {
        return this.isErrored;
    }

    public int getTic() {
        return tic;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (engineManager.getCurrentSpeedInKmPerHour() >= LocomotiveMovable.maxAllowedSpeedInKm) {
                    throw new RailroadHazardException(this.locomotiveMovable);
                }
                if (!this.isPaused) {
                    this.printStationToStationProgress();
                    if (engineManager.getCurrentSpeedInKmPerHour() < LocomotiveMovable.speedThatRequiresCruiseMode) {
                        engineManager.increaseCurrentSpeedByKm(LocomotiveMovable.kmMinimalChangeUnit);
                    } else {
                        this.reflectSpeedByRandomSeed(this.engineManager);
                    }
                }
                tic++;
                Thread.sleep(ONE_SECOND_IN_MILLIS);
            }
        } catch (InterruptedException err) {
            this.logger.log("Locomotive arrived.");
            this.resetTic();
        } catch (RailroadHazardException railroadHazardException) {
            this.isErrored = true;
            this.errLogger.log(railroadHazardException.getMessage());
        }
    }

    public void resetTic() {
        this.tic = 0;
    }

    public void printStationToStationProgress() {
        this.logger.log("\tJourney from station to station completed on : " + this.getStationToStationCompletionPercentage());
    }

    public int getStationToStationCompletionPercentage() {
        int totalPercentage = (int) MathHelper.calculatePercentage(this.getTic(), (float)this.locomotiveMovable.getCurrentTimeEstimation()/SpeedSeeder.ONE_SECOND_IN_MILLIS);
        return totalPercentage >= 100 ? 100 : totalPercentage;
    }

    private void reflectSpeedByRandomSeed(RollingStockEngineManager engineManager) {
        try {
            Method[] selectSpeedSeedsFrom = {
                engineManager.getClass().getDeclaredMethod("increaseCurrentSpeedByPercentage", float.class),
                engineManager.getClass().getDeclaredMethod("decreaseCurrentSpeedByPercentage", float.class),
            };
            int randomSelector = this.random.nextInt(selectSpeedSeedsFrom.length);
            Method selectedSeed = selectSpeedSeedsFrom[randomSelector];
            selectedSeed.invoke(engineManager, LocomotiveMovable.percentageMinimalChangeUnit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
