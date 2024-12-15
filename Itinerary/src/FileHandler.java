

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileHandler {
    public List<String> readFile(String inputFilePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
        List<String> lines = new ArrayList<>();  // Scoped to the try block
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;  // Returns the populated list if successful
    } catch (FileNotFoundException e) {
        ErrorHandler.handleInputNotFound();  
        return Collections.emptyList();  // Returns an empty list if the file isn't found
    } catch (IOException e) {
        ErrorHandler.handleFileProcessingError();
        return Collections.emptyList();  // Returns empty list for other IOExceptions
    }
}
    public void writeFile(String filePath, List<String> lines) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String line : lines) { // For loop that iterates over the list of lines
                writer.write(line + "\n");  // Write the lines seperating them by newlines
            }
        } catch (IOException e) { // Throws IOException with reason
            throw new IOException("Error writing to file: " + filePath, e);
        }
    }
 }
