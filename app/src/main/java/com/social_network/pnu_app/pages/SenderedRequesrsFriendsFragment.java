package com.social_network.pnu_app.pages;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.social_network.pnu_app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SenderedRequesrsFriendsFragment extends Fragment {


    public SenderedRequesrsFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sendered_requesrs_friends, container, false);
    }

}
