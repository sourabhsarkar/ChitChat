package com.chatapp.chitchat;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sourabh on 05-Apr-16.
 */
public class InboxFragment extends android.support.v4.app.ListFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        return rootView;
    }
}
