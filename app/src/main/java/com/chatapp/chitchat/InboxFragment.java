package com.chatapp.chitchat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sourabh on 05-Apr-16.
 */
public class InboxFragment extends android.support.v4.app.ListFragment{

    protected List<ParseObject> mMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.progressBar.setVisibility(View.VISIBLE);
        refreshInbox();
    }

    public void refreshInbox() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                MainActivity.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Inbox refreshed", Toast.LENGTH_SHORT).show();
                if (e == null) {
                    // We found a message
                    mMessages = messages;
                    if (getListView().getAdapter() == null) {
                        MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);
                        setListAdapter(adapter);
                    }
                    else {
                        // refill the adapter
                        ((MessageAdapter)getListView().getAdapter()).refill(mMessages);
                    }
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject message = mMessages.get(position);
        String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
        ParseFile file;
        if(messageType.equals(ParseConstants.TYPE_TEXT)) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_msg_received, null);
            final TextView output = (TextView)view.findViewById(R.id.receivedTextView);
            final ImageView closeButton = (ImageView)view.findViewById(R.id.closeButton);
            output.setText(message.getString(ParseConstants.KEY_MSG));
            output.setMovementMethod(new ScrollingMovementMethod());

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        else {
            file = message.getParseFile(ParseConstants.KEY_FILE);
            Uri fileUri = Uri.parse(file.getUrl());

            if (messageType.equals(ParseConstants.TYPE_IMAGE)) {
                // view the image
                Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                intent.setData(fileUri);
                startActivity(intent);
            } else {
                // view the video
                Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
                intent.setDataAndType(fileUri, "video/*");
                startActivity(intent);
            }
        }

        // Delete it!
        List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);
        if (ids.size() == 1) {
            // last recipient - delete the whole thing!
            message.deleteInBackground();
        }
        else {
            // remove the recipient and save
            ids.remove(ParseUser.getCurrentUser().getObjectId());

            ArrayList<String> idsToRemove = new ArrayList<String>();
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());

            message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
            message.saveInBackground();
        }

    }
}
