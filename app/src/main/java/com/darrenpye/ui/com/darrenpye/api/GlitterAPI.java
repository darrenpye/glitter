package com.darrenpye.ui.com.darrenpye.api;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by darrenpye on 16-02-06.
 */
public class GlitterAPI {

    // SAMPLE STUFF: Simulating network calls by adding in a delay before calling the callbacks

    private static long API_CALL_SIMULATION_DELAY = 1500; // Delay all simulated network calls by 1.5 seconds



    public interface GlitterAPICallback {
        public void callFailed(String message);
    }

    public interface LoginCallback extends GlitterAPICallback {
        public void loginSuccessful(User user);
        public void badCredentials();
    }

    public interface GetGlittersCallback extends GlitterAPICallback {
        public void gotGlitters(ArrayList<Litter> litters);
    }

    public void login(final Context context, final String usernameOrEmail, final String password, final LoginCallback callback) {

        // Get a handle to the DB
        GlitterDBHandler dbHandler = new GlitterDBHandler(context);

        // Find the user by their username or email and password
        final User user = dbHandler.findUser(usernameOrEmail, password);


        // SAMPLE STUFF: Simulate a network call by adding in a delay before calling the callback

        {
            final Handler h = new Handler();

            Runnable r1 = new Runnable() {

                @Override
                public void run() {

                    if (user != null) {
                        callback.loginSuccessful(user);
                    } else {
                        callback.badCredentials();
                    }
                }
            };

            h.postDelayed(r1, API_CALL_SIMULATION_DELAY); // 1.5 second delay
        }
    }


    public void getLitters(final Context context, final long userId, final GetGlittersCallback callback) {

        // Get a handle to the DB
        GlitterDBHandler dbHandler = new GlitterDBHandler(context);

        // Find the litters for this user
        final ArrayList<Litter> litters = dbHandler.getLittersForUser(userId);


        // SAMPLE STUFF: Simulate a network call by adding in a delay before calling the callback

        {
            final Handler h = new Handler();

            Runnable r1 = new Runnable() {

                @Override
                public void run() {

                    if (litters != null) {
                        callback.gotGlitters(litters);
                    } else {
                        callback.callFailed("Could not retrieve Litters!");
                    }
                }
            };

            h.postDelayed(r1, API_CALL_SIMULATION_DELAY); // 1.5 second delay
        }
    }
}
