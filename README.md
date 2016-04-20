## NeonEngine

## Changelog:

Rendering:
- Renamed shaders
- Made it possible to enable bloom after creating the Window
- Optimized updateUniforms function
- Optimized shader2D and forwardParticleAmbient shaders
- Added mesh2DInFrustum function
- Optimized and fixed Skybox class
- Added support for Texture and CubeMap types

Other:
- Java 8 is required
- Added functions to get and set material properties easily
- Removed MappedValues class
- Fixed addToEngine function in the component classes
- Removed getRootEntity and getRootEntity2D functions in the Game class
- Added removeEntity2D function in the Game class
- Optimized removeEmptyStrings function in Util lass
- Made framerate an int
- Renamed some variables and some refactoring
- Removed null check in the get... functions of Materials and the RenderingEngine
- Moved OBJIndex class into the OBJModel class

## Todo:

Rendering:
- Ability to render to a CubeMap
- PointLight Shadows
- Remove the black line of DirectionalLight Shadows
- Rewrite font rendering and loading
- HDR

Physics:
- Masking

Other:
- Add a text input system(copy, paste, delete, return, mark and cut)
- Locked mouse area
- Animation Support
- Controller support
