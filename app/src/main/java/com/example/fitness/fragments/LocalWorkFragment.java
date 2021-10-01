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
import com.example.fitness.adapters.LocalWorkAdapter;
import com.example.fitness.databases.LocalFitnessDatabase;
import com.example.fitness.models.LocalFitness;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class LocalWorkFragment extends Fragment {
    private String username;
    public static LocalFitnessDatabase database;

    private RecyclerView recyclerView;
    private LocalWorkAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton create;
    private EditText nameInput;
    private EditText caloriesInput;
    private EditText durationInput;

    public LocalWorkFragment(String username, LocalWorkAdapter adapter) {
        this.username = username;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_local_fitness_work, container, false);

        database = Room.databaseBuilder(getContext(), LocalFitnessDatabase.class,
                "localFitessDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        recyclerView = view.findViewById(R.id.home_fitness_work);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        if (LocalFitnessFragment.userDatabase.userDao().getWorkInserted(username) == 0) {
            LocalFitnessFragment.userDatabase.userDao().setWorkInserted(username, 1);
            Log.e("workInserted", String.valueOf(LocalFitnessFragment.userDatabase.userDao().getWorkInserted(username)));
            insert();
        }
        adapter.reload(username);


        create = view.findViewById(R.id.create_localWorkFitness);
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

                            database.localFitnessDao().create(username, name, c, d, "Work");
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
        List<LocalFitness> workFitness = Arrays.asList(new LocalFitness[]{
                new LocalFitness(username, "Computer Work", 30, 41, "Work"),
                new LocalFitness(username, "ight Office Work", 30, 45, "Work"),
                new LocalFitness(username, "Sitting in Meetings", 30, 49, "Work"),
                new LocalFitness(username, "Desk Work", 30, 53, "Work"),
                new LocalFitness(username, "Sitting in Class", 30, 53, "Work"),
                new LocalFitness(username, "Truck Driving: sitting", 30, 60, "Work"),
                new LocalFitness(username, "Bartending/Server", 30, 75, "Work"),
                new LocalFitness(username, "Heavy Equip. Operator", 30, 75, "Work"),
                new LocalFitness(username, "Police Officer", 30, 75, "Work"),
                new LocalFitness(username, "Theater Work", 30, 90, "Work"),
                new LocalFitness(username, "Welding", 30, 90, "Work"),
                new LocalFitness(username, "Carpentry Work", 30, 105, "Work"),
                new LocalFitness(username, "Coaching Sports", 30, 120, "Work"),
                new LocalFitness(username, "Masseur, standing", 30, 120, "Work"),
                new LocalFitness(username, "Construction, general", 30, 165, "Work"),
                new LocalFitness(username, "Coal Mining", 30, 180, "Work"),
                new LocalFitness(username, "Horse Grooming", 30, 180, "Work"),
                new LocalFitness(username, "Masonry", 30, 210, "Work"),
                new LocalFitness(username, "Forestry, general", 30, 240, "Work"),
                new LocalFitness(username, "Heavy Tools, not power", 30, 240, "Work"),
                new LocalFitness(username, "Steel Mill: general", 30, 240, "Work"),
                new LocalFitness(username, "Firefighting", 30, 360, "Work")
        });

        for (int i = 0; i < workFitness.size(); i++) {
            LocalFitness current = workFitness.get(i);
            database.localFitnessDao().create(current.getUsername(), current.getName(),
                    current.getDuration(), current.getCalories(), current.getType());
        }
    }
}

