package com.darrenpye.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.darrenpye.R;
import com.darrenpye.ui.utils.ViewUtils;

import java.util.ArrayList;


/**
 * This fragement handles the user login UI
 *
 */
public class LoginFragment extends Fragment {

    // Bundle vars for pre-populating the fields with values
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String REMEMBER_ME = "rememberMe";

    // The components
    private EditText mUsername;
    private EditText mPassword;
    private CheckBox mRememberMeCB;
    private Button mSigninButton;
    private Button mImNewButton;

    // Listener
    private LoginInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(String username, String password, boolean rememberMe) {
        LoginFragment fragment = new LoginFragment();

        Bundle bundle = new Bundle();
        bundle.putString(USERNAME, username);
        bundle.putString(PASSWORD, password);
        bundle.putBoolean(REMEMBER_ME, rememberMe);

        fragment.setArguments(bundle);

        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_login, container, false);

        // Get the login button and assign an OnClickListener
        mSigninButton = (Button)myView.findViewById(R.id.sign_in_button);
        mImNewButton = (Button)myView.findViewById(R.id.new_user);

        mSigninButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if (mUsername.getText() != null && mPassword.getText() != null) {
                            mListener.loginPressed(mUsername.getText().toString(), mPassword.getText().toString(), mRememberMeCB.isChecked());
                        }
                    }
                }
        );


        // Animate the buttons coming in so they are obvious to the user

        long button_scale_time = 750;
        long button_scale_increment_time = 250;

        ViewUtils.animateButtonShow(mSigninButton, button_scale_time + (button_scale_increment_time * 0));
        ViewUtils.animateButtonShow(mSigninButton, button_scale_time + (button_scale_increment_time * 1));

        mUsername = (EditText)myView.findViewById(R.id.username);
        mPassword = (EditText)myView.findViewById(R.id.password);
        mRememberMeCB = (CheckBox)myView.findViewById(R.id.rememberMe_cb);


        // IF the bundle was supplied with arguments, fill in the values
        Bundle arguments = getArguments();

        if (arguments != null) {
            String username = arguments.getString(USERNAME);
            String password = arguments.getString(PASSWORD);
            boolean rememberMe = arguments.getBoolean(REMEMBER_ME);

            mUsername.setText(username);
            mPassword.setText(password);
            mRememberMeCB.setChecked(rememberMe);
        }

        return myView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Someone there? Who's listening?
        try {
            mListener = (LoginInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface LoginInteractionListener
    {
        /** The user pressed the login button
         *
         * @param username The value that was in the username field
         * @param password The value that was in the password field
         * @param rememberMe True if the user clicked remember me
         */
        public void loginPressed(String username, String password, boolean rememberMe);
    }


    /** Enable or disable the UI components for the fragment
     *
     * @param enabled if true, components are enabled otherwise, disabled.
     */
    public void enable(boolean enabled) {

        ArrayList<View> children = ViewUtils.getAllChildren(getView());

        for (View child : children) {
            child.setEnabled(enabled);


            if (child instanceof Button) {
                child.setAlpha(enabled ? 1 : .5f);
            }
        }

    }
}
