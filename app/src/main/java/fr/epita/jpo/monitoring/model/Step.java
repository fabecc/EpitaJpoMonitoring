package fr.epita.jpo.monitoring.model;

import fr.epita.jpo.monitoring.R;

/**
 * Created by fabecc on 18/10/2017.
 */

public class Step {

    // Data model
    public String mId = "";
    public String mName = "";
    public String mTime = "1h24min";
    public int mImgId = R.drawable.epita_logo_mini;

    public Step(String id, String name) {
        mId = id;
        mName = name;
    }

    public Step(String id, String name, int imgId) {
        mId = id;
        mName = name;
        mImgId = imgId;
    }


}
