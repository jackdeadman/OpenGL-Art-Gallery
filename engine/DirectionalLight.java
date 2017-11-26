package engine;
import gmaths.*;
import
java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class DirectionalLight {

  private Vec4 direction;
  private Vec3 colour;
  private Material material;
  private Material offMaterial;
  private Material onMaterial;


  public DirectionalLight(Vec3 direction, Vec3 colour) {
    this.direction = direction.toVec4();
    this.colour = colour;

    onMaterial = new Material();
    onMaterial.setAmbient(0.001f, 0.001f, 0.001f);
    onMaterial.setDiffuse(0.001f, 0.001f, 0.001f);
    onMaterial.setSpecular(0.001f, 0.001f, 0.001f);

    offMaterial = new Material();
    offMaterial.setAmbient(0.0f, 0.0f, 0.0f);
    offMaterial.setDiffuse(0.0f, 0.0f, 0.0f);
    offMaterial.setSpecular(0.0f, 0.0f, 0.0f);

    material = onMaterial;
  }

  public void set(boolean on){}

  public void turnOff() {
      material = offMaterial;
  }

  public void turnOn() {
      material = onMaterial;
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

  public void setColour(Vec3 v) {
    colour.x = v.x;
    colour.y = v.y;
    colour.z = v.z;
  }

  public void setColour(float x, float y, float z) {
    colour.x = x;
    colour.y = y;
    colour.z = z;
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
