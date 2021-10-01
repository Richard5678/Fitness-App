package com.example.fitness.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness.models.LocalFitness;
import com.example.fitness.fragments.LocalFitnessFragment;
import com.example.fitness.R;
import com.example.fitness.activities.SingleLocalFitness;

import java.util.ArrayList;
import java.util.List;
/*
public class LocalFitnessAdapter extends RecyclerView.Adapter<LocalFitnessAdapter.LocalFitnessViewHolder> implements Filterable {
    private String username;
    private Boolean searching = false;

    public LocalFitnessAdapter(String username) {
        this.username = username;
    }


    private List<LocalFitness> localFitnesses = LocalFitnessFragment.database.localFitnessDao().getAll(username);
    private List<LocalFitness> filteredLocalFitnesses = new ArrayList<>();
    private List<LocalFitness> filtered = new ArrayList<>();

    @Override
    public Filter getFilter() {
        return new LocalFitnessFilter();
    }

    public class LocalFitnessFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredLocalFitnesses.clear();
            if (constraint.length() != 0) {
                searching = true;
            } else {
                searching = false;
            }

            if (searching) {
                for (int i = 0; i < localFitnesses.size(); i++) {
                    LocalFitness current = localFitnesses.get(i);
                    if (current.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredLocalFitnesses.add(current);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredLocalFitnesses;
            results.count = filteredLocalFitnesses.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered = (List<LocalFitness>) results.values;
            notifyDataSetChanged();
        }
    }

    public class LocalFitnessViewHolder extends RecyclerView.ViewHolder {
        TextView localFitnessName;
        LinearLayout localFitnessRow;

        public LocalFitnessViewHolder(@NonNull View itemView) {
            super(itemView);
            localFitnessName = itemView.findViewById(R.id.home_row_name);
            localFitnessRow = itemView.findViewById(R.id.home_row);

            localFitnessRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalFitness current = (LocalFitness) localFitnessRow.getTag();
                    Intent intent = new Intent(v.getContext(), SingleLocalFitness.class);
                    intent.putExtra("name", current.name);
                    intent.putExtra("calories", current.calories);
                    intent.putExtra("duration", current.duration);

                    v.getContext().startActivity(intent);

                }
            });
        }
    }


    @NonNull
    @Override
    public LocalFitnessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_row, parent, false);
        return new LocalFitnessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalFitnessViewHolder holder, int position) {
        localFitnesses = LocalFitnessFragment.database.localFitnessDao().getAll(username);

        LocalFitness current;
        if (searching) {
            current = filtered.get(position);
        } else {
            current = localFitnesses.get(position);
        }
        holder.localFitnessName.setText(current.name);
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
        localFitnesses = LocalFitnessFragment.database.localFitnessDao().getAll(username);
        notifyDataSetChanged();
    }




}

 */
