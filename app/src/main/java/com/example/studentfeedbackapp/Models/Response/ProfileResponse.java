package com.example.studentfeedbackapp.Models.Response;

public class ProfileResponse {

        private String status;
        private ProfileData data;

        public String getStatus() { return status; }
        public ProfileData getData() { return data; }

        public static class ProfileData {
            private int student_id;
            private String studentname;
            private String email;

            public int getStudent_id() { return student_id; }
            public String getStudentname() { return studentname; }
            public String getEmail() { return email; }
        }
    }


