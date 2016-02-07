package com.darrenpye.ui.litterlist;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;


import com.darrenpye.R;
import com.darrenpye.ui.GlitterActivity;
import com.darrenpye.ui.com.darrenpye.api.GlitterAPI;
import com.darrenpye.ui.com.darrenpye.api.GlitterDBHandler;
import com.darrenpye.ui.com.darrenpye.api.Litter;
import com.darrenpye.ui.com.darrenpye.api.User;

import java.util.ArrayList;

public class LitterFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String LOG_TAG = "LitterFragment";

    private User mCurrentUser;
    private LitterFragmentListener mListener;
    private View mView;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;


    public LitterFragment() {
    }


    public User getmCurrentUser() {
        return mCurrentUser;
    }

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
        mView = inflater.inflate(R.layout.fragment_gleet, container, false);


        // Get the litters
        getLastestLitters();

        mListView = (AbsListView) mView.findViewById(android.R.id.list);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return mView;
    }


    private void getLastestLitters() {

        final GlitterActivity glitterActivity = (GlitterActivity)getActivity();
        glitterActivity.showBusy(true);

        GlitterAPI glitterAPI = new GlitterAPI();


        glitterAPI.getLitters(glitterActivity, mCurrentUser.getUserId(), new GlitterAPI.GetGlittersCallback() {
            @Override
            public void callFailed(String message) {
                // Woops
                Log.e(LOG_TAG, message);

                glitterActivity.showBusy(false);
            }

            @Override
            public void gotGlitters(ArrayList<Litter> litters) {
                mAdapter = new LitterAdapter(getActivity(), R.layout.litter_item, litters);

                // Set the adapter
                mListView = (AbsListView) mView.findViewById(android.R.id.list);
                ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

                glitterActivity.showBusy(false);
            }
        });


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (LitterFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(LitterContent.ITEMS.get(position).id);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface LitterFragmentListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
