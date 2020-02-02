package model;

import lombok.Data;

import java.util.List;

@Data
public class Student {
    private String name;
    private String job;

    public Student(){}
    public static Student getStudent(){
        return new Student();
    }

}
