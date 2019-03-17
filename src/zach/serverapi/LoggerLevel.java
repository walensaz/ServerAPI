package zach.serverapi;

public enum LoggerLevel {

    LOW(0, Logger.ANSI_GREEN),
    MEDIUM(1, Logger.ANSI_CYAN),
    HIGH(2, Logger.ANSI_BLUE),
    SEVERE(3, Logger.ANSI_RED),
    INFO(4, Logger.ANSI_YELLOW);

    private int level;
    private String color;

    LoggerLevel(int level, String color) {
        this.color = color;
        this.level = level;
    }

    public String getColor() {
        return color;
    }
}
