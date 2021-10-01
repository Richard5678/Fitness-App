package com.example.fitness.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness.models.Fitness;
import com.example.fitness.fragments.FitnessFragment;
import com.example.fitness.R;
import com.example.fitness.activities.SingleFitness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FitnessAdapter extends RecyclerView.Adapter<FitnessAdapter.FitnessViewHolder>{
    String username;


    public FitnessAdapter(String username) {
        this.username = username;
    }

    public static class FitnessViewHolder extends RecyclerView.ViewHolder {
        LinearLayout fitnessView;
        TextView fitnessDate;


        FitnessViewHolder(View v) {
            super(v);
            //wire up local properties
            fitnessView = v.findViewById(R.id.fitness_row);
            fitnessDate = v.findViewById(R.id.fitness_row_date);

            //go to a single fitness activity when the row is clicked
            //pass in id, date and contents
            fitnessView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fitness current = (Fitness)fitnessView.getTag(R.id.KEY_1);
                    String username = (String) fitnessView.getTag(R.id.KEY_2);
                    Intent intent = new Intent(v.getContext(), SingleFitness.class);
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
    private List<Fitness> fitnessCollection = new ArrayList<>();

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
        fitnessCollection = FitnessFragment.fitnessDatabase.fitnessDao().getAllFitness(username);
        Fitness current = fitnessCollection.get(position);
        holder.fitnessDate.setText(current.getDate());
        holder.fitnessView.setTag(R.id.KEY_1, current);
        holder.fitnessView.setTag(R.id.KEY_2, username);
        TextView number = (TextView) holder.fitnessView.findViewById(R.id.fitness_row_number);
        number.setText(Integer.toString(fitnessCollection.size() - position));
    }

    //return the number of fitness activities
    @Override
    public int getItemCount() {
        return fitnessCollection.size();
    }

    //reload fitness fragment
    public void reload(String username) {
        fitnessCollection = FitnessFragment.fitnessDatabase.fitnessDao().getAllFitness(username);
        Collections.reverse(fitnessCollection);
        notifyDataSetChanged();
    }
}
