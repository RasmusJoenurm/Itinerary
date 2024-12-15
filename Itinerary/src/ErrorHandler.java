public class ErrorHandler {
    public static void printUsage() {
        TerminalFormatter.printStatus("Usage: java Prettifier <inputFile> <outputFile> <airportLookupFile>");
        TerminalFormatter.printStatus("Example: java Prettifier ./input.txt ./output.txt ./airports_lookup.csv");
        TerminalFormatter.printStatus("Use '-h' for help.");
    }

    public static void handleInvalidArguments() {
        TerminalFormatter.printError("Error: Invalid number of arguments provided.");
    }

    public static void handleInputNotFound() {
        TerminalFormatter.printError("Error: Input file not found.");
    }

    public static void handleAirportLookupNotFound() {
        TerminalFormatter.printError("Error: Airport lookup file not found.");
    }

    public static void handleMalformedAirportData() {
        TerminalFormatter.printError("Error: Airport lookup data is malformed.");
    }

    public static void handleMalformedOrLostAirportData() {
        TerminalFormatter.printError("Error: Airport lookup data is malformed or lost.");
    }

    public static void handleFileProcessingError() {
        TerminalFormatter.printError("Error: There was an issue processing the file.");
    }
}
