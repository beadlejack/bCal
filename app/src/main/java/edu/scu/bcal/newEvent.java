package edu.scu.bcal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

class NewEvent extends AppCompatActivity {

    private int startMonth, startDay, startYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        EditText editDate = (EditText) findViewById(R.id.editTextDate);
        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
    }

    @IgnoreExtraProperties
    public class Event {

        public String uid;
        public String title;
        public String startTime;
        public String endTime;
        public String date;
        public String description;
        public String location;
        public String creator;

        public Event() {
            // Default constructor for calls to DataSnapshot.getValue(Event.class)
        }

        public Event(String uid, String title, String startTime, String endTime, String date, String description, String location, String creator) {
            this.uid = uid;
            this.title = title;
            this.startTime = startTime;
            this.endTime = endTime;
            this.date = date;
            this.description = description;
            this.location = location;
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


        }
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(),"datePicker");
    }

}
