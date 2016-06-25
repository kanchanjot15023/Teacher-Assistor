package com.example.kanchanjot.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
 * Created by kanchanjot on 25/03/16.
 */
public class Notification extends AppCompatActivity {



    Button profile;
    Button add;

    Button switchto;

    Button updateSkill;
    Button notification;


    Button reviewComments;


    LinearLayout toolbarS;



    String email="";
    String name="";
    String rollno="";


    String status="";

    private ProgressDialog pDialog;
    JSONParser jsonParser;
    JSONObject json;


    String length="";
    int len=0;



    LinearLayout linear,linear1;
    LinearLayout.LayoutParams params,params1;


    String codeFetched="";

    String title="";
    String from="";


    String taskId="";

    String titleFetched="";


    private String QUEUE_NAME;
    private String EXCHANGE_NAME;


    String metaData="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);



        email = getIntent().getExtras().getString("email");
        name = getIntent().getExtras().getString("name");
        rollno = getIntent().getExtras().getString("rollno");



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





        linear = (LinearLayout) findViewById(R.id.notify);
        //linear1 = (LinearLayout) findViewById(R.id.partb);
        params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);







        toolbarS = (LinearLayout) findViewById(R.id.toolbar);




        updateSkill=(Button)findViewById(R.id.button25);


        profile=(Button)findViewById(R.id.button23);

        reviewComments=(Button)findViewById(R.id.button26);

        notification=(Button)findViewById(R.id.button27);
        add=(Button)findViewById(R.id.button24);
        switchto=(Button)findViewById(R.id.button34);








        switchto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, Dashboard.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });


        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, Profile.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });




        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, AddCourseTA.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });






        updateSkill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, UpdateSkill.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        notification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, Notification.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        reviewComments.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, ReviewComments.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });








        new FetchResults().execute();

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






    class FetchResults extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Notification.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", email);



            Log.v("email", email);


            json = jsonParser.makeHttpRequest(new DefineUrl().notification, "GET", params);
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


                    LinearLayout ll = new LinearLayout(Notification.this);
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    final Button btn = new Button(Notification.this);
                    btn.setId(i);
                    final int id_ = btn.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        title = json.getString("title"+i);
                        from = json.getString("from"+i);



                        taskId=json.getString("taskId"+i);

                        metaData=taskId+"--"+title;
                        //courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }
                    btn.setText(Html.fromHtml("<b><big>" + title + "</big></b>" + "<br />" +
                            "<small>" + from + "</small>" + "<br />"));
                    btn.setBackgroundResource(R.drawable.button_transparent2);
                    btn.setTag(metaData);
                    btn.setWidth(480);
                    ll.addView(btn, params1);


                    TextView spaces = new TextView(Notification.this);
                    spaces.setText(" ");
                    spaces.setHeight(2);
                    //ll.addView(spaces);


                    Button btn2 = new Button(Notification.this);
                    btn2.setId(i+200);
                    final int id1_ = btn2.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        title = json.getString("title"+i);
                        // courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }

                    btn2.setText("End Discussion");
                    btn2.setBackgroundResource(R.drawable.button_transparent2);
                    //btn2.setBackgroundResource(R.drawable.trash);             //change here




                    linear.addView(ll);


                    linear.addView(btn2, params1);
                    linear.addView(spaces, params1);


                    TextView space = new TextView(Notification.this);
                    space.setText("");
                    linear.addView(space, params1);
                    // btn = ((Button) findViewById(id_));
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Toast.makeText(view.getContext(),
                            //       "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            //      .show();

                            String task=btn.getTag().toString().split("--")[0];

                            Intent intent = new Intent(Notification.this, Messages.class);
                            intent.putExtra("rollno", rollno);
                            intent.putExtra("taskId", task);
                            intent.putExtra("email", email);
                            intent.putExtra("name", name);
                            intent.putExtra("reciever",from);
                            startActivity(intent);


                        }
                    });


                    btn2 = ((Button) findViewById(id1_));
                    btn2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {


                            //end discussion here


                            codeFetched=btn.getTag().toString().split("--")[0];
                            titleFetched=btn.getTag().toString().split("--")[1];

                            Toast.makeText(getApplicationContext(), codeFetched, Toast.LENGTH_SHORT).show();
                            new RemoveCode().execute();





                        }
                    });



                }






                TextView space = new TextView(Notification.this);
                space.setText("");
                linear.addView(space, params1);
                //changes need to be made here



                // Intent i= new Intent(SignIn.this,Dashboard.class);
                //i.putExtra("email", );

                //i.putExtra("name", name); //get the name here
                //startActivity(i);
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



    class RemoveCode extends AsyncTask<String, String, String> {

        //JSONArray course = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Notification.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            //params.put("email", email);
            params.put("codeTask", codeFetched);          // change here


            Log.v("email", email);
            Log.v("codeFetched", codeFetched);


            json = jsonParser.makeHttpRequest(new DefineUrl().finished, "POST", params);
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



                QUEUE_NAME = from;
                EXCHANGE_NAME = from;

                String publishmessage=from+"--"+email+"--"+titleFetched+"--"+"Rate the TA"+"--"+"NULL";

                new notification().execute(publishmessage);       //to from message title  NULL

                Toast.makeText(getApplicationContext(),"Discussion ended",Toast.LENGTH_SHORT).show();
                linear.removeAllViews();



                new FetchResults().execute();

                //changes need to be made here



                // Intent i= new Intent(SignIn.this,Dashboard.class);
                //i.putExtra("email", );

                //i.putExtra("name", name); //get the name here
                //startActivity(i);
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




}
