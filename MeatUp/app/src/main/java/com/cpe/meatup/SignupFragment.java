package com.cpe.meatup;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import static com.cpe.meatup.MainActivity.localhost;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cpe.meatup.databinding.FragmentSignupBinding;
import com.cpe.meatup.databinding.PopupwindowBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SignupFragment extends Fragment {
    private User user;


    public SignupFragment() {
        this.user = null;
    }
    public SignupFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using data binding
        FragmentSignupBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false);
        ListView list_preference = binding.listPreference;

        BottomNavigationView navBar = ((MainActivity)getActivity()).findViewById(R.id.bottomNavigationBar);
        navBar.setVisibility(View.GONE);


        List<FoodPreference> foods = new ArrayList<FoodPreference>();

        foods.add(new FoodPreference("Barbeque",R.drawable.barbeque_icon));
        foods.add(new FoodPreference("Bars",R.drawable.bars_icon));
        foods.add(new FoodPreference("Bistros",R.drawable.bistros_icon));
        foods.add(new FoodPreference("Italian",R.drawable.italian_icon));
        foods.add(new FoodPreference("Pizza",R.drawable.pizza_icon));
        foods.add(new FoodPreference("Thai",R.drawable.thai_icon));
        foods.add(new FoodPreference("Vietnamese",R.drawable.vietnamese_icon));
        foods.add(new FoodPreference("Brasseries",R.drawable.brasseries_icon));
        foods.add(new FoodPreference("Cafes",R.drawable.cafes_icon));
        foods.add(new FoodPreference("Breakfast & Brunch",R.drawable.breakfast_brunch_icon));
        foods.add(new FoodPreference("Cocktail Bars",R.drawable.cocktail_bars_icon));
        foods.add(new FoodPreference("Coffee & Tea",R.drawable.coffee_tea_icon));
        foods.add(new FoodPreference("Fast Food",R.drawable.fast_food_icon));
        foods.add(new FoodPreference("French",R.drawable.french_icon));
        foods.add(new FoodPreference("Japanese",R.drawable.japanese_icon));
        foods.add(new FoodPreference("Lyonnais",R.drawable.lyonnais_icon));
        foods.add(new FoodPreference("Mediterranean",R.drawable.mediterranean_icon));
        foods.add(new FoodPreference("Pan Asian",R.drawable.pan_asian_icon));
        foods.add(new FoodPreference("Restaurants",R.drawable.restaurants_icon));
        foods.add(new FoodPreference("Salad",R.drawable.salad_icon));
        foods.add(new FoodPreference("Sandwiches",R.drawable.sandwiches_icon));
        foods.add(new FoodPreference("Seafood",R.drawable.seafood_icon));
        foods.add(new FoodPreference("Steakhouses",R.drawable.steakhouses_icon));
        foods.add(new FoodPreference("Tapas Bars",R.drawable.tapas_bars_icon));
        foods.add(new FoodPreference("Tapas/Small Plates",R.drawable.tapas_small_plates_icon));
        foods.add(new FoodPreference("Vegetarian",R.drawable.vegetarian_icon));
        foods.add(new FoodPreference("Wine Bars",R.drawable.wine_bars_icon));


        foodPreferenceListAdapter adapter = new foodPreferenceListAdapter(getContext(), R.layout.preference_item, foods,true);
        binding.listPreference.setAdapter(adapter);
        binding.listPreference.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        if (user == null){
            return setConnectFragment(binding);
        }

        else{return setModifierFragment(binding);}
    }

    public User registerModification(User user)throws IOException{
        URL url = new URL(localhost+"/Users/modify");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type", "application/json");

        //write the json from user
        Gson gson = new Gson();
        String json = gson.toJson(user);
        System.out.println(json);


        OutputStream requestBody = conn.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(requestBody, "utf-8");
        writer.write(json);
        writer.flush();
        writer.close();
        requestBody.close();

        //send request
        conn.connect();

        // Read the response
        InputStream responseBody = conn.getInputStream();
        InputStreamReader reader = new InputStreamReader(responseBody, "utf-8");
        StringBuilder response = new StringBuilder();
        char[] buffer = new char[1024];
        int bytesRead;
        while ((bytesRead = reader.read(buffer)) != -1) {
            response.append(buffer, 0, bytesRead);
        }
        reader.close();
        responseBody.close();

        System.out.println(response.toString());

        // Print the response
        String responseString = response.toString();
        Gson gson1 = new Gson();

        User usr = gson1.fromJson(responseString, User.class);

        // Print the response
        return  usr;

    }
    public String registerUser(User user) throws IOException {
        URL url = new URL(localhost + "/Users/add");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type", "application/json");

        //write the json from user
        Gson gson = new Gson();
        String json = gson.toJson(user);
        System.out.println(json);


        OutputStream requestBody = conn.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(requestBody, "utf-8");
        writer.write(json);
        writer.flush();
        writer.close();
        requestBody.close();

        //send request
        conn.connect();

        // Read the response
        InputStream responseBody = conn.getInputStream();
        InputStreamReader reader = new InputStreamReader(responseBody, "utf-8");
        StringBuilder response = new StringBuilder();
        char[] buffer = new char[1024];
        int bytesRead;
        while ((bytesRead = reader.read(buffer)) != -1) {
            response.append(buffer, 0, bytesRead);
        }
        reader.close();
        responseBody.close();

        System.out.println(response.toString());

        // Print the response
        return response.toString();

    }
    public User connectUser(User user) throws IOException{

        URL url = new URL(localhost + "/Users/connect");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type", "application/json");

        //write the json from user
        Gson gson = new Gson();
        String json = gson.toJson(user);
        System.out.println(json);


        OutputStream requestBody = conn.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(requestBody, "utf-8");
        writer.write(json);
        writer.flush();
        writer.close();
        requestBody.close();

        //send request
        conn.connect();

        // Read the response
        InputStream responseBody = conn.getInputStream();
        InputStreamReader reader = new InputStreamReader(responseBody, "utf-8");
        StringBuilder response = new StringBuilder();
        char[] buffer = new char[1024];
        int bytesRead;
        while ((bytesRead = reader.read(buffer)) != -1) {
            response.append(buffer, 0, bytesRead);
        }
        reader.close();
        responseBody.close();

        String responseString = response.toString();
        Gson gson1 = new Gson();
        User usr = gson1.fromJson(responseString, User.class);
        // Print the response
        return  usr;
    }



    public View setRegisterFragment(FragmentSignupBinding binding){
        ListView list_preference = binding.listPreference;
        list_preference.setVisibility(View.VISIBLE);
        binding.name.setVisibility(View.VISIBLE);
        binding.text1.setText("Sign up :)");

        binding.age.setVisibility(View.VISIBLE);
        binding.text2.setVisibility(View.VISIBLE);


        binding.button.setText("Sign Up");
        binding.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String login = String.valueOf(binding.Login.getText());
                String password = String.valueOf(binding.pwd.getText());
                String name = String.valueOf(binding.name.getText());
                int age = Integer.valueOf(String.valueOf(binding.age.getText()));

                // get all the checked checkboxes in the listview listPreference

                List<String> preferences = ((foodPreferenceListAdapter)list_preference.getAdapter()).getChecked_pref();


                if(preferences.size() < 2){
                    popUp(v,"Fais au moins 2 choix");

                }else {
                    User user = new User(name, login, age,password, preferences);
                    try {
                        String succes = registerUser(user);
                        if (succes.equals("true")) {
                            popUp(v, "Registered");
                            setConnectFragment(binding);

                        } else {
                            popUp(v, "This Login already exist");

                        }
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }

            }});
        binding.button2.setText("I already have an account");
        binding.button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                setConnectFragment(binding);
            }
        });

        return binding.getRoot();

    }

    public View setConnectFragment(FragmentSignupBinding binding){
        ListView list_preference = binding.listPreference;
        list_preference.setVisibility(View.GONE);
        binding.name.setVisibility(View.GONE);
        binding.text1.setText("Connexion :)");
        binding.text2.setVisibility(View.GONE);
        binding.age.setVisibility(View.GONE);
        binding.button.setText("Connect");
        binding.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String login = String.valueOf(binding.Login.getText());
                String password = String.valueOf(binding.pwd.getText());
                User user = new User(login,password);
                try {
                    User succes = connectUser(user);
                    if (succes != null){
                        popUp(v,"Connected");
                        ((MainActivity)getActivity()).setUser(succes);
                        ((MainActivity)getActivity()).MapLauncher(succes);
                    }
                    else{
                        popUp(v,"Incorrect Password or Login");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        binding.button2.setText("Sign Up");
        binding.button2.setOnClickListener(new View.OnClickListener() { public void onClick(View v){
            setRegisterFragment(binding);
        }});
        return binding.getRoot();



    }

    public View setModifierFragment(FragmentSignupBinding binding){ ListView list_preference = binding.listPreference;
        list_preference.setVisibility(View.VISIBLE);
        binding.name.setVisibility(View.GONE);
        binding.Login.setText(user.getLogin());
        binding.pwd.setText(user.getPwd());
        binding.text1.setText("Modifier :)");
        binding.text2.setVisibility(View.VISIBLE);
        binding.button.setText("modifier");
        binding.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String login = String.valueOf(binding.Login.getText());
                String password = String.valueOf(binding.pwd.getText());
                //String name = String.valueOf(binding.name.getText());
                if (!login.equals(user.getLogin())  || !password.equals(user.getPwd())){
                    popUp(v,"Login ou password uncorrect");
                }
                else{
                    List<String> Preferences = ((foodPreferenceListAdapter)list_preference.getAdapter()).getChecked_pref();

                    user.setPreferences(Preferences);
                    try {
                        User succes = registerModification(user);
                        if (succes != null){
                            popUp(v,"Registered");
                            ((MainActivity)getActivity()).MapLauncher(succes);

                        }
                        else{
                            popUp(v,"error 404");

                        }
                    } catch (IOException e) {
                        System.out.println(e);
                    }

                }
                // get all the checked checkboxes in the listview listPreference




            }});
        return binding.getRoot();
    }
    public void popUp(View v,String message){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popupwindow, null);

// get the binding for the popup_window layout
        PopupwindowBinding popupBinding = DataBindingUtil.inflate(inflater, R.layout.popupwindow, null, false);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupBinding.getRoot(), width, height, focusable);
        popupBinding.popupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                popupWindow.dismiss();

            }
        });
        popupBinding.popupText.setText(message);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }
}