package fr.epita.jpo.monitoring.model;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by fabecc on 19/10/2017.
 */

public class JpoRunningData {

    // Start time of visit (in s)
    public long mStartTime;

    // Comment on the visit
    public String mComment = "";

    // All step during the visite with time of each step (time in s relative to start time)
    public HashMap<String, Long> mStep = new HashMap<>();

    public void addStep(Step step) {
        mStep.put(step.mId, (System.currentTimeMillis() / 1000) - mStartTime);
    }
}
