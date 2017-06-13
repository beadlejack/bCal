package edu.scu.bcal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


class NewEvent extends AppCompatActivity {

    static private Date eventDate, startTimeDate, endTimeDate;
    static private EditText editDate, startTime, endTime, title, location;
    static private MultiAutoCompleteTextView description;
    static private CheckBox allDay;

    static private String storeDate, storeStart, storeEnd, storeTitle, storeLocation, storeDescription;

    static private int mChosenTime;
    static int cur = 0;

    static final int START_TIME = 1;
    static final int END_TIME = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        editDate = (EditText) findViewById(R.id.editDate);
        startTime = (EditText) findViewById(R.id.editStartTime);
        endTime = (EditText) findViewById(R.id.editEndTime);
        title = (EditText) findViewById(R.id.editTitle);
        location = (EditText) findViewById(R.id.editLocation);
        description = (MultiAutoCompleteTextView) findViewById(R.id.editDescription);
        allDay = (CheckBox) findViewById(R.id.checkBoxAllDay);


        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }

        });
        editDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("TIME",1);

                showTimePickerDialog(bundle);
            }
        });
        startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("TIME",2);
                    showTimePickerDialog(bundle);
                }
            }
        });

        endTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("TIME",2);
                showTimePickerDialog(bundle);
            }
        });
        endTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("TIME",2);
                    showTimePickerDialog(bundle);
                }
            }
        });

        final Button button = (Button) findViewById(R.id.submitEventButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                submitEvent();
            }
        });

    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Calendar d = Calendar.getInstance();
            d.set(year, month, day);
            eventDate = d.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String dateDisplay = dateFormat.format(eventDate);
            editDate.setText(dateDisplay);


        }
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(),"datePicker");
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
//            return new TimePickerDialog(getActivity(), this, hour, minute,
//                    DateFormat.is24HourFormat(getActivity()));

            Bundle bundle = this.getArguments();
            if(bundle != null){
                mChosenTime = bundle.getInt("TIME",1);
            }



            switch (mChosenTime) {

                case START_TIME:
                    cur = START_TIME;
                    return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

                case END_TIME:
                    cur = END_TIME;
                    return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

            }

            return null;
        }


        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            //String displayTime = (hourOfDay%12) + ":" + minute;
            Calendar t = Calendar.getInstance();
            t.set(Calendar.HOUR_OF_DAY, hourOfDay);
            t.set(Calendar.MINUTE, minute);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            String displayTime = timeFormat.format(t.getTime());
            if(cur == START_TIME){
                // set selected date into textview
                startTime.setText(displayTime, TextView.BufferType.NORMAL);
                startTimeDate = t.getTime();
            }
            else{
                endTime.setText(displayTime, TextView.BufferType.NORMAL);
                endTimeDate = t.getTime();
            }
        }
    }

    public void showTimePickerDialog(Bundle bundle) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void submitEvent() {
        if (!checkInputs()) return;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        SimpleDateFormat storeDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat storeTimeFormat = new SimpleDateFormat("HH:mm");

        storeDate = storeDateFormat.format(eventDate);
        storeStart = storeTimeFormat.format(startTimeDate);
        storeEnd = storeTimeFormat.format(endTimeDate);
        storeTitle = title.getText().toString();
        storeLocation = location.getText().toString();
        storeDescription = description.getText().toString();

        Event newEvent = new Event(storeTitle, storeLocation, storeDate,
                                    storeStart, storeEnd, storeDescription, allDay.isChecked());

        String key = mDatabase.child("events").push().getKey();
        newEvent.uid = key;
        Map<String, Object> eventValues = newEvent.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/events/" + key, eventValues);
        mDatabase.updateChildren(childUpdates);
    }

    private boolean checkInputs() {
        if (isEmpty(title)) {
            showAlert("Need to enter an event title.");
            return false;
        }
        if (isEmpty(location)) {
            showAlert("Need to enter an event location.");
            return false;
        }
        if (isEmpty(editDate)) {
            showAlert("Need to enter a date for the event.");
            return false;
        }
        if (!allDay.isChecked()) {
            if (isEmpty(startTime)) {
                showAlert("Need to enter a start time for the event or select 'All Day'.");
                return false;
            }
            if (isEmpty(endTime)) {
                showAlert("Need to enter an end time for the event or select 'All Day'.");
                return false;
            }
        }
        if (isEmpty(description)) {
            showAlert("Need to enter an event description");
            return false;
        }

        return true;

    }

    private boolean isEmpty(EditText edit) {
        return edit.getText().toString().trim().length() == 0;
    }

    private void showAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(NewEvent.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}
