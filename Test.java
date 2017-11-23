import gmaths.*;

public class Test {

    public static void main(String[] args) {

        Mat4 m1 = new Mat4(1);
        Vec4 v = new Vec4(1f,2f,3f,4f);

        System.out.println(m1);

        Vec4 result = Mat4.multiply(m1, v);
        
        System.out.println(v);
        System.out.println(result);

    }

}
