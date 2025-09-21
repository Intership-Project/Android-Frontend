package com.example.studentfeedbackapp.Models.Response;

public class LoginResponse {
    private String status;
    private Data data;

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public static class Data { // ✅ static inner class
        private String token;
        private String studentname;
        private String email;
        private int student_id;
        private int course_id;
        private int batch_id;
        private String coursename;

        // Getters
        public String getToken() {
            return token;
        }

        public String getStudentname() {
            return studentname;
        }

        public String getEmail() {
            return email;
        }

        public int getStudentId() {
            return student_id;
        }

        public int getCourseId() { // ✅ fixed getter name
            return course_id;
        }

        public int getBatchId() { // ✅ fixed getter name
            return batch_id;
        }

        public String getCoursename() {
            return coursename;
        }
    }
}
