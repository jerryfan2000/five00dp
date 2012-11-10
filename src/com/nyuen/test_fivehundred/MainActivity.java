package com.nyuen.test_fivehundred;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ListView;

import com.fivehundredpx.api.PxApi;
import com.google.gson.Gson;
import com.nyuen.test_fivehundred.adapter.ImageAdapter;
import com.nyuen.test_fivehundred.structure.PhotoResponse;
import com.nyuen.test_fivehundred.util.UIUtils;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {
	
	private static final String TAG = MainActivity.class.getSimpleName();

    private ImageAdapter mImageAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setTitle("Popular");

        Log.d(TAG, "Ran!!");
        new LoadEventTask().execute();
    }

    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        getMenuInflater().inflate(R.menu., menu);
    //        return true;
    //    }
    
    private void updateList(PhotoResponse response) {
        mImageAdapter = new ImageAdapter(this, UIUtils.getImageFetcher(this));
    	mImageAdapter.setPhotos(Arrays.asList(response.getPhotos()));
        ListView view = (ListView) findViewById(R.id.listView1);
        view.setAdapter(mImageAdapter);
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
        private final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            mDialog.setTitle("Please wait...");
            mDialog.setMessage("Retrieving data...");
            mDialog.show();
        }

        protected PhotoResponse doInBackground(final String... args) {
            return MainActivity.getPhotosResponse();
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
