package com.example.kanchanjot.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class AddCourse extends AppCompatActivity {



    LinearLayout toolbarS;



    final Context context = this;
    private ProgressDialog pDialog;
    JSONParser jsonParser;
    JSONObject json;
    String code;

    LinearLayout linear,linear1;
    LinearLayout.LayoutParams params,params1;


    String email="";
    String str_email="";


    String name="";
    String str_name="";

    String status="";

    String length="";
    int len=0;

    String courses="";

    String codeFetched="";

    String rollno="";



    Button profile;
    Button add1;
    Button pendingReview;
    Button chats;
    Button switchto;


    public String getEmail() {

        //Toast.makeText(getApplicationContext(),email,Toast.LENGTH_SHORT).show();
        return str_email;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcourse);



        Button add;
        add=(Button)findViewById(R.id.addbutton);


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

            str_email=email;


            str_name=name;





        linear = (LinearLayout) findViewById(R.id.display);
        //linear1 = (LinearLayout) findViewById(R.id.partb);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        toolbarS = (LinearLayout) findViewById(R.id.toolbar);




        profile=(Button)findViewById(R.id.button20);
        add1=(Button)findViewById(R.id.button19);
        pendingReview=(Button)findViewById(R.id.button22);
        chats=(Button)findViewById(R.id.button21);
        switchto=(Button)findViewById(R.id.button18);




        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCourse.this, Profile.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });




        add1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCourse.this, AddCourse.class);
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });







        pendingReview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCourse.this, PendingReview.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        chats.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCourse.this, Chat.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });


        switchto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCourse.this, DashboardTA.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });


        new HideTA().execute();
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                //startid = 0;
                //Toast.makeText(getApplicationContext(), "in send", Toast.LENGTH_SHORT).show();

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);


                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editText);


                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        code = (userInput.getText().toString());
                                        if (code.equals(""))
                                            Toast.makeText(getApplicationContext(), "No code Entered", Toast.LENGTH_SHORT).show();
                                        else {
                                            code = code.toUpperCase();
                                            //Toast.makeText(getApplicationContext(), code, Toast.LENGTH_SHORT).show();
                                            new Add().execute();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                AlertDialog alertDialog = alertDialogBuilder.create();


                alertDialog.show();


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

            pDialog = new ProgressDialog(AddCourse.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", getEmail());



            Log.v("email", getEmail());


            json = jsonParser.makeHttpRequest(new DefineUrl().profile, "GET", params);
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


                    LinearLayout ll = new LinearLayout(AddCourse.this);
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    final Button btn = new Button(AddCourse.this);
                    btn.setId(i);
                    final int id_ = btn.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        courses = json.getString("course"+i);
                        //courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }
                    btn.setText(courses);
                    btn.setBackgroundColor(Color.rgb(135, 201, 188));
                    btn.setWidth(480);
                    ll.addView(btn, params);


                    TextView spaces = new TextView(AddCourse.this);
                    spaces.setText(" ");
                    ll.addView(spaces);


                    Button btn2 = new Button(AddCourse.this);
                    btn2.setId(i+200);
                    final int id1_ = btn2.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        courses = json.getString("course"+i);
                        // courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }
                    btn2.setText(" ");
                    //btn2.setBackgroundResource(R.drawable.trash);             //change here
                    ll.addView(btn2,params);


                    linear.addView(ll);

                    TextView space = new TextView(AddCourse.this);
                    space.setText("");
                    linear.addView(space, params);
                    // btn = ((Button) findViewById(id_));
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Toast.makeText(view.getContext(),
                            //       "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            //      .show();

                            Intent intent = new Intent(AddCourse.this, ViewTA.class);
                            intent.putExtra("course", btn.getText().toString());
                            intent.putExtra("email", getEmail());
                            intent.putExtra("name",str_name );
                            intent.putExtra("rollno",rollno );
                            startActivity(intent);


                        }
                    });



                    btn2 = ((Button) findViewById(id1_));
                    btn2.setBackgroundResource(R.drawable.trashstudent);
                    btn2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {


                            codeFetched=btn.getText().toString();

                            //  Toast.makeText(view.getContext(),
                            //        "Code Fetched = " + codeFetched, Toast.LENGTH_SHORT)
                            //      .show();
                            new RemoveCode().execute();

                            //Intent intent=new Intent(Addcourse.this,QuizList.class);
                            //intent.putExtra("email",getEmail());
                            //startActivity(intent);




                        }
                    });


                }

                TextView space = new TextView(AddCourse.this);
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









    class Add extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddCourse.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();

            HashMap<String, String> params1 = new HashMap<String, String>();
            params1.put("email", getEmail());
            params1.put("course",code);
            Log.v("email", getEmail());
            Log.v("course",code);
            json = jsonParser.makeHttpRequest(new DefineUrl().add, "POST", params1);
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

                linear.removeAllViews();
                new FetchResults().execute();




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

            pDialog = new ProgressDialog(AddCourse.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", getEmail());
            params.put("course", codeFetched);


            Log.v("email", getEmail());
            Log.v("email", codeFetched);


            json = jsonParser.makeHttpRequest(new DefineUrl().remove, "POST", params);
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




    class HideTA extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(AddCourse.this);
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
