package bailiwick.bjpukh.com.vistarak.GoogleMap;

import java.util.List;

/**
 * Created by Prince on 07-02-2018.
 */

public class DirectionObject {
    private List<RouteObject> routes;
    private String status;
    public DirectionObject(List<RouteObject> routes, String status) {
        this.routes = routes;
        this.status = status;
    }
    public List<RouteObject> getRoutes() {
        return routes;
    }
    public String getStatus() {
        return status;
    }
}
