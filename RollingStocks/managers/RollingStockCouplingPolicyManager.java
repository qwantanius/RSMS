package RSMS.RollingStocks.managers;

import RSMS.RollingStocks.BasicRollingStock;
import RSMS.RollingStocks.types.RollingStockTypesEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class RollingStockCouplingPolicyManager {

    private Map<RollingStockTypesEnum, ArrayList<RollingStockTypesEnum>> COUPLING_POLICIES = new HashMap<>();

    public RollingStockCouplingPolicyManager() {
        this.buildElectricalGridPolicies();
        this.buildBasicFreightRailroadCarPolicies();
        this.buildBasicFreightRailroadCarPolicies();
        this.buildHeavyFreightRailroadCarPolicies();
        this.buildToxicLiquidRailroadCarPolicies();
        this.buildExplosiveLiquidRailroadCarPolicies();
    }

    public Map<RollingStockTypesEnum, ArrayList<RollingStockTypesEnum>> getCouplingPolicies() {
        return COUPLING_POLICIES;
    }

    public AtomicBoolean isCouplingValidAllowed(BasicRollingStock lhsrs, BasicRollingStock rhsrs) {
        AtomicBoolean isValid = new AtomicBoolean(true);
        lhsrs.getRollingStockTypes().forEach(rollingStockType -> {
            if(this.COUPLING_POLICIES.get(RollingStockTypesEnum.electrical_grid_provider).contains(rollingStockType)) {
                isValid.set(rhsrs.getRollingStockTypes().contains(RollingStockTypesEnum.electrical_grid_provider));
            }
        });
        return isValid;
    }

    private void buildElectricalGridPolicies() {
        ArrayList<RollingStockTypesEnum> railroadCarsToApplyPolicyFor = new ArrayList<>();
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.passenger_railroad_car);
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.refrigerated_railroad_car);
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.restaurant_railroad_car);
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.post_office_railroad_car);
        this.getCouplingPolicies().put(RollingStockTypesEnum.electrical_grid_provider, railroadCarsToApplyPolicyFor);
    }

    private void buildBasicFreightRailroadCarPolicies() {
        ArrayList<RollingStockTypesEnum> railroadCarsToApplyPolicyFor = new ArrayList<>();
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.refrigerated_railroad_car);
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.gaseous_materials_railroad_car);
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.liquid_materials_railroad_car);
        this.getCouplingPolicies().put(RollingStockTypesEnum.basic_railroad_freight_car, railroadCarsToApplyPolicyFor);
    }

    private void buildHeavyFreightRailroadCarPolicies() {
        ArrayList<RollingStockTypesEnum> railroadCarsToApplyPolicyFor = new ArrayList<>();
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.toxic_materials_railroad_car);
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.explosives_materials_railroad_car);
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.toxic_liquid_materials_railroad_car);
        this.getCouplingPolicies().put(RollingStockTypesEnum.heavy_railroad_freight_car, railroadCarsToApplyPolicyFor);
    }

    private void buildToxicLiquidRailroadCarPolicies() {
        ArrayList<RollingStockTypesEnum> railroadCarsToApplyPolicyFor = new ArrayList<>();
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.toxic_materials_railroad_car);
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.liquid_materials_railroad_car);
        this.getCouplingPolicies().put(RollingStockTypesEnum.toxic_liquid_materials_railroad_car, railroadCarsToApplyPolicyFor);
    }

    private void buildExplosiveLiquidRailroadCarPolicies() {
        ArrayList<RollingStockTypesEnum> railroadCarsToApplyPolicyFor = new ArrayList<>();
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.explosives_materials_railroad_car);
        railroadCarsToApplyPolicyFor.add(RollingStockTypesEnum.liquid_materials_railroad_car);
        this.getCouplingPolicies().put(RollingStockTypesEnum.explosives_liquid_materials_railroad_car, railroadCarsToApplyPolicyFor);
    }
}
