package engine;

public abstract class LightEmittingModel extends Model {
    public abstract Light getLight();

    public LightEmittingModel(Camera camera, Light light) {
        super(camera, light);
    }
}
