package com.example.kanchanjot.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kanchanjot on 28/03/16.
 */
public class Chat extends AppCompatActivity {


    LinearLayout toolbarS;
    LinearLayout.LayoutParams params,params1;


    Button profile;
    Button add;
    Button pendingReview;
    Button chats;

    Button switchto;


    String email="";
    String name="";
    String rollno="";



    private ProgressDialog pDialog;
    JSONParser jsonParser;
    JSONObject json;
    String code;



    String status="";

    String length="";
    int len=0;



    LinearLayout linear,linear1;


    String title="";
    String from="";
    String final_str="";

    String taskId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);


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







        linear = (LinearLayout) findViewById(R.id.chat);

        params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        toolbarS = (LinearLayout) findViewById(R.id.toolbar);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        profile=(Button)findViewById(R.id.button20);
        add=(Button)findViewById(R.id.button19);
        pendingReview=(Button)findViewById(R.id.button22);
        chats=(Button)findViewById(R.id.button21);
        switchto=(Button)findViewById(R.id.button30);




        switchto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat.this, DashboardTA.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });




        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat.this, Profile.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });




        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat.this, AddCourse.class);
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });







        pendingReview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat.this, PendingReview.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        chats.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat.this, Chat.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });




        new HideTA().execute();


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

            pDialog = new ProgressDialog(Chat.this);
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


            json = jsonParser.makeHttpRequest(new DefineUrl().chat, "GET", params);
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


                    LinearLayout ll = new LinearLayout(Chat.this);
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    final Button btn = new Button(Chat.this);
                    btn.setId(i);
                    final int id_ = btn.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        title = json.getString("title"+i);
                        from = json.getString("from"+i);

                        final_str=title + "\n" +from;

                        taskId=json.getString("taskId"+i);
                        //courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }
                    btn.setText(Html.fromHtml("<b><big>" + title + "</big></b>" + "<br />" +
                            "<small>" + from + "</small>" + "<br />"));
                    btn.setBackgroundResource(R.drawable.button_transparent1);
                    btn.setTag(taskId);
                    btn.setWidth(480);
                    ll.addView(btn, params1);


                    TextView spaces = new TextView(Chat.this);
                    spaces.setText(" ");
                    ll.addView(spaces);


                    Button btn2 = new Button(Chat.this);
                    btn2.setId(i+200);
                    final int id1_ = btn2.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        title = json.getString("title"+i);
                        // courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }



                    linear.addView(ll);

                    TextView space = new TextView(Chat.this);
                    space.setText("");
                    linear.addView(space, params1);
                    // btn = ((Button) findViewById(id_));
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Toast.makeText(view.getContext(),
                            //       "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            //      .show();


                            Intent intent = new Intent(Chat.this, Messages.class);
                            intent.putExtra("rollno",rollno);
                            intent.putExtra("taskId",taskId);
                            intent.putExtra("email", email);
                            intent.putExtra("name",name);
                            intent.putExtra("reciever",from);
                            startActivity(intent);


                        }
                    });






                }

                TextView space = new TextView(Chat.this);
                space.setText("");
                linear.addView(space, params);
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






    class HideTA extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Chat.this);
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


            new FetchResults().execute();

        }
    }





}
