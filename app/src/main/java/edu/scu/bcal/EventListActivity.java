package edu.scu.bcal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.v7.widget.RecyclerView.AdapterDataObserver;
import static android.support.v7.widget.RecyclerView.ViewHolder;

//import android.support.design.widget.FloatingActionButton;

/**
 * An activity representing a list of Events. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EventDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class EventListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    SimpleDateFormat storeFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
    FirebaseRecyclerAdapter mAdapter;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newEvent = new Intent(EventListActivity.this, NewEvent.class);
                newEvent.setAction(getIntent().ACTION_MAIN);
                startActivity(newEvent);

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
            }
        });


        final View events = findViewById(R.id.event_list);
        assert events != null;

        if (findViewById(R.id.event_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot eventSnapshot: snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    Log.i("Event", event.uid+": "+event.title);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Event", "The read failed: " + error.getDetails());
            }
        });

        mAdapter = new FirebaseRecyclerAdapter<Event, EventHolder>(
                Event.class,
                R.layout.event_list_content,
                EventHolder.class,
                ref.child("events")) {
            @Override
            public void populateViewHolder(final EventHolder holder, final Event event, int position) {
                Date startTimeDate = null, endTimeDate = null;
                try {
                    startTimeDate = storeFormat.parse(event.startTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    endTimeDate = storeFormat.parse(event.endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                holder.setTitle(event.title);
                holder.setLocation(event.location);
                if (event.allDay) {
                    holder.setStartTime("All Day");
                } else {
                    if (startTimeDate != null)
                        holder.setStartTime(displayFormat.format(startTimeDate));
                    if (endTimeDate != null) holder.setEndTime(displayFormat.format(endTimeDate));
                }

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTwoPane) {

                        } else {
                            Context context = v.getContext();
                            Intent intent = new Intent(context, EventDetailActivity.class);
                            intent.putExtra(ScrollingActivity.ARG_ITEM_ID, event.uid);

                            context.startActivity(intent);
                        }
                    }
                });
            }
        };

        mAdapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int eventCount = mAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (eventCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    ((RecyclerView) events).scrollToPosition(positionStart);
                }
            }
        });


        ((RecyclerView) events).setLayoutManager(mLinearLayoutManager);
        ((RecyclerView) events).setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Drawable yourdrawable = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
        yourdrawable.mutate();
        yourdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_today:
                CalendarView cal = (CalendarView) findViewById(R.id.monthlyCal);
                cal.setDate(System.currentTimeMillis(),false,true);
                break;
            default:
                break;
        }
        return true;
    }


    public static class EventHolder extends ViewHolder {
        private final View mView;
        private final TextView mTitle;
        private final TextView mLocation;
        private final TextView mStartTime;
        private final TextView mEndTime;

        public EventHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mLocation = (TextView) itemView.findViewById(R.id.location);
            mStartTime = (TextView) itemView.findViewById(R.id.startTime);
            mEndTime = (TextView) itemView.findViewById(R.id.endTime);
        }

        public void setTitle(String title) {
            mTitle.setText(title);
        }

        public void setLocation(String location) {
            mLocation.setText(location);
        }

        public void setStartTime(String startTime) {
            mStartTime.setText(startTime);
        }

        public void setEndTime(String endTime) {
            mEndTime.setText(endTime);
        }
    }
}
