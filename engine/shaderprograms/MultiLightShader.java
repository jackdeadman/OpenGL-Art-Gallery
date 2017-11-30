/**
Mesh backWall = new TwoTrianglesBuilder()
                .setVertexShader()
                .setFragmentShader()
                .setUseMultiLighting(true)
                .addMainTexture()
                .saddSpecularMap()
                .addDiffuseMap()
                .addNormalMap()
                .build();

*/

public class MultiLightShader implements ShaderProgram {


    public void sendDataToGPU(GL3 gl) {
        // Goes in base
        shader.use(gl);
        shader.setFloatArray(gl, "model", model.toFloatArrayForGLSL());
        shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
        shader.setVec3(gl, "viewPos", camera.getPosition());



        ShaderHelpers.setupShaderForMultiLight(shader, worldConfig);




        // Goes in base
        shader.setInt(gl, "first_texture", 0);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureId[0]);



        // Goes in base
        gl.glBindVertexArray(vertexArrayId[0]);
        gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
        gl.glBindVertexArray(0);
    }

}
