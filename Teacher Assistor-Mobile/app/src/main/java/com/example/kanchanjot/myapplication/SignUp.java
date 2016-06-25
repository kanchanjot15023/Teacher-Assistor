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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kanchanjot on 25/03/16.
 */
public class SignUp extends AppCompatActivity {

    TextView error;
    EditText name;
    EditText email;
    EditText confrimpassword;
    EditText password;

    EditText rollNo;

    Button submit;



    String str_email,str_password,str_name,str_confrimpassword,str_rollNo;



    String email_intent,name_intent;

    private ProgressDialog pDialog;
    JSONParser jsonParser;



    String msg="";




    String status="0";



    int status1=0;


    public String getName() {
        return str_name.toUpperCase();
    }

    public String getEmail() {
        return str_email.toUpperCase();
    }



    public String getPassword() {
        return str_password.toUpperCase();
    }

    public String getRollNo() {
        return str_rollNo.toUpperCase();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();

        email.setText("");
        password.setText("");
        name.setText("");
        confrimpassword.setText("");
        rollNo.setText("");
    }




    //check for if the request is comming via the gmail thing.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);


        status= getIntent().getExtras().getString("status");
        email_intent= getIntent().getExtras().getString("email");
        name_intent= getIntent().getExtras().getString("name");

        error=(TextView)findViewById(R.id.textView);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.Password);
        name=(EditText)findViewById(R.id.name);
        confrimpassword=(EditText)findViewById(R.id.Cpassword);
        rollNo=(EditText)findViewById(R.id.rollNo);

        submit=(Button)findViewById(R.id.addbutton);


if(status.equals("1"))
{
    email.setText(email_intent);
    name.setText(name_intent);
    password.setVisibility(View.GONE);
    confrimpassword.setVisibility(View.GONE);




    status1=1;




}




        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if(status1==1)
                {
                    str_email = email.getText().toString();
                    str_name = name.getText().toString();
                    str_rollNo = rollNo.getText().toString();


                    if (str_email.equals("") || str_name.equals("") || str_rollNo.equals(""))
                        error.setText("All Fields Not Provided ");



                    else {

                        if (str_email.endsWith("@IIITD.AC.IN"))


                                new UpdateRollNo().execute();


                        else {
                            error.setText("Email Incorrect");
                            email.setText("");
                        }
                    }










                    status1=0;
                }


                else
                {
                str_email = email.getText().toString();
                str_password = password.getText().toString();
                str_confrimpassword = confrimpassword.getText().toString();
                str_name = name.getText().toString();
                str_rollNo = rollNo.getText().toString();


                if (str_password.equals("") || str_email.equals("") || str_confrimpassword.equals("") || str_name.equals("") || str_rollNo.equals(""))
                    error.setText("All Fields Not Provided ");

                else {

                    if (str_email.endsWith("@gmail.com") || str_email.endsWith("@gmail.co.in"))
                        if (str_password.equals(str_confrimpassword)) {
                            //send details to the server if we get a confrim msg then do this


                            new Results().execute();

                            //Intent i = new Intent(SignUp.this, SignIn.class);
                            //i.putExtra("email",str_email);
                            //startActivity(i);
                        } else {
                            error.setText("Password Incorrect");
                            password.setText("");
                            confrimpassword.setText("");
                        }
                    else {
                        error.setText("Email Incorrect");
                        email.setText("");
                    }
                }

            }
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












    class Results extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUp.this);
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
            params.put("name", getName());
            params.put("password", getPassword());
            params.put("rollno", getRollNo());
           // params.put("registerationId", getRegistration());
            Log.v("email", getEmail());
            Log.v("name", getName());
            Log.v("password", getPassword());
            JSONObject json = jsonParser.makeHttpRequest(new DefineUrl().signup, "POST", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());


                    msg=json.getString("msg");


                }
            } catch (Exception e) {
                //isFetched = -1;

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if(msg.equals("success"))
            {
                Intent i = new Intent(SignUp.this, SignIn.class);
                i.putExtra("email",str_email.toUpperCase());
                startActivity(i);
                name.setText("");
                email.setText("");
                password.setText("");
                confrimpassword.setText("");
                rollNo.setText("");
                error.setText("/");
            }
            else if(msg.equals("error"))
            {
                error.setText("Email already present");
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Server is not working. Please try again later.", Toast.LENGTH_SHORT).show();
            }


        }
    }





    class UpdateRollNo extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUp.this);
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
            params.put("rollno", getRollNo());

            Log.v("email", getEmail());
            Log.v("name", getName());
            Log.v("name", getRollNo());
            JSONObject json = jsonParser.makeHttpRequest(new DefineUrl().signuproll, "POST", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());


                    msg=json.getString("msg");


                }
            } catch (Exception e) {
                //isFetched = -1;

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if(msg.equals("success"))
            {
                Intent i = new Intent(SignUp.this, Dashboard.class);
                i.putExtra("email", str_email.toUpperCase());
                i.putExtra("name", str_name.toUpperCase());
                i.putExtra("rollno", str_rollNo.toUpperCase());
                startActivity(i);
                name.setText("");
                email.setText("");
                rollNo.setText("");
                error.setText("/");


                ChatService.EXCHANGE_NAME = str_email.toUpperCase();
                ChatService.QUEUE_NAME    = str_email.toUpperCase();
                Intent i1= new Intent(getApplicationContext(), ChatService.class);
                i1.putExtra("id", str_email.toUpperCase());
                i.putExtra("name", str_name.toUpperCase());
                i.putExtra("rollno", str_rollNo.toUpperCase());
                getApplicationContext().startService(i1);


            }
            else if(msg.equals("error"))
            {
                error.setText("Email already present");
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Server is not working. Please try again later.", Toast.LENGTH_SHORT).show();
            }


        }
    }










}

