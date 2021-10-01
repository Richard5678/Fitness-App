package com.example.fitness.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.fitness.R;
import com.example.fitness.databases.UserDatabase;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    TextView notification;
    EditText username;
    EditText password;
    EditText email;
    Button register;
    List<String> usernames;
    UserDatabase database;
    String name;
    boolean usernameUsed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //wire up local properties with layout components
        username = (EditText) findViewById(R.id.register_username);
        password = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);
        notification = findViewById(R.id.register_notification);
        register = findViewById(R.id.register_register);

        database = Room.databaseBuilder(getApplicationContext(), UserDatabase.class,
                "Users").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //called when the register button is clicked
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameUsed = false;

                //get all the user names from the database
                usernames = database.userDao().getUsername();

                //go through all the user names
                for (int i = 0; i < usernames.size(); i++) {
                    name = usernames.get(i);
                    String usernameInput = username.getText().toString();

                    //check if username already exists
                    if (usernameInput.equals(name)) {
                        notification.setText("Username Already Used. Please Choose Another One");
                        usernameUsed = true;
                        break;
                    }
                }

                //if username hasn't been used, create a new account and store it into the User database
                //then go back to the login activity
                if (!usernameUsed) {
                    database.userDao().create(username.getText().toString(),
                            password.getText().toString(),
                            email.getText().toString(),
                            0, 0, 0, 0,  0, 0, 0, 0);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
