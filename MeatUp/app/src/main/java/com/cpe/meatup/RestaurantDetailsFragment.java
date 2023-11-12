package com.cpe.meatup;

import static com.cpe.meatup.MainActivity.localhost;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpe.meatup.databinding.FragmentRestaurantDetailsBinding;
import com.cpe.meatup.databinding.UserItemBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailsFragment extends Fragment {

    private Restaurant resto  ;
    private User user;
    private List<Event> listEvents;

    public RestaurantDetailsFragment(User user, Restaurant restaurant){
        this.resto = restaurant;
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater,
                              @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        FragmentRestaurantDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restaurant_details,container,false);
        binding.listComments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.listEvents.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));

        BottomNavigationView navBar = ((MainActivity)getActivity()).findViewById(R.id.bottomNavigationBar);
        navBar.setVisibility(View.GONE);
        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).MapLauncher(user);
            }
        });

        binding.newEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).NewEventLauncher(user,resto);
            }
        });

        binding.nomResto.setText(resto.getName());
        binding.telephoneResto.setText(resto.getPhone());
        if (resto.getUrl() != null) {
            String url = "<a href=\""+ resto.getUrl() + "\">Site du restaurant</a> ";
            binding.siteResto.setText(Html.fromHtml(url));
            binding.siteResto.setMovementMethod(LinkMovementMethod.getInstance());
        } else{
            binding.siteResto.setText("Site indisponible");
        }

        if (resto.getImage_url()!=null){
            String url = resto.getImage_url(); ;
            Picasso.get().load(url).into(binding.imgResto);
        }else{
            binding.imgResto.setImageDrawable(getActivity().getDrawable(R.drawable.resto));;
        }

        System.out.println(resto.getRating());
        String sentiment = resto.getSentiment();
        binding.appreciation.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_sentiment_neutral_24));

        if(sentiment!=null){
            if(sentiment.equals("positive")|| sentiment.equals("positif")){
                binding.appreciation.setImageDrawable(getActivity().getDrawable(R.drawable.happy_faces_24));
            }
            else if(sentiment.equals("negative") || sentiment.equals("negatif")){
                binding.appreciation.setImageDrawable(getActivity().getDrawable(R.drawable.sad_face_24));
            }

        }


        ReviewListAdapter adapter = new ReviewListAdapter(resto.getReviews());
        binding.listComments.setAdapter(adapter);

        try {
            getEventsList(binding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }


    public User setUser(User user) {
         this.user= user;
         return user;
    }
    public void getEventsList (FragmentRestaurantDetailsBinding binding) throws IOException {

        System.out.println("Getting events list from Server - Connection");
        URL url = new URL(localhost + "/Events/"+resto.getId());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();
        System.out.println(responseCode);
        if (responseCode == 200) {
            System.out.println("Connection established");
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result.toString());

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Event>>(){}.getType();
            List<Event> eventsFound = gson.fromJson(String.valueOf(result),listType);
            System.out.println(eventsFound.size()+" événements trouvés");

            EventListAdapter adapter = new EventListAdapter(eventsFound,resto,user);
            binding.listEvents.setAdapter(adapter);
        } else {
            System.out.println("Nothiiing");
        }


    }

    public void setResto (Restaurant resto){
        this.resto = resto;
    }


}