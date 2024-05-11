package RSMS.common.logger;

import RSMS.common.files.FileService;
import RSMS.common.types.MessageFormats;

import java.util.Date;

public class Logger {

    private static boolean enabledForAll = true;
    private boolean enabledForInstance = true;
    private LoggerLevelEnum loggerLevel;
    private String loggerName;
    private LoggerOutputEnum outputForInstance;
    private static LoggerOutputEnum outputForAll;
    private String OUTPUT_DIR;

    public Logger(LoggerLevelEnum loggerLevel, String loggerName) {
        this.loggerLevel = loggerLevel;
        this.loggerName = loggerName;
        Logger.outputForAll = LoggerOutputEnum.SYSTEM_OUT;
        this.OUTPUT_DIR = System.getProperty("user.dir") + "/storage/logs/";
    }

    public String getOutputDir() {
        return this.OUTPUT_DIR;
    }

    public void setOutputDir(String path) {
        this.OUTPUT_DIR = path;
    }

    public static void setOutputForAll(LoggerOutputEnum output) {
        Logger.outputForAll = output;
    }

    public static void enableAll() {
        Logger.enabledForAll = true;
    }

    public static void disableAll() {
        Logger.enabledForAll = false;
    }

    public void enable() {
        this.enabledForInstance = true;
    }

    public void disable() {
        this.enabledForInstance = false;
    }

    public static boolean isLoggingEnabledForAll() {
        return Logger.enabledForAll;
    }

    public boolean isLoggingEnabledForInstance() {
        return this.enabledForInstance;
    }

    public void log(String message) {
        if (Logger.isLoggingEnabledForAll() && this.enabledForInstance){
            String timestamp = String.format("[%s]", new Date());
            String formattedMessage = timestamp + loggerLevel.getFormattedMessage(loggerName, message);
            if (Logger.outputForAll.equals(LoggerOutputEnum.SYSTEM_OUT)) {
                System.out.println(formattedMessage);
            }
            if (Logger.outputForAll.equals(LoggerOutputEnum.FILE)) {
                FileService.writeTo(this.buildFileOutputPath(), formattedMessage);
            }
        }
    }

    public String buildFileOutputPath() {
        return String.format(this.getOutputDir() + "%s.rsms.log", this.loggerName);
    }
    
    public String getFormattedMessage(String message) {
        String defaultThread = MessageFormats.getFullLoggerPath(MessageFormats.DEFAULT_THREAD);
        return defaultThread + MessageFormats.MESSAGE_SEPARATOR + message;
    }
}
