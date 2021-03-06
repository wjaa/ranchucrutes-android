package br.com.agendee.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import br.com.agendee.activity.FacadeActivity;
import br.com.agendee.utils.NotificationUtils;


public class GcmIntentService extends IntentService {
	public static final String TAG = GcmIntentService.class.getSimpleName();
	
	public GcmIntentService(){
		super("GcmIntentService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(GcmIntentService.this);
		String messageType = gcm.getMessageType(intent);
		
		
		if(extras != null){
			if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)){
				Log.i(TAG, "Error: "+extras.toString());
			}
			else if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)){
				Log.i(TAG, "Deleted: "+extras.toString());
			}
			else if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
				String status = extras.getString("status");
				String msg = extras.getString("msg");

				String titulo = "";
				if ("CANCELLATION".equalsIgnoreCase(status)){
					titulo = "Consulta Cancelada!";
				}else if ("CONFIRMATION".equalsIgnoreCase(status)){
					titulo = "Consulta Confirmada!";
				}
				Log.d(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

				NotificationUtils.criarNotificacao(GcmIntentService.this,"Notificação Agendee",titulo,msg, FacadeActivity.class);

				//NotificationCustomUtil.sendNotification(GcmIntentService.this,GcmIntentService.class,
						//title, author, message);
			}
		}
		
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
	
}
