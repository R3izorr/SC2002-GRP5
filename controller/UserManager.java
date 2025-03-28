package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import model.*;
import utils.*;

public class UserManager {
    private List<User> users;

    public UserManager() {
        users = new ArrayList<>(); 
        loadUsers("data\\ApplicantList.csv");
        loadUsers("data\\ManagerList.csv");
        loadUsers("data\\OfficerList.csv");
    }

    private void loadUsers(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length != 5) continue;

                String name = parts[0].trim();
                String nric = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                MaritalStatus status = MaritalStatus.valueOf(parts[3].trim().toUpperCase());
                String password = parts[4].trim();
                Role role = switch (filePath) {
                    case "data\\ApplicantList.csv" -> Role.APPLICANT;
                    case "data\\ManagerList.csv" -> Role.HDB_MANAGER;
                    case "data\\OfficerList.csv" -> Role.HDB_OFFICER;
                    default -> throw new IllegalArgumentException("Unknown file path: " + filePath);
                };

                if (!InputValidator.isValidNRIC(nric)) {
                    System.out.println("⚠ Invalid NRIC format: " + nric);
                    continue;
                }

                switch (role) {
                    case APPLICANT -> users.add(new Applicant(name, nric, password, age, status));
                    case HDB_OFFICER -> users.add(new HDBOfficer(name, nric, password, age, status));
                    case HDB_MANAGER -> users.add(new HDBManager(name,nric, password, age, status));
                }
            }

        } catch (Exception e) {
            System.out.println(" Failed to load users: " + e.getMessage());
        }
    }

    public User authenticate(String nric, String password) {
        for (User u : users) {
            if (u.getNric().equalsIgnoreCase(nric) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public boolean changePassword(User user, String newPassword) {
        if (user != null && newPassword != null && !newPassword.isBlank()) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
}
