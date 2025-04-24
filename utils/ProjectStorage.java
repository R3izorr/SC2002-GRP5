package utils;

import model.Project;
import utils.FlatType;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





public class ProjectStorage {


public static List<Project> loadProjects(String filepath) {
    List<Project> projects = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
        String line;

        // Skip header
        reader.readLine();

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",", -1);
            if (parts.length < 7) continue;

            String name = parts[0].trim();
            String location = parts[1].trim();
            LocalDate openDate = LocalDate.parse(parts[2].trim());
            LocalDate closeDate = LocalDate.parse(parts[3].trim());
            int officerSlots = Integer.parseInt(parts[4].trim());
            int twoRoom = Integer.parseInt(parts[5].trim());
            int threeRoom = Integer.parseInt(parts[6].trim());

            Map<FlatType, Integer> units = new HashMap<>();
            units.put(FlatType.TWO_ROOM, twoRoom);
            units.put(FlatType.THREE_ROOM, threeRoom);

            Project p = new Project(name, location, units, openDate, closeDate);
            p.setAvailableOfficerSlots(officerSlots);

            projects.add(p);
        }

        System.out.println("Loaded " + projects.size() + " projects from CSV.");

    } catch (IOException e) {
        System.out.println(" Failed to load projects: " + e.getMessage());
    }

    return projects;
}




    public static void saveProjects(List<Project> projects, String filepath) {
        try {
            File file = new File(filepath);
            System.out.println("Saving to: " + file.getAbsolutePath());

            // Create parent folders if they don't exist
            file.getParentFile().mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write header
                writer.write("ProjectName,Location,OpenDate,CloseDate,OfficerSlots,TwoRoom,ThreeRoom");
                writer.newLine();

                // Write project data
                for (Project p : projects) {
                    Map<FlatType, Integer> units = p.getUnitCount();

                    String line = String.join(",",
                            p.getName(),
                            p.getLocation(),
                            p.getApplicationOpenDate().toString(),
                            p.getApplicationCloseDate().toString(),
                            String.valueOf(p.getAvailableOfficerSlots()),
                            String.valueOf(units.getOrDefault(FlatType.TWO_ROOM, 0)),
                            String.valueOf(units.getOrDefault(FlatType.THREE_ROOM, 0))
                    );

                    writer.write(line);
                    writer.newLine();
                    System.out.println("Saved project: " + p.getName());
                }
            }

            System.out.println("Successfully saved " + projects.size() + " projects to CSV.");
        } catch (IOException e) {
            System.out.println(" Failed to save projects: " + e.getMessage());
        }
    }
}
