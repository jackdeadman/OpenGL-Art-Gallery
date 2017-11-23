package engine;
import gmaths.*;

public class LightNode extends SGNode {

    private Vec4 localPoint = new Vec4(0, 0, 0, 1);

    public LightNode(String str) {
        super(str);
    }

    public LightNode(String str, Vec3 localPoint) {
        super(str);
        this.localPoint = new Vec4(localPoint.x,
                localPoint.y, localPoint.z, 1);
    }

    public Vec3 getPosition() {
        System.out.println(worldTransform);
        System.out.println(localPoint);
        Vec4 worldPoint = Mat4.multiply(worldTransform, localPoint);
        return worldPoint.toVec3();
    }

}
