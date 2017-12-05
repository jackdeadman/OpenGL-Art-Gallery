package engine.scenegraph;
import gmaths.*;
import engine.lighting.*;

public class PointLightNode extends SGNode {

    private PointLight light;
    private Vec4 startingPosition;

    public PointLightNode(String str, PointLight light) {
        super(str);
        this.light = light;
        Vec3 position = light.getPosition();
        startingPosition = new Vec4(position.x, position.y, position.z, 1);
    }

    protected void update(Mat4 t) {
        super.update(t);
        Vec4 worldPoint = Mat4.multiply(worldTransform, startingPosition);
        light.setPosition(worldPoint.toVec3());
    }
}
