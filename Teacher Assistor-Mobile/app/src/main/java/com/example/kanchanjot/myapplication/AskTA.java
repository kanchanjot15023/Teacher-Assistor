package com.example.kanchanjot.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kanchanjot on 28/03/16.
 */
public class AskTA extends AppCompatActivity {


    Button submit;
    EditText title;
    EditText description;

    String email="";
    String str_email="";

    private ProgressDialog pDialog;
    JSONParser jsonParser;

    JSONObject json;

    String statuss="";

            int length=0;


    String title_str;
    String description_str;



    TextView error;

    String pname="";
    String rollno="";
    String emailF="";
    String emailT="";
    String course="";



    private String QUEUE_NAME;
    private String EXCHANGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_ta);



        submit=(Button)findViewById(R.id.button28);

        title=(EditText)findViewById(R.id.editText4);
        description=(EditText)findViewById(R.id.editText5);


        error=(TextView)findViewById(R.id.textView23);

        email = getIntent().getExtras().getString("email");
        pname = getIntent().getExtras().getString("name");
        rollno = getIntent().getExtras().getString("rollno");
        emailF = getIntent().getExtras().getString("emailF");
        emailT = getIntent().getExtras().getString("emailT");
        course = getIntent().getExtras().getString("course");


        QUEUE_NAME = emailT;
        EXCHANGE_NAME = emailT;

        if(email == null)
        {
            email = "";
        }
        if(pname == null)
        {
            pname = "";
        }
        if(rollno == null)
        {
            rollno = "";
        }


        str_email=email;







        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                title_str = title.getText().toString();
                description_str = description.getText().toString();



                if (title_str.equals("") || description_str.equals(""))
                    error.setText("All Fields Not Provided ");

                else
                    new AskTa().execute();
            }

        });


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



    class AskTa extends AsyncTask<String, String, String> {

        //JSONArray course = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(AskTA.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();


            params.put("course",course);
            params.put("emailF",emailF);
            params.put("emailT",emailT);
            params.put("title",title_str);
            params.put("description",description_str);




            json = jsonParser.makeHttpRequest(new DefineUrl().askTA, "POST", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    statuss=json.getString("status");


                }
            } catch (Exception e) {
                //isFetched = -1;

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(statuss.equals("success"))
            {

                //Go to next question

                // onSubmit();

                String publishmessage=emailT+"--"+emailF+"--"+title_str+"--"+"New Task in "+course+"--"+"NULL";

                new notification().execute(publishmessage);       //to from message title NULL


                Toast.makeText(getApplicationContext(), "Query send to TA", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AskTA.this,ViewTA.class);
                intent.putExtra("email", email);
                intent.putExtra("name", pname);
                intent.putExtra("rollno", rollno);
                intent.putExtra("course", course);
                startActivity(intent);


            }
            else if(statuss.equals("error"))
            {
                try {
                    Toast.makeText(getApplicationContext(), json.getString("msg"), Toast.LENGTH_SHORT).show();
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




}
