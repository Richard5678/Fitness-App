package com.example.fitness.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fitness.databases.LocalFoodDatabase;
import com.example.fitness.R;
import com.example.fitness.activities.MainActivity;
import com.example.fitness.adapters.LocalFoodAdapter;
import com.example.fitness.databases.UserDatabase;
import com.example.fitness.models.LocalFood;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class LocalFoodFragment extends Fragment {
    String username;
    EditText foodInput;
    Button search;
    private RequestQueue requestQueue;

    private String calories;
    private String protein;
    private String fat;
    private String carbs;

    private TextView n;
    private TextView cal;
    private TextView p;
    private TextView f;
    private TextView car;
    private ImageView imageView;

    private String url;

    private RecyclerView recyclerView;
    private LocalFoodAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static LocalFoodDatabase database;
    public static UserDatabase userDatabase;

    private FloatingActionButton create;
    private Button add;

    EditText nameInput;
    EditText caloriesInput;
    EditText proteinInput;
    EditText fatInput;
    EditText carbsInput;
    EditText quantity;
    EditText imageURL;


    public LocalFoodFragment(String username) {
        this.username = username;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_local_food, container, false);

        database = Room.databaseBuilder(getContext(), LocalFoodDatabase.class,
                "localFoodDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        userDatabase = Room.databaseBuilder(getContext(), UserDatabase.class,
                "Users").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        recyclerView = view.findViewById(R.id.home_food);
        adapter = new LocalFoodAdapter(username);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.recordLocalFood(adapter);


        if (userDatabase.userDao().getFoodInserted(username) == 0) {
            userDatabase.userDao().setFoodInserted(username, 1);
            Log.e("foodInserted", String.valueOf(userDatabase.userDao().getFoodInserted(username)));
            insert();
        }

        adapter.reload(username);
        

        foodInput = view.findViewById(R.id.home_food_input);
        search = view.findViewById(R.id.home_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue = Volley.newRequestQueue(getContext());
                String input = foodInput.getText().toString();
                searchFood(input, false);
            }
        });


        create = view.findViewById(R.id.create_localFood);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogInput = new Dialog(getContext());
                dialogInput.setContentView(R.layout.home_input_dialog);

                dialogInput.show();

                nameInput = (EditText) dialogInput.findViewById(R.id.home_input_dialog_name);
                caloriesInput = (EditText) dialogInput.findViewById(R.id.home_input_dialog_calories);
                proteinInput = (EditText) dialogInput.findViewById(R.id.home_input_dialog_protein);
                fatInput = (EditText) dialogInput.findViewById(R.id.home_input_dialog_fat);
                carbsInput = (EditText) dialogInput.findViewById(R.id.home_input_dialog_carbs);
                quantity = (EditText) dialogInput.findViewById(R.id.home_input_dialog_quantity);
                imageURL = (EditText) dialogInput.findViewById(R.id.home_input_dialog_image);


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

                proteinInput.addTextChangedListener(new TextWatcher() {
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
                                proteinInput.setKeyListener(DigitsKeyListener.getInstance("."));
                            } else {
                                proteinInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            }
                        }

                        if (before < count && s.length() > 1) {
                            if (s.subSequence(start + before, start + count).toString().equals(".")) {
                                allowDot = false;
                            }
                        }

                        if (s.length() != 1) {
                            if (allowDot) {
                                proteinInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            } else {
                                proteinInput.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                fatInput.addTextChangedListener(new TextWatcher() {
                    boolean allowDot = false;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (count > after) {
                            if (s.subSequence(start + after, start + count).toString().equals(".")) {
                                allowDot = true;
                            } else {
                                fatInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            }
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 1) {
                            if (s.subSequence(0, 1).toString().equals("0")) {
                                fatInput.setKeyListener(DigitsKeyListener.getInstance("."));
                            }
                        }

                        if (before < count && s.length() > 1) {
                            if (s.subSequence(start + before, start + count).toString().equals(".")) {
                                allowDot = false;
                            }
                        }

                        if (s.length() != 1) {
                            if (allowDot) {
                                fatInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            } else {
                                fatInput.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                carbsInput.addTextChangedListener(new TextWatcher() {
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
                                carbsInput.setKeyListener(DigitsKeyListener.getInstance("."));
                            } else {
                                carbsInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            }
                        }

                        if (before < count && s.length() > 1) {
                            if (s.subSequence(start + before, start + count).toString().equals(".")) {
                                allowDot = false;
                            }
                        }

                        if (s.length() != 1) {
                            if (allowDot) {
                                carbsInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            } else {
                                carbsInput.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                quantity.addTextChangedListener(new TextWatcher() {
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
                                quantity.setKeyListener(DigitsKeyListener.getInstance("."));
                            } else {
                                quantity.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            }
                        }

                        if (before < count && s.length() > 1) {
                            if (s.subSequence(start + before, start + count).toString().equals(".")) {
                                allowDot = false;
                            }
                        }

                        if (s.length() != 1) {
                            if (allowDot) {
                                quantity.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            } else {
                                quantity.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                add = dialogInput.findViewById(R.id.home_input_dialog_add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nam = nameInput.getText().toString();
                        String calo = caloriesInput.getText().toString();
                        String pro = proteinInput.getText().toString();
                        String fa = fatInput.getText().toString();
                        String carb = carbsInput.getText().toString();
                        String quant = quantity.getText().toString();
                        String imgURL = imageURL.getText().toString();

                        NumberFormat nf = DecimalFormat.getInstance();
                        nf.setMaximumFractionDigits(2);
                        nf.setMinimumFractionDigits(0);

                        TextView notification = (TextView) dialogInput.findViewById(R.id.dialog_notification);

                        if (nam.isEmpty() || calo.isEmpty() || pro.isEmpty() || fa.isEmpty() || carb.isEmpty() || quant.isEmpty()) {
                            notification.setText("All Fields Are Required");
                        } else {
                            float Cal = Float.parseFloat(calo);
                            float pr = Float.parseFloat(pro);
                            float F = Float.parseFloat(fa);
                            float Car = Float.parseFloat(carb);
                            float qua = Float.parseFloat(quant);

                            float cal = Float.parseFloat(nf.format(Cal));
                            float p = Float.parseFloat(nf.format(pr));
                            float f = Float.parseFloat(nf.format(F));
                            float car = Float.parseFloat(nf.format(Car));
                            float q = Float.parseFloat(nf.format(qua));

                            database.localFoodDao().create(username, nam, cal, p, f, car, q, imgURL);
                            adapter.reload(username);
                            dialogInput.dismiss();
                        }
                    }
                });
            }
        });


        //allow user to swipe to delete
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List<LocalFood> localFoodList = database.localFoodDao().getAll(username);
                int id = localFoodList.get(viewHolder.getAdapterPosition()).getId();
                database.localFoodDao().delete(id);
                adapter.reload(username);
            }
        });
        helper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void insert() {
        requestQueue = Volley.newRequestQueue(getContext());

        searchFood("apple", true);
        searchFood("orange", true);
        searchFood("pears", true);
        searchFood("grape", true);
        searchFood("banana", true);
        searchFood("beef", true);
        searchFood("pork", true);
        searchFood("chicken", true);
        adapter.reload(username);
    }


    public void searchFood(final String input, final boolean insert) {
        url = "https://api.edamam.com/api/food-database/v2/parser?nutrition-type=logging&" +
                "ingr=" + input +
                "&app_id=f00bfde6&app_key=587282a72ee266ffb7656e776c1530e9";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String name = response.getString("text");
                    JSONArray parsed = response.getJSONArray("parsed");
                    JSONObject first = parsed.getJSONObject(0);
                    JSONObject food = first.getJSONObject("food");
                    JSONObject nutrients = food.getJSONObject("nutrients");

                    final String imageURL = food.getString("image");

                    calories = nutrients.getString("ENERC_KCAL");
                    protein = nutrients.getString("PROCNT");
                    fat = nutrients.getString("FAT");
                    carbs = nutrients.getString("CHOCDF");

                    if (!insert) {
                        final Dialog displayDialog = new Dialog(getContext());
                        displayDialog.setTitle("nutrition information");
                        displayDialog.setContentView(R.layout.home_display_dialog);

                        n = displayDialog.findViewById(R.id.home_display_dialog_name);
                        cal = displayDialog.findViewById(R.id.home_display_dialog_calories);
                        p = displayDialog.findViewById(R.id.home_display_dialog_protein);
                        f = displayDialog.findViewById(R.id.home_display_dialog_fat);
                        car = displayDialog.findViewById(R.id.home_display_dialog_carbs);
                        Button add = displayDialog.findViewById(R.id.home_display_dialog_add);

                        displayDialog.show();

                        n.setText(name);
                        cal.setText(calories);
                        p.setText(protein);
                        f.setText(fat);
                        car.setText(carbs);

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NumberFormat nf = DecimalFormat.getInstance();
                                nf.setMaximumFractionDigits(2);
                                nf.setMinimumFractionDigits(0);
                                float calo = Float.parseFloat(calories);
                                float pr = Float.parseFloat(protein);
                                float fa = Float.parseFloat(calories);
                                float carb = Float.parseFloat(protein);

                                float cal = Float.parseFloat(nf.format(calo));
                                float p = Float.parseFloat(nf.format(pr));
                                float f = Float.parseFloat(nf.format(fa));
                                float car = Float.parseFloat(nf.format(carb));

                                database.localFoodDao().create(username, name, cal, p, f, car, (float) 100.0, imageURL);
                                adapter.reload(username);
                                displayDialog.dismiss();
                            }
                        });

                    } else {
                        NumberFormat nf = DecimalFormat.getInstance();
                        nf.setMaximumFractionDigits(2);
                        nf.setMinimumFractionDigits(0);
                        float calo = Float.parseFloat(calories);
                        float pr = Float.parseFloat(protein);
                        float fa = Float.parseFloat(calories);
                        float carb = Float.parseFloat(protein);

                        float cal = Float.parseFloat(nf.format(calo));
                        float p = Float.parseFloat(nf.format(pr));
                        float f = Float.parseFloat(nf.format(fa));
                        float car = Float.parseFloat(nf.format(carb));

                        database.localFoodDao().create(username, name, cal, p, f, car, (float) 100.0, imageURL);
                        adapter.reload(username);
                    }





                } catch (JSONException e) {
                    Log.e("food", input, e);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("food doesn't exist!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("food", "food search error", error);
            }
        });
        requestQueue.add(request);
    }
}
