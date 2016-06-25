package com.example.kanchanjot.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kanchanjot on 28/03/16.
 */
public class ViewTA extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


String email="";
    String pname="";
    String course="";
    String rollno="";


    private ProgressDialog pDialog;
    JSONParser jsonParser;

    JSONObject json;


    String status;

    String length="";
    int len;


    String length_recommended="";
    int len_recommended=0;

    String tas="";


    LinearLayout linear,linear1;
    LinearLayout.LayoutParams params,params1;


    Spinner dropdown;


    ArrayList<String> skill=new ArrayList<String>();

    List<String> skillList = new ArrayList<>();


    String code_skill="";


    String metaData="";

    String data="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tapage);

        course = getIntent().getExtras().getString("course");
        email = getIntent().getExtras().getString("email");
        pname=getIntent().getExtras().getString("name");
        rollno=getIntent().getExtras().getString("rollno");

        skillList.add("Choose Skill Set");

       dropdown = (Spinner)findViewById(R.id.spinner);
        dropdown.setOnItemSelectedListener(this);

        linear = (LinearLayout) findViewById(R.id.ta);
        //linear1 = (LinearLayout) findViewById(R.id.partb);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);





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






        Log.i("skill", Integer.toString(skillList.size()));
        Log.i("skill", Integer.toString(skill.size()));

        for(int i=0;i<skillList.size();i++)
        {
            Log.i("skill",skillList.get(i));
        }


        dropdown.setPrompt("Choose One");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, skillList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        dropdown.setAdapter(dataAdapter);






        new FetchSkill().execute();

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        if(parent.getChildAt(0)!= null)
        ((TextView) parent.getChildAt(0)).setTextSize(20);
        code_skill = parent.getItemAtPosition(position).toString();


        if (!code_skill.equals("Choose Skill Set")) {// Showing selected spinner item
            Toast.makeText(ViewTA.this, "Selected: " + code_skill, Toast.LENGTH_LONG).show();


            if(code_skill.equals("Select All"))
            {
                linear.removeAllViews();
                new FetchAllResults().execute();
            }
            else {
                linear.removeAllViews();
                new FetchResults().execute();
            }
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void removeduplicates()
    {

        for(int i=0;i<skill.size();i++) {
            if(!skillList.contains(skill.get(i)))
                skillList.add(skill.get(i));
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Log.i("info", "Onback");

        Intent intent=new Intent(ViewTA.this,Dashboard.class);

        intent.putExtra("email",email);
        intent.putExtra("name", pname);
        intent.putExtra("rollno", rollno);
        startActivity(intent);


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

            pDialog = new ProgressDialog(ViewTA.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("course", course);
            params.put("code", code_skill);


            Log.v("course", course);


            json = jsonParser.makeHttpRequest(new DefineUrl().viewTA, "GET", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    status=json.getString("status");


                }
            } catch (JSONException e) {
                //isFetched = -1;
                e.printStackTrace();
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
                    metaData="";

                    LinearLayout ll = new LinearLayout(ViewTA.this);
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    final Button btn = new Button(ViewTA.this);
                    btn.setId(i);
                    final int id_ = btn.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        tas = json.getString("ta"+i);
                        length_recommended=json.getString("len"+i);


                        if(!length_recommended.equals(""))
                            len_recommended=Integer.parseInt(length_recommended);


                        for(int j=0;j<len_recommended;j++)
                        {
                            data=json.getString("recommended"+i+"-"+j);
                            if(j==0)
                                metaData=data;
                                else
                            metaData=metaData+","+data;
                        }






                        //courses=courses.split("\"")[1];
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    btn.setText(Html.fromHtml("<b><big>" + tas + "</big></b>" + "<br />" +
                            "<small>" + metaData + "</small>" + "<br />"));
                    btn.setBackgroundColor(Color.rgb(91, 190, 229));
                    btn.setTag(tas);
                    btn.setGravity(Gravity.LEFT);
                    btn.setPadding(40, 0, 0, 0);
                    btn.setWidth(480);
                    ll.addView(btn, params);


                    TextView spaces = new TextView(ViewTA.this);
                    spaces.setText(" ");
                    ll.addView(spaces);


                    Button btn2 = new Button(ViewTA.this);
                    btn2.setId(i+200);
                    final int id1_ = btn2.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        tas = json.getString("ta"+i);
                        // courses=courses.split("\"")[1];
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    btn2.setText(" ");
                    btn2.setBackgroundResource(R.drawable.arrow);
                    //btn2.setBackgroundResource(R.drawable.trash);             //change here
                    ll.addView(btn2,params);


                    linear.addView(ll);

                    TextView space = new TextView(ViewTA.this);
                    space.setText("");
                    linear.addView(space, params);
                    // btn = ((Button) findViewById(id_));
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Toast.makeText(view.getContext(),
                            //       "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            //      .show();


                            Intent intent=new Intent(ViewTA.this,ViewProfile.class);
                            intent.putExtra("email",btn.getTag().toString());
                            //intent.putExtra("name",pname);
                            //intent.putExtra("rollno",rollno);
                            startActivity(intent);




                        }
                    });



                    btn2 = ((Button) findViewById(id1_));
                    btn2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {


                           //do chatting



                            Intent intent=new Intent(ViewTA.this,AskTA.class);

                            intent.putExtra("course",course);
                            intent.putExtra("email",email);
                            intent.putExtra("name",pname);
                            intent.putExtra("rollno",rollno);
                            intent.putExtra("emailF",email);
                            intent.putExtra("emailT",btn.getTag().toString());
                            startActivity(intent);




                        }
                    });


                }

                TextView space = new TextView(ViewTA.this);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Server is not working. Please try again later.", Toast.LENGTH_SHORT).show();
            }


        }
    }







    class FetchAllResults extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewTA.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("course", course);
            params.put("code", code_skill);


            Log.v("course", course);


            json = jsonParser.makeHttpRequest(new DefineUrl().viewAllTA, "GET", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    status=json.getString("status");


                }
            } catch (JSONException e) {
                //isFetched = -1;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(status.equals("success"))
            {

                try {
                    length=json.getString("length");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(!length.equals(""))
                    len=Integer.parseInt(length);

                Log.i("Length", length);
                for(int i=0;i<len;i++)
                {

                    metaData="";

                    LinearLayout ll = new LinearLayout(ViewTA.this);
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    final Button btn = new Button(ViewTA.this);
                    btn.setId(i);
                    final int id_ = btn.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        tas = json.getString("ta"+i);






                        length_recommended=json.getString("len"+i);


                        if(!length_recommended.equals(""))
                            len_recommended=Integer.parseInt(length_recommended);


                        for(int j=0;j<len_recommended;j++)
                        {
                            data=json.getString("recommended"+i+"-"+j);
                            if(j==0)
                                metaData=data;
                            else
                                metaData=metaData+","+data;
                        }



                        //courses=courses.split("\"")[1];
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    btn.setText(Html.fromHtml("<b><big>" + tas + "</big></b>" + "<br />" +
                            "<small>" + metaData + "</small>" + "<br />"));
                    btn.setBackgroundColor(Color.rgb(91, 190, 229));
                    btn.setTag(tas);
                    btn.setGravity(Gravity.LEFT);
                    btn.setPadding(40, 0, 0, 0);
                    btn.setWidth(480);
                    ll.addView(btn, params);


                    TextView spaces = new TextView(ViewTA.this);
                    spaces.setText(" ");
                    ll.addView(spaces);


                    Button btn2 = new Button(ViewTA.this);
                    btn2.setId(i+200);
                    final int id1_ = btn2.getId();
                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        tas = json.getString("ta"+i);
                        // courses=courses.split("\"")[1];
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    btn2.setText(" ");
                    btn2.setBackgroundResource(R.drawable.arrow);             //change here
                    ll.addView(btn2,params);


                    linear.addView(ll);

                    TextView space = new TextView(ViewTA.this);
                    space.setText("");
                    linear.addView(space, params);
                    // btn = ((Button) findViewById(id_));
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Toast.makeText(view.getContext(),
                            //       "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            //      .show();

                            Intent intent=new Intent(ViewTA.this,ViewProfile.class);
                            intent.putExtra("email",btn.getTag().toString());
                            //intent.putExtra("name",pname);
                            //intent.putExtra("rollno",rollno);
                            startActivity(intent);


                        }
                    });



                    btn2 = ((Button) findViewById(id1_));
                    btn2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {


                            //do chatting



                            Intent intent=new Intent(ViewTA.this,AskTA.class);

                            intent.putExtra("course",course);
                            intent.putExtra("email",email);
                            intent.putExtra("name",pname);
                            intent.putExtra("rollno",rollno);
                            intent.putExtra("emailF",email);
                            intent.putExtra("emailT",btn.getTag().toString());
                            startActivity(intent);




                        }
                    });


                }

                TextView space = new TextView(ViewTA.this);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Server is not working. Please try again later.", Toast.LENGTH_SHORT).show();
            }


        }
    }


















    class FetchSkill extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewTA.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("course", course);



            Log.v("course", course);


            json = jsonParser.makeHttpRequest(new DefineUrl().viewSkill, "GET", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    status=json.getString("status");


                }
            } catch (JSONException e) {
                //isFetched = -1;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(status.equals("success"))
            {

                try {
                    length=json.getString("length");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(!length.equals(""))
                    len=Integer.parseInt(length);

                Log.i("Length", length);
                for(int i=0;i<len;i++)
                {



                    try {


                        skill.add(json.getString("skill" + i));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                skill.add("Select All");
                removeduplicates();

            }
            else if(status.equals("error"))
            {
                try {
                    Toast.makeText(getApplicationContext(),json.getString("msg"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Server is not working. Please try again later.", Toast.LENGTH_SHORT).show();
            }


        }
    }











}
