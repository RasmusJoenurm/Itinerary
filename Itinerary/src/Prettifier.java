

import java.io.IOException;

public class Prettifier {
    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("-h")) {
            ErrorHandler.printUsage();
            return;
        }
        if (args.length != 3) {
            ErrorHandler.handleInvalidArguments();
            return;
        }
        // assigns the given files to variables
        String inputFilePath = args[0];
        String outputFilePath = args[1];
        String airportLookupFilePath = args[2];

        ItineraryProcessor processor = new ItineraryProcessor();

        try {
            processor.process(inputFilePath, outputFilePath, airportLookupFilePath);
            TerminalFormatter.printSuccess("Itinerary processed successfully.");
        } catch (IOException e) {
            TerminalFormatter.printError("Error processing itinerary: " + e.getMessage());
        }
    }
}