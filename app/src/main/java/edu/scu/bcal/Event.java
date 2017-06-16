package edu.scu.bcal;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Beadle on 6/12/17.
 */

@IgnoreExtraProperties
public class Event {


    public String uid;
    public String title;
    public String startTime;
    public String endTime;
    public String date;
    public String description;
    public String location;
    public boolean allDay;

    public Event() {
        // Default constructor for calls to DataSnapshot.getValue(Event.class)
    }

    public Event(String uid, String title, String location, String date, String startTime, String endTime, String description, boolean allDay) {
        this.uid = uid;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.description = description;
        this.location = location;
        this.allDay = allDay;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("title", title);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("date", date);
        result.put("description", description);
        result.put("allDay", allDay);
        result.put("location", location);

        return result;
    }

    public void setEqualTo(Event b) {
        uid = b.uid;
        title = b.title;
        startTime = b.startTime;
        endTime = b.endTime;
        date = b.date;
        description = b.description;
        allDay = b.allDay;
        location = b.location;
    }

}
