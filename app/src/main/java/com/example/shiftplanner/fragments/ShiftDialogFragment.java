package com.example.shiftplanner.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.shiftplanner.GoogleCalendarService;
import com.example.shiftplanner.R;
import com.example.shiftplanner.models.Shift;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShiftDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShiftDialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Shift shift;
    private String UID;
    private int newHoursID, newDay, newMonth, newYear;
    private String newHoursDescription;
    private String summary;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShiftDialogFragment() {
        // Required empty public constructor
    }

    public ShiftDialogFragment(Shift shift, String UID){
        this.shift = shift;
        this.UID = UID;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShiftDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShiftDialogFragment newInstance(String param1, String param2) {
        ShiftDialogFragment fragment = new ShiftDialogFragment();
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_shift_dialog, null);

        // Initialize views in the dialog layout
        TextView textViewShiftDetails = view.findViewById(R.id.textViewShiftDetails);

        // Populate shift details in the dialog
        String shiftDetails = "Shift Date: " + shift.getDate() + "\n"
                + "Employee: " + shift.getFirstName() + " " + shift.getLastName();
        textViewShiftDetails.setText(shiftDetails);

        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnDelete = view.findViewById(R.id.btnDelete);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click
                // Implement your edit logic here
                showEditDialog();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click
                // Implement your delete logic here
                deleteShiftFromDatabase(shift);
                GoogleCalendarService.removeEventFromCalendar(getContext(), shift.getEmployeeID() + shift.getDate().replaceAll("[/]","") + shift.getHoursID());
            }
        });

//        // Set up buttons
//        builder.setView(view)
//                .setPositiveButton("Edit", (dialogInterface, i) -> {
//                    // Handle edit button click
//                    // Implement your edit logic here
//                })
//                .setNegativeButton("Delete", (dialogInterface, i) -> {
//                    // Handle delete button click
//                    // Implement your delete logic here
//                    deleteShiftFromDatabase(shift);
//                    Log.w("Test",shift.getEmployeeID() + shift.getDate().replaceAll("[/]","") + shift.getHoursID());
//                    GoogleCalendarService.removeEventFromCalendar(getContext(),shift.getEmployeeID() + shift.getDate().replaceAll("[/]","") + shift.getHoursID());
//                });

        builder.setView(view);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shift_dialog, container, false);
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View editView = inflater.inflate(R.layout.dialog_edit_shift, null);
        builder.setView(editView);

        // Initialize views in the edit dialog layout
        DatePicker datePicker = editView.findViewById(R.id.datePicker);
        RadioGroup radioGroupOptions = editView.findViewById(R.id.radioGroupOptions);
        RadioButton radioMorning = editView.findViewById(R.id.radioMorning);
        RadioButton radioEvening = editView.findViewById(R.id.radioEvening);
        RadioButton radioNight = editView.findViewById(R.id.radioNight);
        Button btnSubmit = editView.findViewById(R.id.btnSubmit);

        // Pre-fill date picker with existing shift date
        // Set radio button selection based on existing shift details

        AlertDialog editDialog = builder.create();

        btnSubmit.setOnClickListener(v -> {
            // Handle submit button click

            if (radioMorning.isChecked()){
                newHoursID = 0;
                newHoursDescription = "Morning shift: 7:00 AM - 3:00 PM";
                summary = shift.getFirstName() + " " + shift.getLastName() + " - Morning";
            }
            if (radioEvening.isChecked()){
                newHoursID = 1;
                newHoursDescription = "Evening shift: 3:00 PM - 11:00 PM";
                summary = shift.getFirstName() + " " + shift.getLastName() + " - Evening";
            }
            if (radioNight.isChecked()){
                newHoursID = 2;
                newHoursDescription = "Night shift: 11:00 PM - 7:00 AM";
                summary = shift.getFirstName() + " " + shift.getLastName() + " - Night";
            }



            String updatedDate = getDateFromDatePicker(datePicker);
//            getSelectedOptionText(radioGroupOptions.getCheckedRadioButtonId());
            String formatedDate = updatedDate.replaceAll("[/]","");

            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();

            switch (newHoursID){
                case 0:
                    startCalendar.set(newYear,newMonth,newDay,7,0,0);
                    endCalendar.set(newYear,newMonth,newDay,15,0,0);
                    break;
                case 1:
                    startCalendar.set(newYear,newMonth,newDay,15,0,0);
                    endCalendar.set(newYear,newMonth,newDay,23,0,0);
                    break;
                case 2:
                    startCalendar.set(newYear,newMonth,newDay,23,0,0);
                    endCalendar.set(newYear,newMonth,newDay+1,7,0,0);
                    break;
            }


            DatabaseReference myRef;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference("shifts").child(UID).child(formatedDate);

            //Add the new shift
            myRef.setValue(new Shift(shift.getFirstName(), shift.getLastName(), shift.getEmployeeID(), updatedDate, newHoursDescription, newHoursID));

            //Delete from calendar here
//            GoogleCalendarService.removeEventFromCalendar(getContext(), shift.getEmployeeID() + shift.getDate().replaceAll("[/]","") + shift.getHoursID());
            //Create the new event calendar here
//            GoogleCalendarService.addEventToCalendar(getContext(),shift.getEmployeeID() + formatedDate + newHoursID,summary,startCalendar.getTime(),endCalendar.getTime());

            GoogleCalendarService.updateEventFromCalendar(getContext(),shift.getEmployeeID() + shift.getDate().replaceAll("[/]","") + shift.getHoursID(),summary,shift.getEmployeeID() + formatedDate + newHoursID,startCalendar.getTime(),endCalendar.getTime());
            //Delete the old one
            deleteShiftFromDatabase(shift);

            editDialog.dismiss();
        });

        editDialog.show();
    }

    private String getDateFromDatePicker(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

        newDay = datePicker.getDayOfMonth();
        newMonth = datePicker.getMonth(); // Month is 0-based
        newYear = datePicker.getYear();

        String dateFormatDisplay = "dd/MM/yyyy"; // Change this if you want a different date format
        SimpleDateFormat displayFormat = new SimpleDateFormat(dateFormatDisplay, Locale.getDefault());
        return displayFormat.format(calendar.getTime());
    }


//    private void getSelectedOptionText(int selectedOptionId) {
//        switch (selectedOptionId) {
//            case R.id.radioMorning:
//                newHoursID = 0;
//                newHoursDescription = "Morning shift: 7:00 AM - 3:00 PM";
//                break;
//            case R.id.radioEvening:
//                newHoursID = 1;
//                newHoursDescription = "Evening shift: 3:00 PM - 11:00 PM";
//                break;
//            case R.id.radioNight:
//                newHoursID = 2;
//                newHoursDescription = "Night shift: 11:00 PM - 7:00 AM";
//                break;
//        }
//    }

    private void deleteShiftFromDatabase(Shift shift) {
        // Get database reference
        String shiftID = shift.getDate().replaceAll("[/]","");
        Log.w("MyApp","The updated shiftID is: " + shiftID);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("shifts")
                .child(UID) // Assuming you have a userId field in Shift model
                .child(shiftID); // Assuming each shift has a unique ID

        // Remove shift from Firebase Database
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Shift deleted successfully
                    dismiss(); // Close the dialog after successful deletion
                    Log.w("MyApp","Shift deleted successfully");
                } else {
                    // Failed to delete shift
                    // Handle error or show error message
                    Log.w("MyApp","Failed to delete shift");
                }
            }
        });
    }

}