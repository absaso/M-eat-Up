package com.cpe.meatup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cpe.meatup.databinding.EventItemBinding;
import com.cpe.meatup.databinding.RestoItemBinding;

import java.util.List;

public class RestoRecommandAdapter extends RecyclerView.Adapter<RestoRecommandAdapter.ViewHolder> {
    List<Restaurant> listeRestosRecommand;
    User user;
    Context context;


    public RestoRecommandAdapter(List<Restaurant> listEvents,User user,Context context) {
        this.listeRestosRecommand = listEvents;
        this.user=user;
        this.context = context;
    }

    @NonNull
    @Override
    public RestoRecommandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RestoItemBinding binding = DataBindingUtil.inflate( LayoutInflater.from(parent.getContext()), R.layout.resto_item, parent, false);
        return new RestoRecommandAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestoRecommandAdapter.ViewHolder holder, int position) {
        Restaurant resto = listeRestosRecommand.get(position);
        holder.binding.nameResto.setText(resto.getName());
        holder.binding.eatTogetherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).ShowRestoDetails(user,resto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listeRestosRecommand.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RestoItemBinding binding;
        ViewHolder(RestoItemBinding binding) { super(binding.getRoot());
            this.binding = binding;}
    }

}
