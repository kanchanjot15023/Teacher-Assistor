package com.example.kanchanjot.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by kanchanjot on 28/03/16.
 */
public class DashboardTA extends AppCompatActivity {




    Button profile;
    Button add;

    Button switchto;

    Button updateSkill;
    Button notification;


    Button reviewComments;


    String pname,email;

    TextView displayname;
    String rollno;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardta);



        switchto=(Button)findViewById(R.id.button10);
        updateSkill=(Button)findViewById(R.id.button11);
        profile=(Button)findViewById(R.id.button12);

        reviewComments=(Button)findViewById(R.id.button13);

        notification=(Button)findViewById(R.id.button14);
        add=(Button)findViewById(R.id.button15);

        displayname=(TextView)findViewById(R.id.textView4);




        email = getIntent().getExtras().getString("email");
        pname=getIntent().getExtras().getString("name");
        rollno=getIntent().getExtras().getString("rollno");

        //if(pname)
        displayname.setText("HEY! " + pname);


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







        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardTA.this, Profile.class);
                intent.putExtra("email", email);
                intent.putExtra("name", pname);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });




        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardTA.this, AddCourseTA.class);
                intent.putExtra("email",email);
                intent.putExtra("name",pname);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });


        switchto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardTA.this, Dashboard.class);
                intent.putExtra("email", email);
                intent.putExtra("name", pname);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        updateSkill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardTA.this, UpdateSkill.class);
                intent.putExtra("email", email);
                intent.putExtra("name", pname);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        notification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardTA.this, Notification.class);
                intent.putExtra("email", email);
                intent.putExtra("name", pname);
                intent.putExtra("rollno", rollno);
                startActivity(intent);
            }

        });



        reviewComments.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardTA.this, ReviewComments.class);
                intent.putExtra("email", email);
                intent.putExtra("name", pname);
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


}
