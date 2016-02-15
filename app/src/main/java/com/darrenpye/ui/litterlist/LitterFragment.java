package com.darrenpye.ui.litterlist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import com.darrenpye.R;
import com.darrenpye.litter.LitterActivity;
import com.darrenpye.litter.api.Litter;
import com.darrenpye.litter.api.LitterAPI;
import com.darrenpye.litter.api.User;
import com.darrenpye.ui.utils.ViewUtils;

import java.util.ArrayList;

/**
 * Created by darrenpye on 16-02-07
 *
 * The fragment representing the Liter list. Shows the list of the users litters
 *
 */
public class LitterFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String LOG_TAG = "LitterFragment";

    private User mCurrentUser;
    private LitterFragmentListener mListener;
    private View mView;

    private EditText mLitterMessage;
    private View mPostLitterPanel;
    private View mCancelLitterPanel;
    private ImageButton mTakePictureButton;

    /**
     * The fragment's ListView
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView with
     * Views.
     */
    private ListAdapter mAdapter;

    /** Empty constructor **/
    public LitterFragment() {
    }


    /** Returns the current user object
     *
     * @return Current user
     */
    public User getmCurrentUser() {
        return mCurrentUser;
    }

    /** Sets the current user object
     *
     * @param mCurrentUser user to set
     */
    public void setCurrentUser(User mCurrentUser) {
        this.mCurrentUser = mCurrentUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the view and assign to property
        mView = inflater.inflate(R.layout.fragment_litter_list, container, false);

        // Get the litters
        getLastestLitters();

        // Components

        mLitterMessage = (EditText) mView.findViewById(R.id.litterMessage);
        mTakePictureButton = (ImageButton) mView.findViewById(R.id.takePicture);
        mPostLitterPanel =  mView.findViewById(R.id.postLitter);
        mCancelLitterPanel =  mView.findViewById(R.id.cancelLitter);

        mListView = (AbsListView) mView.findViewById(android.R.id.list);


        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        // Listeners for buttons
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.animateButtonPress(v);
            }
        });

        mPostLitterPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.animateButtonPress(v);

                mListener.postLitterPressed(mLitterMessage.getText().toString());
            }
        });

        mCancelLitterPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelLitterPressed();
            }
        });


        // Make the post section "gone" (this is only done here so the file can be easily edited in the view editor)
        View litterLayout = mView.findViewById(R.id.litterLayout);
        litterLayout.setVisibility(View.GONE);

        return mView;
    }


    /** Retrieves the latest litters for the the user and updates the display
     *
     */
    public void getLastestLitters() {

        // Show the busy state
        final LitterActivity glitterActivity = (LitterActivity)getActivity();
        glitterActivity.showBusy(true);

        // Get a reference to the API
        LitterAPI glitterAPI = new LitterAPI();

        // Call the getLitters method with a callback
        glitterAPI.getLitters(mCurrentUser.getUserId(), new LitterAPI.GetLittersCallback() {
            @Override
            public void gotLitters(ArrayList<Litter> litters) {
                mAdapter = new LitterAdapter(getActivity(), R.layout.litter_item, litters);

                // Set the adapter
                mListView = (AbsListView) mView.findViewById(android.R.id.list);
                ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

                glitterActivity.showBusy(false);
            }

            @Override
            public void callFailed(String message) {
                // Woops
                Log.e(LOG_TAG, message);

                glitterActivity.showBusy(false);
            }
        });


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Who's listening?
        try {
            mListener = (LitterFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LitterFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Not doing anything, but could callback the LitterFragmentListner
        }
    }


    /** This makes the litterLayout Gone
     *
     * */
    public void hideLitterLayout() {

        View litterView =  mView.findViewById(R.id.litterLayout);
        litterView.setVisibility(View.GONE);

        // Clear it out
        mLitterMessage.setText("");
    }


    /** Shows the litterLayout view
     *
     */
    public void showLitterLayout() {

        // Show the litterLayout view
        View litterView = mView.findViewById(R.id.litterLayout);
        litterView.setVisibility(View.VISIBLE);

        // Animate the buttons so the user has a clear indication that they are not just graphics
        long button_scale_time = 750;
        long button_scale_increment_time = 250;

        // Animate the buttons coming in
        ViewUtils.animateButtonShow(mTakePictureButton, button_scale_time + (button_scale_increment_time * 0));
        ViewUtils.animateButtonShow(mPostLitterPanel, button_scale_time + (button_scale_increment_time * 1));
        ViewUtils.animateButtonShow(mCancelLitterPanel, button_scale_time + (button_scale_increment_time * 2));

        // Set keyboard focus to the message component
        mLitterMessage.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mLitterMessage, InputMethodManager.SHOW_IMPLICIT);

    }

    /* Listener inteface for interaction callbacks */
    public interface LitterFragmentListener {
        public void cancelLitterPressed();
        public void postLitterPressed(String message);
    }

}
