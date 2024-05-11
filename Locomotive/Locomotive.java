package RSMS.Locomotive;

import RSMS.RollingStocks.BasicRollingStock;
import RSMS.Routing.Station.Station;
import RSMS.common.logger.Logger;
import RSMS.common.logger.LoggerLevelEnum;

import java.util.ArrayList;

public class Locomotive {

    private final String name;
    private LocomotiveHead head;
    private ArrayList<BasicRollingStock> body;
    private final float maxWeightPerRollingStockInKG;
    private final int maxAmountOfRollingStocks;
    private final Station startStation;
    private final Station destinationStation;
    private final Logger logger;
    private Station HOME;
    private final LocomotiveRollingStocksManager locomotiveRollingStocksManager;

    public Locomotive(LocomotiveCreationDto locomotiveCreationDto) {
        this.body = new ArrayList<>();
        this.name = locomotiveCreationDto.locomotiveName;
        this.head = locomotiveCreationDto.LOCOMOTIVES_HEAD;
        this.startStation = locomotiveCreationDto.startStation;
        this.destinationStation = locomotiveCreationDto.destinationStation;
        this.maxWeightPerRollingStockInKG = locomotiveCreationDto.MAX_WEIGHT_PER_ROLLING_STOCK_IN_KG;
        this.maxAmountOfRollingStocks = locomotiveCreationDto.MAX_AMOUNT_OF_ROLLING_STOCKS;
        this.locomotiveRollingStocksManager = new LocomotiveRollingStocksManager(this);
        String loggerName = String.format("%s_%s_%s", this.getClass().getSimpleName(), this.getName(), this.getHead().getName());
        this.logger = new Logger(LoggerLevelEnum.INFO, loggerName);
        this.HOME = this.startStation;
    }

    public Station getHomeStation() {
        return this.HOME;
    }

    public float getMaxWeightPerRollingStockInKG() {
        return maxWeightPerRollingStockInKG;
    }

    public int getMaxAmountOfRollingStocks() {
        return maxAmountOfRollingStocks;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }

    public String getName() {
        return name;
    }

    public LocomotiveHead getHead() {
        return this.head;
    }

    public Logger getLogger() {
        return logger;
    }

    public void  setHead(LocomotiveHead newHead) {
        this.head = newHead;
    }

    public ArrayList<BasicRollingStock> getBody() {
        return this.body;
    }

    public LocomotiveRollingStocksManager getLocomotiveRSManager() {
        return locomotiveRollingStocksManager;
    }

    public void info() {
        int trainSymbolUnicode = 0x1F682;
        int railroadCarUnicode = 0x1F683;
        String trainSymbol = new String(Character.toChars(trainSymbolUnicode));
        String railroadCarSymbol = new String(Character.toChars(railroadCarUnicode));
        String dump = String.format(
            "%s-(%s)x%d %s length - %f",
            trainSymbol,
            railroadCarSymbol,
            this.body.size(),
            this.getName(),
            this.getLocomotiveRSManager().getLength());
        this.logger.log(dump);
    }
}
