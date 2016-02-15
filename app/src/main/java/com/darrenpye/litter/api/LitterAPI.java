package com.darrenpye.litter.api;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by darrenpye on 16-02-06.
 *
 * This class represents a pretend cloud service called "Litter" which allows users
 * to post short text messages for others to view. There is no actual network comms, these
 * are being simulated by introducing a delay to ensure all calls are properly asynchronus and
 * demonstrate that.
 *
 */
public class LitterAPI {

    // SAMPLE STUFF: Simulating network calls by adding in a delay before calling the callbacks

    private static long API_CALL_SIMULATION_DELAY = 1500; // Delay all simulated network calls by 1.5 seconds


    /* Callback interfaces implemented by users of the API */

    // Parent with generic callFailed
    public interface LitterAPICallback {

        /** Called if a communications failure happened while attempting
         * an API call
         *
         * @param message A message describing the failure if known, otherwise null
         */
        public void callFailed(String message);
    }

    // Callback for calls to login
    public interface LoginCallback extends LitterAPICallback {

        /** Called if the login was successful
         *
         * @param user a user object representing the user
         */
        public void loginSuccessful(User user);

        /** Called if the username or password was bad
         *
         */
        public void badCredentials();
    }

    // Callback for calls to getLitters
    public interface GetLittersCallback extends LitterAPICallback {
        /** Called when the list has been retrieved from the server
         *
         * @param litters The list of litters
         */
        public void gotLitters(ArrayList<Litter> litters);
    }

    // Callback for postLitter
    public interface PostLitterCallback extends LitterAPICallback {
        public void litterPosted();
    }

    /** Performs a login to the Litter service by either email or username and password
     *
     * @param context The context
     * @param usernameOrEmail The users username or email address
     * @param password The password
     * @param callback The callback for handling success or failures
     */
    public void login(final Context context, final String usernameOrEmail, final String password, final LoginCallback callback) {

        new DoLogin(context, usernameOrEmail, password, callback).execute();
    }

    private class UsernamePasswordHolder {
        String username;
        String password;
    }

    private class DoLogin extends AsyncTask<Void, Void, Boolean> {
        private Context mContext;
        private String mUsernameOrEmail;
        private String mPassword;
        private LoginCallback mCallback;

        private User mUser;
        private String mCommsErrorMessage;

        public DoLogin(final Context context, final String usernameOrEmail, final String password, final LoginCallback callback) {
            mContext = context;
            mUsernameOrEmail = usernameOrEmail;
            mPassword = password;
            mCallback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // Get a handle to the DB
            LitterDBHandler dbHandler = new LitterDBHandler(mContext);

            // Find the user by their username or email and password
            mUser = dbHandler.findUser(mUsernameOrEmail, mPassword);

            // SAMPLE STUFF: Simulate a network call by adding in a delay before calling the callback
            boolean commsWasOk = true;
            try {
                Thread.sleep(API_CALL_SIMULATION_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return mUser != null && mCommsErrorMessage == null;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                
                mCallback.loginSuccessful(mUser);

            } else {
                // If the communications was ok, but the result still bad, then
                // the credentials must be incorrect
                if (mCommsErrorMessage == null) {
                    mCallback.badCredentials();
                } else {
                    mCallback.callFailed(mCommsErrorMessage);
                }
            }
        }
    }


    /** Retrieves a list of the litters posted by the user
     *
     * @param context The context
     * @param userId The user to find litters for
     * @param callback The callback that will handle the result or failures
     */
    public void getLitters(final Context context, final long userId, final GetLittersCallback callback) {

        // Get a handle to the DB
        LitterDBHandler dbHandler = new LitterDBHandler(context);

        // Find the litters for this user
        final ArrayList<Litter> litters = dbHandler.getLittersForUser(userId);


        // SAMPLE STUFF: Simulate a network call by adding in a delay before calling the callback

        {
            final Handler h = new Handler();

            Runnable r1 = new Runnable() {

                @Override
                public void run() {

                    if (litters != null) {
                        callback.gotLitters(litters);
                    } else {
                        callback.callFailed("Could not retrieve Litters!");
                    }
                }
            };

            h.postDelayed(r1, API_CALL_SIMULATION_DELAY); // 1.5 second delay
        }
    }


    /** Post a litter message to the service
     *
     * @param context The context
     * @param userId The userid posting the message
     * @param message The message to post
     * @param callback The callback for handling results
     */
    public void postLitter(final Context context, final long userId, final String message, final PostLitterCallback callback) {

        // Get a handle to the DB
        LitterDBHandler dbHandler = new LitterDBHandler(context);

        // Createa  new Litter object
        Litter litter = new Litter(userId, message);

        // Find the litters for this user
        final boolean success = dbHandler.addLitter(litter);


        // SAMPLE STUFF: Simulate a network call by adding in a delay before calling the callback

        {
            final Handler h = new Handler();

            Runnable r1 = new Runnable() {

                @Override
                public void run() {

                    if (success) {
                        callback.litterPosted();
                    } else {
                        callback.callFailed("Could not post the Litter!");
                    }
                }
            };

            h.postDelayed(r1, API_CALL_SIMULATION_DELAY); // 1.5 second delay
        }
    }
}
