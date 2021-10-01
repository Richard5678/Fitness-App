package com.example.fitness.fragments;

import android.os.Bundle;
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
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.example.fitness.databases.LocalFoodDatabase;
import com.example.fitness.R;
import com.example.fitness.activities.MainActivity;
import com.example.fitness.adapters.SectionPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {
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


    static LocalFoodDatabase database;

    private FloatingActionButton create;
    private Button add;

    EditText nameInput;
    EditText caloriesInput;
    EditText proteinInput;
    EditText fatInput;
    EditText carbsInput;
    EditText quantity;
    EditText imageURL;


    private SectionPagerAdapter sectionPageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);



        //get the username from main activity
        Bundle bundle = this.getArguments();
        username = bundle.getString("username");

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.recordHomePosition(0);

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
                if (tab.getPosition() == 0) {
                    mainActivity.recordHomePosition(0);
                } else {
                    mainActivity.recordLocalFitnessPosition(0);
                }
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

        adapter.addFragment(new LocalFoodFragment(username), "Food");
        adapter.addFragment(new LocalFitnessFragment(username), "Fitness");


        viewPager.setAdapter(adapter);
    }

    /*
        getFragmentManager().beginTransaction().replace(R.id.activity_main, new LocalFoodFragment()).commit();

        recyclerView = view.findViewById(R.id.home_food);
        adapter = new HomeAdapter(username);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        database = Room.databaseBuilder(getContext(), LocalFoodDatabase.class,
                "localFoodDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        adapter.reload(username);

        foodInput = view.findViewById(R.id.home_food_input);
        search = view.findViewById(R.id.home_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue = Volley.newRequestQueue(getContext());
                String input = foodInput.getText().toString();
                searchFood(input);
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
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List<LocalFood> localFoodList = database.localFoodDao().getAll(username);
                int id = localFoodList.get(viewHolder.getAdapterPosition()).id;
                database.localFoodDao().delete(id);
                adapter.reload(username);
            }
        });
        helper.attachToRecyclerView(recyclerView);

        return view;
    }



    public void searchFood(String input) {
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



                } catch (JSONException e) {
                    Log.e("food", "food search error", e);
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

 */

}
