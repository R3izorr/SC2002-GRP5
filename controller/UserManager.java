package controller;

import model.*;
import utils.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;

    public UserManager() {
        users = new ArrayList<>();
        loadUsers("../data/users.csv");
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

                String nric = parts[0].trim();
                String password = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                MaritalStatus status = MaritalStatus.valueOf(parts[3].trim().toUpperCase());
                Role role = Role.valueOf(parts[4].trim().toUpperCase());

                if (!InputValidator.isValidNRIC(nric)) {
                    System.out.println("⚠ Invalid NRIC format: " + nric);
                    continue;
                }

                switch (role) {
                    case APPLICANT -> users.add(new Applicant(nric, password, age, status));
                    case HDB_OFFICER -> users.add(new HDBOfficer(nric, password, age, status));
                    case HDB_MANAGER -> users.add(new HDBManager(nric, password, age, status));
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
