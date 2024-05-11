/*

   .~~~ ____    .-----------.
  Y_,___|[]|   | RSMS????    |  ---   ----
 {|_|_|_|__|_,_|_____________|   ----   -----
  oo---OO=OO     OO       OO    ------  -------
  --------------------------------------------------------
*/

package RSMS;

import RSMS.Routing.Station.Station;
import RSMS.Routing.Station.StationCreationDto;
import RSMS.common.logger.Logger;
import RSMS.common.logger.LoggerLevelEnum;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Logger logger = new Logger(LoggerLevelEnum.INFO, "RSMS_APP");
        Logger errLogger = new Logger(LoggerLevelEnum.ERROR, "RSMS_ERR");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            try {
                logger.log("Enter command :");
                String command = scanner.nextLine();
                switch (command) {
                    case "/help": Main.help(logger);
                    // create station
                    StationCreationDto stationCreationDto = new StationCreationDto();
                    stationCreationDto.stationName = ""; // input here
                    stationCreationDto.LOCATION_X = 0; // input here
                    stationCreationDto.LOCATION_Y = 0; // input here
                    Station station = new Station(stationCreationDto);
                    // create route
                    // create locomotive
                    // - reserve car numbers
                    // - create railroad car
                    // - create transportation load
                    default: Main.help(logger);
                }
            } catch (Exception err) {
                errLogger.log(err.toString());
            }
        }
    }

    private static void help(Logger logger) {
        logger.log("""

            ┌─┬──┬─┬─┬──┐┌┐┌┬─┬┐┌─┐
            │┼│──┤││││──┤│└┘│┬┤││┼│
            │┐┼──││││├──││┌┐│┴┤└┤┌┘
            └┴┴──┴┴─┴┴──┘└┘└┴─┴─┴┘
                        
        /help           : list all commands
        /create-station : create station
        /create-route   : create route
        /railroad-car   : create railroad car
        /locomotive     : create locomotive
        /attach-car     : attach car to locomotive
        /load-car       : load railroad car
        /unload-car     : unload railroad car
        /create-load    : create transportation load
        /get-car        : get details of railroad car
        /get-locomotive : get details of locomotive
        /get-route      : get route details
        """);
    }

    private static void createStation() { System.out.println("creating station"); }
    private static void getStation() { System.out.println("get station");}
    private static void createRoute() { System.out.println("creating station"); }
    private static void getRoute() { System.out.println("creating station"); }
    private static void createCar() { System.out.println("creating car"); }
    private static void createLocomotive() { System.out.println("create locomotive"); };
    private static void attachCar() {System.out.println("attach car"); }
    private static void loadCar() {System.out.println("load car"); }
    private static void unloadCar() {System.out.println("unload car");}
    private static void getLocomotive() {System.out.println("get locomotive");}
}
