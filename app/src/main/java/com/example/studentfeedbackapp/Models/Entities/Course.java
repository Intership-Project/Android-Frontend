package com.example.studentfeedbackapp.Models.Entities;

public class Course {
    private int course_id;
    private String coursename;

    public Course(int course_id, String coursename) {
        this.course_id = course_id;
        this.coursename = coursename;
    }

    public int getCourse_id() {
        return course_id;
    }

    public String getCoursename() {
        return coursename;
    }

    @Override
    public String toString() {
        return coursename; // Spinner me course name show karne ke liye
    }
}
