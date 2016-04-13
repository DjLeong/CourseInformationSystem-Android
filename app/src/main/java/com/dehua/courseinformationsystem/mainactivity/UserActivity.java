package com.dehua.courseinformationsystem.mainactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.dehua.courseinformationsystem.R;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().setTitle("账户详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharedPreferences sharedPreferences = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        String userID=sharedPreferences.getString("UserID","");
        String userName=sharedPreferences.getString("UserName","");

        TextView id= (TextView) findViewById(R.id.activity_id);
        TextView name= (TextView) findViewById(R.id.activity_name);
        id.setText(userID);
        name.setText(userName);

        CardView cardView= (CardView) findViewById(R.id.activity_logout);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().clear().commit();
                finish();
                MainActivity.getInstance().recreate();
            }
        });
    }
}
