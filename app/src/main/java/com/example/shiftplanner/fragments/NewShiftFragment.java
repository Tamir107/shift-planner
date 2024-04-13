package com.example.shiftplanner.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiftplanner.GoogleCalendarService;
import com.example.shiftplanner.R;
import com.example.shiftplanner.models.Shift;
import com.example.shiftplanner.models.ShiftCount;
import com.example.shiftplanner.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private TextView date;
    private int hoursID, year, month, day;
    private int yearV2, monthV2, dayV2;
    private Button addShiftButton,chooseDateButton;
    private RadioButton radioMorning, radioEvening, radioNight;
    private String hoursDescription, formatedDate;
    private SimpleDateFormat simpleDateFormat;
    private DatabaseReference myRef;
    private Boolean isNUll;


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
        View view = inflater.inflate(R.layout.fragment_new_shift, container, false);

        date = view.findViewById(R.id.textViewDate);
        radioMorning = view.findViewById(R.id.radioMorning);
        radioEvening = view.findViewById(R.id.radioEvening);
        radioNight = view.findViewById(R.id.radioNight);
        addShiftButton = view.findViewById(R.id.buttonAddShift);
        chooseDateButton = view.findViewById(R.id.buttonChooseDate);
        hoursID = -1; // Default for empty hours selection
        final Calendar calendar = Calendar.getInstance();


        String UID = getArguments().getString("UID");
        String firstName = getArguments().getString("firstName");
        String lastName = getArguments().getString("lastName");
        String employeeID = getArguments().getString("employeeID");



        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        yearV2 = year;
                        monthV2 = monthOfYear;
                        dayV2 = dayOfMonth;

                        String dateFormatDisplay = "dd/MM/yyyy"; // Change this if you want a different date format
                        String dateFormatDatabase = "ddMMyyyy";
                        SimpleDateFormat displayFormat = new SimpleDateFormat(dateFormatDisplay, Locale.getDefault());
                        SimpleDateFormat databaseFormat = new SimpleDateFormat(dateFormatDatabase, Locale.getDefault());

                        date.setText(displayFormat.format(calendar.getTime()));
                        formatedDate = databaseFormat.format(calendar.getTime());
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        addShiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String summary;
                    if (radioMorning.isChecked()) {
                        hoursID = 0;
                        hoursDescription = "Morning shift: 7:00 AM - 3:00 PM";
                        summary = firstName + " " + lastName + " - Morning";
                        Toast.makeText(getActivity(), "Morning", Toast.LENGTH_SHORT).show();
                    } else if (radioEvening.isChecked()) {
                        hoursID = 1;
                        hoursDescription = "Evening shift: 3:00 PM - 11:00 PM";
                        summary = firstName + " " + lastName + " - Evening";
                        Toast.makeText(getActivity(), "Evening", Toast.LENGTH_SHORT).show();
                    } else if (radioNight.isChecked()) {
                        hoursID = 2;
                        hoursDescription = "Night shift: 11:00 PM - 7:00 AM";
                        summary = firstName + " " + lastName + " - Night";
                        Toast.makeText(getActivity(), "Night", Toast.LENGTH_SHORT).show();
                    } else {
                        // Empty selection
                        throw new Exception("You must choose shift hours");
                    }

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("shifts").child(UID).child(formatedDate);
                    myRef.setValue(new Shift(firstName, lastName, employeeID, date.getText().toString(), hoursDescription, hoursID));
                    Toast.makeText(getActivity(), "Shift added successfully.", Toast.LENGTH_SHORT).show();

                    Calendar startCalendar = Calendar.getInstance();
                    Calendar endCalendar = Calendar.getInstance();

                    Log.w("myApp",String.valueOf(dayV2));
                    switch (hoursID){
                        case 0:
                            startCalendar.set(yearV2,monthV2,dayV2,7,0,0);
                            endCalendar.set(yearV2,monthV2,dayV2,15,0,0);
                            break;
                        case 1:
                            startCalendar.set(yearV2,monthV2,dayV2,15,0,0);
                            endCalendar.set(yearV2,monthV2,dayV2,23,0,0);
                            break;
                        case 2:
                            startCalendar.set(yearV2,monthV2,dayV2,23,0,0);
                            endCalendar.set(yearV2,monthV2,dayV2+1,7,0,0);
                            break;
                    }
                    Log.w("Test",employeeID + date.getText().toString().replaceAll("[/]","") + hoursID);
                    //0001120720241

                    //0004100620240
                    //0004100620240
                    //0004100620240
                    GoogleCalendarService.addEventToCalendar(getContext(),employeeID + date.getText().toString().replaceAll("[/]","") + hoursID,summary,startCalendar.getTime(),endCalendar.getTime());

                    String shiftInfo = String.valueOf(monthV2 + 1);
                    if (hoursID == 2){
                        shiftInfo += "night";
                    }

                    DatabaseReference countRef = database.getReference("shiftsCount").
                            child(shiftInfo).child(UID).child("count");
                    countRef.runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Integer currentValue = mutableData.getValue(Integer.class);
                            if (currentValue == null) {
                                // If count does not exist, set to 1
                                mutableData.setValue(1);
                            } else {
                                // Increment count by 1
                                mutableData.setValue(currentValue + 1);
                            }
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean b,
                                               @Nullable DataSnapshot dataSnapshot) {
                            if (databaseError != null) {
                                // Handle possible error condition
                                Log.w("WOW", "Error updating count: " + databaseError.getMessage());
                            } else {
                                Log.w("WOW", "Count updated successfully.");
                            }
                        }
                    });













                } catch (Exception error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("myTag", error.getMessage());
                }

            }
        });

        return view;
    }
}