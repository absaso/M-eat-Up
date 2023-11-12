package com.cpe.meatup;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import static com.cpe.meatup.MainActivity.localhost;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cpe.meatup.databinding.EventItemBinding;
import com.cpe.meatup.databinding.PopupwindowBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>{
    List<Event> listEvents;
    User user ;
    Restaurant resto;

    public EventListAdapter(List<Event> listEvents,Restaurant resto,User user) {
        this.listEvents = listEvents;
        this.resto=resto;
        this.user=user;
    }

    @NonNull
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EventItemBinding binding = DataBindingUtil.inflate( LayoutInflater.from(parent.getContext()), R.layout.event_item, parent, false);
        return new EventListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.ViewHolder holder, int position) {
        Event event = listEvents.get(position);
        holder.binding.info.setVisibility(View.INVISIBLE);
        holder.binding.nameEvent.setText(event.getEventName());
        holder.binding.nameOrganizer.setText("Organisé par: "+event.getNameOrganizer());
        holder.binding.nameResto.setText("Lieu: "+resto.getName());
        try {
            holder.binding.date.setText("Date: "+ adaptDate(event.getStartEvent().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.binding.nbParticipants.setText("Max participants: "+event.getNbLimitUsers());
        if(event.getIdUserOrganizer() == user.getId()) {
            holder.binding.joinBtn.setEnabled(false);
            holder.binding.joinBtn.setText("Organisateur du M(eat)Up");
            holder.binding.info.setVisibility(View.VISIBLE);
            holder.binding.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (event.getListParticipants() !=null){
                        popUp(view, String.valueOf(event.getListParticipants().size()) + " participant(s)");
                    } else {
                        popUp(view, "Pas encore de participant(s)");
                    }

                }
            });
        }

        holder.binding.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (event.getListParticipants() != null && event.getListParticipants().contains(user.getId())){
                    holder.binding.joinBtn.setEnabled(false);
                    holder.binding.joinBtn.setText("Déjà participant(e)");
                } else if (event.getListParticipants() == null || event.getListParticipants().size() < event.getNbLimitUsers()){
                    System.out.println("Adding you to the list of participants");
                    List<String> historiqueListIdResto= new ArrayList<>();
                    List<String> historiqueListNameResto= new ArrayList<>();
                    List<Integer> listParticipants= new ArrayList<>();

                    if(event.getListParticipants() !=null){
                        for(Integer element:event.getListParticipants()){
                            listParticipants.add(element);
                        }
                    }
                    if(user.getHistorique_id() !=null){
                        for(String element:user.getHistorique_id()){
                            historiqueListIdResto.add(element);
                        }
                    }
                    if(user.getHistorique_name() !=null){
                        for(String element:user.getHistorique_name()){
                            historiqueListNameResto.add(element);
                        }
                    }

                    historiqueListIdResto.add(resto.getId());
                    historiqueListNameResto.add(resto.getName());
                    listParticipants.add(user.getId());

                    try {
                        addToEvent(event.getId()+1,user.getId(),resto.getId(),resto.getName());
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    popUp(view,"Participation enregistrée ! Il ne reste plus qu'à rejoindre le M(eat)Up au moment venu !");
                    user.setHistorique_id(historiqueListIdResto);
                    user.setHistorique_name(historiqueListNameResto);
                    event.setListParticipants(listParticipants);
                    System.out.println(event.getListParticipants().toString());
                } else {
                    holder.binding.joinBtn.setEnabled(false);
                    holder.binding.joinBtn.setText("Evenement complet");
                }


            }
        });

    }


    public void popUp(View v,String message){
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
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


    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    public void addToEvent (int idEvent, int idUser, String idResto, String nameResto) throws IOException, JSONException {
        //requete http to update list participants et on recupère true si
        //l'user est inscrit et false si pas inscrit bc events

        URL url = new URL(localhost+"/Events/add/"+idEvent);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject obj = new JSONObject();
        obj.put("idUser", idUser);
        obj.put("idResto", idResto);
        obj.put("nameResto", nameResto);
        String json = obj.toString();
        System.out.println(json);

        System.out.println("Infos sent");

        OutputStream requestBody = conn.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(requestBody, "utf-8");
        writer.write(json);
        writer.flush();
        writer.close();
        requestBody.close();

        //send request
        conn.connect();
        //Read the response
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
    }

    private String adaptDate(String toConvert) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date parsedDate = sdf.parse(toConvert);
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.ENGLISH);
        String formattedTime = output.format(parsedDate);
        System.out.println(formattedTime);
        return formattedTime;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private EventItemBinding binding;
        ViewHolder(EventItemBinding binding) { super(binding.getRoot());
            this.binding = binding;}
    }


}
