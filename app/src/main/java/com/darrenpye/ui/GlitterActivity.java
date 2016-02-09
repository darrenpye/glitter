package com.darrenpye.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darrenpye.R;
import com.darrenpye.ui.com.darrenpye.api.LlitterAPI;
import com.darrenpye.ui.com.darrenpye.api.User;
import com.darrenpye.ui.litterlist.LitterFragment;


public class GlitterActivity extends Activity implements
        LoginFragment.LoginInteractionListener,
        BadCredentialsFragment.BadCredentialsDoneListener,
        LitterFragment.LitterFragmentListener
{
    private static final String LOG_TAG = "GlitterActivity";

    private ImageView mGlitterLogo;
    private ImageView mUserImage;
    private TextView mGlitterWelcome;
    private LoginFragment mLoginFragment;
    private LitterFragment mLitterFragment;
    private LlitterAPI mGlitterAPI;

    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glitter);

        // Get an instance to the API
        mGlitterAPI = new LlitterAPI();

        // The components
        mGlitterLogo = (ImageView) findViewById(R.id.glitterLogo);
        mUserImage = (ImageView) findViewById(R.id.userImage);
        mGlitterWelcome = (TextView) findViewById(R.id.glitterWelcome);

        mUserImage.setScaleX(0);
        mUserImage.setScaleY(0);

        mGlitterWelcome.setText("Hello username\nYou have 9999 followers");
        mGlitterWelcome.setVisibility(View.INVISIBLE);

        // Startup sequence
        animateLogoIn();
        showLoginFragment();
    }

    @Override
    public void loginPressed(String usernameOrEmail, String password, boolean rememberMe) {

        Log.d(LOG_TAG, "Login Pressed. Username='" + usernameOrEmail + "', Password='" + password + "', Remember Me=" + rememberMe);

        showBusy(true);

        mLoginFragment.enable(false);

        mGlitterAPI.login(this, usernameOrEmail, password, new LlitterAPI.LoginCallback() {
            @Override
            public void loginSuccessful(User user) {
                // Success!

                mCurrentUser = user;

                showBusy(false);
                showLitter();
            }

            @Override
            public void badCredentials() {
                showBusy(false);
                showBadCredentials();
            }

            @Override
            public void callFailed(String message) {
                // TODO: Network communictions failure displayed here
            }
        });
    }


    private void animateLogoIn() {
        ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(750);
        fade_in.setFillAfter(true);
        fade_in.setInterpolator(new DecelerateInterpolator());
        mGlitterLogo.startAnimation(fade_in);
    }


    private void animateUserWelcome() {
        mUserImage.setImageResource(mCurrentUser.getUserImageResource());
        mUserImage.setScaleX(1);
        mUserImage.setScaleY(1);
        ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(1000);
        fade_in.setFillAfter(true);
        fade_in.setInterpolator(new BounceInterpolator());
        mUserImage.startAnimation(fade_in);

        mGlitterWelcome.setText("Hello " + mCurrentUser.getUsername() + "!\nYou have 0 followers");
        mGlitterWelcome.setVisibility(View.VISIBLE);
    }


    private void showLoginFragment() {
        mLoginFragment = LoginFragment.newInstance("kyloren@emo-sith.com", "password", true);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.animator.frag_in, R.animator.frag_out);
        fragmentTransaction.replace(R.id.fragment_place, mLoginFragment);
        fragmentTransaction.commit();
    }


    private void showLitter() {
        if (mLitterFragment == null) {
            mLitterFragment = new LitterFragment();
        }

        mLitterFragment.setCurrentUser(mCurrentUser);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.animator.frag_in, R.animator.frag_out);
        fragmentTransaction.replace(R.id.fragment_place, mLitterFragment);
        fragmentTransaction.commit();

        animateUserWelcome();
    }


    private void showBadCredentials() {
        BadCredentialsFragment badCredentialsFragment = new BadCredentialsFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.animator.frag_in, R.animator.frag_out);
        fragmentTransaction.replace(R.id.fragment_place, badCredentialsFragment);
        fragmentTransaction.commit();
    }

    /* For BadCredentialsFragment */
    @Override
    public void done() {
        // Called when user hits the try again buttton in Bad Credentials after a failed login attempt
        showLoginFragment();
    }


    public void showBusy(boolean busy) {
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);

        if (busy) {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        } else {
            linlaHeaderProgress.setVisibility(View.GONE);
        }
    }


    /* LitterFragment interactions */
    @Override
    public void onFragmentInteraction(String id) {

    }
}
