package com.example.studentfeedbackapp.Models.Response;

import com.example.studentfeedbackapp.Models.Request.Batch;

import java.util.List;

public class BatchResponse { private String status;
    private List<Batch> data;

    public String getStatus() {
        return status;
    }

    public List<Batch> getData() {
        return data;
    }

}
