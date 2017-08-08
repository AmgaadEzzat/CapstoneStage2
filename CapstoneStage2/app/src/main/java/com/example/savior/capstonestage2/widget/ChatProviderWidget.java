package com.example.savior.capstonestage2.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.savior.capstonestage2.MainActivity;
import com.example.savior.capstonestage2.R;


import static com.example.savior.capstonestage2.MainActivity.ACTION_DATA_UPDATED;

/**
 * Implementation of App Widget functionality.
 */
public class ChatProviderWidget extends AppWidgetProvider {
    public static final String EXTRA_ITEM = "com.example.savior.capstonestage2.widget.ChatProviderWidget.EXTRA_ITEM";

    /**
     * in this method we get the data from the data source (shared ,database,content provider)
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager
            appWidgetManager, int[] appWidgetIds) {


        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.chat_provider_widget);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            views.setRemoteAdapter(R.id.list_view_chat, new Intent(context, ChatRemoteView.class));


            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.list_view_chat, clickPendingIntentTemplate);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_chat);
        }
    }
}

