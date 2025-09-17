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
        private int student_id;   // ✅ Add this field
        private int course_id;
        private int batch_id;
        private String coursename;

        public String getStudentname() { return studentname; }
        public String getToken() { return token; }
        public String getEmail() { return email; }
        public int getStudentId() { return student_id; }  // ✅ Getter
        public int getCourseId() { return course_id; }
        public int getBatchId() { return batch_id; }
        public String getCoursename() { return coursename; }
    }
}
