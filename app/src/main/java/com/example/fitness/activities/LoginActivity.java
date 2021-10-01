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
import com.example.fitness.helpers.Session;
import com.example.fitness.models.User;
import com.example.fitness.databases.UserDatabase;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    Button register;
    List<User> users;
    User user;
    UserDatabase database;
    Boolean logedIn = false;
    TextView notification;
    Session session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get the room database ready
        database = Room.databaseBuilder(getApplicationContext(), UserDatabase.class,
                "Users").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //connect properties with layout components
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        login = findViewById(R.id.login_login);
        register = findViewById(R.id.login_register);
        notification = findViewById(R.id.login_notification);



        session = new Session(this);

        //check if the user is already logged in. If so, auto login and go to main activity
        if (session.loggedin()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //called when the user clicked the login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logedIn = false;

                //get all the users from the database
                users = database.userDao().getAllUsers();

                //go through all the users
                for (int i = 0; i < users.size(); i++) {
                    user = users.get(i);
                    String usernameInput = username.getText().toString();

                    //check if the username input is in the user database
                    if (usernameInput.equals(user.getName())) {
                        String passwordInput = password.getText().toString();

                        //if username exists, check if the password matches the input
                        if (passwordInput.equals(user.getPassword())) {

                            //store information in session accordingly for auto login
                            session.setLoggedin(true);
                            session.setUsername(user.getName());

                            //start main activity
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            logedIn = true;
                            break;
                        }
                    }
                }

                //notify user if login wasn't successful
                if (!logedIn) {
                    notification.setText("Incorrect Username or Password");
                }
            }
        });

        //click register button to register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
