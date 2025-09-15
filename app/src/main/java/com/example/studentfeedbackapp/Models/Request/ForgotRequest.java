package com.example.studentfeedbackapp.Models.Request;

public class ForgotRequest {
    private String email;
    private String resetToken;      // for resetpassword request
    private String newPassword;     // for resetpassword request

    // Constructor for forgotpassword
    public ForgotRequest(String email) {
        this.email = email;
    }

    // Constructor for resetpassword
    public ForgotRequest(String resetToken, String newPassword) {
        this.resetToken = resetToken;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
