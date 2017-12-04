package engine.render;

import com.jogamp.opengl.*;
import engine.lighting.*;
import engine.*;

public abstract class PointLightEmittingModel extends Model {
    // The models should have a light
    protected PointLight containedLight;

    public PointLightEmittingModel(WorldConfiguration worldConfig) {
        super(worldConfig);
    }

    protected void initialise(GL3 gl) {
        // Automatically add the light to the config so other models
        // can calculating the effects of the light
        worldConfig.addPointLight(containedLight);
        super.initialise(gl);
    }

    public PointLight getContainedLight() {
        return containedLight;
    }

    public void set(boolean isOn) {
        // Update the state of the light
        getContainedLight().set(isOn);
    }

    public void setContainedLight(PointLight containedLight) {
        this.containedLight = containedLight;
    }

}
