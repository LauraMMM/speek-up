package ro.hd.speekup.classes;


import java.util.Comparator;

import ro.hd.speekup.entities.ListEvent;
import ro.hd.speekup.entities.Suggestion;

public final class SuggestionScoreComparator implements Comparator<Suggestion> {
    public int compare(Suggestion left, Suggestion right) {
        if(left.getScore() > right.getScore()) {
            return -1;
        } else {
            return 1;
        }
    }

}