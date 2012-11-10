package com.nyuen.test_fivehundred;

import java.util.Arrays;

import com.fivehundredpx.api.PxApi;
import com.google.gson.Gson;
import com.nyuen.test_fivehundred.adapter.ImageAdapter;
import com.nyuen.test_fivehundred.structure.PhotoResponse;
import com.nyuen.test_fivehundred.util.UIUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("NewApi")
public class ImageListFragment extends ListFragment {
    
    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageAdapter mImageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        //setContentView(R.layout.activity_main);
        
        getActivity().getActionBar().setDisplayUseLogoEnabled(true);
        getActivity().getActionBar().setTitle("Popular");
    
        Log.d(TAG, "Ran!!");
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new LoadEventTask().execute();
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_settings:
            
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void updateList(PhotoResponse response) {
        mImageAdapter = new ImageAdapter(getActivity(), UIUtils.getImageFetcher(getActivity()));
        mImageAdapter.setPhotos(Arrays.asList(response.getPhotos()));
        setListAdapter(mImageAdapter);
    }
    
    public static PhotoResponse getPhotosResponse() {
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        
        PhotoResponse photoResponse;
        try {               
            photoResponse = new Gson().fromJson(
                    pxapi.get("/photos?feature=popular&rpp=15&image_size=4").toString(), 
                    PhotoResponse.class);
            
            Log.d(TAG, photoResponse.getPhotos()[0].image_url );
            
            return photoResponse;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }   
    }

    private class LoadEventTask extends AsyncTask<String, Void, PhotoResponse> {
        private final ProgressDialog mDialog = new ProgressDialog(getActivity());

        protected void onPreExecute() {
            mDialog.setTitle("Please wait...");
            mDialog.setMessage("Retrieving data...");
            mDialog.show();
        }

        protected PhotoResponse doInBackground(final String... args) {
            return getPhotosResponse();
        }

        protected void onPostExecute(PhotoResponse response) {
            //Log.d("Main", "adapter size: " + m_adapter.getCount());

            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }

            //if(m_event != null && m_event.size() > 0){
            //m_adapter.clear();
            //               for(int i=0;i<m_event.size();i++)
            //                   m_adapter.add(m_event.get(i));
            //}

            updateList(response);
        }
    }
}
