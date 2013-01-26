/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description:
 */

import java.awt.geom.Point2D;
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
	Unit selection = null;
	Matrix4f tMatrix = new Matrix4f();
	Matrix4f rMatrix = new Matrix4f();
	Map map;
	static Main m;

	public void start()
	{
		initializeGL();

		update();

		Display.destroy();
		System.exit(0);
	}

	public void initializeGL()
	{
		map = new Map(100, 100);

		playerList.add(new Player("Squisy", new Vector3f(1, 0, 0), map, new Vector3f(50, -10, 50)));
		playerList.get(0).addUnit(new Marine(new Point2D.Float(48, 24), .05F, 1f, 5, playerList.get(0), true));
		playerList.get(0).addUnit(new Marine(new Point2D.Float(46, 19), .05F, 1f, 5, playerList.get(0), true));
		playerList.get(0).addBuilding(new CommandCenter(new Point2D.Float(57, 33), playerList.get(0), true));

		this.selection = playerList.get(0).getUnit(0);

		for (int i = 0; i < playerList.get(0).getUnitListSize(); i++)
		{
			playerList.get(0).map.addUnit(playerList.get(0).getUnit(i));
		}

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
