package model;

import utils.MaritalStatus;
import utils.Role;

public abstract class User {
    protected String name;
    protected String nric;
    protected String password;
    protected int age;
    protected MaritalStatus maritalStatus;
    protected Role role;

    public User(String name, String nric, String password, int age, MaritalStatus maritalStatus, Role role) {
        this.name = name;
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.role = role;
    }
    
    public String getName() { return name; }
    public String getNric() { return nric; }
    public String getPassword() { return password; }
    public void setPassword(String newPassword) { this.password = newPassword; }
    public int getAge() { return age; }
    public MaritalStatus getMaritalStatus() { return maritalStatus; }
    public Role getRole() { return role; }
}
