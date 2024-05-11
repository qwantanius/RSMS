package RSMS.RollingStocks.managers;

import RSMS.Locomotive.LocomotiveHead;

public class RollingStockEngineManager {

    private boolean isCruiseControlOn = false;
    private final LocomotiveHead rollingStock;
    private float currentSpeedInKmPerHour;

    public RollingStockEngineManager(LocomotiveHead rollingStock) {
        this.rollingStock = rollingStock;
    }

    public float getCurrentSpeedInKmPerHour() {
        return this.currentSpeedInKmPerHour;
    }

    public float calcPercentageFromSpeed(float percentage) {
        return (this.currentSpeedInKmPerHour/100) * percentage;
    }

    public RollingStockEngineManager increaseCurrentSpeedByPercentage(float percentage) {
        this.currentSpeedInKmPerHour = this.currentSpeedInKmPerHour == 0 ? 1 : this.currentSpeedInKmPerHour;
        this.currentSpeedInKmPerHour += calcPercentageFromSpeed(percentage);
        this.speedReflectedMsg("increased", percentage, "percentage");
        return this;
    }

    public LocomotiveHead getRollingStock() {
        return rollingStock;
    }

    public void decreaseCurrentSpeedByPercentage(float percentage) {
        this.currentSpeedInKmPerHour -= calcPercentageFromSpeed(percentage);
        this.speedReflectedMsg("decreased", percentage, "percentage");
    }

    public void increaseCurrentSpeedByKm(float kmPerHour) {
        this.currentSpeedInKmPerHour += kmPerHour;
        this.speedReflectedMsg("increased", kmPerHour, "km");
    }

    public void decreaseCurrentSpeedByKm(float kmPerHour) {
        this.currentSpeedInKmPerHour -= kmPerHour;
        this.speedReflectedMsg("decreased", kmPerHour, "km");
    }

    private void speedReflectedMsg(String action, float units, String unitName) {
        String SPEED_REFLECTION_TMP_MSG = "Speed was %s by %f %s, now its %f km/h";
        this.rollingStock.getLogger().log(String.format(
            SPEED_REFLECTION_TMP_MSG,
            action,
            units,
            unitName,
            this.currentSpeedInKmPerHour
        ));
    }
}
