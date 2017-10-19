package fr.epita.jpo.monitoring.model;

import fr.epita.jpo.monitoring.R;

/**
 * Created by fabecc on 18/10/2017.
 */

public class School {

    // Data model
    public String mName;
    public int mImgId = R.drawable.epita_logo_mini;

    // Some element use on application life cycle
    public boolean mEnable;

    public School(String name, boolean enable) {
        mName = name;
        mEnable = enable;
    }

}
