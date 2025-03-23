package model;

import utils.MaritalStatus;
import utils.Role;

public class HDBOfficer extends Applicant {
    public HDBOfficer(String nric, String password, int age, MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus);
        this.role = Role.HDB_OFFICER;
    }
}
