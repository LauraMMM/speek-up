package ro.hd.speekup.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ro.hd.speekup.R;
import ro.hd.speekup.classes.ApiManager;
import ro.hd.speekup.classes.DateTimeHelper;
import ro.hd.speekup.entities.ListEvent;

public class EventDescriptionActivity extends AppCompatActivity {

    ListEvent event;

    Button button_attend, button_unattend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        event = (ListEvent) intent.getSerializableExtra("event");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String navUrl = "";
                if (event.getLatitude().isEmpty() || event.getLongitude().isEmpty()) {
                    navUrl = "http://maps.google.com/maps?q=loc:" + event.getLocationName() + "&navigate=yes";
                } else {
                    navUrl = "http://maps.google.com/maps?q=loc:" + event.getLatitude() + "," + event.getLongitude() + "&navigate=yes";
                }
                Uri uri = Uri.parse(navUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setTitle(event.getTitle());

        TextView text_description = (TextView) findViewById(R.id.text_description);
        TextView text_date = (TextView) findViewById(R.id.text_date);
        TextView text_time = (TextView) findViewById(R.id.text_time);
        TextView text_going = (TextView) findViewById(R.id.text_going);
        button_unattend = (Button) findViewById(R.id.button_unattend);
        button_attend = (Button) findViewById(R.id.button_attend);

        text_description.setText(event.getDescription());
        text_date.setText(getString(R.string.event_date, DateTimeHelper.writeDate(event.getStartTime(), this, null)));
        text_time.setText(getString(R.string.starts_at, DateTimeHelper.writeTimeToday(event.getStartTime(), null)));
        text_going.setText(getString(R.string.going, Integer.toString(event.getLocalCount())));

        if (event.isAttending()) {
            button_attend.setVisibility(View.GONE);
            button_unattend.setVisibility(View.VISIBLE);
        } else {
            button_unattend.setVisibility(View.GONE);
            button_attend.setVisibility(View.VISIBLE);
        }
    }

    public void attendClick(View view) {
        ApiManager.attendEvent(view.getContext(), event.getId(), "EventDescriptionActivity");
        event.setIsAttending(true);
        button_attend.setVisibility(View.GONE);
        button_unattend.setVisibility(View.VISIBLE);
    }

    public void unattendClick(View view) {
        ApiManager.unattendEvent(view.getContext(), event.getId(), "EventDescriptionActivity");
        event.setIsAttending(false);
        button_unattend.setVisibility(View.GONE);
        button_attend.setVisibility(View.VISIBLE);
    }
}
