package fr.epita.jpo.monitoring.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.epita.jpo.monitoring.R;
import fr.epita.jpo.monitoring.model.School;

public class CheckPointActivity extends Activity {

    private Button btCancel;
    private Button btValid;
    private Button btComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_point);

        btCancel = (Button)findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancel();
            }
        });

        btValid = (Button)findViewById(R.id.btValid);
        btValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onValid();
            }
        });

        btComment = (Button)findViewById(R.id.btComment);
        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onComment();
            }
        });

        // Construct the data source
        ArrayList<School> arrayOfUsers = new ArrayList<School>();
        arrayOfUsers.add(new School("Accueil Epitech", false));
        arrayOfUsers.add(new School("Accueil EPITA", false));
        arrayOfUsers.add(new School("Amphi", true));
        arrayOfUsers.add(new School("Salle ilot", true));
        arrayOfUsers.add(new School("Salle Machine", false));
        arrayOfUsers.add(new School("Salle de cours", true));
        arrayOfUsers.add(new School("MiniLab", true));
        arrayOfUsers.add(new School("Fin de visite", false));

        // Create the adapter to convert the array to views
        UsersAdapter adapter = new UsersAdapter(this, arrayOfUsers);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    private void onComment() {
        Toast.makeText(this, "Add comment ?", Toast.LENGTH_SHORT).show();
    }

    private void onValid() {
        finish();
    }

    private void onCancel() {
        finish();
    }

    private void onSelect(School school) {
        Toast.makeText(this, school.mName, Toast.LENGTH_SHORT).show();
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
