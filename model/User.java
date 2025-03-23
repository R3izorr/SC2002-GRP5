package model;

import utils.Role;
import utils.MaritalStatus;

public abstract class User {
    protected String nric;
    protected String password;
    protected int age;
    protected MaritalStatus maritalStatus;
    protected Role role;

    public User(String nric, String password, int age, MaritalStatus maritalStatus, Role role) {
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.role = role;
    }

    public String getNric() {
        return nric;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public int getAge() {
        return age;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
