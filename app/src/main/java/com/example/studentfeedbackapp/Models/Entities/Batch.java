package com.example.studentfeedbackapp.Models.Entities;

public class Batch {
    private int batch_id;
    private String batchname;
    private int course_id;

    public Batch(int batch_id, String batchname, int course_id) {
        this.batch_id = batch_id;
        this.batchname = batchname;
        this.course_id = course_id;
    }

    public int getBatch_id() {
        return batch_id;
    }

    public String getBatchname() {
        return batchname;
    }

    public int getCourse_id() {
        return course_id;
    }

    @Override
    public String toString() {
        return batchname; // Spinner me batch name show karne ke liye
    }
}
