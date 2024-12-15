import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItineraryProcessor {
    private final Map<String, String> airportCodes = new HashMap<>();
    
    // Process the itinerary file
    public void process(String inputFile, String outputFile, String lookupFile) throws IOException {
        TerminalFormatter.printStatus("Starting itinerary processing...");
        // Attempt to load airport data first
        if (!loadAirportData(lookupFile)) {
            System.out.println("Airport data loading failed. Output file will not be created.");
            return;
        }
        TerminalFormatter.printSuccess("Airport data loaded successfully.");

        // Use a StringBuilder to store output content in memory
        StringBuilder outputBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = processLine(line);
                outputBuilder.append(processedLine).append("\n");
            }
        } catch (FileNotFoundException e) {
            ErrorHandler.handleInputNotFound();
            return;  // Ensure the output file is not created if the input file is not found
        } catch (IOException e) {
            TerminalFormatter.printError("An unexpected error occurred while processing the file: " + e.getMessage());
            throw e;
        }

        // Trim white space and limit blank lines before writing to file
        String finalOutput = trimWhiteSpace(outputBuilder.toString());

        TerminalFormatter.printStatus("Writing data to output file...");

        // Only now write to the output file if there were no errors
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(finalOutput);
            TerminalFormatter.printSuccess("Output file created successfully at " + outputFile);
        } catch (IOException e) {
            TerminalFormatter.printError("Error writing to file: " + outputFile + ". Output file will not be created.");
            throw e;
        }
        TerminalFormatter.printStatus("Itinerary processing completed.");
    }

    private boolean loadAirportData(String lookupFile) {
        TerminalFormatter.printStatus("Loading airport data from " + lookupFile + "...");
        try (BufferedReader reader = new BufferedReader(new FileReader(lookupFile))) {
            String line;
    
            // Read the first line to capture the headers
            if ((line = reader.readLine()) == null) {
                ErrorHandler.handleMalformedOrLostAirportData();
                return false;
            }
    
            // Map column names to their indices
            String[] headers = line.split(",");
            Map<String, Integer> columnIndices = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                columnIndices.put(headers[i].trim(), i);
            }

            // Validate required columns
            if (!columnIndices.containsKey("name") ||
                (!columnIndices.containsKey("iata_code") && !columnIndices.containsKey("icao_code"))) {
                System.out.println("Lookup file is missing required columns: 'name', 'iata_code' or 'icao_code'.");
                return false;
            }
    
            // Column index mapping with default values for optional columns
            int nameIndex = columnIndices.getOrDefault("name", -1);
            int municipalityIndex = columnIndices.getOrDefault("municipality", -1);
            int iataCodeIndex = columnIndices.getOrDefault("iata_code", -1);
            int icaoCodeIndex = columnIndices.getOrDefault("icao_code", -1);
    
            // Read subsequent lines
            while ((line = reader.readLine()) != null) {
                String[] columns = parseCSVLine(line);
                
                if (columns.length < headers.length) {
                    ErrorHandler.handleMalformedAirportData();
                    return false;  // Stop loading if data is malformed
                }
    
                String airportName = columns[nameIndex].trim();
                String cityName = municipalityIndex != -1 ? columns[municipalityIndex].trim() : "";
                String iataCode = iataCodeIndex != -1 ? columns[iataCodeIndex].trim() : "";
                String icaoCode = icaoCodeIndex != -1 ? columns[icaoCodeIndex].trim() : "";
    
                // Store airport names
                if (!iataCode.isEmpty()) airportCodes.put("#" + iataCode, airportName);
                if (!icaoCode.isEmpty()) airportCodes.put("##" + icaoCode, airportName);
                
                // Store city names with a different prefix
                if (!cityName.isEmpty()) {
                    if (!iataCode.isEmpty()) airportCodes.put("*#" + iataCode, cityName);
                    if (!icaoCode.isEmpty()) airportCodes.put("*##" + icaoCode, cityName);
                }
            }
            return true; // Successfully loaded data
        } catch (FileNotFoundException e) {
            ErrorHandler.handleAirportLookupNotFound();
            return false; // Fail to load data if lookup file is not found
        } catch (IOException e) {
            ErrorHandler.handleMalformedAirportData();
            return false;
        }
    }
    

// Helper method to parse a CSV line considering quoted fields
private String[] parseCSVLine(String line) {
    List<String> result = new ArrayList<>();
    StringBuilder currentField = new StringBuilder();
    boolean inQuotes = false;

    for (char ch : line.toCharArray()) {
        if (ch == '"') {
            inQuotes = !inQuotes; // Toggle the inQuotes flag
        } else if (ch == ',' && !inQuotes) {
            result.add(currentField.toString().trim());
            currentField.setLength(0); // Clear the current field
        } else {
            currentField.append(ch);
        }
    }
    // Add the last field
    result.add(currentField.toString().trim());
    return result.toArray(new String[0]);
}

    private String processLine(String line) {
        line = replaceAirportCodes(line);
        line = replaceDateAndTime(line);
        return line;
    }

    private String replaceAirportCodes(String line) {
        // Create a temporary line to process city and airport codes separately
        String tempLine = line;
    
        // First, handle city names (prefixed with *)
        for (Map.Entry<String, String> entry : airportCodes.entrySet()) {
            // Check if the line contains the city code (with *)
            if (tempLine.contains(entry.getKey()) && entry.getKey().startsWith("*")) {
                tempLine = tempLine.replace(entry.getKey(), entry.getValue()); // Replace with city name
            }
        }
    
        // Then, handle airport names
        for (Map.Entry<String, String> entry : airportCodes.entrySet()) {
            // Now check for regular airport codes
            if (tempLine.contains(entry.getKey()) && !entry.getKey().startsWith("*")) {
                tempLine = tempLine.replace(entry.getKey(), entry.getValue()); // Replace with airport name
            }
        }
    
        return tempLine;
    }
    
    

    private String replaceDateAndTime(String line) {
        Pattern datePattern = Pattern.compile("D\\(([^)]+)\\)");
        Pattern time12Pattern = Pattern.compile("T12\\(([^)]+)\\)");
        Pattern time24Pattern = Pattern.compile("T24\\(([^)]+)\\)");

        Matcher dateMatcher = datePattern.matcher(line);
        Matcher time12Matcher = time12Pattern.matcher(line);
        Matcher time24Matcher = time24Pattern.matcher(line);

        while (dateMatcher.find()) {
            String isoDate = dateMatcher.group(1);
            String formattedDate = DateTimeFormatterHelper.formatDate(isoDate);
            line = line.replace("D(" + isoDate + ")", formattedDate);
        }

        while (time12Matcher.find()) {
            String isoDate = time12Matcher.group(1);
            String formattedTime12 = DateTimeFormatterHelper.format12HourTime(isoDate);
            line = line.replace("T12(" + isoDate + ")", formattedTime12);
        }

        while (time24Matcher.find()) {
            String isoDate = time24Matcher.group(1);
            String formattedTime24 = DateTimeFormatterHelper.format24HourTime(isoDate);
            line = line.replace("T24(" + isoDate + ")", formattedTime24);
        }
        return line;
    }

    private String trimWhiteSpace(String content) {
        // Convert line-break characters (\f, \r, \v) to new-line characters (\n)
        content = content.replaceAll("[\f\r\u000B]+", "\n");
        
        // Limit consecutive blank lines to a maximum of one
        content = content.replaceAll("(?m)^\\s*$\\n{2,}", "\n");
        
        return content.trim();
    }
}
