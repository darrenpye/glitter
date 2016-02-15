package com.darrenpye.litter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.darrenpye.R;
import com.darrenpye.litter.api.Litter;
import com.darrenpye.litter.api.LitterAPI;
import com.darrenpye.litter.api.LitterDBHandler;
import com.darrenpye.litter.api.User;
import com.darrenpye.ui.BadCredentialsFragment;
import com.darrenpye.ui.LoginFragment;
import com.darrenpye.ui.litterlist.LitterFragment;
import com.darrenpye.ui.utils.ViewUtils;

/** The main activity of the app. Represents a manager for UI views (fragments)
 * and interactions for serveral of it's own UI components.
 *
 */
public class LitterActivity extends Activity implements
        LoginFragment.LoginInteractionListener,
        BadCredentialsFragment.BadCredentialsDoneListener,
        LitterFragment.LitterFragmentListener
{
    private static final String LOG_TAG = "LitterActivity";

    // Images
    private ImageView mGlitterLogo;
    private ImageView mUserImage;

    // Buttons
    private ImageButton mLitterButton;
    private ImageButton mSearchButton;
    private ImageButton mFollowersButton;
    private ImageButton mSettingsButton;

    // Components
    private View mButtonBar;

    // Fragments
    private LoginFragment mLoginFragment;
    private LitterFragment mLitterFragment;

    // Data
    private LitterAPI mGlitterAPI;
    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_litter);

        // Create an instance of the Database
        LitterDBHandler.makeInstance(this);


        // Get an instance to the API
        mGlitterAPI = new LitterAPI();

        // The components
        mGlitterLogo = (ImageView) findViewById(R.id.glitterLogo);
        mUserImage = (ImageView) findViewById(R.id.userImage);

        mButtonBar = findViewById(R.id.buttonBar);

        mLitterButton = (ImageButton) findViewById(R.id.litterButton);
        mSearchButton = (ImageButton) findViewById(R.id.searchButton);
        mFollowersButton = (ImageButton) findViewById(R.id.followersButton);
        mSettingsButton = (ImageButton) findViewById(R.id.settingsButton);

        // Hide the bar

        mButtonBar.setAlpha(0);

        // Scale down these things for latter animated showing
        ViewUtils.setScale(mUserImage, 0);

        ViewUtils.setScale(mLitterButton, 0);
        ViewUtils.setScale(mSearchButton, 0);
        ViewUtils.setScale(mFollowersButton, 0);
        ViewUtils.setScale(mSettingsButton, 0);

        // Listeners
        setupButtonListeners();

        // Startup sequence
        animateLogoIn();
        showLoginFragment();
    }


    /** Setups listeners for the button bar buttons
     *
     */
    private void setupButtonListeners() {
        mLitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.animateButtonPress(v);
                litterButtonPressed();
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.animateButtonPress(v);
                // TODO: Not doing anything in this excercise
            }
        });

        mFollowersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.animateButtonPress(v);
                // TODO: Not doing anything in this excercise
            }
        });

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.animateButtonPress(v);
                // TODO: Not doing anything in this excercise
            }
        });
    }


    /** Implements the login button pressed called by the LoginFragment
     *
     * @param usernameOrEmail the username or email entered
     * @param password the password entered
     * @param rememberMe true if the user wants to autologin next time
     */
    @Override
    public void loginPressed(String usernameOrEmail, String password, boolean rememberMe) {

        Log.d(LOG_TAG, "Login Pressed. Username='" + usernameOrEmail + "', Password='" + password + "', Remember Me=" + rememberMe);

        showBusy(true);

        // Disable the fragment so the user can't press or enter text while we are calling the
        // litter service
        mLoginFragment.enable(false);

        mGlitterAPI.login(usernameOrEmail, password, new LitterAPI.LoginCallback() {
            @Override
            public void loginSuccessful(User user) {
                // Success!

                mCurrentUser = user;

                showBusy(false);
                showLittersList();
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


    /** Animation for the logo on app launch
     *
     */
    private void animateLogoIn() {
        ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(750);
        fade_in.setFillAfter(true);
        fade_in.setInterpolator(new DecelerateInterpolator());
        mGlitterLogo.startAnimation(fade_in);
    }


    /** Some simple animations run on user login
     *
     */
    private void animateUserWelcome() {
        mUserImage.setImageResource(mCurrentUser.getUserImageResource());

        ViewUtils.setScale(mUserImage, 1);
        ScaleAnimation scale_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale_in.setDuration(1000);
        scale_in.setFillAfter(true);
        scale_in.setInterpolator(new DecelerateInterpolator());
        mUserImage.startAnimation(scale_in);

        final long button_scale_time = 1000;
        final long button_scale_increment_time = 250;

        // Animate the buttons coming in
        ViewUtils.animateButtonShow(mLitterButton, button_scale_time + (button_scale_increment_time * 0));
        ViewUtils.animateButtonShow(mSearchButton, button_scale_time + (button_scale_increment_time * 1));
        ViewUtils.animateButtonShow(mFollowersButton, button_scale_time + (button_scale_increment_time * 2));
        ViewUtils.animateButtonShow(mSettingsButton, button_scale_time + (button_scale_increment_time * 3));

        // Make the background of the button bar visible
        mButtonBar.setAlpha(1);
    }


    /** Shows the login fragment for the user to enter their credentials
     *
     */
    private void showLoginFragment() {

        mLoginFragment = LoginFragment.newInstance("kyloren@emo-sith.com", "password", true);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.animator.frag_in, R.animator.frag_out);
        fragmentTransaction.replace(R.id.fragment_place, mLoginFragment);
        fragmentTransaction.commit();
    }


    /** Shows the users Litters list
     *
     */
    private void showLittersList() {
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


    /** Shows a message explaining to the user that they have entered bad login credentials
     *
     */
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


    /** Shows or hides a spinner for the App to indicate that it's busy doing something such
     * as calling a service function
     *
     * @param busy if true, the spinner is displayed, otherwise it's hidden
     */
    public void showBusy(boolean busy) {
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);

        if (busy) {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        } else {
            linlaHeaderProgress.setVisibility(View.GONE);
        }
    }


    /** Hides the keyboard
     *
     */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


    /** The user pressed the litter button
     *
     */
    private void litterButtonPressed() {

        mLitterFragment.showLitterLayout();
    }


    /* LitterFragment interactions */
    public void cancelLitterPressed() {
        hideKeyboard();

        mLitterFragment.hideLitterLayout();
    }

    /** The user pressed the postLitter button, sends the message to the API
     *
     * @param message
     */
    public void postLitterPressed(String message) {

        // Validate the message
        boolean messageOk = message.length() > 0 && message.length() < 140 ;

        if (!messageOk) {
            // TODO: Display an error popup to the user explaining message is invalid
            return;
        }

        Log.d(LOG_TAG, "About to post litter message: " + message);

        hideKeyboard();

        mLitterFragment.hideLitterLayout();

        showBusy(true);

        mGlitterAPI.postLitter(mCurrentUser.getUserId(), message, new LitterAPI.PostLitterCallback() {
            @Override
            public void callFailed(String message) {
                showBusy(false);

                // TODO: The API call failed - tell the user
            }

            @Override
            public void litterPosted() {
                showBusy(false);
                mLitterFragment.getLastestLitters();
            }
        });

    }

}
