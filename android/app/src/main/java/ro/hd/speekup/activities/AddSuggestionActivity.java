package ro.hd.speekup.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ro.hd.speekup.R;
import ro.hd.speekup.classes.ApiManager;

public class AddSuggestionActivity extends AppCompatActivity {

    private Spinner mSpinner;
    private ArrayAdapter<String> mSpinnerAdapter;

    private String eventId = "";

    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_suggestion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSpinner = (Spinner) findViewById(R.id.type);
        message = (EditText) findViewById(R.id.text);
        createDropdown();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void createDropdown() {
        mSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                TextView text = (TextView)v.findViewById(android.R.id.text1);
                if (position == getCount()) {
                    text.setText(getItem(getCount()));
                    text.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray));
                } else {
                    text.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // we dont display last item. It is used as hint.
            }
        };

        mSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinnerAdapter.add(getString(R.string.type_speek));
        mSpinnerAdapter.add(getString(R.string.type_location));
        mSpinnerAdapter.add(getString(R.string.select_type));

        mSpinner.setAdapter(mSpinnerAdapter);
        mSpinner.setSelection(mSpinnerAdapter.getCount()); //display hint
    }

    public void saveSuggestionClick(View view) {
        long selectedCategory = mSpinner.getSelectedItemId() + 1;
        String type = "move";
        if (selectedCategory == 1) {
            type = "speek";
        }
        if(selectedCategory > mSpinnerAdapter.getCount()) {
            Toast.makeText(getApplicationContext(), getString(R.string.add_suggestion_no_type_error), Toast.LENGTH_SHORT).show();
        } else {
            if(message.getText().length() == 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.add_suggestion_no_message_error), Toast.LENGTH_SHORT).show();
            } else {
                ApiManager.addSuggestion(getApplicationContext(), eventId, type, message.getText().toString(), "AddSuggestionActivity");
                finish();
            }
        }



    }
}
