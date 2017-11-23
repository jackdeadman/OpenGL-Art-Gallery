package engine;

package engine;

import java.sql.Array;
import java.util.ArrayList;

public class WorldConfiguration {

    private Camera camera;
    private ArrayList<DirectionalLight> directionalLights = new ArrayList<>();
    private ArrayList<PointLight> pointLights = new ArrayList<>();
    private ArrayList<SpotLight> spotLights = new ArrayList<>();

    public WorldConfiguration(Camera camera) {
        this.camera = camera;
    }

    // public void addDirectionalLight(DirectionalLight light) {
    //     directionalLights.add(light);
    // }

    public void addPointLight(PointLight light) {
        pointLights.add(light);
    }

    // public ArrayList<DirectionalLight> getDirectionalLights() {
    //     return directionalLights;
    // }
    //
    // public ArrayList<PointLight> getPointLights() {
    //     return pointLights;
    // }
    //
    // public ArrayList<SpotLight> getSpotLights() {
    //     return spotLights;
    // }

}
