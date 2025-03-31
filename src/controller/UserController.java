package controller;

import model.Applicant; 
import model.HDBManager; 
import model.HDBOfficer; 
import model.User; 
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
        // Persist the change if needed.
        return true;
    }
}
