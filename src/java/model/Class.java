/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class Class {

    private String classID;
    private String ClassName;
    private String checkToday;

    public Class() {
    }

    public Class(String classID, String ClassName, String checkToday) {
        this.classID = classID;
        this.ClassName = ClassName;
        this.checkToday = checkToday;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public String getCheckToday() {
        return checkToday;
    }

    public void setCheckToday(String checkToday) {
        this.checkToday = checkToday;
    }

    
}
