package com.ptrprograms.stayawake.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ptrprograms.stayawake.Activities.IterationActivity;

/**
 * Created by PaulTR on 6/29/14.
 */
public class TimerService extends IntentService
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{

    public TimerService() {
        super( "TimerService" );
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if( IterationActivity.ACTION_SHOW_ALARM.equals( action ) ) {
            showAlarm();
        } else if( IterationActivity.ACTION_REMOVE_TIMER.equals( action ) ) {
            removeAlarm();
        }
    }

    private void showAlarm() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate( 3000 );
        Intent intent = new Intent( this, IterationActivity.class );
        intent.addFlags( ( Intent.FLAG_ACTIVITY_NEW_TASK ) );
        startActivity( intent );
    }

    private void removeAlarm() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from( this );
        notificationManager.cancel( 1 );

        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );

        Intent intent = new Intent( IterationActivity.ACTION_SHOW_ALARM, null, this, TimerService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );

        alarmManager.cancel( pendingIntent );

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong( IterationActivity.SAVED_STATE_SELECTED_DURATION, 0 );
        editor.apply();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
