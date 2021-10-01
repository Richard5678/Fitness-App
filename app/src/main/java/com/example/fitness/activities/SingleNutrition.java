package com.example.fitness.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness.models.Food;
import com.example.fitness.databases.FoodDatabase;
import com.example.fitness.models.LocalFood;
import com.example.fitness.databases.LocalFoodDatabase;
import com.example.fitness.fragments.NutritionFragment;
import com.example.fitness.fragments.PickDateFragment;
import com.example.fitness.R;
import com.example.fitness.adapters.FoodAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;




public class SingleNutrition extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText additionalInfo;
    private int nutritionID;
    private TextView dateDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Button changeDate;

    private RecyclerView breakfastRecyclerView;
    private RecyclerView.LayoutManager breakfastLayoutManager;
    private FoodAdapter breakfastAdapter;

    private RecyclerView lunchRecyclerView;
    private RecyclerView.LayoutManager lunchLayoutManager;
    private FoodAdapter lunchAdapter;

    private RecyclerView dinnerRecyclerView;
    private RecyclerView.LayoutManager dinnerLayoutManager;
    private FoodAdapter dinnerAdapter;

    private RecyclerView snakeRecyclerView;
    private RecyclerView.LayoutManager snakeLayoutManager;
    private FoodAdapter snakeAdapter;

    public static FoodDatabase foodDatabase;


    private Button addBreakfast;
    private Button deleteBreakfast;
    private Button addLunch;
    private Button deleteLunch;
    private Button addDinner;
    private Button deleteDinner;
    private Button addSnake;
    private Button deleteSnake;
    private String username;


    private LocalFoodDatabase localFoodDatabase;

    String name;
    EditText mass;
    String unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_nutrition);

        foodDatabase = Room.databaseBuilder(this, FoodDatabase.class,
                "foodDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        localFoodDatabase = Room.databaseBuilder(this, LocalFoodDatabase.class,
                "localFoodDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //wire up local properties and set date format
        dateDisplay = findViewById(R.id.date_display);
        additionalInfo = findViewById(R.id.edit_text);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

        //get data passed in from nutrition fragment
        nutritionID = getIntent().getIntExtra("id", 0);
        date = getIntent().getStringExtra("date");
        String text = getIntent().getStringExtra("contents");
        username = getIntent().getStringExtra("username");

        //set additional information and date
        additionalInfo.setText(text);
        dateDisplay.setText(date);

        //change the date displayed on the top
        changeDate = findViewById(R.id.change_date);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment pickDate = new PickDateFragment();
                pickDate.show(getSupportFragmentManager(), "pick date");
            }
        });


        //setup breakfast recyclerview
        breakfastRecyclerView = findViewById(R.id.breakfast);
        breakfastAdapter = new FoodAdapter();
        breakfastLayoutManager = new LinearLayoutManager(this);

        breakfastRecyclerView.setAdapter(breakfastAdapter);
        breakfastRecyclerView.setLayoutManager(breakfastLayoutManager);

        //wire up buttons regarding the breakfast recyclerview
        addBreakfast = findViewById(R.id.breakfast_add_food);
        addBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog("breakfast", breakfastAdapter);
            }
        });

        deleteBreakfast= findViewById(R.id.breakfast_delete_food);
        deleteBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodDatabase.foodDao().deleteMealLast("breakfast");
                ;
                breakfastAdapter.reload(nutritionID, "breakfast");
            }
        });


        //setup lunch recyclerview
        lunchRecyclerView = findViewById(R.id.lunch);
        lunchAdapter = new FoodAdapter();
        lunchLayoutManager = new LinearLayoutManager(this);

        lunchRecyclerView.setAdapter(lunchAdapter);
        lunchRecyclerView.setLayoutManager(lunchLayoutManager);

        //wire up buttons regarding the lunch recyclerview
        addLunch = findViewById(R.id.lunch_add_food);
        addLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog("lunch", lunchAdapter);
            }
        });

        deleteLunch = findViewById(R.id.lunch_delete_food);
        deleteLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodDatabase.foodDao().deleteMealLast("lunch");
                ;
                lunchAdapter.reload(nutritionID, "lunch");
            }
        });


        //setup dinner recyclerview
        dinnerRecyclerView = findViewById(R.id.dinner);
        dinnerAdapter = new FoodAdapter();
        dinnerLayoutManager = new LinearLayoutManager(this);

        dinnerRecyclerView.setAdapter(dinnerAdapter);
        dinnerRecyclerView.setLayoutManager(dinnerLayoutManager);

        //wire up buttons regarding the dinner recyclerview
        addDinner = findViewById(R.id.dinner_add_food);
        addDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog("dinner", dinnerAdapter);
            }
        });

        deleteDinner = findViewById(R.id.dinner_delete_food);
        deleteDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodDatabase.foodDao().deleteMealLast("dinner");
                ;
                dinnerAdapter.reload(nutritionID, "dinner");
            }
        });


        //setup snake recyclerview
        snakeRecyclerView = findViewById(R.id.snakes);
        snakeAdapter = new FoodAdapter();
        snakeLayoutManager = new LinearLayoutManager(this);

        snakeRecyclerView.setAdapter(snakeAdapter);
        snakeRecyclerView.setLayoutManager(snakeLayoutManager);

        //wire up buttons regarding the snake recyclerview
        addSnake = findViewById(R.id.snakes_add_food);
        addSnake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog("snake", snakeAdapter);
            }
        });

        deleteSnake = findViewById(R.id.snakes_delete_food);
        deleteSnake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodDatabase.foodDao().deleteMealLast("snake");
                ;
                snakeAdapter.reload(nutritionID, "snake");
            }
        });


        //swipe to delete function for breakfast
        ItemTouchHelper helper1 = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List<Food> foodCollection = foodDatabase.foodDao().getMeal(nutritionID, "breakfast");
                int id = foodCollection.get(viewHolder.getAdapterPosition()).getId();
                foodDatabase.foodDao().delete_id(id);
                breakfastAdapter.reload(nutritionID, "breakfast");
            }
        });
        helper1.attachToRecyclerView(breakfastRecyclerView);


        //swipe to delete function for lunch
        ItemTouchHelper helper2 = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List<Food> foodCollection = foodDatabase.foodDao().getMeal(nutritionID, "lunch");
                int id = foodCollection.get(viewHolder.getAdapterPosition()).getId();
                foodDatabase.foodDao().delete_id(id);
                lunchAdapter.reload(nutritionID, "lunch");
            }
        });
        helper2.attachToRecyclerView(lunchRecyclerView);


        //swipe to delete function for dinner
        ItemTouchHelper helper3 = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List<Food> foodCollection = foodDatabase.foodDao().getMeal(nutritionID, "dinner");
                int id = foodCollection.get(viewHolder.getAdapterPosition()).getId();
                foodDatabase.foodDao().delete_id(id);
                dinnerAdapter.reload(nutritionID, "dinner");
            }
        });
        helper3.attachToRecyclerView(dinnerRecyclerView);


        //swipe to delete function for snake
        ItemTouchHelper helper4 = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List<Food> foodCollection = foodDatabase.foodDao().getMeal(nutritionID, "snake");
                int id = foodCollection.get(viewHolder.getAdapterPosition()).getId();
                foodDatabase.foodDao().delete_id(id);
                snakeAdapter.reload(nutritionID, "snake");
            }
        });
        helper4.attachToRecyclerView(snakeRecyclerView);
    }


    public void displayDialog(final String meal, final FoodAdapter adapter) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.singlenutrition_dialog);
        dialog.setTitle("new food");
        dialog.show();

        final Spinner foodListDropDown = (Spinner) dialog.findViewById(R.id.nutrition_spinner);

        List<String> items = localFoodDatabase.localFoodDao().getAllNames(username);
        foodListDropDown.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));
        foodListDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name = foodListDropDown.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner unitsDropDown = (Spinner) dialog.findViewById(R.id.nutrition_unit_spinner);

        String [] units = {"g", "kg", "pound"};
        unitsDropDown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units));
        unitsDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit = unitsDropDown.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mass = dialog.findViewById(R.id.nutrition_mass);

        Button save = (Button) dialog.findViewById(R.id.single_nutrition_dialog_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double factor;
                if (unit.equals("g")) {
                    factor = 100;
                } else if (unit.equals("kg")) {
                    factor = 0.1;
                } else {
                    factor = 0.220462262;
                }

                if(!mass.getText().toString().isEmpty()) {
                    Float m = Float.parseFloat(mass.getText().toString());
                    LocalFood localFood = localFoodDatabase.localFoodDao().getLocalFood(username, name);
                    Float calories = (float) (localFood.getCalories() * m / factor);
                    Float protein = (float) (localFood.getProtein() * m / factor);
                    Float fat = (float) (localFood.getFat() * m / factor);
                    Float carbs = (float) (localFood.getCarbs() * m / factor);

                    NumberFormat nf = DecimalFormat.getInstance();
                    nf.setMaximumFractionDigits(2);

                    Float cal = Float.parseFloat(nf.format(calories));
                    Float p = Float.parseFloat(nf.format(protein));
                    Float f = Float.parseFloat(nf.format(fat));
                    Float car = Float.parseFloat(nf.format(carbs));


                    foodDatabase.foodDao().create(nutritionID, meal, name, cal, p, f, car,
                            Float.parseFloat(nf.format(m / factor * 100)));
                    adapter.reload(nutritionID, meal);
                    dialog.dismiss();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setMessage("Please enter your mass.")
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


/*
        name = (EditText) d.findViewById(R.id.dialog_name);
        calories = (EditText) d.findViewById(R.id.dialog_calories);
        protein = (EditText) d.findViewById(R.id.dialog_protein);
        fat = (EditText) d.findViewById(R.id.dialog_fat);
        sugar = (EditText) d.findViewById(R.id.dialog_sugar);
        save = (Button) d.findViewById(R.id.dialog_save);





        calories.addTextChangedListener(new TextWatcher() {
            boolean allowDot = true;
            boolean flag = false;

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
                        calories.setKeyListener(DigitsKeyListener.getInstance("."));
                    } else {
                        calories.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                    }
                }

                if (before < count && s.length() > 1) {
                    if (s.subSequence(start + before, start + count).toString().equals(".")) {
                        allowDot = false;
                    }
                }

                if (s.length() != 1) {
                    if (allowDot) {
                        calories.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                    } else {
                        calories.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        protein.addTextChangedListener(new TextWatcher() {
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
                        protein.setKeyListener(DigitsKeyListener.getInstance("."));
                    } else {
                        protein.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                    }
                }

                if (before < count && s.length() > 1) {
                    if (s.subSequence(start + before, start + count).toString().equals(".")) {
                        allowDot = false;
                    }
                }

                if (s.length() != 1) {
                    if (allowDot) {
                        protein.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                    } else {
                        protein.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        fat.addTextChangedListener(new TextWatcher() {
            boolean allowDot = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count > after) {
                    if (s.subSequence(start + after, start + count).toString().equals(".")) {
                        allowDot = true;
                    } else {
                        fat.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    if (s.subSequence(0, 1).toString().equals("0")) {
                        fat.setKeyListener(DigitsKeyListener.getInstance("."));
                    }
                }

                if (before < count && s.length() > 1) {
                    if (s.subSequence(start + before, start + count).toString().equals(".")) {
                        allowDot = false;
                    }
                }

                if (s.length() != 1) {
                    if (allowDot) {
                        fat.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                    } else {
                        fat.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        sugar.addTextChangedListener(new TextWatcher() {
            boolean allowDot = false;

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
                        sugar.setKeyListener(DigitsKeyListener.getInstance("."));
                    } else {
                        sugar.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                    }
                }

                if (before < count && s.length() > 1) {
                    if (s.subSequence(start + before, start + count).toString().equals(".")) {
                        allowDot = false;
                    }
                }

                if (s.length() != 1) {
                    if (allowDot) {
                        sugar.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                    } else {
                        sugar.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
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
                String nam = name.getText().toString();
                String cal = calories.getText().toString();
                String pro = protein.getText().toString();
                String fa = fat.getText().toString();
                String sug = sugar.getText().toString();

                NumberFormat nf = DecimalFormat.getInstance();
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(0);

                TextView notification = (TextView) d.findViewById(R.id.dialog_notification);

                if (nam.isEmpty() || cal.isEmpty() || pro.isEmpty() || fa.isEmpty() || sug.isEmpty()) {
                    notification.setText("All Fields Are Required");
                } else {
                    float ca = Float.parseFloat(cal);
                    float pr = Float.parseFloat(pro);
                    float F = Float.parseFloat(fa);
                    float su = Float.parseFloat(sug);

                    float c = Float.parseFloat(nf.format(ca));
                    float p = Float.parseFloat(nf.format(pr));
                    float f = Float.parseFloat(nf.format(F));
                    float s = Float.parseFloat(nf.format(su));

                    foodDatabase.foodDao().create(nutritionID, meal, nam, c, p, f, s);
                    Log.d("nutritionID: ", String.valueOf(nutritionID));
                    adapter.reload(nutritionID, meal);
                    d.dismiss();
                }

            }
        });

*/

    }


    //set and display date on the top of the screen
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        NutritionFragment.nutritionDatabase.nutritionDao().updateDateInt(nutritionID, year, month, dayOfMonth);
        date = dateFormat.format(c.getTime());
        dateDisplay.setText(date);
    }

    //called when existing this activity. Store changes in database in this case
    @Override
    protected void onPause() {
        super.onPause();
        NutritionFragment.nutritionDatabase.nutritionDao().save(additionalInfo.getText().toString(),
                dateDisplay.getText().toString(), nutritionID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        breakfastAdapter.reload(nutritionID, "breakfast");
        lunchAdapter.reload(nutritionID, "lunch");
        dinnerAdapter.reload(nutritionID, "dinner");
        snakeAdapter.reload(nutritionID, "snake");
    }
}