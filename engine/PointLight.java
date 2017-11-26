package engine;
import gmaths.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class PointLight {

  private Vec4 position;
  private Vec3 colour;
  private Vec3 attenuation;
  private Material material;

  public PointLight(Vec3 colour, Vec3 attenuation) {
    position = new Vec4(0f, 0f, 0f, 1f);
    this.colour = colour;
    this.attenuation = attenuation;
    material = new Material();
    material.setAmbient(0.5f, 0.5f, 0.5f);
    material.setDiffuse(0.8f, 0.8f, 0.8f);
    material.setSpecular(10.0f, 1.0f, 1.0f);
  }

  public Material getMaterial() {
      return material;
  }

  public void set(boolean isOn) {
      if (isOn) {
          material.setAmbient(0.5f, 0.5f, 0.5f);
          material.setDiffuse(0.8f, 0.8f, 0.8f);
          material.setSpecular(10.0f, 1.0f, 1.0f);
      } else {
          material.setAmbient(0.0f, 0.0f, 0.0f);
          material.setDiffuse(0.0f, 0.0f, 0.0f);
          material.setSpecular(0.0f, 0.0f, 0.0f);
      }
  }

  public void setMaterial(Material mat) {
      material = mat;
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

  public void setColour(Vec3 v) {
    colour.x = v.x;
    colour.y = v.y;
    colour.z = v.z;
  }

  public void setAttenuation(Vec3 v) {
    colour.x = v.x;
    colour.y = v.y;
    colour.z = v.z;
  }

  public void setColour(float x, float y, float z) {
    colour.x = x;
    colour.y = y;
    colour.z = z;
  }

  public Vec3 getPosition() {
    // Abstract implementation detail that the
    // position is stored as a vec4.
    return position.toVec3();
  }

  public Vec3 getColour() {
    return colour;
  }

  public Vec3 getAttenuation () {
    return attenuation;
  }

}
