package com.example.fitness.adapters;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness.R;
import com.example.fitness.activities.SingleFitness;
import com.example.fitness.activities.SingleLocalFitness;
import com.example.fitness.fragments.LocalFitnessFragment;
import com.example.fitness.fragments.LocalGymFragment;
import com.example.fitness.models.LocalFitness;

import java.util.ArrayList;
import java.util.List;

public class LocalGymAdapter extends RecyclerView.Adapter<LocalGymAdapter.LocalGymViewHolder> implements Filterable {
    private String username;
    private boolean searching = false;

    public LocalGymAdapter(String username) {
        this.username = username;
    }


    private List<LocalFitness> localFitnesses = LocalFitnessFragment.database.localFitnessDao().get(username, "Gym");
    private List<LocalFitness> filteredLocalFitness = new ArrayList<>();
    private List<LocalFitness> filtered = new ArrayList<>();

    @Override
    public Filter getFilter() {
        return new LocalGymFilter();
    }

    public class LocalGymFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredLocalFitness.clear();
            if (constraint.length() != 0) {
                searching = true;
            } else {
                searching = false;
            }

            if (searching) {
                for (int i = 0; i < localFitnesses.size(); i++) {
                    LocalFitness current = localFitnesses.get(i);
                    if (current.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredLocalFitness.add(current);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredLocalFitness;
            results.count = filteredLocalFitness.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered = (List<LocalFitness>) results.values;
            notifyDataSetChanged();
        }
    }


    public class LocalGymViewHolder extends RecyclerView.ViewHolder {
        TextView localFitnessName;
        LinearLayout localFitnessRow;
        public LocalGymViewHolder(@NonNull final View itemView) {
            super(itemView);

            localFitnessName = itemView.findViewById(R.id.home_row_name);
            localFitnessRow = itemView.findViewById(R.id.home_row);

            localFitnessRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalFitness current = (LocalFitness) localFitnessRow.getTag();
                    Intent intent = new Intent(itemView.getContext(), SingleLocalFitness.class);
                    intent.putExtra("name", current.getName());
                    intent.putExtra("calories", current.getCalories());
                    intent.putExtra("duration", current.getDuration());

                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }

    @NonNull
    @Override
    public LocalGymViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_row, parent, false);
        return new LocalGymViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LocalGymViewHolder holder, int position) {
        LocalFitness current;
        if (searching) {
            current = filtered.get(position);
        } else {
            localFitnesses = LocalFitnessFragment.database.localFitnessDao().get(username, "Gym");
            current = localFitnesses.get(position);
        }
        holder.localFitnessName.setText(current.getName());
        holder.localFitnessRow.setTag(current);
    }

    @Override
    public int getItemCount() {
        if (searching) {
            return filtered.size();
        } else {
            return localFitnesses.size();
        }
    }

    public void reload(String username) {
        localFitnesses = LocalFitnessFragment.database.localFitnessDao().get(username, "Gym");
        notifyDataSetChanged();
    }
}
