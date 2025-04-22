package controller;

import entity.user.Applicant;
import entity.user.HDBManager;
import entity.user.HDBOfficer;
import entity.user.User;
import java.util.List;
import repository.IRepository;


public class UserController {
    private IRepository<Applicant> applicantRepository;
    private IRepository<HDBOfficer> officerRepository;
    private IRepository<HDBManager> managerRepository;
    
    public UserController(IRepository<Applicant> applicantRepository, IRepository<HDBOfficer> officerRepository, IRepository<HDBManager> managerRepository) {
        this.applicantRepository = applicantRepository;
        this.officerRepository = officerRepository;
        this.managerRepository = managerRepository;
    }
    
    public User login(String nric, String password) {
        for (Applicant a : applicantRepository.getAll()) {
            if(a.getNric().equals(nric) && a.getPassword().equals(password))
                return a;
        }
        for (HDBOfficer o : officerRepository.getAll()) {
            if(o.getNric().equals(nric) && o.getPassword().equals(password))
                return o;
        }
        for (HDBManager m : managerRepository.getAll()) {
            if(m.getNric().equals(nric) && m.getPassword().equals(password))
                return m;
        }
        return null;
    }
    
    public boolean changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        // After password change, update the CSV file based on the user type.
        if(user instanceof HDBManager) {
            managerRepository.update();
        } else if(user instanceof HDBOfficer) {
            officerRepository.update();
        } else if(user instanceof Applicant) {
            applicantRepository.update();
        }
        return true;
    }

    public List<Applicant> getAllApplicants() {
        return applicantRepository.getAll();
    }
    
    public List<HDBOfficer> getAllOfficers() {
        return officerRepository.getAll();
    }
    
    public List<HDBManager> getAllManagers() {
        return managerRepository.getAll();
    }

    
}
