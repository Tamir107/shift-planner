package com.example.shiftplanner.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiftplanner.R;
import com.example.shiftplanner.ShiftAdapter;
import com.example.shiftplanner.models.Shift;
import com.example.shiftplanner.models.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyShiftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyShiftsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private ShiftAdapter shiftAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference myRef;


    public MyShiftsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyShiftsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyShiftsFragment newInstance(String param1, String param2) {
        MyShiftsFragment fragment = new MyShiftsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_my_shifts, container, false);

        String UID = getArguments().getString("UID");

        recyclerView = view.findViewById(R.id.recyclerViewShifts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.w("myApp", "OK");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shifts").child(UID);

        FirebaseRecyclerOptions<Shift> options =
                new FirebaseRecyclerOptions.Builder<Shift>()
                        .setQuery(databaseReference, Shift.class)
                        .build();

        Log.w("myApp", "OK2");

        shiftAdapter = new ShiftAdapter(options);
        recyclerView.setAdapter(shiftAdapter);


        Log.w("myApp", "OK3");

        shiftAdapter.setOnItemClickListener(shift -> {
            // Create and show the dialog fragment
            ShiftDialogFragment dialogFragment = new ShiftDialogFragment(shift,UID);
            dialogFragment.show(getChildFragmentManager(), "shift_dialog");
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        shiftAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        shiftAdapter.stopListening();
    }

}