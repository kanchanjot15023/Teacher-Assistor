package com.example.kanchanjot.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class ChatService extends Service {

    public static String QUEUE_NAME;
    public static String EXCHANGE_NAME;
    private MessageConsumer mConsumer;

    private BroadcastReceiver serviceReceiver;
    private static final String ACTION_STRING_SERVICE = "ToService";
    private static final String ACTION_STRING_ACTIVITY = "ToActivity";
    private int flagActivityActive;
    private String activeUserEmail;
    private String activeTask;



    String to_str="";
    String from_str="";

    String message1="";

    String titleIntent="";

    String taskIntent="";

    public static String emailIntent="";
    public static String nameIntent="";
    public static String rollnoIntent="";


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void Notify(Context context,String notificationTitle, String notificationMessage){
        Intent notificationIntent = new Intent(context, Dashboard.class);

        notificationIntent.putExtra("email", emailIntent);
        notificationIntent.putExtra("name", nameIntent);
        notificationIntent.putExtra("rollno", rollnoIntent);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Dashboard.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.notifications))
                .setContentTitle(notificationTitle)
                .setSmallIcon(R.mipmap.notifications)
                .setContentText(notificationMessage);
        builder.setAutoCancel(true);
        builder.setContentIntent(notificationPendingIntent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(generateRandom(), builder.build());

    }
    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }



    public static  boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) ChatApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        flagActivityActive = 0;
        QUEUE_NAME = intent.getStringExtra("id");                   //emailid itself
        EXCHANGE_NAME = intent.getStringExtra("id");
        emailIntent=intent.getStringExtra("id");
        nameIntent=intent.getStringExtra("name");
        rollnoIntent=intent.getStringExtra("rollno");

        Log.i("INFO","I am here. Service started "+QUEUE_NAME+"  "+EXCHANGE_NAME);
        mConsumer = new MessageConsumer(DefineUrl.url_Host, EXCHANGE_NAME,QUEUE_NAME, "fanout");

        serviceReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                String flagStatus = intent.getStringExtra("status");
                flagActivityActive = Integer.parseInt(flagStatus);
                if(flagActivityActive == 1)
                {
                    activeUserEmail = intent.getStringExtra("email");
                    activeTask     = intent.getStringExtra("taskId");
                    Log.i("INFO","I am from service. Activity is active now "+activeUserEmail+"  "+activeTask);
                }
                else
                {
                    Log.i("INFO","I am from service. Activity is stopped now");
                }

            }
        };
        if (serviceReceiver != null) {
            IntentFilter intentFilter = new IntentFilter(ACTION_STRING_SERVICE);
            registerReceiver(serviceReceiver, intentFilter);
        }



        new consumerconnect().execute();

        mConsumer.setOnReceiveMessageHandler(new MessageConsumer.OnReceiveMessageHandler() {

            public void onReceiveMessage(byte[] message) {
                String text = "";
                text = text.concat(MessageConsumer.message);
                Log.i("INFO",text);

                String[] allFields = text.split("--", 5);
                Log.i("INFO", "in Service entire message received " + text);
                if (allFields.length == 5) {

                    to_str=allFields[0];
                    from_str=allFields[1];
                    message1=allFields[2];
                    titleIntent=allFields[3];
                    taskIntent=allFields[4];


                    Log.i("INFO", "while setting message Checking Service message " + message1);

                } else {
                    //rarely happens
                    to_str="";
                    from_str="";
                    message1="";
                    titleIntent="";
                    taskIntent="";



                }
                Log.i("INFO", "From Service " + text);
                if (flagActivityActive == 1) {

                    if (from_str.equals(activeUserEmail) && (taskIntent.equals(activeTask)) ) {
                        Intent new_intent = new Intent();
                        new_intent.setAction(ACTION_STRING_ACTIVITY);
                        new_intent.putExtra("message", message1);
                        new_intent.putExtra("fromEmail", from_str);
                        sendBroadcast(new_intent);
                    } else {
                    //check here
                        if(titleIntent.equals("New Message"))
                        Notify(getApplicationContext(), "Message from " + from_str, message1);
                        else
                            Notify(getApplicationContext(),titleIntent, message1);
                    }
                } else {
                    if(titleIntent.equals("New Message"))
                    Notify(getApplicationContext(), titleIntent , message1);
                    else
                        Notify(getApplicationContext(),titleIntent, message1);
                  //check here
                }

            }
        });

        return START_REDELIVER_INTENT;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }



    private class consumerconnect extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Message) {

            try {
                // Connect to broker
                mConsumer.connectToRabbitMQ();


            }
            catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }


            return null;
        }

    }
}
