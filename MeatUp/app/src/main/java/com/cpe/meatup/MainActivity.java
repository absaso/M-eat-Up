package com.cpe.meatup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;

import com.cpe.meatup.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    private ActivityMainBinding binding;
    public List<Review> listReviews = new ArrayList<>();
    private Review review = new Review();
    private User user;
    public static String localhost = "http://18.212.198.44:8080";
            //"http://10.0.2.2:8080";
            //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ActivityCompat.checkSelfPermission( this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            String[] permissions =
                    {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this,
                    permissions,1);

        }
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectNetwork()
                .penaltyLog()
                .build());
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        bottomNavigationView =(BottomNavigationView) findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        signUpLauncher();
    }
    public void ShowRestoDetails(User user, Restaurant restaurant){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        RestaurantDetailsFragment fragment = new RestaurantDetailsFragment(user, restaurant);
        fragment.setUser(user);
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void signUpLauncher() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SignupFragment fragment = new SignupFragment();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }
    public void signUpLauncher(User user) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SignupFragment fragment = new SignupFragment(user);
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

    public void MapLauncher(User user) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MapFragment fragment = new MapFragment();
        fragment.setUser(user);
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

    /*public void EventLauncher(User user,Restaurant resto) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        EventFragment fragment = new EventFragment();
        fragment.setUser(user);
        fragment.setResto(resto);
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }*/

    public void NewEventLauncher(User user,Restaurant restaurant) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NewEventFragment fragment = new NewEventFragment(user,restaurant);
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

   /* public void UserLauncher(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        List <String> pref =new ArrayList<>();
        pref.add("viande");
        pref.add("sushi");
        pref.add("burger");
        User user1 = new User("marouane","marouane","pwd",pref);
        UserFragment fragment = new UserFragment(user1);

        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }*/
    public void UserLauncher(User user){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        UserFragment fragment = new UserFragment(user);
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        switch(id){
            case R.id.map:
                MapLauncher(user);
                return true;
            case R.id.profile:
                UserLauncher(user);
                return true;

        }
        return false;
    }


}