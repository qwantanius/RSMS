package RSMS.common.logger;

import RSMS.common.types.MessageFormats;

public enum LoggerLevelEnum {
    INFO {
        @Override
        public String getFormattedMessage(String loggerName, String message) {
            String fullPath = MessageFormats.getFullLoggerPath(loggerName, MessageFormats.LOGGER_INFO_THREAD);
            return fullPath + MessageFormats.MESSAGE_SEPARATOR + message;
        }
    },

    WARNING {
        @Override
        public String getFormattedMessage(String loggerName, String message) {
            String fullPath = MessageFormats.getFullLoggerPath(loggerName, MessageFormats.LOGGER_WARNING_THREAD);
            return fullPath + MessageFormats.MESSAGE_SEPARATOR + message;
        }
    },

    ERROR {
        @Override
        public String getFormattedMessage(String loggerName, String message) {
            String fullPath = MessageFormats.getFullLoggerPath(loggerName, MessageFormats.LOGGER_ERROR_THREAD);
            return fullPath + MessageFormats.MESSAGE_SEPARATOR + message;
        }
    };

    abstract String getFormattedMessage(String loggerName, String msg);
}