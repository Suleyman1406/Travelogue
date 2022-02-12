package com.okada.travelogue.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.okada.travelogue.Activity.HomeActivity;
import com.okada.travelogue.R;


public class ProfilePageFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    Button btn_sign_out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        btn_sign_out = view.findViewById(R.id.profile_sign_out);

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent toHome = new Intent(getActivity(), HomeActivity.class);
                toHome.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(toHome);
            }
        });

        if (firebaseAuth.getCurrentUser() == null)
            btn_sign_out.setVisibility(View.INVISIBLE);
        else
            btn_sign_out.setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        return view;
    }
}