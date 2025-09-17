package com.example.studentfeedbackapp.Models.Response;

import com.example.studentfeedbackapp.Models.Request.Course;

import java.util.List;

public class CourseResponse {

        private String status;
        private List<Course> data;

        public String getStatus() {
            return status;
        }

        public List<Course> getData() {
            return data;
        }
    }


