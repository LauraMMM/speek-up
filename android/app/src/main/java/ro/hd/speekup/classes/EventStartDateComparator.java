package ro.hd.speekup.classes;


import android.util.Log;

import java.util.Comparator;

import ro.hd.speekup.entities.ListEvent;

public final class EventStartDateComparator implements Comparator<ListEvent> {
    public int compare(ListEvent left, ListEvent right) {
        if(left.getStartTime() > right.getStartTime()) {
            return -1;
        } else {
            return 1;
        }
    }

}