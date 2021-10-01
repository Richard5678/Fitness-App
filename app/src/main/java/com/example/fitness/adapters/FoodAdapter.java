package com.example.fitness.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness.models.Food;
import com.example.fitness.R;
import com.example.fitness.activities.SingleNutrition;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    String username;
    TextView name;
    TextView calories;
    TextView protein;
    TextView fat;
    TextView carbs;
    TextView mass;
    int fitnessID;

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        LinearLayout foodView;

        FoodViewHolder(View v) {
            super(v);
            foodView = v.findViewById(R.id.food_row);
        }
    }

    private List<Food> foodCollection = new ArrayList<>();

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_row, parent, false);
        return new FoodAdapter.FoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        name = (TextView) holder.foodView.findViewById(R.id.food_row_name);
        calories = (TextView) holder.foodView.findViewById(R.id.food_row_calories);
        protein = (TextView) holder.foodView.findViewById(R.id.food_row_protein);
        fat = (TextView) holder.foodView.findViewById(R.id.food_row_fat);
        carbs = (TextView) holder.foodView.findViewById(R.id.food_row_carbs);
        mass = (TextView) holder.foodView.findViewById(R.id.food_row_mass);


        TextView rowNumber = (TextView) holder.foodView.findViewById(R.id.food_row_number);
        String p = Integer.toString(position + 1);
        rowNumber.setText(p);

        Food f = foodCollection.get(position);
        name.setText(f.getName());
        calories.setText(String.valueOf(f.getCalories()));
        protein.setText(String.valueOf(f.getProtein()));
        fat.setText(String.valueOf(f.getFat()));
        carbs.setText(String.valueOf(f.getCarbs()));
        mass.setText(String.valueOf(f.getMass()));
    }

    @Override
    public int getItemCount() {
        return foodCollection.size();
    }

    public void reload(int fitnessID, String meal) {
        foodCollection = SingleNutrition.foodDatabase.foodDao().getMeal(fitnessID, meal);
        notifyDataSetChanged();
    }

}
