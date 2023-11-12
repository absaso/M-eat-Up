package com.cpe.meatup;

import static com.cpe.meatup.MainActivity.localhost;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

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

public class HistoriqueAdapter extends ArrayAdapter<String> {

    private final LayoutInflater inflater;
    private final int layoutResource;
    private Context context;

    public HistoriqueAdapter(@NonNull Context context, int resource, @NonNull List<String> Historique,User usr){
        super(context, resource, Historique);
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(layoutResource, parent, false);
        }

        TextView name = view.findViewById(R.id.resto_name);
        String item = getItem(position);
        name.setText(item);
        ImageButton like_button = view.findViewById(R.id.like_button);
        User user = ((MainActivity)getContext()).getUser();
        List <String> like_history = user.getLiked_restaurants();
        if (like_history == null){
            like_history = new ArrayList<>();
        }
        boolean is_liked = false;
        if (like_history!=null){
            for (String resto:like_history){
                if(resto.equals(item)){
                    like_button.setImageResource(R.drawable.liked);
                    like_button.setTag("liked");
                    is_liked = true;
                    break;
                }
            }}

        like_button.setOnClickListener(view1 -> {

            String drawable = String.valueOf(like_button.getTag());

            // int A = getContext().getResources().getIdentifier(id_name, "drawable", getContext().getPackageName());
            //  int B = R.drawable.like;
            if (drawable.equals("like")){
                like_button.setTag("liked");
                like_button.setImageResource(R.drawable.liked);
                List<String> new_liked = user.getLiked_restaurants();
                if (new_liked==null){
                    new_liked = new ArrayList<>();
                }
                new_liked.add(item);
                user.setLiked_restaurants(new_liked);}
            else{
                like_button.setImageResource(R.drawable.like);
                like_button.setTag("like");
                List<String> new_liked = user.getLiked_restaurants();

                new_liked.remove(item);
                user.setLiked_restaurants(new_liked);

            }




            sendHttpRequest(user);


        });









        return view;
    }


    private static void sendHttpRequest( User user) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                // Perform the HTTP request in the background thread
                try {
                    System.out.println("Calling Server");
                    URL url = new URL(localhost+"/history/like");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(15000);

                    Gson gson = new Gson();
                    String json = gson.toJson(user);
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

        }.execute();
    }
}