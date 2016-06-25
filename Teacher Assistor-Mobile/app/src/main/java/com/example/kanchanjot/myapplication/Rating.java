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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
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
import java.util.List;

/**
 * Created by kanchanjot on 28/03/16.
 */
public class Rating extends AppCompatActivity {


    RatingBar ratingBar;

    String ratedValue="";

    Button submit;

    String comments_str;

    EditText comments;

    CheckBox showToTa;

    String status;


    private ProgressDialog pDialog;
    JSONParser jsonParser;
    JSONObject json;



    String email="";

    String name="";

    String rollno="";

    String emailT="";

    String emailF="";
    String taskId="";


    String recommended_str="";


    EditText dropdown;


    String length="";

    int len=0;

    String course;

    ArrayList<String> skill=new ArrayList<String>();

    List<String> skillList = new ArrayList<>();



    private String QUEUE_NAME;
    private String EXCHANGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);



        email = getIntent().getExtras().getString("email");
        name = getIntent().getExtras().getString("name");
        rollno = getIntent().getExtras().getString("rollno");
        emailT= getIntent().getExtras().getString("emailT");
        emailF = getIntent().getExtras().getString("emailF");
        taskId = getIntent().getExtras().getString("taskId");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);



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






        QUEUE_NAME = emailT;
        EXCHANGE_NAME = emailT;


        submit=(Button)findViewById(R.id.button29);

        comments=(EditText)findViewById(R.id.editText6);



        course=taskId.split("-")[0];

        showToTa = (CheckBox) findViewById(R.id.checkBox);




        skillList.add("Choose Skill Set");

        dropdown = (EditText)findViewById(R.id.editText7);




        //dropdown.setPrompt("Choose One");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, skillList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        //dropdown.setAdapter(dataAdapter);







        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                int value = (int) Math.ceil(ratingBar.getRating());
                ratedValue = String.valueOf(value);
                Toast.makeText(getApplicationContext(), ratedValue, Toast.LENGTH_SHORT).show();
            }
        });




recommended_str=dropdown.getText().toString();





        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                comments_str=comments.getText().toString();
                recommended_str=dropdown.getText().toString();

                if(comments_str.equals("")||recommended_str.equals("")||ratedValue.equals(""))

                    Toast.makeText(getApplicationContext(), "All Fields Not Provided", Toast.LENGTH_SHORT).show();


                else
                {

                    if(showToTa.isChecked()== true)
                        new ShowComments().execute();

                        else

                        new DontShowComments().execute();

                }


            }

        });




    }







    public void removeduplicates()
    {

        for(int i=0;i<skill.size();i++) {
            if(!skillList.contains(skill.get(i)))
                skillList.add(skill.get(i));
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






    class DontShowComments extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Rating.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();

            HashMap<String, String> params1 = new HashMap<String, String>();


            params1.put("taskId", taskId);
            params1.put("emailT",emailT);
            params1.put("emailF",emailF);
            params1.put("rating", ratedValue);
            params1.put("comment", comments_str);
            params1.put("recommended",recommended_str.toUpperCase());
            params1.put("show","false");


            json = jsonParser.makeHttpRequest(new DefineUrl().dontShowComments, "POST", params1);
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


                Intent intent = new Intent(Rating.this, PendingReview.class);
                intent.putExtra("rollno",rollno);

                intent.putExtra("email", email);
                intent.putExtra("name",name );


                startActivity(intent);



            }
            else if(status.equals("error"))
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











    class ShowComments extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Rating.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();

            HashMap<String, String> params1 = new HashMap<String, String>();


            params1.put("taskId", taskId);
            params1.put("emailT",emailT);
            params1.put("emailF",emailF);
            params1.put("rating", ratedValue);
            params1.put("comment", comments_str);
            params1.put("recommended",recommended_str.toUpperCase());
            params1.put("show","true");



            json = jsonParser.makeHttpRequest(new DefineUrl().showComments, "POST", params1);
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



                String publishmessage=emailT+"--"+emailF+"--"+"By "+emailF+"--"+"New Rating"+"--"+"NULL";

                new notification().execute(publishmessage);       //to from message title   NULL



                Intent intent = new Intent(Rating.this, PendingReview.class);
                intent.putExtra("rollno",rollno);

                intent.putExtra("email", email);
                intent.putExtra("name",name );


                startActivity(intent);

            }
            else if(status.equals("error"))
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
