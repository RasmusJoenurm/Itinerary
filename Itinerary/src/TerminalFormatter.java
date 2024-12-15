public class TerminalFormatter {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    
    // Print a success message in green and bold
    public static void printSuccess(String message) {
        System.out.println(ANSI_GREEN + ANSI_BOLD + message + ANSI_RESET);
    }

    // Print a warning message in yellow and bold
    public static void printWarning(String message) {
        System.out.println(ANSI_YELLOW + ANSI_BOLD + message + ANSI_RESET);
    }

    // Print an error message in red and bold
    public static void printError(String message) {
        System.out.println(ANSI_RED + ANSI_BOLD + message + ANSI_RESET);
    }

    // Print an underlined status message
    public static void printStatus(String message) {
        System.out.println(ANSI_UNDERLINE + message + ANSI_RESET);
    }
}
