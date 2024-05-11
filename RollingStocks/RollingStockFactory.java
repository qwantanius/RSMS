package RSMS.RollingStocks;

import RSMS.RollingStocks.types.Feature;
import RSMS.RollingStocks.types.RollingStockTypesEnum;
import RSMS.common.logger.Logger;
import RSMS.common.logger.LoggerLevelEnum;
import RSMS.common.types.AbstractFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RollingStockFactory extends AbstractFactory<BasicRollingStock, RollingStockConfig> {

    private final Logger logger;
    private BasicRollingStock rollingStock;

    public RollingStockFactory() {
        this.logger = new Logger(LoggerLevelEnum.INFO, this.getClass().getSimpleName());
        this.logger.log("Factory initiated");
    }

    @Override
    public BasicRollingStock create(RollingStockConfig config) {
        this.rollingStock = new BasicRollingStock(config.rollingStockCouplingPolicyManager);
        for(RollingStockTypesEnum rollingStockType: config.rollingStockTypes) {
            this.rollingStock.addRollingStockType(rollingStockType);
        }
        this.rollingStock.getUnitsManager().setMaxAmountOfUnits(config.availableUnits);
        this.rollingStock.getUnitsManager().setMaxWeightPerUnitKg(config.weighKgPerUnits);
        this.rollingStock.setName(config.name);
        // well, ofcourse this is mock feature,
        // but implementation of every of them will took to much time
        // in order to do not spend it , we just set the feature enum id
        // and later if we wish we may add FeatureExecutor class into
        // BasicRollingStock class which wait for events/signals that
        // will trigger this feature
        // if there are any chance to use this project in future projects
        // with GUI libs I would be happy to demonstrate such an approach :)
        this.setFeature(rollingStock);
        // for nicer experience use random generator:
        // this.applyRandomFeatures(rollingStock);
        return this.rollingStock;
    }

    private void applyRandomFeatures(BasicRollingStock rollingStock) {
        Random random = new Random();
        for (int i=0; i<= random.nextInt(5); i++) {
            Feature selectedFeature = Feature.values()[random.nextInt(Feature.values().length)];
            this.rollingStock.addFeature(selectedFeature);
        }
    }

    private void setFeature(BasicRollingStock rollingStock) {
        for (RollingStockTypesEnum rsType: rollingStock.getRollingStockTypes()) {
            switch (rsType) {
                case passenger_railroad_car:
                    // or use applyRandomFeatures() if you wish
                    this.rollingStock.addFeature(Feature.OnboardOffice); // useful for business travelers who need to work during the journey
                    this.rollingStock.addFeature(Feature.WifiEnabled); // standard feature for passenger cars
                    this.rollingStock.increaseWeight(1_000); // some additional loadings or protection like armor so on utilites
                    break;
                case post_office_railroad_car:
                    this.rollingStock.addFeature(Feature.TruckLoader); // useful for loading and unloading packages directly from the railcar
                    this.rollingStock.addFeature(Feature.SmartMonitoring); // useful for tracking the location and status of packages in transit
                    this.rollingStock.increaseWeight(500); // some additional loadings or protection like armor so on utilites
                    break;
                case baggage_railroad_car:
                    this.rollingStock.addFeature(Feature.FlexiMove); // useful for moving baggage around inside the railcar
                    this.rollingStock.addFeature(Feature.HighSecurityLocks); // helps prevent theft of valuable baggage
                    this.rollingStock.increaseWeight(700); // some additional loadings or protection like armor so on utilites
                    break;
                case mail_railroad_car:
                    this.rollingStock.addFeature(Feature.EcoFriendly); // operates using sustainable energy sources, reducing environmental impact
                    this.rollingStock.addFeature(Feature.AutomatedLoading); // streamlines the loading and unloading process, reducing labor costs
                    this.rollingStock.increaseWeight(900); // some additional loadings or protection like armor so on utilites
                    break;
                case restaurant_railroad_car:
                    this.rollingStock.addFeature(Feature.OnboardEntertainment); // enhances the dining experience for passengers
                    this.rollingStock.addFeature(Feature.AdjustableLighting); // allows for customizable ambiance during meal service
                    this.rollingStock.increaseWeight(555); // some additional loadings or protection like armor so on utilites
                    break;
                case basic_railroad_freight_car:
                    this.rollingStock.addFeature(Feature.HighSecurityLocks); // helps prevent theft of valuable cargo
                    this.rollingStock.addFeature(Feature.AutomatedLoading); // streamlines the loading and unloading process, reducing labor costs
                    this.rollingStock.increaseWeight(1_000); // some additional loadings or protection like armor so on utilites
                    break;
                case heavy_railroad_freight_car:
                    this.rollingStock.addFeature(Feature.CargoCompartment); // provides dedicated space for carrying large and heavy cargo
                    this.rollingStock.addFeature(Feature.ArticulatingCouplers); // improves ride quality and stability for heavier loads
                    this.rollingStock.increaseWeight(2_000); // some additional loadings or protection like armor so on utilites
                    break;
                case refrigerated_railroad_car:
                    this.rollingStock.addFeature(Feature.ClimateControl); // maintains a consistent temperature for transporting temperature-sensitive cargo
                    this.rollingStock.addFeature(Feature.SmartMonitoring); // allows for remote monitoring of temperature and other conditions
                    this.rollingStock.increaseWeight(2_500); // some additional loadings or protection like armor so on utilites
                    break;
                case liquid_materials_railroad_car:
                    this.rollingStock.addFeature(Feature.HybridPropulsion); // reduces emissions and fuel costs when transporting liquids over long distances
                    this.rollingStock.addFeature(Feature.EmergencyBrakeSystem); // improves safety in case of emergency stops or accidents
                    this.rollingStock.increaseWeight(2_000); // some additional loadings or protection like armor so on utilites
                    break;
                case gaseous_materials_railroad_car:
                    this.rollingStock.addFeature(Feature.SelfCleaning); // helps maintain cleanliness and hygiene when transporting gaseous materials
                    this.rollingStock.addFeature(Feature.HighSecurityLocks); // helps prevent unauthorized access to potentially dangerous materials
                    this.rollingStock.increaseWeight(2_000); // some additional loadings or protection like armor so on utilites
                    break;
                case explosives_materials_railroad_car:
                    this.rollingStock.addFeature(Feature.HighSecurityLocks); // helps prevent theft and unauthorized access to explosives
                    this.rollingStock.addFeature(Feature.EmergencyBrakeSystem); // improves safety in case of accidents or explosions
                    this.rollingStock.increaseWeight(2_500); // some additional loadings or protection like armor so on utilites
                    break;
                case explosives_liquid_materials_railroad_car:
                    this.rollingStock.addFeature(Feature.FlexiWheels); // reduces emissions and fuel costs when transporting hazardous materials
                    this.rollingStock.addFeature(Feature.SUPERHUMIDIdk); // improves safety
                    this.rollingStock.increaseWeight(2_500); // some additional loadings or protection like armor so on utilites
                    break;
                case toxic_liquid_materials_railroad_car:
                    this.rollingStock.addFeature(Feature.ISOStamp7435);
                    this.rollingStock.addFeature(Feature.ToxicPumpProtection);
                    this.rollingStock.increaseWeight(2_500); // some additional loadings or protection like armor so on utilites
                    break;
                case toxic_materials_railroad_car:
                    this.rollingStock.addFeature(Feature.ToxicPressureTrackingSystem);
                    this.rollingStock.addFeature(Feature.ToxicPumpProtection);
                    this.rollingStock.increaseWeight(2_700); // some additional loadings or protection like armor so on utilites
                    break;
                case electrical_grid_provider:
                    this.rollingStock.addFeature(Feature.SwitchCaseProtection);
                    this.rollingStock.addFeature(Feature.ACConnectorFeatureLight);
                    this.rollingStock.increaseWeight(200); // some additional loadings or protection like armor so on utilites
                    break;
                default:
                    this.logger.log("No feature found for " + rollingStock.getName() + " of type " + rsType);
            }
        }
    }

}
