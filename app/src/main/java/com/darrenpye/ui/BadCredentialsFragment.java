package com.darrenpye.ui;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.darrenpye.R;


public class BadCredentialsFragment extends Fragment {

    private BadCredentialsDoneListener mListener;

    public BadCredentialsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (BadCredentialsDoneListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BadCredentialsDoneListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_bad_credentials, container, false);

        // Get the login button and assign an OnClickListener
        Button tryAgainButton = (Button)myView.findViewById(R.id.try_again);

        tryAgainButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mListener.done();
                    }
                }
        );

        return myView;
    }


    public interface BadCredentialsDoneListener {
        public void done() ;
    }
}
