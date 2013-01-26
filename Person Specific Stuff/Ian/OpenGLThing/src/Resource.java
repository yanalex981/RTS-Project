/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description: Contains general purpose methods which can be accessed throughout the program.
 */

import static org.lwjgl.opengl.GL11.*;

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
	
	/**
	 * Loads Wavefront .obj files and accompanying .mtl files into the program and interprets them into mesh/model data.
	 * Each file defines vertices normals and faces and is subdivided into meshes which allow for multi-material models.
	 * @param String fileName: The name of the file being read from.
	 * @return Model: The Model constructed from the file data.
	 * @throws FileNotFoundException: Exception pertaining to reading files.
	 * @throws IOException: Exception pertaining to reading files.
	 */
	public static Model loadModel(String fileName) throws FileNotFoundException, IOException
	{
		Scanner s = new Scanner(new File(fileName));
		Model model = new Model();
		String line;

		while (s.hasNextLine())
		{
			line = s.nextLine();

			if (line.startsWith("o "))
			{
				model.meshList.add(new Mesh(line.split(" ")[1], model));
			}
			else if (line.startsWith("v "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				model.verticies.add(new Vector3f(x, y, z));
			}
			else if (line.startsWith("vn "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				model.normals.add(new Vector3f(x, y, z));
			}
			else if (line.startsWith("f "))
			{
				if (line.split(" ").length == 4)
				{
					Vector3f vertexIndicies = new Vector3f(Float.valueOf(line.split(" ")[1].split("//")[0]), Float.valueOf(line.split(" ")[2].split("//")[0]), Float.valueOf(line.split(" ")[3].split("//")[0]));
					Vector3f normalIndicies = new Vector3f(Float.valueOf(line.split(" ")[1].split("//")[1]), Float.valueOf(line.split(" ")[2].split("//")[1]), Float.valueOf(line.split(" ")[3].split("//")[1]));
					model.meshList.get(model.meshList.size() - 1).triFaces.add(new TriangleFace(vertexIndicies, normalIndicies));
				}
				if (line.split(" ").length == 5)
				{
					Vector4f vertexIndicies = new Vector4f(Float.valueOf(line.split(" ")[1].split("//")[0]), Float.valueOf(line.split(" ")[2].split("//")[0]), Float.valueOf(line.split(" ")[3].split("//")[0]), Float.valueOf(line.split(" ")[4].split("//")[0]));
					Vector4f normalIndicies = new Vector4f(Float.valueOf(line.split(" ")[1].split("//")[1]), Float.valueOf(line.split(" ")[2].split("//")[1]), Float.valueOf(line.split(" ")[3].split("//")[1]), Float.valueOf(line.split(" ")[4].split("//")[1]));
					model.meshList.get(model.meshList.size() - 1).quadFaces.add(new QuadFace(vertexIndicies, normalIndicies));
				}
			}
		}
		s.close();

		/*
		s = new Scanner(new File(fileName.replace(".obj", ".mtl")));
		while (s.hasNextLine())
		{
			line = s.nextLine();

			for (int i = 0; i < model.meshList.size(); i++)
			{
				if(model.meshList.get(i).name.equals(line))
				{
					line = s.nextLine();
					model.meshList.get(i).setKa(new Vector3f(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3])));
					line = s.nextLine();
					model.meshList.get(i).setKd(new Vector3f(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3])));
					line = s.nextLine();
					model.meshList.get(i).setKs(new Vector3f(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3])));
					line = s.nextLine();
					model.meshList.get(i).setShininess(Float.valueOf(line.split(" ")[1]));
				}
			}
		}
		*/

		model.loadCollisionMask();

		for (int i = 0; i < model.meshList.size(); i++)
		{
			model.meshList.get(i).generateDrawList();
		}
		//model.initializeDisplayList();

		return model;
	}

	
	/**
	 * Method to make interacting with LWJGL lighting easier.
	 * 
	 * @param i: Identifies which light to operate on.
	 * @param j: Identifies the light aspect to change.
	 * @param a: First given value.
	 * @param b: Second given value.
	 * @param c: THird given value.
	 */
	public static void initLightAspect(int i, int j, float a, float b, float c)
	{
		glLight(i, j, asFloatBuffer(new float[] { a, b, c, 1f }));
	}
	
	/**
	 * Method to interact with float BUffers.
	 * 
	 * @param values: Given values to put through the float buffer.
	 * @return: The buffer created.
	 */
	public static FloatBuffer asFloatBuffer(float[] values)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}

	/**
	 * Tests for a collision between an axis alignesd bounding box and a point.
	 * 
	 * @param xCollisionRadii: The bounding boxes x radii.
	 * @param yCollisionRadii: The bounding boxes y radii.
	 * @param zCollisionRadii: The bounding boxes z radii.
	 * @param point: The Point to test collision with.
	 * @return Boolean: Value that denotes a true or false collision.
	 */
	public static boolean axisAlignedBoundingBoxCollision(float xCollisionRadii, float yCollisionRadii, float zCollisionRadii, Vector3f point)
	{
		// System.out.println("Box: Min(" + (box.position.x + box.xMin) + ", " +
		// (box.position.y + box.yMin) + ", " + (box.position.z + box.zMin) +
		// ") Max(" + (box.position.x + box.xMax) + ", "+ (box.position.y +
		// box.yMax) + ", " + (box.position.z + box.zMax) + ")");
		// System.out.println("Point: (" + point.x + ", " + point.y + ", " +
		// point.z + ")");

		if (Math.abs(point.x) < xCollisionRadii && Math.abs(point.y) < yCollisionRadii && Math.abs(point.z) < zCollisionRadii)
			return true;

		return false;
	}

	public static boolean axisAlignedBoundingBoxCollisionAltModel(Vector3f point, Unit box)
	{
		if (point.x > box.getModel().xMin + box.getPosition().x && point.x < box.getModel().xMax + box.getPosition().x && point.y > box.getModel().yMin + box.getPosition().y && point.y < box.getModel().yMax + box.getPosition().y && point.z > box.getModel().zMin + box.getPosition().z && point.z < box.getModel().zMax + box.getPosition().z)
			return true;

		return false;
	}
	
	public static float magnitude2DSq(Vector3f position, Vector3f target)
	{
		return (float) ((position.x - target.x) * (position.x - target.x) + (position.z - target.z) * (position.z - target.z));
	}

	/**
	 * Two dimensional collision between a point and a rectangle.
	 * 
	 * @param point: The point to be tested for intersection.
	 * @param xMin: The minimum x value for the point so that a collision is true.
	 * @param xMax: The maximum x value for the point so that a collision is true.
	 * @param zMin: The minimum y value for the point so that a collision is true.
	 * @param zMax: The maximum y value for the point so that a collision is true.
	 * @return Boolean: Identifier in order to tell if a collision occured.
	 */
	public static boolean pointRectangleCollision(Vector3f point, float xMin, float xMax, float zMin, float zMax)
	{
		//System.out.println("Point: " + point);
		//System.out.println("Min : " + xMin + ", " + zMin + " Max: " + xMax + ", " + zMax);

		if (point.x > xMin && point.x < xMax && point.z > zMin && point.z < zMax)
			return true;

		return false;
	}

	/**
	 * Interprets the mouse position on the screen into a three dimensional vector used for selecting things
	 * by multiplying it by the inverse of the projection matrix and applying model view transformations.
	 * 
	 * @param rMatrix: The rotational transformations applied to the ray being created.
	 * @return Vector4f: A ray representing a mouse click in the three dimensional space.
	 */
	public static Vector4f rayPick(Matrix4f rMatrix)
	{
		Vector4f mVector = new Vector4f((float) Mouse.getX() * 2 / Display.getWidth() - 1, (float) Mouse.getY() * 2 / Display.getHeight() - 1, -1, 1);

		FloatBuffer modMatrix = BufferUtils.createFloatBuffer(16);
		FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, modMatrix);
		glGetFloat(GL_PROJECTION_MATRIX, projMatrix);

		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f projectionMatrix = new Matrix4f();
		Matrix4f temp = new Matrix4f();

		//modelMatrix.load(modMatrix);
		projectionMatrix.load(projMatrix);
		//System.out.println(projectionMatrix);

		Matrix4f.mul(modelMatrix, projectionMatrix, temp);
		Matrix4f.mul(temp, rMatrix, temp);
		temp.invert();
		Matrix4f.transform(temp, mVector, mVector);

		mVector.normalise();

		return mVector;
	}

	/**
	 * constructs a plane from four points, three are used to define the plane and one is used to define a point which intersects the plane.
	 * 
	 * @param a: The first point used to define the plane.
	 * @param b: The second point used to define the plane.
	 * @param c: The third point used to define the plane.
	 * @param planePoint: A point which intersects the plane
	 * @return A plane object defined by a normal and a point.
	 */
	public static Plane computePlane(Vector3f a, Vector3f b, Vector3f c, Vector3f planePoint)
	{
		Vector3f normal = new Vector3f();
		float d;

		Vector3f.sub(a, b, b);
		Vector3f.sub(a, c, c);
		Vector3f.cross(b, c, normal);
		normal.normalise();
		d = Vector3f.dot(normal, a);

		//System.out.println("Plane Normal: " + normal);
		//System.out.println("Point on Plane: " + planePoint);

		return new Plane(normal, d, planePoint);
	}

	/**
	 * Gives the scalar needed to apply to a line in order to get the point where the line and plane intersect.
	 * 
	 * @param ray: THe line being tested for intersection.
	 * @param point: a point off setting the plane to conform to the lines local space.
	 * @param plane: THe plane being tested for intersection.
	 * @return The scalar of intersection.
	 */
	public static float linePlaneIntersection(Vector3f ray, Vector3f point, Plane plane)
	{
		float scalar = 0;
		float nDotPoint, nDotRay;
		Vector3f v = new Vector3f(0, 0, 0);
		;
		Vector3f.sub(plane.getPoint(), point, v);
		scalar = (Vector3f.dot(plane.getNormal(), v) / (Vector3f.dot(plane.getNormal(), ray)));

		return scalar;
	}
	
	/**
	 * Gets the unit vector which represents the direction from object p to object d.
	 * 
	 * @param pX: X value of the p object.
	 * @param pY: y value of the p object.
	 * @param dX: x value of the d object.
	 * @param dY: y value of the d object.
	 * @return THe vector representing the direction from p to d.
	 */
	public static Vector2f getDirection2D(float pX, float pY, float dX, float dY)
	{
		Vector2f direction = new Vector2f(pX - dX, dY - pY);
		return direction.normalise(direction);
	}
	
	/**
	 * * Gets the unit vector which represents the direction from object p to object d.
	 * 
	 * @param pX: X value of the p object.
	 * @param pY: y value of the p object.
	 * @param dX: x value of the d object.
	 * @param dY: y value of the d object.
	 * @param pZ: z value of the p object.
	 * @param dZ: x value of the d object
	 * @return The vector representing the direction from p to d.
	 */
	public static Vector3f getDirection3D(float pX, float pY, float pZ, float dX, float dY, float dZ)
	{
		Vector3f direction = new Vector3f(dX - pX, dY - pY, dZ - pZ);
		return direction.normalise(direction);
	}
	
	public static float magnitude2D(Vector3f position, Vector3f target)
	{
		return (float)Math.sqrt(Math.pow((position.x - target.x), 2) + Math.pow((position.z - target.z), 2));
	}

	/**
	 * Selects a unit form the given list based on collision with a pick ray.
	 * 
	 * @param unitList: The list being scanned for a selection candidate candidate.
	 * @param level: The plane representing the underlying level.
	 * @param mVector: The pick ray to test for collisions
	 * @param position: the position of the camera.
	 * @return The unit found to be within the selection bounds. (null if no unit found)
	 */
	public static ControllableObject select(ArrayList<ControllableObject> unitList, Vector4f mVector, Vector4f position)
	{
		for (int i = 0; i < unitList.size(); i++)
		{
			ControllableObject unit = unitList.get(i);

			//System.out.println(unit.getName());
			Vector3f point1, point2, point3;
			point1 = new Vector3f(unit.getModel().xMin, unit.getModel().yMin, unit.getModel().zMin);
			point2 = new Vector3f(unit.getModel().xMin, unit.getModel().yMin, unit.getModel().zMax);
			point3 = new Vector3f(unit.getModel().xMax, unit.getModel().yMin, unit.getModel().zMin);
			Plane plane = computePlane(point1, point2, point3, unit.getPosition());
			float scalar = linePlaneIntersection(new Vector3f(mVector.x, mVector.y, mVector.z), new Vector3f(position.x, -position.y, position.z), plane);

			//System.out.println("Scalar: " + scalar);

			if (pointRectangleCollision(new Vector3f(-position.x + mVector.x * scalar, -position.y + mVector.y * scalar, -position.z + mVector.z * scalar), unit.getModel().xMin + unit.getPosition().x, unit.getModel().xMax + unit.getPosition().x, unit.getModel().zMin + unit.getPosition().z, unit.getModel().zMax + unit.getPosition().z) && scalar > 0)
			{
				return unit;
			}
			//System.out.println(position);
			//System.out.println(mVector);
			/*
			 * for (int j = 0; j < 5000; j++) { Vector3f point = new Vector3f((float) (-position.x + mVector.x * j * .1), (float) (-position.y + mVector.y * j * .1), (float) (-position.z + mVector.z * j * .1)); //if (axisAlignedBoundingBoxCollision(unitList.get(i).getModel().xCollisionRadii, unitList.get(i).getModel().yCollisionRadii, unitList.get(i).getModel().zCollisionRadii, new Vector3f(point.x - unitList.get(i).getPosition().x, point.y - unitList.get(i).getPosition().y, point.z -
			 * unitList.get(i).getPosition().z))) if(axisAlignedBoundingBoxCollisionAltModel(point, unitList.get(i))) { return unitList.get(i); } }
			 */
		}

		return null;
	}

	/**
	 * Finds the point of intersection between the level plane and a line(pick ray).
	 * 
	 * @param level: The level plane of intersection.
	 * @param mVector: The pick ray of intersection.
	 * @param position: The camera position.
	 * @return The point of intersection of the pick ray with the level plane.
	 */
	public static Vector3f selectLevelPlane(Model level, Vector4f mVector, Vector4f position, Vector3f levelPosition)
	{
		//System.out.println(level.getName());
		Vector3f point1, point2, point3;
		point1 = new Vector3f(level.xMin, level.yMin, level.zMin);
		point2 = new Vector3f(level.xMin, level.yMin, level.zMax);
		point3 = new Vector3f(level.xMax, level.yMin, level.zMin);
		Plane plane = computePlane(point1, point2, point3, levelPosition);
		float scalar = linePlaneIntersection(new Vector3f(mVector.x, mVector.y, mVector.z), new Vector3f(position.x, -position.y, position.z), plane);
		return new Vector3f(mVector.x * scalar, mVector.y * scalar, mVector.z * scalar);
	}

}
