package com.cpe.meatup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.cpe.meatup.databinding.FragmentUserBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserFragment extends Fragment {

    private User user;
    public UserFragment(User user){
        this.user = user;
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment using data binding
        FragmentUserBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        List<FoodPreference> foods = new ArrayList<FoodPreference>();
        List<String> historique = user.getHistorique_name();
        List<String> foods2 = user.getPreferences();
        for(String elem: foods2){
            int image = getResources().getIdentifier(elem.toLowerCase()+"_icon", "drawable", getActivity().getPackageName());

            FoodPreference food= new FoodPreference(elem,image);
            foods.add(food);
        }



        foodPreferenceListAdapter adapter = new foodPreferenceListAdapter(getContext(),R.layout.preference_item,foods,false);
        HistoriqueAdapter adapterHisto = new HistoriqueAdapter(getContext(),R.layout.historique_item,historique,user);
        binding.listPreference.setAdapter(adapter);
        binding.name.setText(user.getName());
        binding.historique.setAdapter(adapterHisto);
        binding.buttonModifier.setOnClickListener(view -> {
            ((MainActivity)getActivity()).signUpLauncher(user);
        });
        return binding.getRoot();

    }
}
