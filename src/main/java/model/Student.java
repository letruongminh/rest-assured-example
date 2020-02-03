package model;

import lombok.Data;



@Data
public class Student {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;

    public Student setEmail(String email) {
        this.email = email;
        return this;
    }

    public Student setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    public Student setId(int id) {
        this.id = id;
        return this;
    }

    public Student setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Student(){}
    public static Student getStudent(){
        return new Student();
    }

    @Override
    public String toString() {
        return "Student: " + this.firstName.concat(this.lastName);
    }
}
