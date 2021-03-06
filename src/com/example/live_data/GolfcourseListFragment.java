package com.example.live_data;



import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class GolfcourseListFragment extends ListFragment implements 
	LoaderManager.LoaderCallbacks<ArrayList<Golfcourse>>{

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    
    private ArrayList<Golfcourse> courses = new ArrayList<Golfcourse>();
    private String live_data_url = "http://jason.mobileappdocs.com/live-data";
    
    ArrayAdapter<Golfcourse> adapter = null;

    public interface Callbacks {

//        public void onItemSelected(String id);
        public void onItemSelected(Golfcourse c);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Golfcourse c) {
//        public void onItemSelected(String id) {
        }
    };
   
    public GolfcourseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState == null) {
        if (getArguments().containsKey("courses")) {
        	courses = getArguments().getParcelableArrayList("courses");
            Log.v("myApp", "List Fragment: argument first course:" + courses.get(1).name);
        }

       adapter = new ArrayAdapter<Golfcourse>(getActivity(),
               R.layout.simple_list_item_activated_1,
               R.id.text1,
               courses);
        setListAdapter(adapter);
        
        getActivity().getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    	}
    


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState
                .containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
//        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
        mCallbacks.onItemSelected((Golfcourse) listView.getItemAtPosition(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    	Log.v("myApp", "List Fragment: Ready to save courses in outState.");
//        outState.putParcelableArrayList("courses", courses);
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
	// Loader callbacks
	
	@Override
	public Loader<ArrayList<Golfcourse>> onCreateLoader(
			int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		Log.v("myapp", "Loader created.");
		return new LiveDataLoader(getActivity() , live_data_url);
	}

	@Override
	public void onLoadFinished(
			Loader<ArrayList<Golfcourse>> arg0,
			ArrayList<Golfcourse> coursesOnline) {
		// TODO Auto-generated method stub
		if (coursesOnline.equals(courses)) {
			Log.v("myapp", "The online courses have already been downloaded.");
		}
		else {
			// Update data and notify adapter
	    	courses.clear();
	    	courses.addAll(coursesOnline);
	    	adapter.notifyDataSetChanged();
	    //	((GolfcourseListActivity) getActivity()).
		}
		Log.v("myapp", "Loader results: " + coursesOnline);

	};

	@Override
	public void onLoaderReset(
			Loader<ArrayList<Golfcourse>> arg0) {
		// TODO Auto-generated method stub		
	}
}
