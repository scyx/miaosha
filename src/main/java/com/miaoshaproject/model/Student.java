package com.miaoshaproject.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author cyx
 * @data 2019/4/1 15:01
 */
public class Student {
    private Long id;
    private String no;
    private String firstname;
    private String lastname;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;
    public Student(){

    }
    public Student(Long id, String no, String firstname,
                   String lastname, String gender, Date birthday) {
        this.id = id;
        this.no = no;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.birthday = birthday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
