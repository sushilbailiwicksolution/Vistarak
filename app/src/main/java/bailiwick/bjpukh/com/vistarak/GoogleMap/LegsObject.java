package bailiwick.bjpukh.com.vistarak.GoogleMap;

import java.util.List;

/**
 * Created by Prince on 07-02-2018.
 */

public class LegsObject {
    private List<StepsObject> steps;
    public LegsObject(List<StepsObject> steps) {
        this.steps = steps;
    }
    public List<StepsObject> getSteps() {
        return steps;
    }
}
