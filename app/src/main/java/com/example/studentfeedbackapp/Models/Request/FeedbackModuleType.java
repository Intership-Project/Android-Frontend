package com.example.studentfeedbackapp.Models.Request;

import com.google.gson.annotations.SerializedName;

public class FeedbackModuleType {
    @SerializedName("feedbackmoduletype_id")
    private int feedbackModuleTypeId;

    @SerializedName("fbmoduletypename")
    private String fbModuleTypeName;

    @SerializedName("feedbacktype_id")
    private int feedbackTypeId;

    @SerializedName("fbtypename")
    private String fbTypeName;

    // Getters
    public int getFeedbackModuleTypeId() { return feedbackModuleTypeId; }
    public String getFbModuleTypeName() { return fbModuleTypeName; }
    public int getFeedbackTypeId() { return feedbackTypeId; }
    public String getFbTypeName() { return fbTypeName; }
}
