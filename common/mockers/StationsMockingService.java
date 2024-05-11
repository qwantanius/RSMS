package RSMS.common.mockers;

import RSMS.Routing.Station.StationCreationDto;

import java.util.ArrayList;

public class StationsMockingService {

    private static int counter = 0;

    public static ArrayList<StationCreationDto> genStationsDtos(int amount) {
        ArrayList<StationCreationDto> stations = new ArrayList<>();
        for(int i=0; i<amount; i++) {
            stations.add(StationsMockingService.getDto());
        }
        return stations;
    }

    public static StationCreationDto getDto() {
        StationCreationDto stationCreationDto = new StationCreationDto();
        stationCreationDto.stationName = "MOCK_STATION_" + StationsMockingService.counter + "_" + Math.round(Math.random()*100);
        stationCreationDto.LOCATION_X = Math.random()*1000;
        stationCreationDto.LOCATION_Y = Math.random()*1000;
        StationsMockingService.counter++;
        return stationCreationDto;
    }
}
