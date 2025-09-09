package com.example.studentfeedbackapp.Models.Request;

public class RegisterRequest {
    private String studentname;
    private String email;
    private String password;
    private int course_id;
    private int batch_id;

    // Correct constructor
    public RegisterRequest(String studentname, String email, String password, int course_id, int batch_id) {
        this.studentname = studentname;
        this.email = email;
        this.password = password;
        this.course_id = course_id;
        this.batch_id = batch_id;
    }

    // Optional: Getters
    public String getStudentname() { return studentname; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public int getCourse_id() { return course_id; }
    public int getBatch_id() { return batch_id; }
}
