package RSMS.Routing.Station;

import java.util.ArrayList;

public class Station {
    public static int METERS_IN_KM = 1_000;

    private String stationName;
    private double LOCATION_X;
    private double LOCATION_Y;

    public Station(StationCreationDto stationCreationDto) {
        this.stationName = stationCreationDto.stationName;
        this.LOCATION_X = stationCreationDto.LOCATION_X;
        this.LOCATION_Y = stationCreationDto.LOCATION_Y;
    }

    public String getStationName() {
        return this.stationName;
    }

    public double getLOCATION_X() {
        return LOCATION_X;
    }

    public double getLOCATION_Y() {
        return LOCATION_Y;
    }

    public double getDistanceInKmFrom(Station station) {
        double deltaX = this.LOCATION_X - station.LOCATION_X;
        double deltaY = this.LOCATION_Y - station.LOCATION_Y;
        return Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2)) * Station.METERS_IN_KM;
    }

    @Override
    public String toString() {
        return String.format("Station(%s)(x:%5.5f, y:%5.5f)", this.stationName, this.LOCATION_X, this.LOCATION_Y);
    }

    public static ArrayList<StationCreationDto> getStationDtosFromStations(ArrayList<Station> stations) {
        ArrayList<StationCreationDto> stationCreationDtos = new ArrayList<>();
        stations.forEach((station -> stationCreationDtos.add(Station.convertStationToDto(station))));
        return stationCreationDtos;
    }

    public static StationCreationDto convertStationToDto(Station station) {
        StationCreationDto stationCreationDto = new StationCreationDto();
        stationCreationDto.stationName = station.getStationName();
        stationCreationDto.LOCATION_X = station.getLOCATION_X();
        stationCreationDto.LOCATION_Y = station.getLOCATION_Y();
        return stationCreationDto;
    }
}
