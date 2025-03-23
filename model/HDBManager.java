package model;

import utils.MaritalStatus;
import utils.Role;

public class HDBManager extends User {
    public HDBManager(String nric, String password, int age, MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus, Role.HDB_MANAGER);
    }
}
