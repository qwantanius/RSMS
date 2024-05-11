package RSMS.common.types;

public class MessageFormats {

    // LOGING TEMPLATES
    public static final String DEFAULT_ROW_SEPARATOR = "===========";
    public static final String DEFAULT_LOGGER_NAME = "logger";
    public static final String LOGGER_INFO_THREAD = "info";
    public static final String LOGGER_WARNING_THREAD = "warn";
    public static final String LOGGER_ERROR_THREAD = "error";
    public static final String DEFAULT_THREAD = "default_thread";
    public static final String DOMAIN_SEPARATOR = "@";
    public static final String MESSAGE_SEPARATOR = " > ";

    // ROLLING STOCK TEMPLATES
    public static final String ROLLING_STOCK_NO_STOCKS_ATTACHED = "[NO ATTACHED ROLLING STOCKS]";
    public static final String ROLLING_STOCK_TOSTRING_TEMPLATE = "[ROLLING STOCK][UUID: %s][ATTACHED WITH: %s]";
    public static final String TRANSPORTATION_LOADING_TO_STRING_TEMPLATE = "[load: %s][%d units * %f kg]";
    public static final String ROLLING_STOCK_INFO_DUMP_TEMPLATE =
            """
            ~~~~~~~~~~~~~~~~~~~~INFO DUMP
            [rolling stock name ] %s
            [uuid               ] %s
            [attached to uuid   ] %s
            [current load       ] %s
            [max units          ] %d
            [loaded units       ] %d
            [available units    ] %d
            [max weight per unit] %f
            [currently loaded on] %f
            """;


    public static String getFullLoggerPath(String thread) {
        return MessageFormats.getFullLoggerPath(MessageFormats.DEFAULT_LOGGER_NAME, thread);
    }
    public static String getFullLoggerPath(String loggerName, String thread) {
        return "[ "
                + loggerName
                + MessageFormats.DOMAIN_SEPARATOR
                + thread +
                " ]";
    }

    public static String ROUTE_TO_STRING_TMP = "Route %s, %s stations, (starts at %s stops at %s)= %d KM";

    public static String getWaterMark() {
        return """
        ╔═══╦═══╦═╗╔═╦═══╗  Railway            
        ║╔═╗║╔═╗║║╚╝║║╔═╗║  Station
        ║╚═╝║╚══╣╔╗╔╗║╚══╗  Management              
        ║╔╗╔╩══╗║║║║║╠══╗║  System              
        ║║║╚╣╚═╝║║║║║║╚═╝║  alpha 0.0.1              
        ╚╝╚═╩═══╩╝╚╝╚╩═══╝                
        ╔══╗         ╔═══╦═══╦═══╦═══╦╗ ╔╗ 
        ║╔╗║         ║╔═╗║╔═╗║╔═╗║╔═╗║║ ║║ 
        ║╚╝╚╦╗ ╔╗ ╔══╬╝╔╝║╚═╝╠╝╔╝╠╝╔╝║╚═╝║ 
        ║╔═╗║║ ║║ ║══╬═╝╔╣╔═╗╠═╝╔╬═╝╔╩══╗║ 
        ║╚═╝║╚═╝║ ╠══║║╚═╣╚═╝║║╚═╣║╚═╗  ║║ 
        ╚═══╩═╗╔╝ ╚══╩═══╩═══╩═══╩═══╝  ╚╝ 
            ╔═╝║                          
            ╚══╝    
        (C) s28224 for GUI course at PJATK provided by Mr. Sławomir Aleksander Dańczak                    
        """;
    }

    public static String getReportTemplate() {
        return "\n%s\n%s\n\nWARN: please dont forget to reload/reopen file as you may not view latest changes\n    " +
                ": please consider waiting for 5 seconds if you see some strange output, there are a " +
                "chance that tic of update made a report from pereferial app state" +
                "\n\n\t\t\t\tLOCOMOTIVES_DUMP\n%s\n\n\t\t\t\tROLLING_STOCKS_DUMP\n%s\n\n\t\t\tSTATIONS_DUMP\n%s";
    }
}
