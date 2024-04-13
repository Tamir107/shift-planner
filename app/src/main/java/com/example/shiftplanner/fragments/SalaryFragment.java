package com.example.shiftplanner.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shiftplanner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SalaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalaryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String firstMonthPath, firstMonthNightPath,
            secondMonthPath, secondMonthNightPath, thirdMonthPath, thirdMonthNightPath;
    private Button showReportButton;
    private TextView textViewMonth1, textViewMonth2, textViewMonth3, textViewDayShifts1, textViewDayShifts2, textViewDayShifts3,
            textViewNightShifts1, textViewNightShifts2, textViewNightShifts3, textViewSalary1, textViewSalary2, textViewSalary3;
    private String UID;
    private RadioButton radioQ1, radioQ2, radioQ3, radioQ4;

    public SalaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SalaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SalaryFragment newInstance(String param1, String param2) {
        SalaryFragment fragment = new SalaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_salary, container, false);
//        Spinner spinnerQuarters = view.findViewById(R.id.spinnerQuarters);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.quarters, R.layout.my_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        spinnerQuarters.setAdapter(adapter);

        UID = getArguments().getString("UID");
        showReportButton = view.findViewById(R.id.buttonShowReport);
        textViewMonth1 = view.findViewById(R.id.textViewMonth1);
        textViewMonth2 = view.findViewById(R.id.textViewMonth2);
        textViewMonth3 = view.findViewById(R.id.textViewMonth3);
        textViewDayShifts1 = view.findViewById(R.id.textViewDayShifts1);
        textViewDayShifts2 = view.findViewById(R.id.textViewDayShifts2);
        textViewDayShifts3 = view.findViewById(R.id.textViewDayShifts3);
        textViewNightShifts1 = view.findViewById(R.id.textViewNightShifts1);
        textViewNightShifts2 = view.findViewById(R.id.textViewNightShifts2);
        textViewNightShifts3 = view.findViewById(R.id.textViewNightShifts3);
        textViewSalary1 = view.findViewById(R.id.textViewSalary1);
        textViewSalary2 = view.findViewById(R.id.textViewSalary2);
        textViewSalary3 = view.findViewById(R.id.textViewSalary3);
        radioQ1 = view.findViewById(R.id.radioQ1);
        radioQ2 = view.findViewById(R.id.radioQ2);
        radioQ3 = view.findViewById(R.id.radioQ3);
        radioQ4 = view.findViewById(R.id.radioQ4);

        showReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String selectedQuarter = spinnerQuarters.getSelectedItem().toString();

                if(radioQ1.isChecked()){
                    textViewMonth1.setText("January");
                    textViewMonth2.setText("February");
                    textViewMonth3.setText("March");
                    firstMonthPath = "1";
                    firstMonthNightPath = "1night";
                    secondMonthPath = "2";
                    secondMonthNightPath = "2night";
                    thirdMonthPath = "3";
                    thirdMonthNightPath = "3night";
                }
                if (radioQ2.isChecked()){
                    textViewMonth1.setText("April");
                    textViewMonth2.setText("May");
                    textViewMonth3.setText("June");
                    firstMonthPath = "4";
                    firstMonthNightPath = "4night";
                    secondMonthPath = "5";
                    secondMonthNightPath = "5night";
                    thirdMonthPath = "6";
                    thirdMonthNightPath = "6night";
                }
                if(radioQ3.isChecked()){
                    textViewMonth1.setText("July");
                    textViewMonth2.setText("August");
                    textViewMonth3.setText("September");
                    firstMonthPath = "7";
                    firstMonthNightPath = "7night";
                    secondMonthPath = "8";
                    secondMonthNightPath = "8night";
                    thirdMonthPath = "9";
                    thirdMonthNightPath = "9night";
                }
                if (radioQ4.isChecked()){
                    textViewMonth1.setText("October");
                    textViewMonth2.setText("November");
                    textViewMonth3.setText("December");
                    firstMonthPath = "10";
                    firstMonthNightPath = "10night";
                    secondMonthPath = "11";
                    secondMonthNightPath = "11night";
                    thirdMonthPath = "12";
                    thirdMonthNightPath = "12night";
                }


