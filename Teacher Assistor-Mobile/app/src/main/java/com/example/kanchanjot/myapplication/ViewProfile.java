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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kanchanjot on 25/03/16.
 */
public class ViewProfile extends AppCompatActivity {




    String status="";


    String statusTA="";
    String statusSkill="";
    String statusRecommended="";
    String statusName="";




    String length="";

    String lengthTA="";

    String lengthSkill="";

    String lengthRecommended="";

    String courses="";

    String coursesTA="";

    String coursesSkill="";

    String skillRecommeded="";



    int len=0;
    int lenTA=0;
    int lenSkill=0;
    int lenRecommended=0;

    String pname="";
            String email="";
    String rollno="";

    TextView displayname,displayemail,displayrollno;




    ArrayList<String> skillL=new ArrayList<String>();

    List<String> skillList = new ArrayList<>();






    ArrayList<String> courseL=new ArrayList<String>();

    List<String> courseList = new ArrayList<>();




    ArrayList<String> courseTAL=new ArrayList<String>();

    List<String> courseTAList = new ArrayList<>();


    ArrayList<String> recommendedL=new ArrayList<String>();

    List<String> recommendedList = new ArrayList<>();



    String metaDataS="";
    String metaDataC="";
    String metaDataCTA="";
            String metaDataR="";




    public String getName() {
        return str_displayname;
    }

    public String getEmail() {

        //Toast.makeText(getApplicationContext(),email,Toast.LENGTH_SHORT).show();
        return str_email;
    }




    private ProgressDialog pDialog;
    JSONParser jsonParser;

    JSONObject json;


    String str_displayname;
    String str_email;
    String str_rollno="";



    LinearLayout linearcourse,linearta,linearskill,linearrecommended;
    LinearLayout.LayoutParams params;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


        displayname=(TextView)findViewById(R.id.textView9);
        displayemail=(TextView)findViewById(R.id.textView10);
        displayrollno=(TextView)findViewById(R.id.textView11);



        email = getIntent().getExtras().getString("email");
       // pname=getIntent().getExtras().getString("name");
       // rollno=getIntent().getExtras().getString("rollno");


            new NameRoll().execute();


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


        str_email=email;





        if(email != null)
            str_email=email;
        else
            str_email=displayemail.getText().toString();

        if(pname != null)
            str_displayname=pname;
        else
            str_displayname=displayname.getText().toString();


        if(rollno != null)
            str_rollno=rollno;
        else
            str_rollno=displayrollno.getText().toString();


        Log.i("email", email);
        Log.i("name", pname);
        Log.i("rollno", rollno);




        linearcourse = (LinearLayout) findViewById(R.id.courses);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        linearta = (LinearLayout) findViewById(R.id.tacourse);

        linearskill = (LinearLayout) findViewById(R.id.skillset);
        linearrecommended = (LinearLayout) findViewById(R.id.recommended);







            Log.i("info", "ta fetched");


