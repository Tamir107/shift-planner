package com.example.shiftplanner;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.StrictMode;
import android.util.Log;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.annotations.concurrent.Background;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;

public class GoogleCalendarService {

    // API init
    private static final String APPLICATION_NAME = "ShiftPlanner";
    private static final int CREDENTIALS_RAW_ID = R.raw.credentials;

    public static Calendar getService(Context context) throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleCredentials credentials = GoogleCredentials.fromStream(context.getResources().openRawResource(CREDENTIALS_RAW_ID))
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR_EVENTS));

        // Use the credentials to create a Calendar service
        return new Calendar.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void createEvent(Calendar calendarService, String calendarId, String eventID, String summary,  Date startTime, Date endTime) throws IOException {
        Event event = new Event();
        event.setSummary(summary);
        event.setId(eventID);

        DateTime startDateTime = new DateTime(startTime);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime);
        event.setStart(start);

        DateTime endDateTime = new DateTime(endTime);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime);
        event.setEnd(end);


        Event createdEvent = calendarService.events().insert(calendarId, event).execute();
        Log.w("MyApp","Event created:");
        Log.w("MyApp","Event ID: " + createdEvent.getId());
        Log.w("MyApp","Event Summary: " + createdEvent.getSummary());
        Log.w("MyApp","Event Start Time: " + createdEvent.getStart().getDateTime());
        Log.w("MyApp","Event End Time: " + createdEvent.getEnd().getDateTime());
        Log.w("MyApp","Event created successfully.");
    }


    public static String addEventToCalendar(Context context,String eventID, String summary,Date startingDate, Date endingDate) {
        try {
            Calendar calendarService = getService(context);
            String calendarId = "shiftplanner2@gmail.com"; // Use "primary" for the primary calendar

            createEvent(calendarService, calendarId, eventID, summary, startingDate, endingDate);
            Log.w("myApp","Event added to calendar successfully");

            return "";

        } catch (IOException | GeneralSecurityException e) {
            Log.w("myApp",e.getMessage().toString());

            return "Error occurred";
        }

    }

    public static String removeEventFromCalendar(Context context,String eventID) {
        try {
            Calendar calendarService = getService(context);
            String calendarId = "shiftplanner2@gmail.com"; // Use "primary" for the primary calendar

            calendarService.events().delete(calendarId, eventID).execute();
            Log.w("myApp","Event removed from calendar successfully");

            return "";
        } catch (IOException | GeneralSecurityException e) {
            Log.w("myApp",e.getMessage().toString());
            return "Error occurred";
        }

    }

}
