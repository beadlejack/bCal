package edu.scu.bcal;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.scu.bcal.dummy.DummyContent;

/**
 * A fragment representing a single Event detail screen.
 * This fragment is either contained in a {@link EventListActivity}
 * in two-pane mode (on tablets) or a {@link EventDetailActivity}
 * on handsets.
 */
public class EventDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "event_id";

    private DummyContent.DummyItem mItem;


    private Event event = new Event();
    private String eventID;
    private DatabaseReference mRef, mDatabase;
    private String title;
    private String dateToDisplay, timeToDisplay;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(ARG_ITEM_ID)) {


            eventID = getArguments().getString(ARG_ITEM_ID);
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

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.event_detail, container, false);

//        if (getArguments().containsKey(ARG_ITEM_ID)) {
//
//
//            eventID = getArguments().getString(ARG_ITEM_ID);
//            System.out.println("Event ID: " + eventID);
//            mRef = FirebaseDatabase.getInstance().getReference("events");
//
//            // Attach a listener to read the data at our events reference
//            mRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Event event = dataSnapshot.child(eventID).getValue(Event.class);
//                    System.out.println("Event ID: " + event.uid + " Event title: " + event.title);
//                    System.out.println("newEvent ID (db): " + eventID);
//                    title = event.title;
//                    System.out.println("Event: " + event);
//
//                    // Show the dummy content as text in a TextView.
//                    if (event != null) {
//                        SimpleDateFormat dateDisplay = new SimpleDateFormat("MM/dd/yyyy");
//                        SimpleDateFormat dateStore = new SimpleDateFormat("yyyy-MM-dd");
//                        SimpleDateFormat timeDisplay = new SimpleDateFormat("hh:mm a");
//                        SimpleDateFormat timeStore = new SimpleDateFormat("HH:mm");
//
//                        System.out.println("onCreateView event: " + event.title);
//
//                        Date date = null, startTime = null, endTime = null;
//                        try {
//                            date = dateStore.parse(event.date);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            startTime = timeStore.parse(event.startTime);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            endTime = timeStore.parse(event.endTime);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        String timeText = timeDisplay.format(startTime) + " - " + timeDisplay.format(endTime);
//                        System.out.println("timeText: " + timeText);
//                        System.out.println("location: " + event.location);
//                        String location = event.location;
//
//                        populateFields(event.title, event.location, dateDisplay.format(date), timeText, event.description, event.allDay, rootView);
//
////                        ((TextView) rootView.findViewById(R.id.detailLocation)).setText(event.location);
////                        ((TextView) rootView.findViewById(R.id.detailDate)).setText(dateDisplay.format(date));
////                        if (event.allDay) {
////                            ((TextView) rootView.findViewById(R.id.detailTime)).setText("All day");
////                        } else {
////                            ((TextView) rootView.findViewById(R.id.detailTime)).setText(timeText);
////                        }
////                        ((TextView) rootView.findViewById(R.id.detailDescription)).setText(event.description);
////
////                        Activity activity = getActivity();
////                        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
////                        if (appBarLayout != null) {
////                            appBarLayout.setTitle(event.title);
////
////                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    System.out.println("The read failed: " + databaseError.getCode());
//                }
//            });
//
//        }


        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        populateFields(event.title, event.location, dateToDisplay, timeToDisplay, event.description, event.allDay, view);

    }

    public void populateFields(String title, String location, String date, String time, String description, boolean allDay, View view) {

        TextView locationText = (TextView) view.findViewById(R.id.detailLocation);
        TextView dateText = (TextView) view.findViewById(R.id.detailDate);
        TextView timeText = (TextView) view.findViewById(R.id.detailTime);
        TextView descriptionText = (TextView) view.findViewById(R.id.detailDescription);

        locationText.setText(location);
        dateText.setText(date);
        if (allDay) {
            timeText.setText("All Day");
        } else {
            timeText.setText(time);
        }
        descriptionText.setText(description);


        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(title);
        }
    }

}
