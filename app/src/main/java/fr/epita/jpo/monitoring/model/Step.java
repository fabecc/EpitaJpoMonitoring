package fr.epita.jpo.monitoring.model;

import fr.epita.jpo.monitoring.R;

/**
 * Created by fabecc on 18/10/2017.
 */

public class Step {

    // Data model
    public String mName = "";
    public String mTime = "1h24min";
    public int mImgId = R.drawable.epita_logo_mini;

    // Some element use on application life cycle
    public boolean mEnable = true;

    public Step(String name) {
        mName = name;
    }

    public Step(String name, int imgId) {
        mName = name;
        mImgId = imgId;
    }


}
