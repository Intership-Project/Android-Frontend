package com.example.studentfeedbackapp.Models.Response;

import com.example.studentfeedbackapp.Models.Request.FeedbackModuleType;

import java.util.List;

public class FeedbackModuleResponse {

    private String status;
    private List<FeedbackModuleType> data;  // ✅ List instead of single object

    public String getStatus() {
        return status;
    }

    public List<FeedbackModuleType> getData() {  // ✅ Return List
        return data;
    }
    }


