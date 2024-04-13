package com.example.shiftplanner.fragments;

import static com.example.shiftplanner.fragments.SignUpFragment.isValidEmail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button signInButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        email = (EditText) view.findViewById(R.id.editTextEmailSignIn);
        password = (EditText) view.findViewById(R.id.editTextPasswordSignIn);
        signInButton = (Button) view.findViewById(R.id.buttonSignIn);

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String emailStr = email.getText().toString();
                    if(!isValidEmail(emailStr)){
                        throw new Exception("Invalid email, please enter a valid one");
                    }
                    String passwordStr = password.getText().toString();
                    //WORKING
//                    mAuth.signInWithEmailAndPassword(emailStr, passwordStr)
//                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        // Sign in success, update UI with the signed-in user's information
//                                        FirebaseUser user = mAuth.getCurrentUser();
//                                        Toast.makeText(getActivity(), "Login ok.", Toast.LENGTH_SHORT).show();
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("UID", user.getUid());
//                                        Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_frontPageFragment,bundle);
//                                    } else {
//                                        // If sign in fails, display a message to the user.
//                                        Toast.makeText(getActivity(), "Login failed.", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });

                    mAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(getActivity(), "Login ok.", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("users").child(user.getUid());
                                        myRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                User loggedInUser = dataSnapshot.getValue(User.class);
                                                bundle.putString("UID", user.getUid());
                                                bundle.putString("firstName", loggedInUser.getFirstName());
                                                bundle.putString("lastName", loggedInUser.getLastName());
                                                bundle.putString("employeeID", loggedInUser.getEmployeeID());
                                                Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_frontPageFragment,bundle);
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                // Failed to read value
                                                Toast.makeText(getActivity(), "Problem occurred while reading data", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getActivity(), "Login failed.", Toast.LENGTH_SHORT).show();
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
}




