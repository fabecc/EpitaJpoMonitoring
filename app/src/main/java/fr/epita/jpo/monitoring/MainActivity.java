package fr.epita.jpo.monitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Construct the data source
        ArrayList<School> arrayOfUsers = new ArrayList<School>();
        arrayOfUsers.add(new School("Lyon", false));
        arrayOfUsers.add(new School("Paris", false));
        arrayOfUsers.add(new School("Rennes", true));
        arrayOfUsers.add(new School("Strasbourg", false));
        arrayOfUsers.add(new School("Toulouse", false));

        // Create the adapter to convert the array to views
        UsersAdapter adapter = new UsersAdapter(this, arrayOfUsers);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    private void onSelect(School school) {
        Toast.makeText(this, school.mName, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, CheckPointActivity.class);
        startActivity(intent);
    }

    private class UsersAdapter extends ArrayAdapter<School> {
        public UsersAdapter(Context context, ArrayList<School> users) {
            super(context, 0, users);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            School school = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_school, parent, false);
            }

            // Lookup view for data population
            TextView viewText = (TextView) convertView.findViewById(R.id.text);

            // Populate the data into the template view using the data object
            viewText.setText(school.mName);

            convertView.setTag(position);
            if (school.mEnable) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (Integer) view.getTag();
                        // Access the row position here to get the correct data item
                        School school = getItem(position);

                        // Do what you want here...
                        onSelect(school);
                    }
                });
                viewText.setTextColor(Color.WHITE);
            }
            else {
                // TODO: Find a way to display on click event
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                viewText.setTextColor(Color.GRAY);
            }

            // Return the completed view to render on screen
            return convertView;
        }
    }

}
