package com.cpe.meatup;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import static com.cpe.meatup.MainActivity.localhost;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import com.cpe.meatup.databinding.FragmentNewEventBinding;
import com.cpe.meatup.databinding.PopupwindowBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NewEventFragment extends Fragment {

    private User user ;
    private Restaurant resto ;
    private int yearDebut, monthDebut, dayDebut, hourDebut, minuteDebut;

    public NewEventFragment() {
        // Required empty public constructor
    }

    public NewEventFragment(User user, Restaurant resto) {
        this.user=user;
        this.resto=resto;
    }


    @ Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewEventBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_event, container, false);

        binding.goBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((MainActivity)getActivity()).ShowRestoDetails(user,resto);
        }
        });

        binding.restoName.setText(resto.getName());

        binding.selectDateDebutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                yearDebut = c.get(Calendar.YEAR);
                monthDebut = c.get(Calendar.MONTH);
                dayDebut = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(binding.dateDebut.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthofyear, int dayofmonth) {
                        binding.dateDebut.setText(year + "-" + selectMonth(monthofyear) + "-" + selectDay(dayofmonth));
                    }
                }, yearDebut, monthDebut, dayDebut);
                dpd.show();
            }
        });

        binding.selectHeureDebutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                hourDebut = c.get(Calendar.HOUR_OF_DAY);
                minuteDebut = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(binding.heureDebut.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                binding.heureDebut.setText(hourOfDay + ":" + minute + ":00");
                            }
                        }, hourDebut, minuteDebut, true);
                timePickerDialog.show();
            }
        });


        binding.newEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.eventName.getText().length()==0 || binding.restoName.getText().length()==0|| binding.nbPers.getText().length()==0 ||
                binding.dateDebut.getText().length()==0 || binding.heureDebut.getText().length()==0 ){
                    popUp(view,"Remplis tous les champs");

                }else {
                    String eventName = String.valueOf(binding.eventName.getText());
                    String restoId = String.valueOf(resto.getId());
                    String start= String.valueOf(binding.dateDebut.getText())+ " " +String.valueOf(binding.heureDebut.getText());
                    Integer nbLimitUsers = Integer.valueOf(String.valueOf(binding.nbPers.getText()));
                    try {
                        String date = adaptDate(start);
                        saveEvent(eventName,restoId,date,nbLimitUsers);
                        popUp(view,"Votre événement est enregistré ! ");
                        binding.eventName.getText().clear();
                        binding.nbPers.getText().clear();
                        binding.dateDebut.getText().clear();
                        binding.heureDebut.getText().clear();

                        ((MainActivity)getActivity()).ShowRestoDetails(user,resto);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public String selectMonth (int month){
        if (month<9){
            return "0"+(month+1);
        }
        return String.valueOf(month+1);
    }

    public String selectDay (int day){
        if (day<10){
            return "0"+day;
        }
        return String.valueOf(day);
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

    public void saveEvent(String eventName, String idResto, String date, int nbLimitUsers) throws IOException {
        int IdUser = user.getId();
        String nameOrganizer = user.getName();
        System.out.println("Je suis: "+nameOrganizer);
        Event event = new Event(eventName,idResto,date,IdUser,nameOrganizer,nbLimitUsers);
        URL url = new URL(localhost + "/Events/add");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type", "application/json");

        //write the json from event
        Gson gson = new Gson();
        String json = gson.toJson(event);
        System.out.println(json);

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date parsedDate = sdf.parse(toConvert);
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String formattedTime = output.format(parsedDate);
        System.out.println(formattedTime);
        Date date = output.parse(formattedTime);
        return formattedTime;
    }

    public void setUser(User user) {
        this.user=user;
    }
    public void setResto(Restaurant resto) {
        this.resto=resto;
    }
}