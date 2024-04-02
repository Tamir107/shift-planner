package com.example.shiftplanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiftplanner.R;
import com.example.shiftplanner.models.Shift;
import com.example.shiftplanner.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewShiftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewShiftFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText date;
    private int hoursID, year, month, day;
    private Button addShiftButton;
    private RadioButton radioMorning,radioEvening,radioNight;
    private String hoursDescription;

    public NewShiftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewShiftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewShiftFragment newInstance(String param1, String param2) {
        NewShiftFragment fragment = new NewShiftFragment();
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
        View view =  inflater.inflate(R.layout.fragment_new_shift, container, false);

        date = view.findViewById(R.id.editTextDate);
        radioMorning = view.findViewById(R.id.radioMorning);
        radioEvening = view.findViewById(R.id.radioEvening);
        radioNight = view.findViewById(R.id.radioNight);
        addShiftButton = view.findViewById(R.id.buttonAddShift);
        hoursID = -1; // Default for empty hours selection

        String UID = getArguments().getString("UID");
        String firstName = getArguments().getString("firstName");
        String lastName = getArguments().getString("lastName");
        String employeeID = getArguments().getString("employeeID");

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        addShiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(radioMorning.isChecked()){
                        hoursID = 0;
                        hoursDescription = "Morning shift: 7:00 AM - 3:00 PM";
                        Toast.makeText(getActivity(), "Morning", Toast.LENGTH_SHORT).show();
                    }
                    else if(radioEvening.isChecked()){
                        hoursID = 1;
                        hoursDescription = "Evening shift: 3:00 PM - 11:00 PM";
                        Toast.makeText(getActivity(), "Evening", Toast.LENGTH_SHORT).show();
                    }
                    else if(radioNight.isChecked()){
                        hoursID = 2;
                        hoursDescription = "Night shift: 11:00 PM - 7:00 AM";
                        Toast.makeText(getActivity(), "Night", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Empty selection
                        throw new Exception("You must choose shift hours");
                    }

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("shifts").child(UID).child(date.getText().toString());
                    myRef.setValue(new Shift(firstName,lastName, employeeID, date.getText().toString(), hoursDescription, hoursID));
                    Toast.makeText(getActivity(), "Shift added successfully.", Toast.LENGTH_SHORT).show();

                }catch (Exception error){
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("myTag", error.getMessage());
                }

            }
        });

        return view;
    }
}