package com.example.kanchanjot.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.Plus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kanchanjot on 25/03/16.
 */
public class Dashboard extends AppCompatActivity {




    Button profile;
    Button add;

    Button switchto;

    Button pendingReview;
    Button chats;

    String pname,email,rollno;

    TextView displayname;



    private ProgressDialog pDialog;
    JSONParser jsonParser;
    JSONObject json;


    String status="";


    int statuss=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);


        switchto=(Button)findViewById(R.id.button6);
        profile=(Button)findViewById(R.id.button7);
        add=(Button)findViewById(R.id.button16);

        displayname=(TextView)findViewById(R.id.textView3);

        pendingReview=(Button)findViewById(R.id.button8);
        chats=(Button)findViewById(R.id.button9);


        email = getIntent().getExtras().getString("email");
        pname=getIntent().getExtras().getString("name");
        rollno=getIntent().getExtras().getString("rollno");


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





        //if(pname)
        displayname.setText("HEY! "+pname);


        new HideTA().execute();


      // new FetchRollNo().execute();


        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Profile.class);
                intent.putExtra("email", email);
                intent.putExtra("name", pname);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });




        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, AddCourse.class);
                intent.putExtra("email",email);
                intent.putExtra("name",pname);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });


        switchto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, DashboardTA.class);
                intent.putExtra("email", email);
                intent.putExtra("name", pname);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });




        pendingReview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, PendingReview.class);
                intent.putExtra("email", email);
                intent.putExtra("name", pname);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        chats.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Chat.class);
                intent.putExtra("email", email);
                intent.putExtra("name", pname);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



    }




    @Override
    protected void onRestart()
    {
        super.onRestart();

        Log.i("info","Onrestart");


        }






    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Log.i("info", "Onback");

    }


    @Override
    protected void onStop()
    {
        super.onStop();


        Log.i("info", "OnStop");


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











    class HideTA extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Dashboard.this);
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


            json = jsonParser.makeHttpRequest(new DefineUrl().hideTA, "GET", params);
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
            if(status.equals("0"))
            {

                switchto.setVisibility(View.INVISIBLE);


            }
            else if(status.equals("1"))
            {

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Server is not working. Please try again later.", Toast.LENGTH_SHORT).show();
            }


        }
    }








}

