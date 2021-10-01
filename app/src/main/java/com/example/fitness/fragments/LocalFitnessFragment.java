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
import androidx.viewpager.widget.ViewPager;

import com.example.fitness.adapters.LocalGymAdapter;
import com.example.fitness.adapters.LocalHomeAdapter;
import com.example.fitness.adapters.LocalOutdoorAdapter;
import com.example.fitness.adapters.LocalSportAdapter;
import com.example.fitness.adapters.LocalWorkAdapter;
import com.example.fitness.adapters.SectionPagerAdapter;
import com.example.fitness.databases.LocalFitnessDatabase;
import com.example.fitness.R;
import com.example.fitness.databases.UserDatabase;
import com.example.fitness.activities.MainActivity;
import com.example.fitness.models.LocalFitness;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class LocalFitnessFragment extends Fragment {
    private String username;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public static LocalFitnessDatabase database;
    private FloatingActionButton create;

    public static UserDatabase userDatabase;

    private EditText nameInput;
    private EditText caloriesInput;
    private EditText durationInput;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private LocalGymAdapter localGymAdapter;
    private LocalSportAdapter localSportAdapter;
    private LocalOutdoorAdapter localOutdoorAdapter;
    private LocalHomeAdapter localHomeAdapter;
    private LocalWorkAdapter localWorkAdapter;

    public LocalFitnessFragment(String username) {
        this.username = username;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_local_fitness, container, false);

        database = Room.databaseBuilder(getContext(), LocalFitnessDatabase.class,
                "localFitessDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        userDatabase = Room.databaseBuilder(getContext(), UserDatabase.class,
                "Users").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();


        viewPager = view.findViewById(R.id.localFitness_viewPager);
        tabLayout = view.findViewById(R.id.localFitness_tabLayout);


        /*

        recyclerView = view.findViewById(R.id.home_fitness);
        adapter = new LocalFitnessAdapter(username);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        if (!userDatabase.userDao().getInserted(username)) {
            insert();
            userDatabase.userDao().setInserted(username, true);
        }




        adapter.reload(username);

        create = view.findViewById(R.id.create_localFitness);
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

                            database.localFitnessDao().create(username, name, c, d);
                            adapter.reload(username);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });


         */

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.recordLocalFitnessPosition(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        localGymAdapter = new LocalGymAdapter(username);
        localSportAdapter = new LocalSportAdapter(username);
        localOutdoorAdapter = new LocalOutdoorAdapter(username);
        localHomeAdapter = new LocalHomeAdapter(username);
        localWorkAdapter = new LocalWorkAdapter(username);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.recordAdapters(localGymAdapter, localSportAdapter,
                localOutdoorAdapter, localHomeAdapter, localWorkAdapter);

        adapter.addFragment(new LocalGymFragment(username, localGymAdapter), "Gym");
        adapter.addFragment(new LocalSportFragment(username, localSportAdapter), "Sport");
        adapter.addFragment(new LocalOutdoorFragment(username, localOutdoorAdapter), "Outdoor");
        adapter.addFragment(new LocalHomeFragment(username, localHomeAdapter),"Home");
        adapter.addFragment(new LocalWorkFragment(username, localWorkAdapter), "Work");

        viewPager.setAdapter(adapter);
    }




}
