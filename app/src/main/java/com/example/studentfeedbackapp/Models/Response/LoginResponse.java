package com.example.studentfeedbackapp.Models.Response;
public class LoginResponse {
    private String status;
    private Data data;

    public String getStatus() { return status; }
    public Data getData() { return data; }

    public class Data {
        private String token;
        private String studentname;
        private String email;
        private int course_id;
        private int batch_id;
        private String coursename;

        public String getStudentname() { return studentname; }
        public String getToken() { return token; }
    }
}
