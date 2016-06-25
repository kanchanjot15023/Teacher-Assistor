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
 * Created by kanchanjot on 28/03/16.
 */
public class UpdateSkill extends AppCompatActivity {



    Button profile;
    Button add;

    Button switchto;

    Button updateSkill;
    Button notification;


    Button reviewComments;


    LinearLayout toolbarS;



    Button addmore;
    Button submit;

    EditText skill1;
    TextView error;



    private ProgressDialog pDialog;
    JSONParser jsonParser;

    JSONObject json;



    int length=1;
int len=0;

    int status=0;

    LinearLayout linear;
    LinearLayout.LayoutParams params;

    ArrayList<EditText> allEds = new ArrayList<EditText>();

    String submit_value="";

    int flag=0;


    String email="";
    String str_email="";


    String name="";
    String str_name="";

    String statuss="";


    String rollno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateskillset);






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



        submit=(Button)findViewById(R.id.button17);
        addmore=(Button)findViewById(R.id.button4);

        skill1=(EditText)findViewById(R.id.editText3);
        error=(TextView)findViewById(R.id.textView16);


        linear = (LinearLayout) findViewById(R.id.addskill);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        toolbarS = (LinearLayout) findViewById(R.id.toolbar);




        updateSkill=(Button)findViewById(R.id.button25);


        profile=(Button)findViewById(R.id.button23);

        reviewComments=(Button)findViewById(R.id.button26);

        notification=(Button)findViewById(R.id.button27);
        add=(Button)findViewById(R.id.button24);
        switchto=(Button)findViewById(R.id.button33);








        switchto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateSkill.this, Dashboard.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });


        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateSkill.this, Profile.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });




        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateSkill.this, AddCourseTA.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });






        updateSkill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateSkill.this, UpdateSkill.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        notification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateSkill.this, Notification.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        reviewComments.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateSkill.this, ReviewComments.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });






        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                checkempty();

                if(flag==0) {

                    //submit the values

                    submitvalues();

                    new AddSkill().execute();

                }
            }

        });



        addmore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                checkempty();
                if (flag == 0) {
                    create(length);

                }

            }

        });





    }






    public void submitvalues()
    {


        for (int i=0;i<allEds.size();i++)
        {
            submit_value=submit_value + allEds.get(i).getText().toString()+"--";

        }
        submit_value=submit_value+skill1.getText().toString();

    }




    private void create(int id) {
        final TextView textView = new TextView(UpdateSkill.this);
        textView.setId(id);
        textView.setText("Add Course");
        linear.addView(textView, params);


        TextView spaces = new TextView(UpdateSkill.this);
        spaces.setText(" ");
        linear.addView(spaces);


        final LinearLayout ll = new LinearLayout(UpdateSkill.this);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        EditText skill = new EditText(UpdateSkill.this);
        skill.setText("");
        skill.setId(id);
        ll.addView(skill);

        TextView space = new TextView(UpdateSkill.this);
        space.setText(" ");
        ll.addView(space);

        final Button remove = new Button(UpdateSkill.this);
        remove.setText(" R");
        remove.setId(id);
        ll.addView(remove);

        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Toast.makeText(view.getContext(),
                //       "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                //      .show();

                ll.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                allEds.remove(textView.getId());


            }
        });

        linear.addView(ll);

    Toast.makeText(getApplicationContext(),"in down adding",Toast.LENGTH_SHORT).show();
    allEds.add(skill);
    Toast.makeText(getApplicationContext(),Integer.toString(allEds.size()),Toast.LENGTH_SHORT).show();
    length++;



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









    class AddSkill extends AsyncTask<String, String, String> {

        //JSONArray course = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(UpdateSkill.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            jsonParser= new JSONParser();
            JSONArray doctor = null;
            HashMap<String, String> params = new HashMap<String, String>();



            params.put("email",str_email);
            params.put("length",Integer.toString(length));
            params.put("skill",submit_value.toUpperCase());




            json = jsonParser.makeHttpRequest(new DefineUrl().skillsetadd, "POST", params);
            try {
                if (json != null) {


                    Log.i("INFO", json.toString());



                    statuss=json.getString("status");


                }
            } catch (Exception e) {
                //isFetched = -1;

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(statuss.equals("success"))
            {

                //Go to next question

                // onSubmit();
                Toast.makeText(getApplicationContext(),"Skill Set Updated",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateSkill.this, DashboardTA.class);
                intent.putExtra("email", email);
                intent.putExtra("name", str_name);
                intent.putExtra("rollno", rollno);
                startActivity(intent);


            }
            else if(statuss.equals("error"))
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










    public void checkempty()
    {
        flag=0;
        error.setText("");
        if(skill1.getText().toString().equals("") || skill1.getText().toString().equals(" ") )
        {
            error.setText("Please add skill");
            flag=1;
        }

        else
        {

            if(allEds == null) {
                len = 0;
                Toast.makeText(getApplicationContext(),"in if",Toast.LENGTH_SHORT).show();
            }
            else
            {
                len = allEds.size();
                Toast.makeText(getApplicationContext(),"else",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),len,Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),len,Toast.LENGTH_SHORT).show();

            }
            error.setText("");



            for (int i=0;i<len;i++)
            {
                Toast.makeText(getApplicationContext(),Boolean.toString(allEds.get(i).getText().toString().equals("")),Toast.LENGTH_SHORT).show();
                if (allEds.get(i).getText().toString().equals(""))
                {
                    error.setText("Please add skill");
                    flag=1;
                    break;
                }
            }



        }
    }





}
