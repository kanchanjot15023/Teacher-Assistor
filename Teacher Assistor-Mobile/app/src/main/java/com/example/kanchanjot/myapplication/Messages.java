package com.example.kanchanjot.myapplication;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kanchanjot on 27/04/16.
 */
public class Messages extends AppCompatActivity {


    String email = "";
    String name = "";
    String rollno = "";

    String taskId = "";

    String reciever = "";


    private String QUEUE_NAME;
    private String EXCHANGE_NAME;


    private FloatingActionButton floatingActionButton;
    private EditText editTextMessage;


    private ListView messagesList;


    ArrayList<String> messages = new ArrayList<String>();
    ArrayList<String> sendBy = new ArrayList<String>();


    String message1 = "";


    private BroadcastReceiver activityReceiver;


    private static final String ACTION_STRING_SERVICE = "ToService";
    private static final String ACTION_STRING_ACTIVITY = "ToActivity";



    private MyListAdapter<String> adapter;


    private ProgressDialog pDialog;
    JSONParser jsonParser;
    JSONObject json;


    String status="";


    String message="";



    String length="";

    int len=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);


        email = getIntent().getExtras().getString("email");
        name = getIntent().getExtras().getString("name");
        rollno = getIntent().getExtras().getString("rollno");
        taskId = getIntent().getExtras().getString("taskId");
        reciever = getIntent().getExtras().getString("reciever");


        if(email == null)
        {
            email = "";
        }
        if(name == null)
        {
            name = "";
        }
        if(rollno == null)
        {
            rollno = "";
        }





        QUEUE_NAME = reciever;
        EXCHANGE_NAME = reciever;


        floatingActionButton = (FloatingActionButton) findViewById(R.id.sendButton);
        editTextMessage = (EditText) findViewById(R.id.messageBodyField);

        messagesList = (ListView) findViewById(R.id.listMessages);

        adapter = new MyListAdapter();
        messagesList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

Log.i("info","in messages");

        new FetchMessages().execute();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ChatService.isConnectingToInternet()) {
                    message = editTextMessage.getText().toString();

                    if(!message.equals("")) {
                        new send().execute();
                        adapter.notifyDataSetChanged();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connectivity", Toast.LENGTH_LONG).show();
                }
            }
        });


        activityReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {


                messages.add(intent.getStringExtra("message"));
                sendBy.add(intent.getStringExtra("fromEmail"));
                adapter.notifyDataSetChanged();
                messagesList.setSelection(messagesList.getAdapter().getCount() - 1);
            }
        };


        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter(ACTION_STRING_ACTIVITY);
            registerReceiver(activityReceiver, intentFilter);
        }
        Intent intentService = new Intent();
        intentService.setAction(ACTION_STRING_SERVICE);
        intentService.putExtra("status", "1");
        intentService.putExtra("email", reciever);          //check
        intentService.putExtra("taskId", taskId);
        sendBroadcast(intentService);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intentService = new Intent();
        intentService.setAction(ACTION_STRING_SERVICE);
        intentService.putExtra("status", "0");
        sendBroadcast(intentService);
        unregisterReceiver(activityReceiver);
    }


    private class MyListAdapter<A> extends ArrayAdapter<String> {

        public MyListAdapter() {
            super(getApplicationContext(), R.layout.message_left, messages);
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView;

        String sender = sendBy.get(position);
        if (sender.equals(email)) {
            itemView = getLayoutInflater().inflate(R.layout.message_right, parent, false);
        } else {
            itemView = getLayoutInflater().inflate(R.layout.message_left, parent, false);
        }
        TextView messageText = (TextView) itemView.findViewById(R.id.txtMessage);
        TextView messageSender = (TextView) itemView.findViewById(R.id.txtSender);
        messageSender.setText(sender);
        messageText.setText(messages.get(position));

        return itemView;
    }

}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }























    class send extends AsyncTask<String, String, String> {

        //JSONArray course = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Messages.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();

            params.put("task", taskId);
            params.put("emailT", reciever);
            params.put("emailF", email);
            params.put("message", message);


            Log.v("emailT", reciever);
            Log.v("emailF", email);
            Log.v("message", message);


            json = jsonParser.makeHttpRequest(new DefineUrl().send, "POST", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    status=json.getString("status");


                }
            } catch (Exception e) {
                //isFetched = -1;

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(status.equals("success"))
            {

        String publishmessage=reciever+"--"+email+"--"+message+"--"+"New Message"+"--"+taskId;

                new notification().execute(publishmessage);       //to from message title taskid
                messages.add(message);
                sendBy.add(email);
                adapter.notifyDataSetChanged();
                messagesList.setSelection(messagesList.getAdapter().getCount() - 1);
                editTextMessage.setText("");



            }
            else if(status.equals("error"))
            {
                try {
                    Toast.makeText(getApplicationContext(),json.getString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Server is not working. Please try again later.", Toast.LENGTH_SHORT).show();
            }


        }
    }







    private class notification extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Message) {
            try {

                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(DefineUrl.url_Host);
                factory.setUsername("kapish");
                factory.setPassword("kapish");
                factory.setPort(5672);
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                String tempstr = "";
                for (String aMessage : Message) tempstr += aMessage;

                channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null,
                        tempstr.getBytes());

                channel.close();

                connection.close();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            // TODO Auto-generated method stub
            return null;
        }

    }






    class FetchMessages extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Messages.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("task", taskId);


            Log.v("task", taskId);


            json = jsonParser.makeHttpRequest(new DefineUrl().fetchMessage, "GET", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    status=json.getString("status");


                }
            } catch (Exception e) {
                //isFetched = -1;

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(status.equals("success"))
            {

                try {
                    length=json.getString("length");
                } catch (Exception e) {

                }

                if(!length.equals(""))
                    len=Integer.parseInt(length);

                Log.i("Length", length);
                for(int i=0;i<len;i++)
                {



                    try {


                        messages.add(json.getString("message" + i));
                        sendBy.add(json.getString("send" + i));
                    } catch (Exception e) {

                    }


                }




            }
            else if(status.equals("error"))
            {
                try {
                    Toast.makeText(getApplicationContext(),json.getString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Server is not working. Please try again later.", Toast.LENGTH_SHORT).show();
            }


        }
    }






}
