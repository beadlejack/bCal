package edu.scu.bcal.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.scu.bcal.dummy.DummyContent.Event.description;

public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.uid, item);
    }

    private static DummyItem createDummyItem(int id) {
        return new DummyItem(String.valueOf(id), Event.title, makeDetails(id));
    }

    private static String makeDetails(int id) {//don't know about id thing
        StringBuilder builder = new StringBuilder();
        builder.append("Location: ").append(Event.location);
        builder.append("Date: ").append(Event.date);
        if(!Event.allDay) {
            builder.append("Start Time: ").append(Event.startTime);
            builder.append("End Time: ").append(Event.endTime);
        }
        else builder.append("All-day event");
        builder.append("Description: ").append(description);
        return builder.toString();
    }

    public static class Event {
        public final String eid;
        public static String title;
        public static String location;
        public static String date;
        public static String startTime;
        public static String endTime;
        public static String description;
        public static boolean allDay;

        public Event(String eid, String title, String location, String date, String startTime, String endTime, String description) {
            this.eid = eid;
            this.title = title;
            this.location = location;
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.description = description;
            this.allDay = allDay;
        }
    }

    /**
     * A dummy item representing a piece of content.
     */

    public static class DummyItem {
        public final String uid;
        public final String content;
        public final String details;

        public DummyItem(String uid, String content, String details) {
            this.uid = uid;
            this.content = content;
            this.details = details;
        }

    @Override
        public String toString() {
            return content;
        }
    }
}
