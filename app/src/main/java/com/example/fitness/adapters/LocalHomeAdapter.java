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

import com.example.fitness.R;
import com.example.fitness.activities.SingleLocalFitness;
import com.example.fitness.fragments.LocalFitnessFragment;
import com.example.fitness.fragments.LocalHomeFragment;
import com.example.fitness.models.LocalFitness;

import java.util.ArrayList;
import java.util.List;

public class LocalHomeAdapter extends RecyclerView.Adapter<LocalHomeAdapter.LocalHomeViewHolder> implements Filterable {
    private String username;
    private boolean searching = false;

    public LocalHomeAdapter(String username) {
        this.username = username;
    }

    private List<LocalFitness> localFitnesses = LocalFitnessFragment.database.localFitnessDao().get(username, "Home");
    private List<LocalFitness> filteredLocalFitness = new ArrayList<>();
    private List<LocalFitness> filtered = new ArrayList<>();

    public Filter getFilter() {
        return new LocalHomeFilter();
    }
    
    public class LocalHomeFilter extends Filter {

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

    public class LocalHomeViewHolder extends RecyclerView.ViewHolder {
        private TextView localFitnessName;
        private LinearLayout localFitnessRow;
        public LocalHomeViewHolder(@NonNull final View itemView) {
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
                    intent.putExtra("type", current.getType());

                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public LocalHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_row, parent, false);
        return new LocalHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalHomeViewHolder holder, int position) {
        LocalFitness current;
        if (searching) {
            current = filtered.get(position);
        } else {
            localFitnesses = LocalFitnessFragment.database.localFitnessDao().get(username, "Home");
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
        localFitnesses = LocalFitnessFragment.database.localFitnessDao().get(username, "Home");
        notifyDataSetChanged();
    }

}
