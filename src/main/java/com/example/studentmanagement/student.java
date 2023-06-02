package com.example.studentmanagement;

public class student {
    private int CNE;
    private String firstName;
    private String lastName;
    private String email;
    private String birthDay;

    public student(){
        this.CNE = 0;
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.birthDay = "";
    }
    public student(int CNE, String firstName, String lastName, String email, String birthDay) {
        this.CNE = CNE;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDay = birthDay;
    }

    public int getCNE() {
        return CNE;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setCNE(int CNE) {
        this.CNE = CNE;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
}
