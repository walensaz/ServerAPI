package zach.serverapi;

public class Logger {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static int LOW = 0, MEDIUM = 1, HIGH = 2, SEVERE = 3;

    public static void log(String message, LoggerLevel level) {
        System.out.println(level.getColor() + "[" + level.name() + "] " + message + ANSI_RESET);
    }

    public static void log(String message) {
        System.out.println(LoggerLevel.INFO.getColor() + "[INFO] " + message + ANSI_RESET);
    }


}
