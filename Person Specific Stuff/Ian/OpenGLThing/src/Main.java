/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description:
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
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
	ArrayList<Unit> unitList = new ArrayList<Unit>();
	ArrayList<Player> playerList = new ArrayList<Player>();
	Vector4f cameraPosition = new Vector4f(0, 0, 0, 1);
	Vector4f globalLook = new Vector4f(0, 0, -1, 1);
	Vector4f cameraUp = new Vector4f(0, 1, 0, 1);
	Vector3f cameraRotation = new Vector3f(20, 45, 0);
	Vector3f lightPosition = new Vector3f(0, 0, 0);
	Vector4f mVector = new Vector4f(10, 0, 0, 1);
	Matrix4f textureMatrix = new Matrix4f();
	Vector4f pos = new Vector4f(0, 0, 0, 1);
	Vector3f cursorPosition = new Vector3f(0, 0, 0);
	Animation a = new Animation();
	UserInterface UI = new UserInterface();
	Unit selection = null;
	Matrix4f tMatrix = new Matrix4f();
	Matrix4f rMatrix = new Matrix4f();
	Map map;
	static Main m;

	int shaderProgram;
	int vertexShader;
	int fragmentShader;

	public void start()
	{
		initializeGL();

		update();

		glDeleteProgram(shaderProgram);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		Display.destroy();
		System.exit(0);
	}

	public void initializeGL()
	{
		try
		{
			Display.setDisplayMode(Display.getDesktopDisplayMode());
			Display.setFullscreen(true);
			//Display.setVSyncEnabled(true);
			System.out.println("Display: " + Display.getDisplayMode().getWidth() + "x" + Display.getDisplayMode().getHeight() + "x" + Display.getDisplayMode().getBitsPerPixel() + " " + Display.getDisplayMode().getFrequency() + "Hz");
			Display.create();
			System.out.println("Display Working");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		map = new Map(100, 100);

		ModelReference.loadModels();

		tMatrix.translate(new Vector3f(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z));
		rMatrix.rotate(cameraRotation.x, new Vector3f(1, 0, 0));
		rMatrix.rotate(cameraRotation.y, new Vector3f(0, 1, 0));
		rMatrix.rotate(cameraRotation.z, new Vector3f(0, 0, 1));

		playerList.add(new Player("Squisy", new Vector3f(1, 0, 0), map, ModelReference.level, new Vector3f(50, -10, 50)));
		playerList.get(0).addUnit(new Marine(new Vector3f(48, -10, 24), .05F, 1f, 5, playerList.get(0), true));
		playerList.get(0).addUnit(new Marine(new Vector3f(46, -10, 19), .05F, 1f, 5, playerList.get(0), true));
		playerList.get(0).addBuilding(new CommandCenter(new Vector3f(57, -10, 33), playerList.get(0), true));
		playerList.get(0).getUnit(0).setColour(1, 0, 0);
		playerList.get(0).getUnit(1).setColour(1, 1, 0);
		playerList.get(0).getBuilding(0).setColour(0, 1, 0);

		this.selection = playerList.get(0).getUnit(0);

		for (int i = 0; i < playerList.get(0).getUnitListSize(); i++)
		{
			playerList.get(0).map.addUnit(playerList.get(0).getUnit(i));
		}

		//map.getUnitsInRadius(unitList.get(0), 100);

		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(80.0f, Display.getWidth() / Display.getHeight(), 0.3f, 1000.0f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		initializeShaders();
		glUseProgram(shaderProgram);
		initializeLighting();

		glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		glClearDepth(1.0f);
		glDepthFunc(GL_LEQUAL);

		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

		getDelta();
		lastFPS = getTime();

		Node[][] nodes = map.getNodes();
		for (int y = 0; y < nodes.length; y++)
		{
			for (int x = 0; x < nodes.length; x++)
			{
				System.out.print(nodes[y][x].getMaxUnitRadius() + "\t");
			}
			System.out.println("");
		}
	}

	public void update()
	{
		int delta = getDelta();
		while (!Display.isCloseRequested())
		{
			delta = getDelta();

			playerList.get(0).update(delta, getTime());

			updateFPS();

			cameraPosition = new Vector4f(0, 0, 0, 1);
			Matrix4f.transform(tMatrix, cameraPosition, cameraPosition);
			globalLook = new Vector4f(0, 0, -1, 1);
			Matrix4f.transform(rMatrix, globalLook, globalLook);
			cameraUp = new Vector4f(0, 1, 0, 1);
			Matrix4f.transform(rMatrix, cameraUp, cameraUp);
			Display.update();
			Display.sync(30);
		}
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
			Scanner s = new Scanner(new File("Shader.vert"));
			String line;
			while (s.hasNextLine())
			{
				line = s.nextLine();
				vertexShaderSource.append(line).append("\n");
			}
			// System.out.println(vertexShaderSource);
			s.close();
		} catch (IOException e)
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
			while (t.hasNextLine())
			{
				line = t.nextLine();
				fragmentShaderSource.append(line).append("\n");
			}
			// System.out.println(vertexShaderSource);
			t.close();
		} catch (IOException e)
		{
			System.out.println("Fragment shader did not load properly.");
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);
		if (glGetShader(vertexShader, GL_COMPILE_STATUS) == GL_FALSE)
			System.err.println("Vertex shader didn't Compile Properly.");
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		if (glGetShader(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE)
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
		glLightModel(GL_LIGHT_MODEL_AMBIENT, Resource.asFloatBuffer(new float[] { 0.05f, 0.05f, 0.05f, 1f }));
		// Resource.initLightAspect(GL_LIGHT0, GL_AMBIENT, lightPosition.x,
		// lightPosition.y, lightPosition.z);
		// Resource.initLightAspect(GL_LIGHT0, GL_DIFFUSE, 1, 1, 1);
		// Resource.initLightAspect(GL_LIGHT0, GL_SPECULAR, 1, 1, 1);
		Resource.initLightAspect(GL_LIGHT0, GL_POSITION, lightPosition.x, lightPosition.y, lightPosition.z);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_COLOR_MATERIAL);
		glMaterialf(GL_FRONT, GL_SHININESS, 10.0f);
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

	public int getDelta()
	{
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	public long getTime()
	{
		// System.out.println(Sys.getTimerResolution());
		return (Sys.getTime());

	}

	public void updateFPS()
	{
		if (getTime() - lastFPS > 1000)
		{
			Display.setTitle("FPS: " + FPS);
			FPS = 0;
			lastFPS += 1000;
		}
		FPS++;
	}

	public static void main(String[] args)
	{
		// System.out.println(glGetString(GL_VERSION));
		try
		{
			LibraryLoader.loadNativeLibraries();
		} catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Libraries Loaded");

		m = new Main();
		m.start();
	}
}
