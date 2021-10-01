package com.example.fitness.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness.R;
import com.example.fitness.fragments.LocalFitnessFragment;
import com.example.fitness.models.Fitness;
import com.example.fitness.models.LocalFitness;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SingleFitnessAdapter extends RecyclerView.Adapter<SingleFitnessAdapter.SingleFitnessViewHolder> {
    private TextView name;
    private TextView energy;
    private TextView duration;
    private TextView type;
    private TextView number;

    public class SingleFitnessViewHolder extends RecyclerView.ViewHolder {
        LinearLayout fitnessRow;

        public SingleFitnessViewHolder(@NonNull View itemView) {
            super(itemView);

            fitnessRow = itemView.findViewById(R.id.single_fitness_row);
        }
    }

    private List<LocalFitness> fitnessList = new ArrayList<>();

    @NonNull
    @Override
    public SingleFitnessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_fitness_row, parent, false);
        return new SingleFitnessViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleFitnessViewHolder holder, int position) {
        name = holder.fitnessRow.findViewById(R.id.single_fitness_row_name);
        energy = holder.fitnessRow.findViewById(R.id.single_fitness_row_energy);
        duration = holder.fitnessRow.findViewById(R.id.single_fitness_row_duration);
        type = holder.fitnessRow.findViewById(R.id.single_fitness_row_type);
        number = holder.fitnessRow.findViewById(R.id.single_fitness_row_number);

        number.setText(String.valueOf(position + 1));

        LocalFitness localFitness = fitnessList.get(position);

        name.setText(localFitness.getName());
        energy.setText(String.valueOf(localFitness.getCalories()));
        duration.setText(String.valueOf(localFitness.getDuration()));
        type.setText(localFitness.getType());
    }

    @Override
    public int getItemCount() {
        return fitnessList.size();
    }

    public void reload(List<Integer> fitnessID) {
        for (int i = 0; i < fitnessID.size(); i++) {
            fitnessList.add(LocalFitnessFragment.database.localFitnessDao().getViewId(fitnessID.get(i)));
        }
        notifyDataSetChanged();
    }

}
