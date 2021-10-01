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


import com.example.fitness.models.LocalFood;
import com.example.fitness.fragments.LocalFoodFragment;
import com.example.fitness.R;
import com.example.fitness.activities.SingleFood;

import java.util.ArrayList;
import java.util.List;

public class LocalFoodAdapter extends RecyclerView.Adapter<LocalFoodAdapter.LocalFoodViewHolder> implements Filterable {
    private static String username;
    boolean searching = false;
    public LocalFoodAdapter(String username) {
        this.username = username;
    }

    private List<LocalFood> localFoodList = LocalFoodFragment.database.localFoodDao().getAll(username);
    private List<LocalFood> filteredLocalFood = new ArrayList<>();
    private List<LocalFood> filtered = new ArrayList<>();

    @Override
    public Filter getFilter() {
        return new LocalFoodFilter();
    }

    public class LocalFoodFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredLocalFood.clear();
            if (constraint.length() != 0) {
                searching = true;
            } else {
                searching = false;
            }

            if (searching) {
                for (int i = 0; i < localFoodList.size(); i++) {
                    LocalFood current = localFoodList.get(i);
                    if (current.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredLocalFood.add(current);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredLocalFood;
            results.count = filteredLocalFood.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered = (List<LocalFood>) results.values;
            notifyDataSetChanged();
        }
    }


    public static class LocalFoodViewHolder extends RecyclerView.ViewHolder {
        TextView localFoodName;
        LinearLayout localFoodRow;
        public LocalFoodViewHolder(@NonNull View v) {
            super(v);
            localFoodName = v.findViewById(R.id.home_row_name);
            localFoodRow = v.findViewById(R.id.home_row);

            localFoodRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalFood current = (LocalFood) localFoodRow.getTag();
                    Intent intent = new Intent(v.getContext(), SingleFood.class);
                    intent.putExtra("username", username);
                    intent.putExtra("name", current.getName());
                    intent.putExtra("Calories", current.getCalories());
                    intent.putExtra("Protein", current.getProtein());
                    intent.putExtra("Fat", current.getFat());
                    intent.putExtra("Carbs", current.getCarbs());
                    intent.putExtra("imageURL", current.getImageURL());

                    v.getContext().startActivity(intent);

                }
            });

        }
    }

    @NonNull
    @Override
    public LocalFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_row, parent, false);

        return new LocalFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalFoodViewHolder holder, int position) {
        localFoodList = LocalFoodFragment.database.localFoodDao().getAll(username);
        LocalFood current;
        if (searching) {
            current = filtered.get(position);
        } else {
            current = localFoodList.get(position);
        }
        holder.localFoodName.setText(current.getName());
        holder.localFoodRow.setTag(current);
    }

    @Override
    public int getItemCount() {
        if (searching) {
            return filtered.size();
        } else {
            return localFoodList.size();
        }
    }

    public void reload(String username) {
        localFoodList = LocalFoodFragment.database.localFoodDao().getAll(username);
        notifyDataSetChanged();
    }
}
