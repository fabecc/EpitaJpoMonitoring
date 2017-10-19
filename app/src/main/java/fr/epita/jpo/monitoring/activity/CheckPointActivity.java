package fr.epita.jpo.monitoring.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.epita.jpo.monitoring.R;
import fr.epita.jpo.monitoring.model.JpoRunningData;
import fr.epita.jpo.monitoring.model.Step;

public class CheckPointActivity extends Activity {

    // Data
    private ArrayList<Step> mSteps;
    private JpoRunningData mCurrentJpoData;

    // Graphics
    private ListView mListView;
    private UsersAdapter mAdapter;

    private Button btCancel;
    private Button btValid;
    private Button btComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_point);

        // Load data
        loadData();
        mCurrentJpoData = new JpoRunningData();
        mCurrentJpoData.mStartTime = System.currentTimeMillis() / 1000;

        // Load graphics
        loadGraphics();

        // Create the adapter to convert the array to views
        mAdapter = new UsersAdapter(this, mSteps);

        // Attach the adapter to a ListView
        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
    }

    private void loadData() {
        // Construct the data source
        mSteps = new ArrayList<Step>();
        mSteps.add(new Step("step-accueil-epitech",  "Accueil Epitech", R.drawable.epita_site_rennes_step_accueil_epitech));
        mSteps.add(new Step("step-accueil-epita",    "Accueil EPITA",   R.drawable.epita_site_rennes_step_accueil_epita));
        mSteps.add(new Step("step-amphi",            "Amphi",           R.drawable.epita_site_rennes_step_amphi));
        mSteps.add(new Step("step-salle-ilot",       "Salle îlot",      R.drawable.epita_site_rennes_step_salle_ilot));
        mSteps.add(new Step("step-salle-machine",    "Salle Machine",   R.drawable.epita_site_rennes_step_salle_machine));
        mSteps.add(new Step("step-salle-cours",      "Salle de cours",  R.drawable.epita_site_rennes_step_salle_cours));
        mSteps.add(new Step("step-minilab",          "MiniLab",         R.drawable.epita_site_rennes_step_minilab));
        mSteps.add(new Step("step-fin-de-visite",    "Fin de visite",   R.drawable.epita_site_rennes_step_fin_de_visite));
    }

    private void loadGraphics() {
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
    }

    @Override
    public void onBackPressed() {
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

    private void onSelect(Step step) {
        mCurrentJpoData.addStep(step);
        Toast.makeText(this, step.mName, Toast.LENGTH_SHORT).show();
    }

    private class UsersAdapter extends ArrayAdapter<Step> {
        public UsersAdapter(Context context, ArrayList<Step> users) {
            super(context, 0, users);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Step step = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_step, parent, false);
            }

            // Lookup view for data population
            TextView txtName = (TextView) convertView.findViewById(R.id.text);
            TextView txtTime = (TextView) convertView.findViewById(R.id.text2);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.img);

            // Populate the data into the template view using the data object
            txtName.setText(step.mName);
            imageView.setImageDrawable(ContextCompat.getDrawable(CheckPointActivity.this, step.mImgId));
            convertView.setTag(position);

            // Adapt cell if step is not already join
            if (!mCurrentJpoData.mStep.containsKey(step.mId)) {
                // TODO: Find a way to display on click event
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSelect(getItem((Integer)view.getTag()));
                        mAdapter.notifyDataSetChanged();
                    }
                });
                txtName.setTextColor(Color.WHITE);
                txtTime.setTextColor(Color.WHITE);
                txtTime.setText("-");
            }
            // Adapt cell if step is already join
            else {
                // TODO: Find a way to display on click event
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                txtName.setTextColor(Color.GRAY);
                txtTime.setTextColor(Color.GRAY);
                txtTime.setText(Long.toString(mCurrentJpoData.mStep.get(step.mId)));
        }

            // Return the completed view to render on screen
            return convertView;
        }
    }

}
