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
import com.example.fitness.adapters.LocalOutdoorAdapter;
import com.example.fitness.databases.LocalFitnessDatabase;
import com.example.fitness.models.LocalFitness;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class LocalOutdoorFragment extends Fragment {
    private String username;
    public static LocalFitnessDatabase database;

    private RecyclerView recyclerView;
    private LocalOutdoorAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton create;
    private EditText nameInput;
    private EditText caloriesInput;
    private EditText durationInput;

    public LocalOutdoorFragment(String username, LocalOutdoorAdapter adapter) {
        this.username = username;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_local_fitness_outdoor, container, false);

        database = Room.databaseBuilder(getContext(), LocalFitnessDatabase.class,
                "localFitessDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        recyclerView = view.findViewById(R.id.home_fitness_outdoor);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


        if (LocalFitnessFragment.userDatabase.userDao().getOutdoorInserted(username) == 0) {
            LocalFitnessFragment.userDatabase.userDao().setOutdoorInserted(username, 1);
            Log.e("outdoorInserted", String.valueOf(LocalFitnessFragment.userDatabase.userDao().getOutdoorInserted(username)));
            insert();
        }
        adapter.reload(username);


        create = view.findViewById(R.id.create_localOutdoorFitness);
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

                            database.localFitnessDao().create(username, name, c, d, "Outdoor");
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
        List<LocalFitness> outdoorFitness = Arrays.asList(new LocalFitness[]{
                new LocalFitness(username, "Planting seedlings, shrubs", 30, 120, "Outdoor"),
                new LocalFitness(username, "Raking Lawn", 30, 120, "Outdoor"),
                new LocalFitness(username, "Sacking grass or leaves", 30, 120, "Outdoor"),
                new LocalFitness(username, "Gardening: general", 30, 135, "Outdoor"),
                new LocalFitness(username, "Mowing Lawn: push, power", 30, 135, "Outdoor"),
                new LocalFitness(username, "Operate Snow Blower: walking", 30, 135, "Outdoor"),
                new LocalFitness(username, "Plant trees", 30, 135, "Outdoor"),
                new LocalFitness(username, "Gardening: weeding", 30, 139, "Outdoor"),
                new LocalFitness(username, "Carrying & stacking wood", 30, 150, "Outdoor"),
                new LocalFitness(username, "Digging, spading dirt", 30, 150, "Outdoor"),
                new LocalFitness(username, "Laying sod / crushed rock", 30, 150, "Outdoor"),
                new LocalFitness(username, "Mowing Lawn: push, hand", 30, 165, "Outdoor"),
                new LocalFitness(username, "Chopping & splitting wood", 30, 180, "Outdoor"),
                new LocalFitness(username, "Shoveling Snow: by hand", 30, 180, "Outdoor"),

        });


        for (int i = 0; i < outdoorFitness.size(); i++) {
            LocalFitness current = outdoorFitness.get(i);
            database.localFitnessDao().create(current.getUsername(), current.getName(),
                    current.getDuration(), current.getCalories(), current.getType());
        }
    }
}