            Log.i("info", "skill fetched");





    }




    public void removeduplicates(ArrayList<String> skill,List<String> skillList)
    {

        if (skill != null)
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






    class Course extends AsyncTask<String, String, String> {



        //JSONArray course = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(courseL != null)
            courseL.clear();

            if ((courseList != null))
            courseList.clear();


            pDialog = new ProgressDialog(ViewProfile.this);
            pDialog.setMessage("Processing Course...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
           // pDialog.show();
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

                final TextView btn = new TextView(ViewProfile.this);

                for(int i=0;i<len;i++)
                {




                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        courses = json.getString("course"+i);
                        courseL.add(courses);
                        //courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }



                    /*


                    btn.setText(courses);

                    //btn.setBackgroundColor(Color.rgb(173, 168, 168));
                    linearcourse.addView(btn, params);
                    TextView space = new TextView(ViewProfile.this);
                    space.setText("");
                    linearcourse.addView(space, params);
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Toast.makeText(view.getContext(),
                            //         "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            //        .show();






                        }
                    });







                    */
                }


                removeduplicates(courseL,courseList);

                if(courseList != null)
                for(int i=0;i<courseList.size();i++)
                {

                    if(i==0)
                        metaDataC=courseList.get(i);
                    else
                    metaDataC=metaDataC+","+courseList.get(i);
                }

                btn.setText(metaDataC);
                btn.setTextSize(20);
                linearcourse.setPadding(40, 0, 20, 0);
                linearcourse.addView(btn, params);
                TextView space = new TextView(ViewProfile.this);
                space.setText("");
                linearcourse.addView(space, params);



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

            Log.i("info", "course fetched");
            try {
              new CourseTA().execute();
            } catch (Exception e) {

            }
        }


    }






    class CourseTA extends AsyncTask<String, String, String> {

        //JSONArray course = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewProfile.this);
            pDialog.setMessage("Processing TA...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
         //   pDialog.show();



            if(courseTAL != null)
                courseTAL.clear();

            if ((courseTAList != null))
                courseTAList.clear();


        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", getEmail());



            Log.v("email", getEmail());


            json = jsonParser.makeHttpRequest(new DefineUrl().profileTA, "GET", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    statusTA=json.getString("status");


                }
            } catch (Exception e) {
                //isFetched = -1;

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(statusTA.equals("success"))
            {

                try {
                    lengthTA=json.getString("length");
                } catch (Exception e) {

                }

                if(!lengthTA.equals(""))
                    lenTA=Integer.parseInt(lengthTA);

                Log.i("Length", lengthTA);

                final TextView btn = new TextView(ViewProfile.this);

                for(int i=0;i<lenTA;i++)
                {




                    try {
                        //course=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        coursesTA = json.getString("course"+i);

                        courseTAL.add(coursesTA);
                        //courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }


                    /*

                    btn.setText(coursesTA);
                   // btn.setBackgroundColor(Color.rgb(173, 168, 168));
                    linearta.addView(btn, params);
                    TextView space = new TextView(ViewProfile.this);
                    space.setText("");
                    linearta.addView(space, params);
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Toast.makeText(view.getContext(),
                            //         "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            //        .show();






                        }
                    });







                    */
                }






                removeduplicates(courseTAL, courseTAList);

                if(courseTAList != null)
                    for(int i=0;i<courseTAList.size();i++)
                    {

                        if(i==0)
                            metaDataCTA=courseTAList.get(i);
                        else
                            metaDataCTA=metaDataCTA+","+courseTAList.get(i);
                    }

                btn.setText(metaDataCTA);
                btn.setTextSize(20);
                linearta.setPadding(40,0,20,0);
                linearta.addView(btn, params);
                TextView space = new TextView(ViewProfile.this);
                space.setText("");
                linearta.addView(space, params);



                //changes need to be made here



                // Intent i= new Intent(SignIn.this,Dashboard.class);
                //i.putExtra("email", );

                //i.putExtra("name", name); //get the name here
                //startActivity(i);
            }
            else if(statusTA.equals("error"))
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
            Log.i("info", "ta fetched");
            new SkillSet().execute();

        }
    }








    class SkillSet extends AsyncTask<String, String, String> {

        //JSONArray course = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewProfile.this);
            pDialog.setMessage("Processing");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


            if(skillL != null)
                skillL.clear();

            if ((skillList != null))
                skillList.clear();

        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", getEmail());



            Log.v("emailofskilset", getEmail());


            json = jsonParser.makeHttpRequest(new DefineUrl().skillset, "GET", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    statusSkill=json.getString("status");


                }
            } catch (Exception e) {
                //isFetched = -1;

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(statusSkill.equals("success"))
            {
                Log.v("successofskilset", getEmail());

                try {
                    lengthSkill=json.getString("length");
                } catch (Exception e) {

                }

                if(!lengthSkill.equals(""))
                    lenSkill=Integer.parseInt(lengthSkill);

                Log.i("Length", lengthSkill);

                final TextView btn = new TextView(ViewProfile.this);
                for(int i=0;i<lenSkill;i++)
                {




                    try {
                        //skill=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        coursesSkill = json.getString("skill"+i);

                        skillL.add(coursesSkill);
                        //courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }


                    /*
                    btn.setText(coursesSkill);
                    btn.setHeight(50);
                    btn.setTextSize(20);
                    //btn.setBackgroundColor(Color.rgb(91, 190, 229));
                    linearskill.addView(btn, params);
                    TextView space = new TextView(ViewProfile.this);
                    space.setText(" , ");
                    linearskill.addView(space, params);
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Toast.makeText(view.getContext(),
                            //         "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            //        .show();






                        }
                    });







                    */


















                }






                removeduplicates(skillL,skillList);

                if(skillList != null)
                    for(int i=0;i<skillList.size();i++)
                    {

                        if(i==0)
                            metaDataS=skillList.get(i);
                        else
                            metaDataS=metaDataS+","+skillList.get(i);
                    }

                btn.setText(metaDataS);
                btn.setTextSize(20);
                linearskill.addView(btn, params);
                TextView space = new TextView(ViewProfile.this);
                space.setText("");
                linearskill.addView(space, params);
                //changes need to be made here



                // Intent i= new Intent(SignIn.this,Dashboard.class);
                //i.putExtra("email", );

                //i.putExtra("name", name); //get the name here
                //startActivity(i);
            }
            else if(statusSkill.equals("error"))
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

            Log.i("info", "skill fetched");
            new RecommendedSkill().execute();

        }
    }











    class RecommendedSkill extends AsyncTask<String, String, String> {

        //JSONArray course = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewProfile.this);
            pDialog.setMessage("Processing");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();


            if(recommendedL != null)
                recommendedL.clear();

            if ((recommendedList != null))
                recommendedList.clear();

        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", getEmail());



            Log.v("emailofrecommended", getEmail());


            json = jsonParser.makeHttpRequest(new DefineUrl().recommendedSkill, "GET", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    statusRecommended=json.getString("status");


                }
            } catch (Exception e) {
                //isFetched = -1;

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(statusRecommended.equals("success"))
            {
                Log.v("successofrecommended", getEmail());

                try {
                    lengthRecommended=json.getString("length");
                } catch (Exception e) {

                }

                if(!lengthRecommended.equals(""))
                    lenRecommended=Integer.parseInt(lengthRecommended);

                Log.i("Length", lengthRecommended);
                final TextView btn = new TextView(ViewProfile.this);
                for(int i=0;i<lenRecommended;i++)
                {




                    try {
                        //skill=json.getJSONArray("course"+i);
                        //JSONObject c = course.getJSONObject(i);
                        skillRecommeded = json.getString("recommended"+i);
                        recommendedL.add(skillRecommeded);
                        //courses=courses.split("\"")[1];
                    } catch (Exception e) {

                    }




                    /*

                    btn.setText(skillRecommeded);
                    btn.setHeight(50);
                    btn.setTextSize(20);
                    //btn.setBackgroundColor(Color.rgb(91, 190, 229));
                    linearrecommended.addView(btn, params);
                    TextView space = new TextView(ViewProfile.this);
                    space.setText(" , ");
                    linearrecommended.addView(space, params);
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Toast.makeText(view.getContext(),
                            //         "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            //        .show();






                        }
                    });





                */


                }





                removeduplicates(recommendedL, recommendedList);

                if(recommendedList != null)
                    for(int i=0;i<recommendedList.size();i++)
                    {

                        if(i==0)
                            metaDataR=recommendedList.get(i);
                        else
                            metaDataR=metaDataR+","+recommendedList.get(i);
                    }

                btn.setText(metaDataR);
                btn.setTextSize(20);
                //linearrecommended.setPadding(20,0,20,0);
                linearrecommended.addView(btn, params);
                TextView space = new TextView(ViewProfile.this);
                space.setText("");
                linearrecommended.addView(space, params);





            }
            else if(statusRecommended.equals("error"))
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

            Log.i("info", "recommended fetched");


        }
    }










    class NameRoll extends AsyncTask<String, String, String> {

        //JSONArray course = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewProfile.this);
            pDialog.setMessage("Processing");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", getEmail());



            Log.v("emailofName", getEmail());


            json = jsonParser.makeHttpRequest(new DefineUrl().name, "GET", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    statusName=json.getString("status");


                }
            } catch (Exception e) {
                //isFetched = -1;

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(statusName.equals("success"))
            {
                Log.v("successofName", getEmail());

                try {
                    pname=json.getString("name");
                    rollno=json.getString("roll");
                } catch (Exception e) {

                }


                displayname.setText(pname);
                displayemail.setText(email);
                displayrollno.setText(rollno);





            }
            else if(statusName.equals("error"))
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

            Log.i("info", "name rollno fetched");

            new Course().execute();
        }
    }











}
