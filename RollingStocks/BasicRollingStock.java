package RSMS.RollingStocks;

import RSMS.Locomotive.Locomotive;
import RSMS.RollingStocks.managers.RollingStockCouplingManager;
import RSMS.RollingStocks.managers.RollingStockCouplingPolicyManager;
import RSMS.RollingStocks.managers.RollingStockLoadingManager;
import RSMS.RollingStocks.managers.RollingStockUnitsManager;
import RSMS.RollingStocks.types.Feature;
import RSMS.RollingStocks.types.RollingStockTypesEnum;
import RSMS.RollingStocks.types.TransportationLoadDto;
import RSMS.Routing.Station.Station;
import RSMS.common.logger.Logger;
import RSMS.common.logger.LoggerLevelEnum;
import RSMS.common.types.MessageFormats;

import java.util.ArrayList;
import java.util.UUID;

public class BasicRollingStock {

    public BasicRollingStock(RollingStockCouplingPolicyManager rollingStockCouplingPolicyManager) {
        super();
        this.uuid = UUID.randomUUID();
        this.rollingStockTypes = new ArrayList<RollingStockTypesEnum>();
        this.logger = new Logger(LoggerLevelEnum.INFO, this.getClass().getSimpleName());
        this.rollingStockUnitsManager = new RollingStockUnitsManager(this);
        this.rollingStockCouplingManager = new RollingStockCouplingManager(this, rollingStockCouplingPolicyManager);
        this.rollingStockLoadingManager = new RollingStockLoadingManager(this);
        this.features = new ArrayList<>();
    }

    private final UUID uuid;
    private String name;
    private final Logger logger;
    private final RollingStockCouplingManager rollingStockCouplingManager;
    private RollingStockUnitsManager rollingStockUnitsManager;
    private final RollingStockLoadingManager rollingStockLoadingManager;
    private final ArrayList<RollingStockTypesEnum> rollingStockTypes;
    private final ArrayList<Feature> features;
    public static final float ROLLING_STOCK_LENGTH_IN_METERS = 25f;
    private float defaultWeight = 5_000;
    private static boolean cyclingLoadingEnabled = false;

    public static void enableCyclingLoading() {
        BasicRollingStock.cyclingLoadingEnabled = true;
    }

    public static void disableCyclingLoading() {
        BasicRollingStock.cyclingLoadingEnabled = false;
    }

    public static boolean isCyclingLoadingAvailable() {
        return BasicRollingStock.cyclingLoadingEnabled;
    }


    public void increaseWeight(float amountInKg) {
        if ((amountInKg > 0) && (amountInKg <= 5_000)) {
            this.defaultWeight += amountInKg;
        }
    }

    public float getRollingStockWeight() {
        return this.defaultWeight;
    }

    public float getSummaryWeight() {
        return this.getRollingStockWeight() + this.getCurrentLoadWeight();
    }

    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void addFeature(Feature feature) {
        this.features.add(feature);
    }

    public ArrayList<Feature> getFeatures() {
        return this.features;
    }

    public String getUnitLoadingName(Locomotive locomotive) {
        String res = null;
        boolean isACProvider = false;
        String acProviderUnitName = "AC Provider";
        for(RollingStockTypesEnum rollingStockTypesEnum : this.rollingStockTypes) {
            switch (rollingStockTypesEnum) {
                case electrical_grid_provider:
                    isACProvider = true;
                case basic_railroad_freight_car:
                    res = "regular freight container";
                    break;
                case heavy_railroad_freight_car:
                    res = "extra armored freight container";
                    break;
                case baggage_railroad_car:
                    res = "place for baggage";
                    break;
                case passenger_railroad_car:
                    res = "regular seat unit";
                    break;
                case refrigerated_railroad_car:
                    res = "fridge and freezer unit";
                    break;
                case liquid_materials_railroad_car:
                    res = "liquid gallon";
                    break;
                case toxic_liquid_materials_railroad_car:
                    res = "extra armored liquid gallon";
                    break;
                case toxic_materials_railroad_car:
                    res = "extra armored hazard protective freight car";
                    break;
                case explosives_liquid_materials_railroad_car:
                    res = "extra armored safe gallons";
                    break;
                case explosives_materials_railroad_car:
                    res = "extra armored explosion protection freight car";
                    break;
                case restaurant_railroad_car:
                    res = "tables and bar for enjoying restaurant food";
                    break;
                case gaseous_materials_railroad_car:
                    res = "extra armored gas friendly gallon car";
                    break;
                case post_office_railroad_car:
                    res = "post office car";
                    break;
                case mail_railroad_car:
                    res = "mail car";
                    break;
            }
        }
        if (isACProvider) {
            res = res != null ? res + " + " + acProviderUnitName : acProviderUnitName;
        }
        return res;
    }

    public RollingStockUnitsManager getUnitsManager() {
        return this.rollingStockUnitsManager;
    }

    public RollingStockLoadingManager getLoadingManager() {
        return this.rollingStockLoadingManager;
    }

    public BasicRollingStock setUnitsOperator(RollingStockUnitsManager rollingStockUnitsManager) {
        this.rollingStockUnitsManager = rollingStockUnitsManager;
        return this;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public RollingStockCouplingManager getCouplingManager() {
        return this.rollingStockCouplingManager;
    }

    public ArrayList<RollingStockTypesEnum> getRollingStockTypes() {
        return this.rollingStockTypes;
    }

    public void addRollingStockType(RollingStockTypesEnum rollingStockType) {
        this.rollingStockTypes.add(rollingStockType);
    }

    public BasicRollingStock removeRollingStockType(RollingStockTypesEnum rollingStockType) {
        this.rollingStockTypes.remove(rollingStockType);
        return this;
    }

    public float getCurrentLoadWeight() {
        RollingStockUnitsManager unitsManager = this.getUnitsManager();
        return unitsManager.getLoadedUnits() * unitsManager.getMaxWeighPerUnitKg();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public BasicRollingStock info() {
        this.getLogger().log(
            String.format(
                MessageFormats.ROLLING_STOCK_INFO_DUMP_TEMPLATE,
                this.getName(),
                this.getUuid(),
                this.getCouplingManager().isAttached() ? this.getCouplingManager().getConnectedRollingStock().getUuid() : "NOT ATTACHED",
                this.getLoadingManager().isLoaded() ? this.getLoadingManager().getTransportationLoadDto() : "NOT LOADED YET",
                this.getUnitsManager().getMaxAmountOfUnits(),
                this.getUnitsManager().getLoadedUnits(),
                this.getUnitsManager().getAvailableUnitsForLoading(),
                this.getUnitsManager().getMaxWeighPerUnitKg(),
                this.getCurrentLoadWeight()
            )
        );
        return this;
    }
}
