import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;

public class Main 
{
	long lastFrame, lastFPS;
	int FPS;
	boolean keyForewards, keyBackwards, keyLeft, keyRight, keyUp, keyDown, keyLightMove;
    ArrayList<Unit> unitList = new ArrayList<Unit>();
    Vector3f globalPosition = new Vector3f(0, 0, 0), globalRotation = new Vector3f(45, 0, 0);
    Vector3f lightPosition = new Vector3f(0, 0, -10);
    Matrix4f textureMatrix = new Matrix4f();
    Animation a = new Animation();
    Unit selection = null;
    Matrix4f tMatrix = new Matrix4f();

    int shaderProgram;
	int vertexShader;
	int fragmentShader;

	public static void main(String[] args) 
	{
		//System.out.println(glGetString(GL_VERSION));
		try {
			LibraryLoader.loadNativeLibraries();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Libraries Loaded");

		Main m = new Main();
		m.start();
	}

	public void start() 
	{
		initializeGL();

		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			update(delta);
			renderGL();

			Display.update();
			Display.sync(30);
		}
		glDeleteProgram(shaderProgram);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		Display.destroy();
		System.exit(0);

	}

	public void initializeGL() 
	{
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Display Working");
		
		ModelReference.loadModels();
		
		unitList.add(new Unit(ModelReference.rocketTank, ModelReference.rocketTankAnimation));
		unitList.add(new Unit(ModelReference.commandCenter, null));
		unitList.add(new Unit(ModelReference.level, null));
		unitList.add(new Unit(ModelReference.barracks, null));
		unitList.add(new Unit(ModelReference.die, null));
		//unitList.add(new Unit(ModelReference.drake, null));
			
		//unitList.get(0).translate(0,  0,  -20);
		unitList.get(0).translate(-2,  -9, -20);
		unitList.get(1).translate(-5, -10, -26);
		unitList.get(2).translate(0, -10, -20);
		unitList.get(3).translate(5, -10, -13);
		unitList.get(0).setColour(1, 0, 0);
		unitList.get(1).setColour(1, 0, 0);
		unitList.get(2).setColour(1, 1, 0);
		unitList.get(3).setColour(0, 1, 0);
		//unitList.get(4).setColour(0, 0, 255);
		unitList.get(0).setState(Unit.SETUP);
		
		glViewport( 0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(80.0f, Display.getWidth() / Display.getHeight(), 0.3f, 1000.0f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		initializeShaders();
		initializeLighting();
	
		glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		glClearDepth(1.0f);
		glDepthFunc(GL_LEQUAL);
	
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		
		getDelta();
		lastFPS = getTime();
	}

	public void update(int delta)
	{
		updateInput(delta);
		
		
		
		
		//Matrix4f tempMatrix = new Matrix4f();
		//tempMatrix.rotate(globalRotation.x, new Vector3f(1, 0, 0));
		//tempMatrix.rotate(globalRotation.y, new Vector3f(0, 1, 0));
		//tempMatrix.rotate(globalRotation.z, new Vector3f(0, 0, 1));
		//tempMatrix.rotate(globalRotation.x, new Vector3f(1, 0, 0)); 
		//transformationMatrix.transform(transformationMatrix, lightPosition, lightPosition);
		//Vector4f v = new Vector4f(0, 0, 0, 1);
		//Matrix4f.transform(tMatrix, v, v);
		//Resource.initLightAspect(GL_LIGHT0, GL_POSITION, v.x, v.y, v.z);
		//System.out.println("Light Position: " + v.x + ", " + v.y + ", " + v.z);
		unitList.get(4).setPosition(lightPosition.x, lightPosition.y, lightPosition.z);
		for(int i = 0; i < unitList.size(); i++)
		{
			unitList.get(i).update();
		}
		
		/*if(Resource.planeCollision(modelList.get(0), modelList.get(1)))
		{
			modelList.get(0).setPosition(modelList.get(0).position.x, modelList.get(1).position.y, modelList.get(0).position.z);
		}*/
		updateFPS();
	}

	public void initializeShaders()
	{
		shaderProgram = glCreateProgram();
		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		StringBuilder vertexShaderSource = new StringBuilder();
		StringBuilder fragmentShaderSource = new StringBuilder();
		
		try
		{
			Scanner s = new Scanner(new File("AdvShader.vert"));
			String line;
			while(s.hasNextLine())
			{
				line = s.nextLine();
				vertexShaderSource.append(line).append("\n");
			}
			//System.out.println(vertexShaderSource);
			s.close();
		}
		catch(IOException e)
		{
			System.out.println("Vertex shader did not load properly.");
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		
		try
		{
			Scanner t = new Scanner(new File("Shader.frag"));
			String line;
			while(t.hasNextLine())
			{
				line = t.nextLine();
				fragmentShaderSource.append(line).append("\n");
			}
			//System.out.println(vertexShaderSource);
			t.close();
		}
		catch(IOException e)
		{
			System.out.println("Fragment shader did not load properly.");
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		
		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);
		if(glGetShader(vertexShader, GL_COMPILE_STATUS) == GL_FALSE)
			System.err.println("Vertex shader didn't Compile Properly.");
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		if(glGetShader(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE)
			System.err.println("Fragment shader didn't Compile Properly.");
		
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
	}

	public void initializeLighting()
	{
		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		//glEnable(GL_LIGHT1);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, Resource.asFloatBuffer(new float[]{0.05f, 0.05f, 0.05f, 1f}));
		//Resource.initLightAspect(GL_LIGHT0, GL_AMBIENT, lightPosition.x, lightPosition.y, lightPosition.z);
		//Resource.initLightAspect(GL_LIGHT0, GL_DIFFUSE, 1, 1, 1);
		//Resource.initLightAspect(GL_LIGHT0, GL_SPECULAR, 1, 1, 1);
		Resource.initLightAspect(GL_LIGHT0, GL_POSITION, lightPosition.x, lightPosition.y, lightPosition.z);
		//Resource.initLightAspect(GL_LIGHT1, GL_POSITION, 20, 0, -20);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_COLOR_MATERIAL);
		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);
		glColorMaterial(GL_FRONT_AND_BACK, GL_DIFFUSE);
	}

	public void initializeShadowMapping()
	{
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_DEPTH_TEXTURE_MODE, GL_INTENSITY);
        
        glTexGeni(GL_S, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);
        glTexGeni(GL_T, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);
        glTexGeni(GL_R, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);
        glTexGeni(GL_Q, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);
	}

	public void updateInput(int delta) 
	{
		keyForewards = Keyboard.isKeyDown(Keyboard.KEY_W);
		keyBackwards = Keyboard.isKeyDown(Keyboard.KEY_S);
		keyLeft = Keyboard.isKeyDown(Keyboard.KEY_A);
		keyRight = Keyboard.isKeyDown(Keyboard.KEY_D);
		keyUp = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		keyDown = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		keyLightMove = Keyboard.isKeyDown(Keyboard.KEY_E);
		
		if(Mouse.isButtonDown(0))
		{
			Vector3f mVector = Resource.mouseVector(globalRotation);
			//System.out.println("X: " + (Mouse.getX() - Display.getWidth()/2) + " Y: " + (Mouse.getY() - Display.getHeight()/2) + " Mouse Vector: (" + temp.x + ", " + temp.y + ", " + temp.z + ")");
			selection = Resource.select(unitList, mVector, globalPosition);
		
			if(selection != null)
			{
				System.out.println("We've got something!");
			}
		}
        if (Mouse.isButtonDown(1)) 
        	Mouse.setGrabbed(true);
        else if (Mouse.isButtonDown(2))
        	Mouse.setGrabbed(false);
		
		if (Mouse.isGrabbed()) {
            float mouseDX = Mouse.getDX() * 0.32f;
            float mouseDY = Mouse.getDY() * 0.32f;
            
            if (globalRotation.y + mouseDX >= 360)
            	globalRotation.y = globalRotation.y + mouseDX - 360;
            else if (globalRotation.y + mouseDX < 0)
            	globalRotation.y = 360 - globalRotation.y + mouseDX;
            else
            	globalRotation.y += mouseDX;
            
            if (globalRotation.x - mouseDY > 85)
            	globalRotation.x = 85;
            else if (globalRotation.x - mouseDY < -85)
            	globalRotation.x = -85;
            else
            	globalRotation.x -= mouseDY;
        }
		
		int mouseDWheel = Mouse.getDWheel();
		if(mouseDWheel != 0)
		{
			System.out.println(Mouse.getDWheel());
			tMatrix.translate(new Vector3f((float)(mouseDWheel * -0.01 * Math.sin(Math.toRadians(globalRotation.y))), 
										(float)(mouseDWheel * 0.01 * Math.sin(Math.toRadians(globalRotation.x))), 
										(float)(mouseDWheel * 0.01 * Math.cos(Math.toRadians(globalRotation.y)))));
		}
		
		/*if (Mouse.isGrabbed()) {
            float mouseDX = Mouse.getDX() * 0.32f;
            float mouseDY = Mouse.getDY() * 0.32f;
            
            tMatrix.rotate(mouseDX, new Vector3f(0, 1, 0));
            tMatrix.rotate(mouseDX, new Vector3f(1, 0, 0));
        }*/
		
		if (keyLeft) 
		{
			tMatrix.translate(new Vector3f((float)(delta * 0.01 * Math.sin(Math.toRadians(globalRotation.y + 90))), 0f, 
											(float)(delta * -0.01 * Math.cos(Math.toRadians(globalRotation.y + 90)))));
		}
		if (keyRight) 
		{
			tMatrix.translate(new Vector3f((float)(delta * -0.01 * Math.sin(Math.toRadians(globalRotation.y + 90))), 0f, 
											(float)(delta * 0.01 * Math.cos(Math.toRadians(globalRotation.y + 90)))));
		}
		if(keyForewards) 
		{
			tMatrix.translate(new Vector3f((float)(delta * -0.01 * Math.sin(Math.toRadians(globalRotation.y))), 
											(float)(delta * 0.01 * Math.sin(Math.toRadians(globalRotation.x))), 
											(float)(delta * 0.01 * Math.cos(Math.toRadians(globalRotation.y)))));
		}
		if(keyBackwards)
		{
			tMatrix.translate(new Vector3f((float)(delta * 0.01 * Math.sin(Math.toRadians(globalRotation.y))), 
											(float)(delta * -0.01 * Math.sin(Math.toRadians(globalRotation.x))), 
											(float)(delta * -0.01 * Math.cos(Math.toRadians(globalRotation.y)))));
		}
		if(keyUp)
		{
			tMatrix.translate(new Vector3f(0, 0.01f * delta, 0));
		}
			
		if(keyDown) 
		{
			tMatrix.translate(new Vector3f(0, -0.01f * delta, 0));
		}
		if(keyLightMove)
		{
			Vector4f v = new Vector4f(0, 0, 0, 1);
			Matrix4f.transform(tMatrix, v, v);
			Resource.initLightAspect(GL_LIGHT0, GL_POSITION, v.x, v.y, v.z);
		}
	}

	public int getDelta()
	{
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	public long getTime() 
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public void updateFPS() 
	{
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + FPS);
			FPS = 0;
			lastFPS += 1000;
		}
		FPS++;
	}

	public void renderGL() 
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		
		Vector4f v = new Vector4f(0, 0, 0, 1);
		Matrix4f.transform(tMatrix, v, v);
		//System.out.println(v);
		
		glPushMatrix();
		glRotatef(globalRotation.x, 1, 0, 0);
		glRotatef(globalRotation.y, 0, 1, 0);
		glRotatef(globalRotation.z, 0, 0, 1);
		glTranslatef(v.x, v.y, v.z);

		glUseProgram(shaderProgram);
		for(int i = 0; i < unitList.size(); i++)
		{
			unitList.get(i).draw();
		}
		glUseProgram(0);
		glPopMatrix();
	}
}
