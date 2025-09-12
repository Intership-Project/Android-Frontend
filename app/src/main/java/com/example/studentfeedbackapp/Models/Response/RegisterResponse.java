package com.example.studentfeedbackapp.Models.Response;


public class RegisterResponse {

        private boolean success;
        private String message;

    public RegisterResponse(String name, String email, String password, int courseId, int batchId) {
    }

    public boolean isSuccess(){ return success; }
        public String getMessage(){ return message; }
    }



