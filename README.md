# COM4503 3D Computer Graphics Programming Assignment

Video URL: https://www.youtube.com/watch?v=CkXqwmIe0yY

Main Program: Arty.java

[![Demo video](https://img.youtube.com/vi/CkXqwmIe0yY/0.jpg)](https://www.youtube.com/watch?v=CkXqwmIe0yY)

## Project Structure

All the required features have been implemented. The code for the lighting has been mainly taken from `https://learnopengl.com/`.
The textures have been either created by me or taken from `https://www.textures.com/`. The images on the hands on the wall can be found `unsplash.com`

Some additional features added:
- Anti-aliasing
- Different View matrix for skybox
- Normal map for wall with the window.

Some important packages...

### engine
The core classes for generating Computer Graphics are found in here. The classes
are extended or used in combination to create the actual scene.

### engine.animation
The core code for the animation engine is here. The code is very generic and allows any animation to be
created with an object that is "Interpolatable" e.g A hand configuration or simply a number.

## enigne.render
This package contains a lot the classes that tie the whole application together. Mainly Scene and Model, these classes abstract
a lot of the opengl code to allow for further models and scenes to be more easily implemented.

### gmaths
Maths library created by Dr Steve Maddock similar to glm. Some slight modifications have
been made by me. (Changed Near Plane distance, MatrixVector multplication and added a view matrix for sky box)

### meshes
Contains simple geometry that can be combined to create models. ShaderPrograms can be attached
to these models alter their appearance. These have been taken from the labsheets but adapted to be less coupled to perticular shaders.

### models
Contains classes to create 3D objects which are a combination of meshes which have been transformed and textured.

### scene
Files specific to the scene we are creating for this project.

### shaders
Contains glsl files for the vertex and fragment shaders.
