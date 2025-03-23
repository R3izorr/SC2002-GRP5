package controller;

import model.Applicant;
import model.HDBOfficer;
import model.HDBManager;
import model.User;
import utils.MaritalStatus;
import utils.Role;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;

    public UserManager() {
        users = new ArrayList<>();
        loadMockUsers(); // simulate loading users from file
    }

    private void loadMockUsers() {
        users.add(new Applicant("S1234567A", "password", 36, MaritalStatus.SINGLE));
        users.add(new HDBOfficer("S2345678B", "password", 40, MaritalStatus.MARRIED));
        users.add(new HDBManager("S3456789C", "password", 45, MaritalStatus.MARRIED));
    }

    public User authenticate(String nric, String password) {
        for (User user : users) {
            if (user.getNric().equalsIgnoreCase(nric) && user.getPassword().equals(password)) {
                return user;
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
