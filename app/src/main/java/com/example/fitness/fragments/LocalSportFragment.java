package com.example.fitness.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.fitness.R;
import com.example.fitness.activities.MainActivity;
import com.example.fitness.adapters.LocalSportAdapter;
import com.example.fitness.databases.LocalFitnessDatabase;
import com.example.fitness.models.LocalFitness;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class LocalSportFragment extends Fragment {
    private String username;

    private RecyclerView recyclerView;
    private LocalSportAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static LocalFitnessDatabase database;

    private FloatingActionButton create;
    private EditText nameInput;
    private EditText caloriesInput;
    private EditText durationInput;

    public LocalSportFragment(String username, LocalSportAdapter adapter) {
        this.username = username;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_local_fitness_sport, container, false);

        database = Room.databaseBuilder(getContext(), LocalFitnessDatabase.class,
                "localFitessDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        recyclerView = view.findViewById(R.id.home_fitness_sport);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


        if (LocalFitnessFragment.userDatabase.userDao().getSportInserted(username) == 0) {
            LocalFitnessFragment.userDatabase.userDao().setSportInserted(username, 1);
            Log.e("sportInserted", String.valueOf(LocalFitnessFragment.userDatabase.userDao().getSportInserted(username)));
            insert();
        }
        adapter.reload(username);


        create = view.findViewById(R.id.create_localSportFitness);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.home_fitness_input_dialog);
                dialog.show();

                nameInput = (EditText) dialog.findViewById(R.id.home_fitness_input_dialog_name);
                caloriesInput = (EditText) dialog.findViewById(R.id.home_fitness_input_dialog_calories);
                durationInput = (EditText) dialog.findViewById(R.id.home_fitness_input_dialog_duration);



                caloriesInput.addTextChangedListener(new TextWatcher() {
                    boolean allowDot = true;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (count > after) {
                            if (s.subSequence(start + after, start + count).toString().equals(".")) {
                                allowDot = true;
                            }
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 1) {
                            if (s.subSequence(0, 1).toString().equals("0")) {
                                caloriesInput.setKeyListener(DigitsKeyListener.getInstance("."));
                            } else {
                                caloriesInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            }
                        }

                        if (before < count && s.length() > 1) {
                            if (s.subSequence(start + before, start + count).toString().equals(".")) {
                                allowDot = false;
                            }
                        }

                        if (s.length() != 1) {
                            if (allowDot) {
                                caloriesInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            } else {
                                caloriesInput.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                durationInput.addTextChangedListener(new TextWatcher() {
                    boolean allowDot = true;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (count > after) {
                            if (s.subSequence(start + after, start + count).toString().equals(".")) {
                                allowDot = true;
                            }
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 1) {
                            if (s.subSequence(0, 1).toString().equals("0")) {
                                durationInput.setKeyListener(DigitsKeyListener.getInstance("."));
                            } else {
                                durationInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            }
                        }

                        if (before < count && s.length() > 1) {
                            if (s.subSequence(start + before, start + count).toString().equals(".")) {
                                allowDot = false;
                            }
                        }

                        if (s.length() != 1) {
                            if (allowDot) {
                                durationInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            } else {
                                durationInput.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                Button add = dialog.findViewById(R.id.home_fitness_input_dialog_add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = nameInput.getText().toString();
                        String calories = caloriesInput.getText().toString();
                        String duration = durationInput.getText().toString();

                        NumberFormat nf = DecimalFormat.getInstance();
                        nf.setMaximumFractionDigits(2);
                        nf.setMinimumFractionDigits(0);

                        if (name.isEmpty() || calories.isEmpty() || duration.isEmpty()) {

                        } else {
                            Float calo = Float.parseFloat(calories);
                            Float dura = Float.parseFloat(duration);

                            Float c = Float.parseFloat(nf.format(calo));
                            Float d = Float.parseFloat(nf.format(dura));

                            database.localFitnessDao().create(username, name, c, d, "Sport");
                            adapter.reload(username);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });


        return view;
    }

    private void insert() {
        List<LocalFitness> sportFitness = Arrays.asList(new LocalFitness[]{
                new LocalFitness(username, "Billiards", 30, 75, "Sport"),
                new LocalFitness(username, "Bowling", 30, 90, "Sport"),
                new LocalFitness(username, "Dancing: slow, waltz, foxtrot", 30, 90, "Sport"),
                new LocalFitness(username, "Frisbee", 30, 90, "Sport"),
                new LocalFitness(username, "Volleyball: non-competitive, general play", 30, 90, "Sport"),
                new LocalFitness(username, "Water Volleyball", 30, 90, "Sport"),
                new LocalFitness(username, "Archery: non-hunting", 30, 105, "Sport"),
                new LocalFitness(username, "Golf: using cart", 30, 105, "Sport"),
                new LocalFitness(username, "Hang Gliding", 30, 105, "Sport"),
                new LocalFitness(username, "Curling", 30, 120, "Sport"),
                new LocalFitness(username, "Sportnastics: general", 30, 120, "Sport"),
                new LocalFitness(username, "Horseback Riding: general", 30, 120, "Sport"),
                new LocalFitness(username, "Tai Chi", 30, 120, "Sport"),
                new LocalFitness(username, "Volleyball: competitive, Gymnasium play", 30, 120, "Sport"),
                new LocalFitness(username, "Walking: 3.5 mph (17 min/mi)", 30, 120, "Sport"),
                new LocalFitness(username, "Badminton: general", 30, 135, "Sport"),
                new LocalFitness(username, "Walking: 4 mph (15 min/mi)", 30, 135, "Sport"),
                new LocalFitness(username, "Kayaking", 30, 150, "Sport"),
                new LocalFitness(username, "Skateboarding", 30, 150, "Sport"),
                new LocalFitness(username, "Snorkeling", 30, 150, "Sport"),
                new LocalFitness(username, "Softball: general play", 30, 150, "Sport"),
                new LocalFitness(username, "Walking: 4.5 mph (13 min/mi)", 30, 150, "Sport"),
                new LocalFitness(username, "Whitewater: rafting, kayaking", 30, 150, "Sport"),
                new LocalFitness(username, "Dancing: disco, ballroom, square", 30, 165, "Sport"),
                new LocalFitness(username, "Golf: carrying clubs", 30, 165, "Sport"),
                new LocalFitness(username, "Dancing: Fast, ballet, twist", 30, 180, "Sport"),
                new LocalFitness(username, "Fencing", 30, 180, "Sport"),
                new LocalFitness(username, "Hiking: cross-country", 30, 180, "Sport"),
                new LocalFitness(username, "Skiing: downhill", 30, 180, "Sport"),
                new LocalFitness(username, "Swimming: general", 30, 180, "Sport"),
                new LocalFitness(username, "Walk/Jog: jog <10 min.", 30, 180, "Sport"),
                new LocalFitness(username, "Water Skiing", 30, 180, "Sport"),
                new LocalFitness(username, "Wrestling", 30, 180, "Sport"),
                new LocalFitness(username, "Basketball: wheelchair", 30, 195, "Sport"),
                new LocalFitness(username, "Race Walking", 30, 195, "Sport"),
                new LocalFitness(username, "Ice Skating: general", 30, 210, "Sport"),
                new LocalFitness(username, "Racquetball: casual, general", 30, 210, "Sport"),
                new LocalFitness(username, "Rollerblade Skating", 30, 210, "Sport"),
                new LocalFitness(username, "Scuba or skin diving", 30, 210, "Sport"),
                new LocalFitness(username, "Sledding, luge, toboggan", 30, 210, "Sport"),
                new LocalFitness(username, "Soccer: general", 30, 210, "Sport"),
                new LocalFitness(username, "Tennis: general", 30, 210, "Sport"),
                new LocalFitness(username, "Basketball: playing a game", 30, 240, "Sport"),
                new LocalFitness(username, "Bicycling: 12-13.9 mph", 30, 240, "Sport"),
                new LocalFitness(username, "Football: touch, flag, general", 30, 240, "Sport"),
                new LocalFitness(username, "Hockey: field & ice", 30, 240, "Sport"),
                new LocalFitness(username, "Rock Climbing: rappelling", 30, 240, "Sport"),
                new LocalFitness(username, "Running: 5 mph (12 min/mile)", 30, 240, "Sport"),
                new LocalFitness(username, "Skiing: cross-country", 30, 240, "Sport"),
                new LocalFitness(username, "Swimming: backstroke", 30, 240, "Sport"),
                new LocalFitness(username, "Volleyball: beach", 30, 240, "Sport"),
                new LocalFitness(username, "Bicycling: BMX or mountain", 30, 255, "Sport"),
                new LocalFitness(username, "Boxing: sparring", 30, 270, "Sport"),
                new LocalFitness(username, "Football: competitive", 30, 270, "Sport"),
                new LocalFitness(username, "Orienteering", 30, 270, "Sport"),
                new LocalFitness(username, " Running: 5.2 mph (11.5 min/mile)", 30, 270, "Sport"),
                new LocalFitness(username, "Running: 5.2 mph (11.5 min/mile)", 30, 270, "Sport"),
                new LocalFitness(username, "Running: cross-country", 30, 270, "Sport"),
                new LocalFitness(username, "Bicycling: 14-15.9 mph", 30, 300, "Sport"),
                new LocalFitness(username, "Martial Arts: judo, karate, kickbox", 30, 300, "Sport"),
                new LocalFitness(username, "Racquetball: competitive", 30, 300, "Sport"),
                new LocalFitness(username, "Rope Jumping", 30, 300, "Sport"),
                new LocalFitness(username, "Running: 6 mph (10 min/mile)", 30, 300, "Sport"),
                new LocalFitness(username, "Swimming: breaststroke", 30, 300, "Sport"),
                new LocalFitness(username, "Swimming: laps, vigorous", 30, 300, "Sport"),
                new LocalFitness(username, "Swimming: treading, vigorous", 30, 300, "Sport"),
                new LocalFitness(username, "Water Polo", 30, 300, "Sport"),
                new LocalFitness(username, "Rock Climbing: ascending", 30, 330, "Sport"),
                new LocalFitness(username, "Running: 6.7 mph (9 min/mile)", 30, 330, "Sport"),
                new LocalFitness(username, "Swimming: butterfly", 30, 330, "Sport"),
                new LocalFitness(username, "Swimming: crawl", 30, 330, "Sport"),
                new LocalFitness(username, "Bicycling: 16-19 mph", 30, 360, "Sport"),
                new LocalFitness(username, "Handball: general", 30, 360, "Sport"),
                new LocalFitness(username, "Running: 7.5 mph (8 min/mile)", 30, 375, "Sport"),
                new LocalFitness(username, "Running: 8.6 mph (7 min/mile)", 30, 435, "Sport"),
                new LocalFitness(username, "Bicycling: > 20 mph", 30, 495, "Sport"),
                new LocalFitness(username, "Running: 10 mph (6 min/mile)", 30, 495, "Sport")
        });

        for (int i = 0; i < sportFitness.size(); i++) {
            LocalFitness current = sportFitness.get(i);
            database.localFitnessDao().create(current.getUsername(), current.getName(),
                    current.getDuration(), current.getCalories(), current.getType());
        }
    }
}
