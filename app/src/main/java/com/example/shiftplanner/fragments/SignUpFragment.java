package com.example.shiftplanner.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shiftplanner.R;
import com.example.shiftplanner.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private EditText firstName, lastName, email, employeeID, password;
    private Button signUpButton;


    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        firstName = (EditText) view.findViewById(R.id.editTextFirstNameSignUp);
        lastName = (EditText) view.findViewById(R.id.editTextLastNameSignUp);
        email = (EditText) view.findViewById(R.id.editTextEmailSignUp);
        password = (EditText) view.findViewById(R.id.editTextPasswordSignUp);
        employeeID = (EditText) view.findViewById(R.id.editTextEmployeeIDSignUp);
        signUpButton = (Button) view.findViewById(R.id.buttonSignUp);

        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String emailStr = email.getText().toString();
                    if (!isValidEmail(emailStr)) {
                        throw new Exception("Invalid email, please enter a valid one");
                    }
                    String firstNameStr = firstName.getText().toString();
                    String lastNameStr = lastName.getText().toString();
                    String employeeIDStr = employeeID.getText().toString();
                    String passwordStr = password.getText().toString();

                    mAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, display a message to the user.
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        // Write a message to the database
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("users").child(user.getUid());
                                        myRef.setValue(new User(firstNameStr, lastNameStr, employeeIDStr, emailStr, passwordStr));
                                        Toast.makeText(getActivity(), "You have successfully registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign up fails, display a message to the user.
                                        Toast.makeText(getActivity(), "Register failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (Exception error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}