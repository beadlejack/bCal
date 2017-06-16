package edu.scu.bcal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScrollingActivity extends AppCompatActivity {

    private Event event = new Event();
    private String eventID;
    private DatabaseReference mRef, mDatabase;
    private String title;
    private String dateToDisplay, timeToDisplay;

    public static final String ARG_ITEM_ID = "event_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        eventID = getIntent().getStringExtra(ScrollingActivity.ARG_ITEM_ID);
        System.out.println("Event ID: " + eventID);
        mRef = FirebaseDatabase.getInstance().getReference("events");

        // Attach a listener to read the data at our events reference
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.child(eventID).getValue(Event.class);
                System.out.println("Event ID: " + event.uid + " Event title: " + event.title);
                System.out.println("newEvent ID (db): " + eventID);
                title = event.title;
                System.out.println("Event: " + event);

                // Show the dummy content as text in a TextView.
                if (event != null) {
                    SimpleDateFormat dateDisplay = new SimpleDateFormat("MM/dd/yyyy");
                    SimpleDateFormat dateStore = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat timeDisplay = new SimpleDateFormat("hh:mm a");
                    SimpleDateFormat timeStore = new SimpleDateFormat("HH:mm");

                    System.out.println("onCreateView event: " + event.title);

                    Date date = null, startTime = null, endTime = null;
                    try {
                        date = dateStore.parse(event.date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        startTime = timeStore.parse(event.startTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        endTime = timeStore.parse(event.endTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    timeToDisplay = timeDisplay.format(startTime) + " - " + timeDisplay.format(endTime);
                    System.out.println("timeText: " + timeToDisplay);
                    System.out.println("location: " + event.location);
                    dateToDisplay = dateDisplay.format(date);

                    populateFields(event.title, event.location, dateToDisplay, timeToDisplay, event.description, event.allDay);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public void populateFields(String title, String location, String date, String time, String description, boolean allDay) {

        TextView locationText = (TextView) findViewById(R.id.detailLocation);
        TextView dateText = (TextView) findViewById(R.id.detailDate);
        TextView timeText = (TextView) findViewById(R.id.detailTime);
        TextView descriptionText = (TextView) findViewById(R.id.detailDescription);

        locationText.setText(location);
        dateText.setText(date);
        if (allDay) {
            timeText.setText("All Day");
        } else {
            timeText.setText(time);
        }
        descriptionText.setText(description);


        Activity activity = this;
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, EventListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
