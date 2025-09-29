package com.example.studentfeedbackapp.Presentation.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studentfeedbackapp.R;

public class ProfileFragment extends Fragment {

    private static final String ARG_STUDENT_ID = "studentId";
    private static final String ARG_STUDENT_NAME = "studentName";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_COURSE_NAME = "courseName";
    private static final String ARG_BATCH_NAME = "batchName";

    public static ProfileFragment newInstance(int studentId, String studentName, String email, String courseName, String batchName) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STUDENT_ID, studentId);
        args.putString(ARG_STUDENT_NAME, studentName);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_COURSE_NAME, courseName);
        args.putString(ARG_BATCH_NAME, batchName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvName = view.findViewById(R.id.tvProfileName);
        TextView tvEmail = view.findViewById(R.id.tvProfileEmail);
        TextView tvCourse = view.findViewById(R.id.tvProfileCourse);
        TextView tvBatch = view.findViewById(R.id.tvProfileBatch);

        if (getArguments() != null) {
            tvName.setText(getArguments().getString(ARG_STUDENT_NAME));
            tvEmail.setText(getArguments().getString(ARG_EMAIL));
            tvCourse.setText(getArguments().getString(ARG_COURSE_NAME));
            tvBatch.setText(getArguments().getString(ARG_BATCH_NAME));
        }

        return view;
    }

}
