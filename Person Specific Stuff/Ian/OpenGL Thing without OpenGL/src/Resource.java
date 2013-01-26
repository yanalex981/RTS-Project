/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description: Contains general purpose methods which can be accessed throughout the program.
 */

import static org.lwjgl.opengl.GL11.*;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Scanner;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Resource
{
	public static float magnitude2DSq(Point2D.Float position, Point2D.Float target)
	{
		return (float) ((position.x - target.x) * (position.x - target.x) + (position.y - target.y) * (position.y - target.y));
	}
}
