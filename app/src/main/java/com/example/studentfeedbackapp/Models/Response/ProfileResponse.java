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
            private int course_id;   // ✅ Add this
            private int batch_id;    // ✅ Add this
            public int getStudent_id() { return student_id; }
            public String getStudentname() { return studentname; }
            public String getEmail() { return email; }
            public int getCourse_id() { return course_id; }   // ✅ Getter
            public int getBatch_id() { return batch_id; }
        }
    }


