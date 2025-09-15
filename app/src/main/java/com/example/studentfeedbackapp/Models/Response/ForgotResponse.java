package com.example.studentfeedbackapp.Models.Response;

public class ForgotResponse {


        private String status;
        private String message;
        private Data data;  // nested object

        public String getStatus() { return status; }
        public String getMessage() { return message; }

        public String getResetToken() {
            return data != null ? data.resetToken : null;
        }

        public class Data {
            private String resetToken;
        }
    }

