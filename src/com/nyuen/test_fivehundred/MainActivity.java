package com.nyuen.test_fivehundred;

import com.fivehundredpx.api.PxApi;
import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.api.auth.OAuthAuthorization;
import com.google.gson.Gson;
import com.nyuen.test_fivehundred.adapter.ImageAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends ListActivity {

    private ImageAdapter test_adapter;
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        test_adapter = new ImageAdapter(this);
        
        
        
                
        Bitmap[] mPhotos = new Bitmap[4];
        
        mPhotos[0] = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        mPhotos[1] = BitmapFactory.decodeResource(getResources(), R.drawable.b);
        mPhotos[2] = BitmapFactory.decodeResource(getResources(), R.drawable.c);
        mPhotos[3] = BitmapFactory.decodeResource(getResources(), R.drawable.d);
//        mPhotos[4] = BitmapFactory.decodeResource(getResources(), R.drawable.a);
//        mPhotos[5] = BitmapFactory.decodeResource(getResources(), R.drawable.b);
//        mPhotos[6] = BitmapFactory.decodeResource(getResources(), R.drawable.c);
//        mPhotos[7] = BitmapFactory.decodeResource(getResources(), R.drawable.d);
        
//                new BitmapDrawable(getResources(), R.dr);
        test_adapter.setPhotos(mPhotos);
        setListAdapter(this.test_adapter);
        new loadEventTask().execute();
        Log.d("500px", "Ran!!");
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu., menu);
//        return true;
//    }
    private class loadEventTask extends AsyncTask<String, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            dialog.setTitle("Please wait...");
            dialog.setMessage("Retrieving data...");
            dialog.show();
        }

        protected Void doInBackground(final String... args) {
            String key = "H44vO2VyWiCcW0RBlZ3EAEJU4o6nuI2MlNCLLDmx";
            String secret = "Ufi5Logkfwy0uujKpzZj729hQJQlIgUj5gOahmMj";
     
            //PxApi pxapi = new PxApi(, key, secret);
            PxApi pxapi = new PxApi(secret);
            Log.d("500px", "Ready!!");
            try {
                Gson results = new Gson();    
                Log.d("500px", pxapi.get("https://api.500px.com/v1/photos?feature=popular").toString() );
            } catch (Exception e) {
                Log.e("500px", e.toString());
            }
            
            return null;
        }

        protected void onPostExecute(final Void unused) {
            //Log.d("Main", "adapter size: " + m_adapter.getCount());

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            
            //if(m_event != null && m_event.size() > 0){
               //m_adapter.clear();
//               for(int i=0;i<m_event.size();i++)
//                   m_adapter.add(m_event.get(i));
            //}
          
        }
     }
    
    
}
