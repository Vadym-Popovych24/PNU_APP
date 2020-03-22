package com.social_network.pnu_app.entity;

public class AllAuthUsers {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLinkMainStudentPage() {
        return linkMainStudentPage;
    }

    public void setLinkMainStudentPage(String linkMainStudentPage) {
        this.linkMainStudentPage = linkMainStudentPage;
    }

    public String name;
    public String lastName;
    public String group;
    public String linkMainStudentPage;


    public AllAuthUsers(){}

    public AllAuthUsers(String studentName, String studentLastName, String studentGroup ,String studentImage){
        this.name = studentName;
        this.lastName = studentLastName;
        this.group = studentGroup;
        this.linkMainStudentPage = studentImage;

    }
}
