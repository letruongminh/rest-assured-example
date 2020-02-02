package model;

import lombok.Data;



@Data
public class Student {
    private String name;
    private String job;

    public Student(){}
    public static Student getStudent(){
        return new Student();
    }

}
