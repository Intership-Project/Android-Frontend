package com.example.studentfeedbackapp.Models.Request;

import com.google.gson.annotations.SerializedName;

public class Batch {

        @SerializedName("batch_id")
        private int batch_id;
        @SerializedName("batchname")
        private String batchname;
        @SerializedName("course_id")
        private int course_id;

        public Batch() {}
        public int getBatch_id() { return batch_id; }
        public String getBatchname() { return batchname; }
        public int getCourse_id() { return course_id; }
        public void setBatch_id(int id) { this.batch_id = id; }
        public void setBatchname(String n) { this.batchname = n; }
        public void setCourse_id(int id) { this.course_id = id; }
    }


