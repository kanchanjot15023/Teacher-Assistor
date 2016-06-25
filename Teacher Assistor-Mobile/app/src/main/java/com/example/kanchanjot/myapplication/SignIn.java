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
public class SignIn extends AppCompatActivity {


    TextView error;
    EditText email;
    EditText password;

    String str_email,str_password;
    String recieved_email;

    Button signIn;


    private ProgressDialog pDialog;
    JSONParser jsonParser;

    JSONObject json;

    String status="";

    String name="abc";
            String rollno="123";

    public String getEmail() {
        return str_email.toUpperCase();
    }



    public String getPassword() {
        return str_password.toUpperCase();
    }



    @Override
    public void onRestart()
    {
        super.onRestart();

        email.setText("");
        password.setText("");
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);




        error=(TextView)findViewById(R.id.textView2);
        email=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);

        signIn=(Button)findViewById(R.id.button5);

        recieved_email = getIntent().getExtras().getString("email");
        email.setText(recieved_email);



        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                str_email = email.getText().toString();
                str_password = password.getText().toString();

                if (str_password.equals("") || str_email.equals(""))
                    error.setText("All Fields Not Provided ");

                else {
                    //get the name from the server and send it



                    new Results().execute();

                    //Intent i= new Intent(SignIn.this,Dashboard.class);
                    //i.putExtra("email", str_email);

                    //i.putExtra("name", ""); //get the name here
                    //startActivity(i);
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
            pDialog = new ProgressDialog(SignIn.this);
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

            params.put("password", getPassword());

            Log.v("email", getEmail());

            Log.v("password", getPassword());
            json = jsonParser.makeHttpRequest(new DefineUrl().signin, "GET", params);
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
                    name=json.getString("name");
                    rollno=json.getString("rollno");
                } catch (Exception e) {

                }
                Intent i= new Intent(SignIn.this,Dashboard.class);
                i.putExtra("email", str_email.toUpperCase());

                i.putExtra("name", name.toUpperCase()); //get the name here
                i.putExtra("rollno", rollno.toUpperCase());
                startActivity(i);



                ChatService.EXCHANGE_NAME = str_email.toUpperCase();
                ChatService.QUEUE_NAME    = str_email.toUpperCase();
                Intent i1= new Intent(getApplicationContext(), ChatService.class);
                i1.putExtra("id", str_email.toUpperCase());
                i.putExtra("name", name.toUpperCase());
                i.putExtra("rollno", rollno.toUpperCase());
               getApplicationContext().startService(i1);


            }
            else if(status.equals("error"))
            {
                try {
                    error.setText(json.getString("msg"));
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
