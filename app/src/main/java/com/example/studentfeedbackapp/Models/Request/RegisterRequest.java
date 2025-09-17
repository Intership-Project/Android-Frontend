package com.example.studentfeedbackapp.Models.Request;

import com.google.gson.annotations.SerializedName;





    public class RegisterRequest {
        @SerializedName("studentname") // backend jo expect karta hai, wahi name
        private String studentname;

        @SerializedName("email")
        private String email;

        @SerializedName("password")
        private String password;

        @SerializedName("course_id")
        private int courseId;

        @SerializedName("batch_id")
        private int batchId;

        public RegisterRequest(String studentname, String email, String password, int courseId, int batchId) {
            this.studentname = studentname;
            this.email = email;
            this.password = password;
            this.courseId = courseId;
            
            this.batchId = batchId;
        }

        public String getStudentname() {
            return studentname;
        }

        public void setStudentname(String studentname) {
            this.studentname = studentname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public int getBatchId() {
            return batchId;
        }

        public void setBatchId(int batchId) {
            this.batchId = batchId;
        }
    }




