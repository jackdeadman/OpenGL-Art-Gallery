# COM4503 3D Computer Graphics Programming Assignement

## Project Structure

### engine
The core classes for generating Computer Graphics are found in here. The classes
are extended or used in combination to create the actual scene.

### gmaths
Maths library created by Dr Steve Maddock similar to glm. Some slight modifications have
been made by me.

### meshes
Contains simple geometry that can be combined to create models. ShaderPrograms can be attached
to these models alter their appearance.

### models
Contains classes to create 3D objects which are a combination of meshes which have been transformed and textured.

### scene
Files specific to the scene we are creating for this project.

### shaders
Contains glsl files for the vertex and fragment shaders.

### Textures
Load of images found on textures.com and unsplash.com
