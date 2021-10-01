package com.example.fitness.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness.models.Nutrition;
import com.example.fitness.fragments.NutritionFragment;
import com.example.fitness.R;
import com.example.fitness.activities.SingleNutrition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.FitnessViewHolder>{
    String username;


    public NutritionAdapter(String username) {
        this.username = username;
    }

    public static class FitnessViewHolder extends RecyclerView.ViewHolder {
        LinearLayout nutritionView;
        TextView nutritionDate;


        FitnessViewHolder(View v) {
            super(v);
            //wire up local properties
            nutritionView = v.findViewById(R.id.fitness_row);
            nutritionDate = v.findViewById(R.id.fitness_row_date);

            //go to a single fitness activity when the row is clicked
            //pass in id, date and contents
            nutritionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Nutrition current = (Nutrition) nutritionView.getTag(R.id.KEY_1);
                    String username = (String) nutritionView.getTag(R.id.KEY_2);
                    Intent intent = new Intent(v.getContext(), SingleNutrition.class);
                    intent.putExtra("id", current.getId());
                    intent.putExtra("date", current.getDate());
                    intent.putExtra("contents", current.getContents());
                    intent.putExtra("username", username);

                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    //instantiate an arraylist to store all the fitness activities
    private List<Nutrition> nutritionCollection = new ArrayList<>();

    //inflate fitness_row to display fitness activities in rows in the fitness fragment
    @NonNull
    @Override
    public FitnessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fitness_row, parent, false);
        return new FitnessViewHolder(v);
    }

    //automatically adjust the view of the view holder in the fitness fragment when any modifications are made
    //for example, new activities added, activities deleted.
    @Override
    public void onBindViewHolder(@NonNull FitnessViewHolder holder, int position) {
        nutritionCollection = NutritionFragment.nutritionDatabase.nutritionDao().getAllNutrition(username);
        Collections.reverse(nutritionCollection);
        Nutrition current = nutritionCollection.get(position);
        holder.nutritionDate.setText(current.getDate());
        holder.nutritionView.setTag(R.id.KEY_1, current);
        holder.nutritionView.setTag(R.id.KEY_2, username);
        TextView number = (TextView) holder.nutritionView.findViewById(R.id.fitness_row_number);
        number.setText(Integer.toString(nutritionCollection.size() - position));
    }

    //return the number of fitness activities
    @Override
    public int getItemCount() {
        return nutritionCollection.size();
    }

    //reload fitness fragment
    public void reload(String username) {
        nutritionCollection = NutritionFragment.nutritionDatabase.nutritionDao().getAllNutrition(username);
        notifyDataSetChanged();
    }
}
