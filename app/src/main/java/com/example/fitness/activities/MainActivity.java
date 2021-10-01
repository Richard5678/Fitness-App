package com.example.fitness.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.Toolbar;


import com.example.fitness.adapters.LocalGymAdapter;
import com.example.fitness.adapters.LocalHomeAdapter;
import com.example.fitness.adapters.LocalOutdoorAdapter;
import com.example.fitness.adapters.LocalSportAdapter;
import com.example.fitness.adapters.LocalWorkAdapter;
import com.example.fitness.helpers.CustomedBottomNagivationView;
import com.example.fitness.adapters.FitnessAdapter;
import com.example.fitness.fragments.FitnessFragment;
import com.example.fitness.fragments.HomeFragment;
import com.example.fitness.adapters.LocalFoodAdapter;
import com.example.fitness.adapters.NutritionAdapter;
import com.example.fitness.fragments.NutritionFragment;
import com.example.fitness.R;
import com.example.fitness.helpers.Session;
import com.example.fitness.fragments.StatisticsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    String username;
    Session session;
    Toolbar toolbar;
    boolean searching = false;
    String fragmentName;
    String adapterName;

    private FitnessAdapter fitnessAdapter;
    private NutritionAdapter nutritionAdapter;
    private RecyclerView.Adapter settingAdapter;

    private LocalFoodAdapter localFoodAdapter;
    private LocalGymAdapter localGymAdapter;
    private LocalSportAdapter localSportAdapter;
    private LocalOutdoorAdapter localOutdoorAdapter;
    private LocalHomeAdapter localHomeAdapter;
    private LocalWorkAdapter localWorkAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_toolbar, menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //create a new session and get the username
        session = new Session(this);
        username = session.getUsername();

        //connect local property with costumed navigation bar in the layout
        CustomedBottomNagivationView bottomNagivationView = findViewById(R.id.bottomNavigationBar);

        //user bundle to pass username to home fragment
        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        Fragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);

        //automatically go to home fragment when main activity is called
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, homeFragment).commit();

        //switch fragments when tabs on the bottom navigation bar are clicked
        //username is passed to the new fragment
        bottomNagivationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selected = null;
                switch (menuItem.getItemId()) {
                    case R.id.ic_home:
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("username", username);
                        Fragment homeFragment = new HomeFragment();
                        homeFragment.setArguments(bundle1);
                        selected = homeFragment;

                        break;
                    case R.id.ic_fitness:
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("username", username);
                        Fragment fitnessFragment = new FitnessFragment();
                        fitnessFragment.setArguments(bundle2);
                        selected = fitnessFragment;

                        break;
                    case R.id.ic_nutrition:
                        Bundle bundle3 = new Bundle();
                        bundle3.putString("username", username);
                        Fragment nutritionFragment = new NutritionFragment();
                        nutritionFragment.setArguments(bundle3);
                        selected = nutritionFragment;

                        break;
                    case R.id.ic_stats:
                        Bundle bundle4 = new Bundle();
                        bundle4.putString("username", username);
                        Fragment statisticsFragment = new StatisticsFragment();
                        statisticsFragment.setArguments(bundle4);
                        selected = statisticsFragment;

                        break;

                }

                //switch fragments
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, selected).commit();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                session.setLoggedin(false);
                finish();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void recordLocalFood(LocalFoodAdapter adapter) {
        localFoodAdapter = adapter;
        Log.e("name:", fragmentName);
    }

    public void recordFitness(String fragmentName, FitnessAdapter adapter) {
        this.fragmentName = fragmentName;
        fitnessAdapter = adapter;
        Log.e("name:", fragmentName);
    }

    public void recordNutrition(String fragmentName, NutritionAdapter adapter) {
        this.fragmentName = fragmentName;
        nutritionAdapter = adapter;
        Log.e("name:", fragmentName);
    }

    public void recordSetting(String fragmentName, RecyclerView.Adapter adapter) {
        this.fragmentName = fragmentName;
        settingAdapter = adapter;
        Log.e("name:", fragmentName);
    }

    public void recordGym(LocalGymAdapter adapter) {
        localGymAdapter = adapter;
    }

    public void recordSport(LocalSportAdapter adapter) { localSportAdapter = adapter; }

    public void recordOutdoor(LocalOutdoorAdapter adapter) { localOutdoorAdapter = adapter; }

    public void recordHome(LocalHomeAdapter adapter) { localHomeAdapter = adapter; }

    public void recordWork(LocalWorkAdapter adapter) { localWorkAdapter = adapter; }

    public void recordHomePosition(int position) {
        if (position == 0) {
            fragmentName = "localFood";
            Log.e("fragment", fragmentName);
        } else {
            fragmentName = "localFitness";
            Log.e("fragment", fragmentName);
        }
    }


    public void recordLocalFitnessPosition(int position) {
        Log.e("mainPosition", String.valueOf(position));
        if (position == 0) {
            fragmentName = "localGym";
            localGymAdapter.reload(username);
        } else if (position == 1) {
            fragmentName = "localSport";
            localSportAdapter.reload(username);
        } else if (position == 2) {
            fragmentName = "localOutdoor";
            localOutdoorAdapter.reload(username);
        } else if (position == 3) {
            fragmentName = "localHome";
            localHomeAdapter.reload(username);
        } else if (position == 4){
            fragmentName = "localWork";
            localWorkAdapter.reload(username);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (fragmentName.equals("localGym")) {
            localGymAdapter.getFilter().filter(query);
            Log.e("success,","fitness");
        } else if (fragmentName.equals("localFood")) {
            localFoodAdapter.getFilter().filter(query);
        } else if (fragmentName.equals("localSport")) {
            localSportAdapter.getFilter().filter(query);
        } else if (fragmentName.equals("localOutdoor")) {
            localOutdoorAdapter.getFilter().filter(query);
        } else if (fragmentName.equals("localHome")) {
            localHomeAdapter.getFilter().filter(query);
        } else if (fragmentName.equals("localWork")) {
            localWorkAdapter.getFilter().filter(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (fragmentName.equals("localGym")) {
            localGymAdapter.getFilter().filter(newText);
            Log.e("success,","fitness");
        } else if (fragmentName.equals("localFood")) {
            localFoodAdapter.getFilter().filter(newText);
        } else if (fragmentName.equals("localSport")) {
            localSportAdapter.getFilter().filter(newText);
        } else if (fragmentName.equals("localOutdoor")) {
            localOutdoorAdapter.getFilter().filter(newText);
        } else if (fragmentName.equals("localHome")) {
            localHomeAdapter.getFilter().filter(newText);
        } else if (fragmentName.equals("localWork")) {
            localWorkAdapter.getFilter().filter(newText);
        }
        return false;
    }

    public void recordAdapters(LocalGymAdapter localGymAdapter, LocalSportAdapter localSportAdapter, LocalOutdoorAdapter localOutdoorAdapter, LocalHomeAdapter localHomeAdapter, LocalWorkAdapter localWorkAdapter) {
        this.localGymAdapter = localGymAdapter;
        this.localSportAdapter = localSportAdapter;
        this.localOutdoorAdapter = localOutdoorAdapter;
        this.localHomeAdapter = localHomeAdapter;
        this.localWorkAdapter = localWorkAdapter;
    }
}
