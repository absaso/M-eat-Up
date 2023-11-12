package com.cpe.meatup;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class foodPreferenceListAdapter extends ArrayAdapter<FoodPreference> {
    private final LayoutInflater inflater;
    private final int layoutResource;
    private final boolean is_Signup_fragment;
    private List<String> checked_pref = new ArrayList<>();

    public List<String> getChecked_pref() {
        return checked_pref;
    }

    public foodPreferenceListAdapter(@NonNull Context context, int resource, @NonNull List<FoodPreference> preferences, @NonNull boolean is_Signup_fragment){
        super(context, resource, preferences);
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
        this.is_Signup_fragment = is_Signup_fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(layoutResource, parent, false);
        }


        ImageView imageFood = view.findViewById(R.id.imageFood);
        FoodPreference item = getItem(position);
        if (is_Signup_fragment){
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            checkBox.setText(item.getName());
            checkBox.setOnClickListener(view1 -> {
                if (checked_pref.contains(item.getName())){
                checked_pref.remove(item.getName());}
                else{
                    checked_pref.add(item.getName());
                }
            });

        }
        else{
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            checkBox.setVisibility(View.GONE);
            TextView text = view.findViewById(R.id.text_pref);
            text.setText(item.getName());
        }
        Drawable drawable;

        drawable = getContext().getDrawable(item.getImage());


        imageFood.setImageDrawable(drawable);


        return view;
    }
}
