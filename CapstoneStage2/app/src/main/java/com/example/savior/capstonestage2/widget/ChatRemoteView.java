package com.example.savior.capstonestage2.widget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.savior.capstonestage2.model.Message;
import com.example.savior.capstonestage2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by savior on 8/5/2017.
 */

public class ChatRemoteView extends RemoteViewsService {


    private ArrayList<Message> list = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("message");
        list = new ArrayList<>();


        return new RemoteViewsFactory() {


            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {

                final long identityToken = Binder.clearCallingIdentity();
                mMessageDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.e("Count ", "" + snapshot.getChildrenCount());
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Message message = postSnapshot.getValue(Message.class);
                            list.add(message);
                            Log.e("Get Data", message.getName());

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("The read failed: ", databaseError.getMessage());
                    }
                });

                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                //in here we set the values for the views
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.item_message_widget);
                //display the last message only
                views.setTextViewText(R.id.widget_NameTextView, list.get(position).getName());

                views.setTextViewText(R.id.widget_MessageTextView, list.get(position).getText());

                String strURL = list.get(position).getPhotoUrl();
                try {
                    URL urlURL = new URL(strURL);
                    HttpURLConnection con = (HttpURLConnection) urlURL.openConnection();
                    InputStream is = con.getInputStream();
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    views.setImageViewBitmap(R.id.widget_PhotoImageView, bmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // views.setTextViewText(R.id.widgetPhotoImageView, list.get(list.size()-1).getPhotoUrl());
                //the item for the widget
                views.setOnClickFillInIntent(R.id.widget_item, new Intent());
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

        };

    }

}