//                switch (selectedQuarter) {
//                    case "Q1":
//                        textViewMonth1.setText("January");
//                        textViewMonth2.setText("February");
//                        textViewMonth3.setText("March");
//                        firstMonthPath = "1";
//                        firstMonthNightPath = "1night";
//                        secondMonthPath = "2";
//                        secondMonthNightPath = "2night";
//                        thirdMonthPath = "3";
//                        thirdMonthNightPath = "3night";
//                        break;
//                    case "Q2":
//                        textViewMonth1.setText("April");
//                        textViewMonth2.setText("May");
//                        textViewMonth3.setText("June");
//                        firstMonthPath = "4";
//                        firstMonthNightPath = "4night";
//                        secondMonthPath = "5";
//                        secondMonthNightPath = "5night";
//                        thirdMonthPath = "6";
//                        thirdMonthNightPath = "6night";
//                        break;
//                    case "Q3":
//                        textViewMonth1.setText("July");
//                        textViewMonth2.setText("August");
//                        textViewMonth3.setText("September");
//                        firstMonthPath = "7";
//                        firstMonthNightPath = "7night";
//                        secondMonthPath = "8";
//                        secondMonthNightPath = "8night";
//                        thirdMonthPath = "9";
//                        thirdMonthNightPath = "9night";
//                        break;
//                    case "Q4":
//                        textViewMonth1.setText("October");
//                        textViewMonth2.setText("November");
//                        textViewMonth3.setText("December");
//                        firstMonthPath = "10";
//                        firstMonthNightPath = "10night";
//                        secondMonthPath = "11";
//                        secondMonthNightPath = "11night";
//                        thirdMonthPath = "12";
//                        thirdMonthNightPath = "12night";
//                        break;
//                }

                // Initialize Firebase Database reference
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("shiftsCount");

                // Create a map to hold the paths and their corresponding DataSnapshot results
                Map<String, DataSnapshot> dataSnapshotMap = new HashMap<>();

                // Fetch data from multiple paths in a single call
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshotMap.put(firstMonthPath, dataSnapshot.child(firstMonthPath).child(UID).child("count"));
                        dataSnapshotMap.put(firstMonthNightPath, dataSnapshot.child(firstMonthNightPath).child(UID).child("count"));
                        dataSnapshotMap.put(secondMonthPath, dataSnapshot.child(secondMonthPath).child(UID).child("count"));
                        dataSnapshotMap.put(secondMonthNightPath, dataSnapshot.child(secondMonthNightPath).child(UID).child("count"));
                        dataSnapshotMap.put(thirdMonthPath, dataSnapshot.child(thirdMonthPath).child(UID).child("count"));
                        dataSnapshotMap.put(thirdMonthNightPath, dataSnapshot.child(thirdMonthNightPath).child(UID).child("count"));
                        // Process or use the retrieved data as needed
                        // Example: Extract user data
                        DataSnapshot firstMonthSnapshot = dataSnapshotMap.get(firstMonthPath);
                        DataSnapshot firstMonthNightSnapshot = dataSnapshotMap.get(firstMonthNightPath);
                        DataSnapshot secondMonthSnapshot = dataSnapshotMap.get(secondMonthPath);
                        DataSnapshot secondMonthNightSnapshot = dataSnapshotMap.get(secondMonthNightPath);
                        DataSnapshot thirdMonthSnapshot = dataSnapshotMap.get(thirdMonthPath);
                        DataSnapshot thirdMonthNightSnapshot = dataSnapshotMap.get(thirdMonthNightPath);

                        int firstMonth, firstMonthNight, secondMonth, secondMonthNight, thirdMonth, thirdMonthNight;

                        if (firstMonthSnapshot.exists()) {
                            firstMonth = firstMonthSnapshot.getValue(Integer.class);
                            textViewDayShifts1.setText(String.valueOf(firstMonth));
                        } else {
                            firstMonth = 0;
                            textViewDayShifts1.setText("0");
                        }

                        if (firstMonthNightSnapshot.exists()) {
                            firstMonthNight = firstMonthNightSnapshot.getValue(Integer.class);
                            textViewNightShifts1.setText(String.valueOf(firstMonthNight));
                        } else {
                            firstMonthNight = 0;
                            textViewNightShifts1.setText("0");
                        }

                        if (secondMonthSnapshot.exists()) {
                            secondMonth = secondMonthSnapshot.getValue(Integer.class);
                            textViewDayShifts2.setText(String.valueOf(secondMonth));
                        } else {
                            secondMonth = 0;
                            textViewDayShifts2.setText("0");
                        }

                        if (secondMonthNightSnapshot.exists()) {
                            secondMonthNight = secondMonthNightSnapshot.getValue(Integer.class);
                            textViewNightShifts2.setText(String.valueOf(secondMonthNight));
                        } else {
                            secondMonthNight = 0;
                            textViewNightShifts2.setText("0");
                        }

                        if (thirdMonthSnapshot.exists()) {
                            thirdMonth = thirdMonthSnapshot.getValue(Integer.class);
                            textViewDayShifts3.setText(String.valueOf(thirdMonth));
                        } else {
                            thirdMonth = 0;
                            textViewDayShifts3.setText("0");
                        }

                        if (thirdMonthNightSnapshot.exists()) {
                            thirdMonthNight = thirdMonthNightSnapshot.getValue(Integer.class);
                            textViewNightShifts3.setText(String.valueOf(thirdMonthNight));
                        } else {
                            thirdMonthNight = 0;
                            textViewNightShifts3.setText("0");
                        }

                        textViewSalary1.setText(String.valueOf((firstMonth * 40) + (firstMonthNight * 60) + "₪"));
                        textViewSalary2.setText(String.valueOf((secondMonth * 40) + (secondMonthNight * 60) + "₪"));
                        textViewSalary3.setText(String.valueOf((thirdMonth * 40) + (thirdMonthNight * 60) + "₪"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle potential errors
                        Log.w("Firebase", "Data fetch cancelled", databaseError.toException());
                    }
                });


            }
        });

        return view;
    }
}