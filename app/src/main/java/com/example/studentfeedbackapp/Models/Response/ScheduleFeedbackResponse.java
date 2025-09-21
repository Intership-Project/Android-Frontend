package com.example.studentfeedbackapp.Models.Response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ScheduleFeedbackResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<FeedbackData> data;

    public String getStatus() {
        return status;
    }

    public List<FeedbackData> getData() {
        return data;
    }

    public static class FeedbackData {
        @SerializedName("schedulefeedback_id")
        private int scheduleFeedbackId;

        @SerializedName("StartDate")
        private String startDate;

        @SerializedName("EndDate")
        private String endDate;

        @SerializedName("feedbacktype_id")
        private int feedbacktype_id;

        @SerializedName("feedbackmoduletype_id")
        private int feedbackmoduletype_id;

        @SerializedName("course_id")
        private int course_id;

        @SerializedName("coursename")
        private String coursename;

        @SerializedName("subject_id")
        private int subject_id;

        @SerializedName("subjectname")
        private String subjectname;

        @SerializedName("faculty_id")
        private int faculty_id;

        @SerializedName("facultyname")
        private String facultyname;

        @SerializedName("batch_id")
        private Integer batch_id;

        @SerializedName("batchname")
        private String batchname;

        @SerializedName("status")   // ✅ Add status field
        private String status;

        // Getters
        public int getScheduleFeedbackId() { return scheduleFeedbackId; }
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public int getFeedbacktype_id() { return feedbacktype_id; }
        public int getFeedbackmoduletype_id() { return feedbackmoduletype_id; }
        public int getCourse_id() { return course_id; }
        public String getCoursename() { return coursename; }
        public int getSubject_id() { return subject_id; }
        public String getSubjectname() { return subjectname; }
        public int getFaculty_id() { return faculty_id; }
        public String getFacultyname() { return facultyname; }
        public Integer getBatch_id() { return batch_id; }
        public String getBatchname() { return batchname; }
        public String getStatus() { return status; } // ✅ Getter for status
    }
}
