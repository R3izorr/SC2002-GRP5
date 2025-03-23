package model;

import utils.MaritalStatus;
import utils.Role;

public class Applicant extends User {
    public Applicant(String nric, String password, int age, MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus, Role.APPLICANT);
    }
}
