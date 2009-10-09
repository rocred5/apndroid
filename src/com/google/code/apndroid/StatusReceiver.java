/*
 * This file is part of APNdroid.
 *
 * APNdroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * APNdroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with APNdroid. If not, see <http://www.gnu.org/licenses/>.
 */

package com.google.code.apndroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author Pavlov "Zelgadis: Dmitry
 */
public class StatusReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ApplicationConstants.APN_DROID_STATUS.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.getBoolean(ApplicationConstants.APN_DROID_SHOW_NOTIFICATION, true)) {
                boolean isNetEnabled = extras.getBoolean(ApplicationConstants.APN_DROID_STATUS_EXTRA);
                performNotificationStatusChange(context, isNetEnabled);
            }
        }
    }

    private void performNotificationStatusChange(Context context, boolean isNetEnabled) {
        int iconId = isNetEnabled ? R.drawable.stat_apndroid_on : R.drawable.stat_apndroid_off;
        // display notification icon without text
        Notification notification = new Notification(iconId, null, System.currentTimeMillis());

        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra(InfoActivity.EXTRA_IS_NET_ENABLED, isNetEnabled);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, -1 /* not used in SDK1.0 */, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        String notifyTitle = context.getResources().getString(R.string.app_name);
        int notifySummaryId = isNetEnabled ? R.string.status_enabled : R.string.status_disabled;
        String notifySummaryText = context.getResources().getString(notifySummaryId);
        notification.setLatestEventInfo(context, notifyTitle, notifySummaryText, pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}