package com.example.fitness.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitness.R;

import java.io.IOException;
import java.net.URL;

public class SingleFood extends AppCompatActivity {
    private String username;
    private String name;
    private float calories;
    private float protein;
    private float fat;
    private float carbs;
    private String imageURL;

    private TextView n;
    private TextView cal;
    private TextView p;
    private TextView f;
    private TextView car;
    private ImageView imageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_food);

        username = getIntent().getStringExtra("username");
        name = getIntent().getStringExtra("name");
        calories = getIntent().getFloatExtra("Calories", 0);
        protein = getIntent().getFloatExtra("Protein", 0);
        fat = getIntent().getFloatExtra("Fat", 0);
        carbs = getIntent().getFloatExtra("Carbs", 0);
        imageURL = getIntent().getStringExtra("imageURL");

        n = findViewById(R.id.single_food_name);
        cal = findViewById(R.id.single_food_calories);
        p = findViewById(R.id.single_food_protein);
        f = findViewById(R.id.single_food_fat);
        car = findViewById(R.id.single_food_carbs);

        imageView = findViewById(R.id.single_food_image);
        new DownloadSpriteTask().execute(imageURL);

        n.setText(name);
        cal.setText(String.valueOf(calories));
        p.setText(String.valueOf(protein));
        f.setText(String.valueOf(fat));
        car.setText(String.valueOf(carbs));
    }

    private class DownloadSpriteTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                return BitmapFactory.decodeStream(url.openStream());
            } catch (IOException e) {
                Log.e("cs50", "Download sprite error", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
