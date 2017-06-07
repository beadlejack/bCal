package edu.scu.bcal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

class NewEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
    }

    @IgnoreExtraProperties
    public class Event {

        public String uid;
        public String title;
        public String startTime;
        public String endTime;
        public String date;
        public String description;
        public String creator;

        public Event() {
            // Default constructor for calls to DataSnapshot.getValue(Event.class)
        }

        public Event(String uid, String title, String startTime, String endTime, String date, String description, String creator) {
            this.uid = uid;
            this.title = title;
            this.startTime = startTime;
            this.endTime = endTime;
            this.date = date;
            this.description = description;
            this.creator = creator;
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
            result.put("creator", creator);

            return result;
        }

    }
}
