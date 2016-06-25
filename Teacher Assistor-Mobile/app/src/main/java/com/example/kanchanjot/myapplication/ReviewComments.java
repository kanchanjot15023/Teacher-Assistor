package com.example.kanchanjot.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
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





public class ReviewComments extends AppCompatActivity {


    Button profile;
    Button add;

    Button switchto;

    Button updateSkill;
    Button notification;


    Button reviewComments;


    LinearLayout toolbarS;




    private ProgressDialog pDialog;
    JSONParser jsonParser;
    JSONObject json;


    LinearLayout.LayoutParams params,params1;


    LinearLayout linear,linear1;



    String email="";

    String name="";

    String rollno="";


    String status="";


    String length="";

    int len=0;



    String rating="";

    String from="";


    String title="";

    String comment="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewcomments);




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






        linear = (LinearLayout) findViewById(R.id.rating);

        params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);



        toolbarS = (LinearLayout) findViewById(R.id.toolbar);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);



        updateSkill=(Button)findViewById(R.id.button25);


        profile=(Button)findViewById(R.id.button23);

        reviewComments=(Button)findViewById(R.id.button26);

        notification=(Button)findViewById(R.id.button27);
        add=(Button)findViewById(R.id.button24);
        switchto=(Button)findViewById(R.id.button32);



        new FetchResults().execute();




        switchto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewComments.this, Dashboard.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });


        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewComments.this, Profile.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });




        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewComments.this, AddCourseTA.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });






        updateSkill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewComments.this, UpdateSkill.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        notification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewComments.this, Notification.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        reviewComments.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewComments.this, ReviewComments.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
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




    class FetchResults extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ReviewComments.this);
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


            json = jsonParser.makeHttpRequest(new DefineUrl().reviewComments, "GET", params);
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


                    LinearLayout ll = new LinearLayout(ReviewComments.this);
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    final Button btn = new Button(ReviewComments.this);
                    btn.setId(i);
                    final int id_ = btn.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        rating = json.getString("rating"+i);
                        from = json.getString("from"+i);
                        comment = json.getString("comment"+i);
                        title = json.getString("title"+i);
                        //final_str=title + "\n" +from;



                        //courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }
                    btn.setText(Html.fromHtml("<b><big>" + title + "</big></b>" + "<br />" +
                                    "<small>" +"<b>"+ "Rating:  "+"</b>"+rating +"/5"+ "</small>" + "<br />"+
                                    "<small>" + "<b>"+"Comment:  "+"</b>"+comment + "</small>" + "<br />"+
                            "<small>" + from + "</small>" + "<br />"));
                    btn.setBackgroundResource(R.drawable.button_transparent2);
                    btn.setGravity(Gravity.LEFT);
                    btn.setPadding(40,0,0,0);
                    btn.setWidth(480);
                    ll.addView(btn, params1);


                    TextView spaces = new TextView(ReviewComments.this);
                    spaces.setText(" ");
                    ll.addView(spaces);


                    Button btn2 = new Button(ReviewComments.this);
                    btn2.setId(i+200);
                    final int id1_ = btn2.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        rating = json.getString("rating"+i);
                        // courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }



                    linear.addView(ll);

                    TextView space = new TextView(ReviewComments.this);
                    space.setText("");
                    linear.addView(space, params1);
                    // btn = ((Button) findViewById(id_));
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Toast.makeText(view.getContext(),
                            //       "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            //      .show();




                        }
                    });






                }

                TextView space = new TextView(ReviewComments.this);
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








}
