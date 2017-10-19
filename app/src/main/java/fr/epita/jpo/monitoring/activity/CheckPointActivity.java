package fr.epita.jpo.monitoring.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    private TextView mTxtTime;
    private Button mBtCancel;
    private Button mBtValid;
    private Button mBtComment;

    // Internal code
    private final static String mail = "fabrice.hesling@epita.fr";
    private final static int CODE_SEND = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_point);

        // Load data
        loadData();
        mCurrentJpoData = new JpoRunningData();
        mCurrentJpoData.mStartTime = System.currentTimeMillis() / 1000;
        mCurrentJpoData.mStartTimeForHuman = Calendar.getInstance().getTime();

        // Load graphics
        loadGraphics();

        // Create the adapter to convert the array to views
        mAdapter = new UsersAdapter(this, mSteps);

        // Attach the adapter to a ListView
        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(mAdapter);

        udpateTime();
    }

    private void loadData() {
        // Construct the data source
        mSteps = new ArrayList<Step>();
        mSteps.add(new Step("step-accueil-epitech",  "Accueil Epitech", R.drawable.epita_site_rennes_step_accueil_epitech));
        mSteps.add(new Step("step-accueil-epita",    "Accueil EPITA",   R.drawable.epita_site_rennes_step_accueil_epita));
        mSteps.add(new Step("step-amphi",            "Amphi",           R.drawable.epita_site_rennes_step_amphi));
        mSteps.add(new Step("step-salle-ilot",       "Salle Pédago",      R.drawable.epita_site_rennes_step_salle_ilot));
        mSteps.add(new Step("step-salle-machine",    "Salle Projets",   R.drawable.epita_site_rennes_step_salle_machine));
        mSteps.add(new Step("step-salle-cours",      "Salle de cours",  R.drawable.epita_site_rennes_step_salle_cours));
        mSteps.add(new Step("step-minilab",          "MiniLab",         R.drawable.epita_site_rennes_step_minilab));
        mSteps.add(new Step("step-fin-de-visite",    "Fin de visite",   R.drawable.epita_site_rennes_step_fin_de_visite));
    }

    private void loadGraphics() {
        int imgId = getIntent().getIntExtra("img-id", -1);
        if (imgId != -1) {
            ImageView img = (ImageView)findViewById(R.id.titleImg);
            img.setImageDrawable(ContextCompat.getDrawable(CheckPointActivity.this, imgId));
        }

        mBtCancel = (Button)findViewById(R.id.btCancel);
        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancel();
            }
        });

        mBtValid = (Button)findViewById(R.id.btValid);
        mBtValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onValid();
            }
        });

        mBtComment = (Button)findViewById(R.id.btComment);
        mBtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onComment();
            }
        });

        mTxtTime = (TextView)findViewById(R.id.txtTime);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                udpateTime();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    @Override
    public void onBackPressed() {
        onCancel();
    }

    private void udpateTime() {
        mTxtTime.setText(formatTime(mCurrentJpoData.getTimeSinceStart()));
    }

    private String formatTime(long second) {
        long hours = second / 3600;
        long minutes = (second % 3600) / 60;
        long seconds = second % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void onComment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter un commentaire");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(mCurrentJpoData.mComment);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentJpoData.mComment = input.getText().toString();
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void onValid() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Voulez-vous terminer la visite ?");

        // Set up the buttons
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                validAndFinishVisit();
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void validAndFinishVisit() {
        String data = getVisitDataAsCSV();

        String message = "Bonjour,\n"
                + "\n"
                + "Voici les informations suite à une visite pendant la JPO:\n"
                + "\n"
                + data
                + "\n"
                + "Commentaire de visite: '" + mCurrentJpoData.mComment + "'\n"
                + "\n"
                + "Bonne réception\n";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
        intent.putExtra(Intent.EXTRA_SUBJECT, "JPO - Data sur une visite");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        startActivityForResult(Intent.createChooser(intent, "Send Email"), CODE_SEND);
//        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_SEND){
            finish();
        }
    }

    // Format is
    //
    private String getVisitDataAsCSV() {
        StringBuilder header = new StringBuilder();
        StringBuilder data = new StringBuilder();

        // Start
        header.append("start").append(',');
        data.append(new SimpleDateFormat("HH:mm:ss").format(mCurrentJpoData.mStartTimeForHuman)).append(',');

        // Each step
        for(Step step: mSteps) {
            header.append(step.mId).append(',');

            if (!mCurrentJpoData.mStep.containsKey(step.mId))
                data.append(',');
            else
                data.append(mCurrentJpoData.mStep.get(step.mId)).append(',');
        }

        header.append("comment");
        data.append(mCurrentJpoData.mComment);

        return header.toString() + "\n\n" + data.toString() + "\n";
    }

    private void onCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Voulez-vous annuler la visite ?");

        // Set up the buttons
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void onSelect(Step step) {
        mCurrentJpoData.addStep(step);
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

                txtTime.setText(formatTime(mCurrentJpoData.mStep.get(step.mId)));
        }

            // Return the completed view to render on screen
            return convertView;
        }
    }

}
