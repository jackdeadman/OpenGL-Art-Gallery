package engine.animation;
import gmaths.*;

public class AnimationFunctions {

    public static AnimationFunction linear() {
        return new AnimationFunction() {
            public float run(float t) {
                System.out.println("Time: " + t);
                return t;
            }
        };
    }

    public static AnimationFunction sin() {
        return new AnimationFunction() {
            public float run(float t) {
                return (float)Math.sin(t * Math.PI / 2.0f);
            }
        };
    }

    public static AnimationFunction cos() {
        return new AnimationFunction() {
            public float run(float t) {
                double t2 = (Math.PI/2.0 - (t/2 * Math.PI));
                return (float)Math.cos(t2);
            }
        };
    }

    // Returns x(t) given t, x1, and x2, or y(t) given t, y1, and y2.
    //function calcBezier (aT, aA1, aA2) { return ((A(aA1, aA2) * aT + B(aA1, aA2)) * aT + C(aA1)) * aT; }

/*
    public static AnimationFunction cos() {
        return new AnimationFunction() {
            run(float t) {
                return Math.cos(t);
            }
        };
    }


    public static AnimationFunction cubicBezier() {
        return new AnimationFunction() {
            run(float t) {
                return Math.cos(t);
            }
        };
    }
*/
}
