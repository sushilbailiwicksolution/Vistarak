package bailiwick.bjpukh.com.vistarak.GoogleMap;

import java.util.List;

/**
 * Created by Prince on 07-02-2018.
 */

public class RouteObject {
    private List<LegsObject> legs;

    public RouteObject(List<LegsObject> legs) {
        this.legs = legs;
    }

    public List<LegsObject> getLegs() {
        return legs;
    }
}
