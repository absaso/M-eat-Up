package com.cpe.meatup;

import static com.cpe.meatup.MainActivity.localhost;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cpe.meatup.databinding.FragmentMapBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements LocationListener {

    LocationManager lm;
    double latitude = 45.7712;
    double longitude = 4.8643;
    MapView map;
    Marker marker;
    IMapController mapController;
    List<Restaurant> les_resto;
    List<Restaurant> restoRecommandes;
    boolean listVisible=false;

    private User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment

        FragmentMapBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);

        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        BottomNavigationView navBar = ((MainActivity)getActivity()).findViewById(R.id.bottomNavigationBar);
        navBar.setVisibility(View.VISIBLE);

        binding.listRecommand.setVisibility(View.GONE);
        binding.listRecommand.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(!listVisible){
                        binding.listRecommand.setVisibility(View.VISIBLE);
                        listVisible = true;
                        getRestoRecommandes(binding);
                    } else {
                        listVisible = false;
                        binding.listRecommand.setVisibility(View.GONE);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //On initialise la map
        try {
            initMap(binding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return binding.getRoot();

    }



    //Initialisation de la map
    public void initMap(FragmentMapBinding binding) throws IOException {
        map = binding.mapView;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120, 50, this);
        marker = new Marker(map);
        mapController = (MapController) map.getController();
        mapController.setZoom(20);
        GeoPoint startPoint = new GeoPoint(45.764042, 4.835659);
        BigDecimal x = BigDecimal.valueOf(startPoint.getLatitude());
        BigDecimal y = BigDecimal.valueOf(startPoint.getLongitude());
        Coords coords = new Coords(x, y);
        mapController.setCenter(startPoint);
    }

    public User setUser(User user) {
        this.user= user;
        return user;
    }

    public void getRestoRecommandes(FragmentMapBinding binding) throws IOException {
        URL url = new URL(localhost + "/recommendation/"+longitude+"/"+latitude);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        Gson gson = new Gson();
        String json = gson.toJson(user);

        OutputStream requestBody = conn.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(requestBody, "utf-8");
        writer.write(json);
        writer.flush();
        writer.close();
        requestBody.close();

        conn.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        Type listType = new TypeToken<ArrayList<Restaurant>>(){}.getType();
        restoRecommandes = gson.fromJson(response.toString(),listType);

        RestoRecommandAdapter a = new RestoRecommandAdapter(restoRecommandes,user,getContext());
        binding.listRecommand.setAdapter(a);




        // Print the response
        System.out.println(restoRecommandes.get(1).toString());
    }
    private static void sendHttpRequest(Context context,Coords coords, MapView map, User user) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                // Perform the HTTP request in the background thread
                try {
                    System.out.println("Calling Server");
                    URL url = new URL(localhost +"/Restaurants/currentUserLocation");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(15000);

                    Gson gson = new Gson();
                    String json = gson.toJson(coords);
                    OutputStream requestBody = connection.getOutputStream();
                    OutputStreamWriter writer = new OutputStreamWriter(requestBody, "utf-8");
                    writer.write(json);
                    writer.flush();
                    writer.close();
                    requestBody.close();

                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    System.out.println(responseCode);
                    if (responseCode == 200) {
                        System.out.println("Connection established");
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        return result.toString();
                    } else {
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                System.out.println(result);
                // Update the UI with the result in the main thread
                if (result != null) {
                    System.out.println("Creating markers");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Restaurant>>(){}.getType();
                    List<Restaurant> les_resto = gson.fromJson(result,listType);
                    System.out.println(les_resto.size()+" Resto trouvés");
                    List<GeoPoint> les_pos = new ArrayList<>();
                    for (Restaurant restaurant:les_resto){
                        GeoPoint geo2 = new GeoPoint(restaurant.getX().floatValue(), restaurant.getY().floatValue());
                        boolean equalLatLngExists = true;
                        while (equalLatLngExists && les_pos.size()>0) {
                            for (GeoPoint geo : les_pos) {
                                if (geo.getLatitude() == geo2.getLatitude() && geo.getLongitude() == geo2.getLongitude()) {
                                    equalLatLngExists = true;
                                   // geo2.setLongitude(geo2.getLongitude()-0.001+Math.random()*0.002);
                                    geo2.setLatitude(geo2.getLatitude()-0.001+Math.random()*0.002);
                                    break;
                                }
                               equalLatLngExists = false;
                            }

                        }
                        les_pos.add(geo2);


                        Marker restomarker = new Marker(map);
                        restomarker.setPosition(geo2);
                        System.out.print(geo2);
                        restomarker.setIcon(context.getResources().getDrawable(R.drawable.sushi_icon));
                        restomarker.setTitle(restaurant.getName());
                        restomarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                        restomarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker, MapView mapView) {

                                ((MainActivity)context).ShowRestoDetails(user,restaurant);

                                return true;
                            }
                        });
                        System.out.println(restaurant.getName());
                        map.getOverlays().add(restomarker);
                        System.out.println(map.getOverlays().size());
                        System.out.println(restaurant.getName());

                    }
                    map.invalidate();

                    // Success
                } else {
                    // Error
                }
            }
        }.execute();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        GeoPoint geo = new GeoPoint(latitude, longitude);
        OverlayManager overlayManager = map.getOverlayManager();
        overlayManager.clear();
        marker.setPosition(geo);
        marker.setIcon(getActivity().getResources().getDrawable(R.drawable.location_marker));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setTitle("MOI");
        mapController = map.getController();
        map.getOverlays().add(marker);
        mapController.setCenter(geo);

        Coords coords = new Coords(BigDecimal.valueOf(latitude),BigDecimal.valueOf(longitude));
        sendHttpRequest(getActivity(),coords,map,user);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle changes in the status of the location provider
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Handle when the location provider is enabled
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Handle when the location provider is disabled
    }





}