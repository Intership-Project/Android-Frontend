package com.example.studentfeedbackapp.Models.Request;

import com.google.gson.annotations.SerializedName;

public class Course {

        @SerializedName("course_id")
        private int course_id;
        @SerializedName("coursename")
        private String coursename;

        public Course() {}
        public int getCourse_id() { return course_id; }
        public String getCoursename() { return coursename; }
        public void setCourse_id(int id) { this.course_id = id; }
        public void setCoursename(String name) { this.coursename = name; }
    }


