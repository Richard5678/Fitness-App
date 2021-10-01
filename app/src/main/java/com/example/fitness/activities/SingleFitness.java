package com.example.fitness.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fitness.adapters.ExerciseAdapter;
import com.example.fitness.databases.ExerciseDatabase;
import com.example.fitness.databases.FitnessDatabase;
import com.example.fitness.databases.FoodDatabase;
import com.example.fitness.databases.LocalFoodDatabase;
import com.example.fitness.R;
import com.example.fitness.adapters.FoodAdapter;
import com.example.fitness.fragments.LocalFitnessFragment;
import com.example.fitness.fragments.PickDateFragment;
import com.example.fitness.models.LocalFitness;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SingleFitness extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText additionalInfo;
    private int fitnessID;
    private TextView dateDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Button changeDate;

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Button add;
    private Button delete;

    public static FoodDatabase foodDatabase;
    public static FitnessDatabase fitnessDatabase;
    public static ExerciseDatabase exerciseDatabase;

    private String username;
    String unit;
    EditText mass;
    String nam;

    private String u = "kg";
    private String exerciseName = "Weight Lifting: general";


    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fitness);

        //get fitness database
        fitnessDatabase = Room.databaseBuilder(this, FitnessDatabase.class,
                "fitnessDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        foodDatabase = Room.databaseBuilder(this, FoodDatabase.class,
                "foodDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        exerciseDatabase = Room.databaseBuilder(this, ExerciseDatabase.class,
                "exerciseDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        start = findViewById(R.id.start_walking);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GoogleMap.class);
                startActivity(intent);
            }
        });

        //wire up local properties and set date format
        dateDisplay = findViewById(R.id.date_display);
        additionalInfo = findViewById(R.id.edit_text);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

        //get data passed in from fitness fragment
        fitnessID = getIntent().getIntExtra("id", 0);
        date = getIntent().getStringExtra("date");
        String text = getIntent().getStringExtra("contents");
        username = getIntent().getStringExtra("username");

        //set additional information and date
        additionalInfo.setText(text);
        dateDisplay.setText(date);

        recyclerView = findViewById(R.id.single_fitness_recyclerview);
        adapter = new ExerciseAdapter();
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        adapter.reload(fitnessID);

        //change the date displayed on the top
        changeDate = findViewById(R.id.change_date);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment pickDate = new PickDateFragment();
                pickDate.show(getSupportFragmentManager(), "pick date");
            }
        });


        add = findViewById(R.id.single_fitness_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LocalFitnessFragment.userDatabase.userDao().getMassStored(username) == 0) {
                    final Dialog d = new Dialog(SingleFitness.this);
                    d.setContentView(R.layout.mass_dialog_input);
                    d.show();
                    final EditText massInput = d.findViewById(R.id.mass_dialog_number);
                    final Spinner unit = (Spinner) d.findViewById(R.id.mass_dialog_unit);

                    String[] units = new String[] {"kg", "pound"};
                    unit.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, units));
                    unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            u = unit.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    massInput.addTextChangedListener(new TextWatcher() {
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
                                    massInput.setKeyListener(DigitsKeyListener.getInstance("."));
                                } else {
                                    massInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                                }
                            }

                            if (before < count && s.length() > 1) {
                                if (s.subSequence(start + before, start + count).toString().equals(".")) {
                                    allowDot = false;
                                }
                            }

                            if (s.length() != 1) {
                                if (allowDot) {
                                    massInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                                } else {
                                    massInput.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    Button save = d.findViewById(R.id.mass_dialog_save);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String m = massInput.getText().toString();
                            if (!m.isEmpty()) {
                                double factor;
                                if (u.equals("kg")) {
                                    factor = 1;
                                } else {
                                    factor = 2.20462262;
                                }
                                Float mas = Float.parseFloat(m);
                                NumberFormat nf = DecimalFormat.getInstance();
                                nf.setMaximumFractionDigits(2);
                                LocalFitnessFragment.userDatabase.userDao().setMass(Float.parseFloat(nf.format(mas / factor)), username);
                                LocalFitnessFragment.userDatabase.userDao().setMassStored(1, username);
                                d.dismiss();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                builder.setMessage("Please enter a mass")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                    });
                }

                if (LocalFitnessFragment.userDatabase.userDao().getMassStored(username) == 1) {
                    final Dialog dialog = new Dialog(SingleFitness.this);
                    dialog.setContentView(R.layout.singlefitness_dialog);
                    dialog.show();

                    final Spinner exerciseListDropDown = (Spinner) dialog.findViewById(R.id.single_fitness_dialog_spinner_fitness);
                    final EditText duration = (EditText) dialog.findViewById(R.id.single_fitness_dialog_duration_minute);
                    Button save = dialog.findViewById(R.id.single_fitness_dialog_save);

                    List<String> exercises = LocalFitnessFragment.database.localFitnessDao().getNames(username);
                    exerciseListDropDown.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, exercises));
                    exerciseListDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            exerciseName = exerciseListDropDown.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    duration.addTextChangedListener(new TextWatcher() {
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
                                    duration.setKeyListener(DigitsKeyListener.getInstance("."));
                                } else {
                                    duration.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                                }
                            }

                            if (before < count && s.length() > 1) {
                                if (s.subSequence(start + before, start + count).toString().equals(".")) {
                                    allowDot = false;
                                }
                            }

                            if (s.length() != 1) {
                                if (allowDot) {
                                    duration.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                                } else {
                                    duration.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String d = duration.getText().toString();
                            if (d.isEmpty()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                builder.setMessage("Please enter the duration")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                NumberFormat nf = DecimalFormat.getInstance();
                                nf.setMinimumFractionDigits(0);
                                nf.setMaximumFractionDigits(2);

                                LocalFitness current = LocalFitnessFragment.database.localFitnessDao().getSingle(exerciseName);
                                float mass = LocalFitnessFragment.userDatabase.userDao().getMass(username);
                                double kgToPound = 2.20462262;
                                float duration = Float.parseFloat(d);
                                float cal = (float) (current.getCalories() / 125 * mass * kgToPound / 30 * duration);
                                exerciseDatabase.exerciseDao().create(fitnessID, exerciseName,
                                        Float.parseFloat(nf.format(cal)),
                                        Float.parseFloat(nf.format(duration)),
                                        current.getType());
                                adapter.reload(fitnessID);
                                dialog.dismiss();
                            }
                        }
                    });
                }

            }
        });

        delete = findViewById(R.id.single_fitness_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseDatabase.exerciseDao().deleteLast(fitnessID);
                adapter.reload(fitnessID);
            }
        });




    }

    //set and display date on the top of the screen
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        fitnessDatabase.fitnessDao().updateDateInt(fitnessID, year, month, dayOfMonth);
        date = dateFormat.format(c.getTime());
        dateDisplay.setText(date);
    }

    //called when existing this activity. Store changes in database in this case
    @Override
    protected void onPause() {
        super.onPause();
        fitnessDatabase.fitnessDao().save(additionalInfo.getText().toString(),
                dateDisplay.getText().toString(), fitnessID);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
