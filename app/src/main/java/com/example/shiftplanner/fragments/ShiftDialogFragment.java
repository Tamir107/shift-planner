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
import android.widget.TextView;

import com.example.shiftplanner.GoogleCalendarService;
import com.example.shiftplanner.R;
import com.example.shiftplanner.models.Shift;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        // Set up buttons
        builder.setView(view)
                .setPositiveButton("Edit", (dialogInterface, i) -> {
                    // Handle edit button click
                    // Implement your edit logic here
                })
                .setNegativeButton("Delete", (dialogInterface, i) -> {
                    // Handle delete button click
                    // Implement your delete logic here
                    deleteShiftFromDatabase(shift);
                    Log.w("Test",shift.getEmployeeID() + shift.getDate().replaceAll("[/]","") + shift.getHoursID());
                    GoogleCalendarService.removeEventFromCalendar(getContext(),shift.getEmployeeID() + shift.getDate().replaceAll("[/]","") + shift.getHoursID());
                });

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shift_dialog, container, false);
    }

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