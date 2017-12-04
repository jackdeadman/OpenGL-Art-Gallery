package engine.lighting;
import gmaths.*;
import engine.render.*;

public class DirectionalLight {

  private Vec4 direction;
  private Vec3 colour;
  private Material material;

  public DirectionalLight(Vec3 direction, Vec3 colour) {
    this.direction = direction.toVec4();
    this.colour = colour;

    material = new Material();
    material.setAmbient(0.01f, 0.01f, 0.01f);
    material.setDiffuse(0.01f, 0.01f, 0.01f);
    material.setSpecular(0.01f, 0.01f, 0.01f);
  }

  public void set(boolean on) {
      material = new Material();
      if (on) {
          material.setAmbient(0.01f, 0.01f, 0.01f);
          material.setDiffuse(0.01f, 0.01f, 0.01f);
          material.setSpecular(0.01f, 0.01f, 0.01f);
      } else {
          material.setAmbient(0.0f, 0.0f, 0.0f);
          material.setDiffuse(0.0f, 0.0f, 0.0f);
          material.setSpecular(0.0f, 0.0f, 0.0f);
      }
  }

  public Material getMaterial() {
      return material;
  }

  public void setDirection(Vec3 v) {
    direction.x = v.x;
    direction.y = v.y;
    direction.z = v.z;
  }

  public void setDirection(float x, float y, float z) {
    direction.x = x;
    direction.y = y;
    direction.z = z;
  }


  public Vec3 getDirection() {
    // Abstract implementation detail that the
    // direction is stored as a vec4.
    return direction.toVec3();
  }

  public Vec3 getColour() {
    return colour;
  }

}
