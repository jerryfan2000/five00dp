package com.nyuen.five00dp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.nyuen.five00dp.R;

public class MenuFragment extends SherlockListFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sliding_menu_fragment, null);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] items = getResources().getStringArray(R.array.menu);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(getActivity(), 
                android.R.layout.simple_list_item_1, items);
        setListAdapter(itemAdapter);
    }
}
