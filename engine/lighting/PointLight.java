package engine.lighting;
import gmaths.*;
import engine.render.*;

public class PointLight extends Light {
    /**
    * @author Jack Deadman
    */

    // Example Attenuations: https://learnopengl.com/#!Lighting/Light-casters
    public static final Vec3 SHORT_ATTENUATION = new Vec3(1f, 0.7f, 1.8f);
    public static final Vec3 MEDIUM_ATTENUATION = new Vec3(1f, 0.22f, 0.2f);
    public static final Vec3 LONG_ATTENUATION = new Vec3(1f, 0.045f, 0.0075f);

    private Vec3 position;
    private Vec3 attenuation;

    public PointLight(Material material, Vec3 attenuation) {
        super(material);
        position = new Vec3(0f, 0f, 0f);
        this.attenuation = attenuation;
    }


    public void setPosition(Vec3 v) {
        position.x = v.x;
        position.y = v.y;
        position.z = v.z;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setAttenuation(Vec3 v) {
        attenuation.x = v.x;
        attenuation.y = v.y;
        attenuation.z = v.z;
    }

    public Vec3 getPosition() {
      return position;
  }
    public Vec3 getAttenuation () {
        return attenuation;
    }
    public float getConstant() {
        return attenuation.x;
    }
    public float getLinear() {
        return attenuation.y;
    }
    public float getQuadratic() {
        return attenuation.z;
    }

}
