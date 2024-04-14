package com.example.shiftplanner.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shiftplanner.GoogleCalendarService;
import com.example.shiftplanner.R;
import com.example.shiftplanner.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.lifecycle.Observer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FrontPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrontPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView welcomeUser;
    private Button buttonToAddShift, buttonToMyShifts, buttonToMySalary, logOutButton;

    public FrontPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrontPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FrontPageFragment newInstance(String param1, String param2) {
        FrontPageFragment fragment = new FrontPageFragment();
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
        View view = inflater.inflate(R.layout.fragment_front_page, container, false);

        welcomeUser = view.findViewById(R.id.textViewWelcomeUser);
        buttonToAddShift = view.findViewById(R.id.buttonToAddNewShift);
        buttonToMyShifts = view.findViewById(R.id.buttonToMyShifts);
        logOutButton = view.findViewById(R.id.buttonLogOut);
        buttonToMySalary = view.findViewById(R.id.buttonToSalary);
        Bundle bundle = new Bundle();
        String UID = getArguments().getString("UID");
        String firstName = getArguments().getString("firstName");
        String lastName = getArguments().getString("lastName");
        String employeeID = getArguments().getString("employeeID");
        welcomeUser.setText("Hello, " + firstName);

        bundle.putString("UID", UID);
        bundle.putString("firstName", firstName);
        bundle.putString("lastName", lastName);
        bundle.putString("employeeID", employeeID);

        buttonToAddShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_frontPageFragment_to_newShiftFragment, bundle);
            }
        });

        buttonToMyShifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_frontPageFragment_to_myShiftsFragment, bundle);
            }
        });

        buttonToMySalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_frontPageFragment_to_salaryFragment, bundle);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view).navigate(R.id.action_frontPageFragment_to_welcomeFragment2);
            }
        });

        return view;
    }
}
