package engine.scenegraph;
import gmaths.*;
import engine.lighting.*;

public class LightNode extends SGNode {

    private PointLight light;
    private Vec4 worldPosition;

    public LightNode(String str, PointLight light) {
        super(str);
        this.light = light;
    }

    protected void update(Mat4 t) {
        super.update(t);
        Vec4 worldPoint = Mat4.multiply(worldTransform, new Vec4());
        light.setPosition(worldPoint.toVec3());
    }
}
