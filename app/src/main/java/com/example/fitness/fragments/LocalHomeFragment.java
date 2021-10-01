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
import com.example.fitness.adapters.LocalHomeAdapter;
import com.example.fitness.databases.LocalFitnessDatabase;
import com.example.fitness.models.LocalFitness;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class LocalHomeFragment extends Fragment {
    private String username;
    public static LocalFitnessDatabase database;

    private RecyclerView recyclerView;
    private LocalHomeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton create;
    private EditText nameInput;
    private EditText caloriesInput;
    private EditText durationInput;

    public LocalHomeFragment(String username, LocalHomeAdapter adapter) {
        this.username = username;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_local_fitness_home, container, false);

        database = Room.databaseBuilder(getContext(), LocalFitnessDatabase.class,
                "localFitessDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        recyclerView = view.findViewById(R.id.home_fitness_home);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        if (LocalFitnessFragment.userDatabase.userDao().getHomeInserted(username) == 0) {
            LocalFitnessFragment.userDatabase.userDao().setHomeInserted(username, 1);
            Log.e("homeInserted", String.valueOf(LocalFitnessFragment.userDatabase.userDao().getHomeInserted(username)));
            insert();
        }
        adapter.reload(username);


        create = view.findViewById(R.id.create_localHomeFitness);
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

                            database.localFitnessDao().create(username, name, c, d, "Home");
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
        List<LocalFitness> homeFitness = Arrays.asList(new LocalFitness[]{
                new LocalFitness(username, "Sleeping", 30, 19, "Home"),
                new LocalFitness(username, "Watching TV", 30, 23, "Home"),
                new LocalFitness(username, "Reading: sitting", 30, 34, "Home"),
                new LocalFitness(username, "Standing in line", 30, 38, "Home"),
                new LocalFitness(username, "Cooking", 30, 75, "Home"),
                new LocalFitness(username, "Child-care: bathing, feeding, etc.", 30, 105, "Home"),
                new LocalFitness(username, "Food Shopping: with cart", 30, 105, "Home"),
                new LocalFitness(username, "Moving: unpacking", 30, 105, "Home"),
                new LocalFitness(username, "Playing w/kids: moderate efforts", 30, 120, "Home"),
                new LocalFitness(username, "Heavy Cleaning: wash car, windows", 30, 135, "Home"),
                new LocalFitness(username, "Child games: hop-scotch, jacks, etc.", 30, 150, "Home"),
                new LocalFitness(username, "Playing w/kids: vigorous effort", 30, 150, "Home"),
                new LocalFitness(username, "Moving: household furniture", 30, 180, "Home"),
                new LocalFitness(username, "Moving: carrying boxes", 30, 210, "Home"),

                new LocalFitness(username, "Auto Repair", 30, 90, "Home"),
                new LocalFitness(username, "Wiring and Plumbing", 30, 90, "Home"),
                new LocalFitness(username, "Carpentry: refinish furniture", 30, 135, "Home"),
                new LocalFitness(username, "Lay or remove carpet/tile", 30, 135, "Home"),
                new LocalFitness(username, "Paint, paper, remodel: inside", 30, 135, "Home"),
                new LocalFitness(username, "Cleaning rain gutters", 30, 150, "Home"),
                new LocalFitness(username, "Hanging storm windows", 30, 150, "Home"),
                new LocalFitness(username, "Paint house: outside", 30, 150, "Home"),
                new LocalFitness(username, "Carpentry: outside", 30, 180, "Home"),
                new LocalFitness(username, "Roofing", 30, 180, "Home")
        });




        for (int i = 0; i < homeFitness.size(); i++) {
            LocalFitness current = homeFitness.get(i);
            database.localFitnessDao().create(current.getUsername(), current.getName(),
                    current.getDuration(), current.getCalories(), current.getType());
        }
    }
}

