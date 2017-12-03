# GameEngine
A game engine created in Java using the Light Weight Java Game Library (LWJGL).

How to add javadocs to project using IntelliJ IDEA IDE:
<br/>
1. File -> Project Structure...
2. Select "Modules" on the left side.  Navigate to the "Dependencies" tab.
3. Select the .jar file you wish to add a javadoc to.  Click "Edit" (pencil icon).
4. Click "Specify Documentation URL"
5. Type in the name of the URL of the specified javadoc.
<br/>
Here are a list of javadocs for the following JARs:
<br/>
lwjgl.jar --> http://legacy.lwjgl.org/javadoc/
<br/>
lwjgl_util.jar --> http://legacy.lwjgl.org/javadoc/
<br/>
PNGDecoder.jar --> http://twl.l33tlabs.org/dist/javadoc/
<br/>
slick-util.jar --> http://slick.ninjacave.com/javadoc/

<br/>
<br/>

Next you will need to specify file extensions that should be interpreted by IntelliJ IDEA:
<br/>
1. File -> Settings...
2. Build, Execution, Deployment -> Compiler
3. Add these two lines of code to the Resource Patterns text area: "!?*.glsl" and "!?*.txt"

<br/>
<br/>

How to export the game as JAR file:
1. File -> Project Structure...
2. Click Add (plus button) -> JAR -> From modules with dependencies...
3. Browse to the Main class of the project (MainGameLoop)
4. Select the directory of the MANIFEST.MF to be the Desktop.
5. 
