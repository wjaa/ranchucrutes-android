package br.com.agendee.service;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by wagner on 10/09/15.
 */
public interface FacebookService extends View.OnClickListener{

    void onCreate(Activity context);
    void onResume();
    void onStop();
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onStart();

}
