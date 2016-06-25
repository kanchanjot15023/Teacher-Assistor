package com.example.kanchanjot.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    Button googlesignin;

    Button signin,signup;



    private ProgressDialog pDialog;
    JSONParser jsonParser;

    private static final String TAG = "SignIn Log";

    private static final int RC_SIGN_IN = 0;
    private static final int RC_PERM_GET_ACCOUNTS = 2;

    private GoogleApiClient mGoogleApiClient;



    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;


    Person currentPerson;

    String email="";


    String personName="";



    String regid;

    GoogleCloudMessaging gcm;


    JSONObject json;

    String status="";


    String rollno="";


    public String getName() {
        return personName.toUpperCase();
    }

    public String getEmail() {
        return email.toUpperCase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);











        googlesignin=(Button)findViewById(R.id.button3);


        signin=(Button)findViewById(R.id.button2);

        signup=(Button)findViewById(R.id.button);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();



        signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i= new Intent(MainActivity.this,SignIn.class);
                i.putExtra("email","");
                startActivity(i);

            }

        });





        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i= new Intent(MainActivity.this,SignUp.class);
                i.putExtra("email","");
                i.putExtra("name","");
                i.putExtra("status","0");
                startActivity(i);

            }

        });



        googlesignin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onSignInClicked();
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








    @Override
    protected void onRestart()
    {
        super.onRestart();


        Log.i("ONRESTART", "ONRESTART");

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            Toast.makeText(getApplicationContext(), "Logging Out..", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getApplicationContext(), "Logging Out..", Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }






    @Override
    protected void onStop()
    {
        super.onStop();


        Log.i("ONSTOP", "ONSTOP");

    }



    @Override
    protected void onStart() {
        super.onStart();

        Log.i("ONSTART", "ONSTART");

        //mGoogleApiClient.connect();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("ONDESTROY", "ONDESTROY");

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }





    public static boolean isUserOnline(Context context) {
        try {
            ConnectivityManager nConManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (nConManager != null) {
                NetworkInfo nNetworkinfo = nConManager.getActiveNetworkInfo();

                if (nNetworkinfo != null) {
                    return nNetworkinfo.isConnected();
                }
            }
        } catch (Exception e) {
        }
        return false;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult:" + requestCode);
        if (requestCode == RC_PERM_GET_ACCOUNTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                Toast.makeText(getApplicationContext(), "%%" + currentPerson, Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "GET_ACCOUNTS Permission Denied.");
            }
        }
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }



    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a toast to the user that we are signing in.
        Toast toast = Toast.makeText(getApplicationContext(), "Signing In", Toast.LENGTH_SHORT);
        toast.show();
    }




















    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            }
        }


    }







    class Results extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
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

            Log.v("email", getEmail());

           Log.v("name", getName());
            json = jsonParser.makeHttpRequest(new DefineUrl().googlesignin, "POST", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    status=json.getString("status");


                }
            } catch (Exception e) {
                //isFetched = -1;




                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    Toast.makeText(getApplicationContext(), "Logging Out..", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), "Logging Out..", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(status.equals("success"))
            {



                Intent intent = new Intent(MainActivity.this, SignUp.class);
                intent.putExtra("email", email.toUpperCase());
                intent.putExtra("name",personName.toUpperCase());
                intent.putExtra("status","1");

                startActivity(intent);

            }
            else if(status.equals("error"))
            {

                try {
                    rollno=json.getString("rollno");
                } catch (Exception e) {

                }

                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                intent.putExtra("email", email.toUpperCase());
                intent.putExtra("name",personName.toUpperCase());
                intent.putExtra("rollno", rollno.toUpperCase());
                startActivity(intent);


                ChatService.EXCHANGE_NAME = email.toUpperCase();
                ChatService.QUEUE_NAME    = email.toUpperCase();
                Intent i= new Intent(getApplicationContext(), ChatService.class);
                i.putExtra("id", email.toUpperCase());
                i.putExtra("name", personName.toUpperCase());
                i.putExtra("rollno", rollno.toUpperCase());
                getApplicationContext().startService(i);

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Server is not working. Please try again later.", Toast.LENGTH_SHORT).show();
            }


        }
    }







    @Override
    public void onConnected(Bundle bundle) {




        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;



        email = Plus.AccountApi.getAccountName(mGoogleApiClient);



        //code for access token

        /*
        AsyncTask<Void, Void, String > task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                final String SCOPES = "https://www.googleapis.com/auth/userinfo.profile";

                try {
                    token = GoogleAuthUtil.getToken(
                            getApplicationContext(),
                            Plus.AccountApi.getAccountName(mGoogleApiClient),
                            "oauth2:" + SCOPES);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }


                return token;

            }

            @Override
            protected void onPostExecute(String token) {
                Log.v("INFO", "Access token retrieved:" + token);
            }

        };


*/





        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null)
        {
            if(email.endsWith("iiitd.ac.in")) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                personName = currentPerson.getDisplayName();
                //Toast.makeText(this, personName, Toast.LENGTH_SHORT).show();
            }

            else
            {
                Toast.makeText(this, "Signin with iiitd account", Toast.LENGTH_SHORT).show();

                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
            }


        }
        else {

        }

        Log.d("email", email);

        String domain = "iiitd.ac.in";
        String domain1 ="gmail.com";
        if (!isUserOnline(this)) {
            //toast("No Internet Connection");
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_LONG).show();

            Log.i("noti::::","no net");
/////-------
            if (mGoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
            }


            mGoogleApiClient.connect();
//////------
            //onStart();
            //  mGoogleApiClient.connect();
            //onPause();
            //onResume();
//            this.onCreate(savedInstanceState1);
        }
        else{
            if(!email.split("@")[1].equals(domain)){
                // Clear the default account so that GoogleApiClient will not automatically
                // connect in the future.
                if (mGoogleApiClient.isConnected()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please use iiitd account", Toast.LENGTH_SHORT);
                    toast.show();
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
            }
            else{
                // Open the Geofencing Activity
                //task.execute();

                new Results().execute();

            }

        }





    }











}
