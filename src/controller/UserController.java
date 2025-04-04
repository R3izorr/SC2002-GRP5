package controller;

import model.user.Applicant;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.User;
import repository.UserRepository;


public class UserController {
    private UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User login(String nric, String password) {
        for (Applicant a : userRepository.getApplicants()) {
            if(a.getNric().equals(nric) && a.getPassword().equals(password))
                return a;
        }
        for (HDBOfficer o : userRepository.getOfficers()) {
            if(o.getNric().equals(nric) && o.getPassword().equals(password))
                return o;
        }
        for (HDBManager m : userRepository.getManagers()) {
            if(m.getNric().equals(nric) && m.getPassword().equals(password))
                return m;
        }
        return null;
    }
    
    public boolean changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        // After password change, update the CSV file based on the user type.
        if(user instanceof HDBManager) {
            userRepository.saveManagers();
        } else if(user instanceof HDBOfficer) {
            userRepository.saveOfficers();
        } else if(user instanceof Applicant) {
            userRepository.saveApplicants();
        }
        return true;
    }
}
