package meshes.shaderprograms;

import java.util.*;
import engine.*;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public Interface ShaderProgram {

    public void ShaderProgram(GL3 gl, String vertexShader, String fragmentShader);
    public void sendDataToGPU(GL3 gl);

}
