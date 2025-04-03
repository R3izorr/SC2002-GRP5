package utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList; 
import java.util.List;

public class FileUtils {
    @SuppressWarnings("CallToPrintStackTrace")
    public static List<String[]> readCSV(String filePath) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                // Skip header if present
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                // Handle quoted fields properly
                List<String> tokens = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                boolean inQuotes = false;
                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        inQuotes = !inQuotes; // Toggle the inQuotes flag
                    } else if (c == ',' && !inQuotes) {
                        tokens.add(sb.toString());
                        sb.setLength(0); // Clear the StringBuilder
                    } else {
                        sb.append(c);
                    }
                }
                tokens.add(sb.toString()); // Add the last token
                records.add(tokens.toArray(new String[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static void writeCSV(String filePath, List<String[]> records) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            // Optionally, write a header here if needed.
            for (String[] tokens : records) {
                pw.println(String.join(",", tokens));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
