package RSMS.common.mockers;

import RSMS.Locomotive.LocomotiveMovable;
import RSMS.RollingStocks.BasicRollingStock;
import RSMS.RollingStocks.RollingStockConfig;
import RSMS.RollingStocks.RollingStockFactory;
import RSMS.RollingStocks.managers.RollingStockLoadingManager;
import RSMS.RollingStocks.managers.RollingStockUnitsManager;
import RSMS.RollingStocks.types.RollingStockTypesEnum;
import RSMS.RollingStocks.types.TransportationLoadDto;
import RSMS.Routing.Route.Route;

import java.util.ArrayList;
import java.util.Random;

public class RollingStockMockingService {

    private static int counter = 0;

    public static ArrayList<BasicRollingStock> createRandomRollingStocks(RollingStockFactory rollingStockFactory, int amount) {
        counter = 0;
        ArrayList<BasicRollingStock> rollingStocks = new ArrayList<>();
        for(int i=0; i<amount; i++) {
            rollingStocks.add(rollingStockFactory.create(RollingStockMockingService.generateRandomDto()));
            counter++;
        }
        return rollingStocks;
    }

    public static ArrayList<BasicRollingStock> createSpecificCars(
        RollingStockFactory rollingStockFactory,
        ArrayList<RollingStockTypesEnum> specificTypes,
        int amount
    ) {
        counter = 0;
        ArrayList<BasicRollingStock> rollingStocks = new ArrayList<>();
        for(int i=0; i<amount; i++) {
            RollingStockConfig config = RollingStockMockingService.generateRandomDto();
            config.rollingStockTypes = specificTypes;
            rollingStocks.add(rollingStockFactory.create(config));
            counter++;
        }
        return rollingStocks;
    }

    public static ArrayList<RollingStockConfig> genRandomRollingStocksConfigs(int amount) {
        ArrayList<RollingStockConfig> rollingStockConfigs = new ArrayList<>();
        for(int i=0; i<=amount; i++) {
            rollingStockConfigs.add(generateRandomDto());
        }
        return  rollingStockConfigs;
    }

    public static RollingStockConfig generateRandomDto() {
        return new RollingStockConfig(
            String.format("RS-%d-MOCK", new Random().nextInt(1000)),
            RollingStockMockingService.genRandomUnitsAmount(10, 50, new Random()),
            RollingStockMockingService.genRandomWeight(100, 700, new Random()),
            RollingStockMockingService.getRandomEnumElements(1)
        );
    }

    public static float genRandomWeight(float from, float to, Random seed) {
        return seed.nextFloat(to - from) + from;
    }

    public static int genRandomUnitsAmount(int from, int to, Random seed) {
        return seed.nextInt(to - from) + from;
    }

    public static ArrayList<RollingStockTypesEnum> getRandomEnumElements(int numElements) {
        ArrayList<RollingStockTypesEnum> elements = new ArrayList<>();
        Random random = new Random();
        RollingStockTypesEnum[] values = RollingStockTypesEnum.values();
        for (int i = 0; i < numElements; i++) {
            int randomIndex = random.nextInt(values.length);
            elements.add(values[randomIndex]);
        }
        return elements;
    }

    public static void genRandomLoads(LocomotiveMovable locomotiveMovable) {
        Route route = locomotiveMovable.getAssignedRoute();
        locomotiveMovable.getLocomotive().getBody().forEach(rs -> {
            RollingStockLoadingManager rslm = rs.getLoadingManager();
            RollingStockUnitsManager rsum = rs.getUnitsManager();
            Random random = new Random();
            if (!rslm.isLoaded()) {
                rslm.load(new TransportationLoadDto(
                    "_mock_load_" + rs.getName(),
                    rsum.getAvailableUnitsForLoading(),
                    rsum.getMaxWeighPerUnitKg(),
                    rs.getRollingStockTypes().get(0),
                    // set static unloading and loading
                    // to load/unload at some different
                    // stations - use api unload and load functions
                    route.getStations().get(random.nextInt(route.getStations().size()))
                ));
            }
        });
    }
}
