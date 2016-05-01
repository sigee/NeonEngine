# NeonEngine

## Changelog:

**Rendering:**
- Added the ability to render to a CubeMap
- Renamed shadowMapGenerator shaders to shadowMapping
- Renamed filterGausBlur7x1 shader to filterGausBlur
- Fixed bloom blurScale value
- Fixed font rendering and made the mainFont settable

**Physics:**
- Fixed physics simulation slowing down at lower frame rates

**Other:**
- Moved image functions from the Util class into the new ImageUtil class
- Changed CoreEngines init function to take in the game object first
- Renamed CoreEngine class to NeonEngine
- Fixed getMousePosition function in the Input class
- Moved all physics component classes to the components package
- Moved the demos to the new demo repository
- The icon and cursor resources now have to be in the res folder

## Features needed for 1.0.0:

**Rendering:**
- PointLight Shadows

**Physics:**
- Masking

**Other:**
- Animation Support

## Ideas and bugs:

**Rendering:**
- Remove the black line of DirectionalLight Shadows
- HDR

**Other:**
- Add a text input system(copy, paste, delete, return, mark and cut)
- Locked mouse area
- Controller support
- Networking