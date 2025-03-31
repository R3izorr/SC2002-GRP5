package utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; 
import java.util.List;

public class FileUtils {
    @SuppressWarnings("CallToPrintStackTrace")
    public static List<String[]> 
    readCSV(String filePath) { 
        List<String[]> records = new ArrayList<>(); 
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { 
            String line; boolean isFirstLine = true; 
            while ((line = br.readLine()) != null) { 
                // Skip header if present
                if(isFirstLine) { 
                    isFirstLine = false; 
                    continue;
                } 
                String[] tokens = line.split(","); 
                records.add(tokens);
            } 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } return records; 
    }
}
