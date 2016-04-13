package com.dehua.courseinformationsystem.bean;

import java.io.Serializable;

/**
 * Created by dehua on 16/4/5 005.
 */
public class CourseBean implements Serializable {
    private int courseID;
    private String courseName;
    private int signInStatus;
    private int signInCount;
    private String courseIntro;

    public String getCourseIntro() {
        return courseIntro;
    }

    public void setCourseIntro(String courseIntro) {
        this.courseIntro = courseIntro;
    }

    public int getSignInStatus() {
        return signInStatus;
    }
    public void setSignInStatus(int signInStatus) {
        this.signInStatus = signInStatus;
    }
    public int getCourseID() {
        return courseID;
    }
    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public int getSignInCount() {
        return signInCount;
    }
    public void setSignInCount(int signInCount) {
        this.signInCount = signInCount;
    }
}
