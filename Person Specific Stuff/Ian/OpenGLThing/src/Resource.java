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
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Resource
{
	public static Model loadModel(String st) throws FileNotFoundException, IOException
	{
		Scanner s = new Scanner(new File(st));
		Model model = new Model();
		String line;

		while (s.hasNextLine())
		{
			line = s.nextLine();
			if (line.startsWith("v "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				model.verticies.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vn "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				model.normals.add(new Vector3f(x, y, z));
			} else if (line.startsWith("f "))
			{
				if (line.split(" ").length == 4)
				{
					Vector3f vertexIndicies = new Vector3f(Float.valueOf(line.split(" ")[1].split("//")[0]), Float.valueOf(line.split(" ")[2].split("//")[0]), Float.valueOf(line.split(" ")[3].split("//")[0]));
					Vector3f normalIndicies = new Vector3f(Float.valueOf(line.split(" ")[1].split("//")[1]), Float.valueOf(line.split(" ")[2].split("//")[1]), Float.valueOf(line.split(" ")[3].split("//")[1]));
					model.triFaces.add(new TriangleFace(vertexIndicies, normalIndicies));
				}
				if (line.split(" ").length == 5)
				{
					Vector4f vertexIndicies = new Vector4f(Float.valueOf(line.split(" ")[1].split("//")[0]), Float.valueOf(line.split(" ")[2].split("//")[0]), Float.valueOf(line.split(" ")[3].split("//")[0]), Float.valueOf(line.split(" ")[4].split("//")[0]));
					Vector4f normalIndicies = new Vector4f(Float.valueOf(line.split(" ")[1].split("//")[1]), Float.valueOf(line.split(" ")[2].split("//")[1]), Float.valueOf(line.split(" ")[3].split("//")[1]), Float.valueOf(line.split(" ")[4].split("//")[1]));
					model.quadFaces.add(new QuadFace(vertexIndicies, normalIndicies));
				}
			}
		}
		s.close();
		model.loadCollisionMask();
		model.initializeDisplayList();

		return model;
	}

	public static void initLightAspect(int i, int j, float a, float b, float c)
	{
		glLight(i, j, asFloatBuffer(new float[] { a, b, c, 1f }));
	}

	public static FloatBuffer asFloatBuffer(float[] values)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}

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

	public static boolean pointRectangleCollision(Vector3f point, float xMin, float xMax, float zMin, float zMax)
	{
		System.out.println("Point: " + point);
		System.out.println("Min : " + xMin + ", " + zMin + " Max: " + xMax + ", " + zMax);

		if (point.x > xMin && point.x < xMax && point.z > zMin && point.z < zMax)
			return true;

		return false;
	}

	public static Vector4f mouseVector(Vector3f rotation, Vector4f position, Matrix4f rMatrix)
	{
		Vector4f mVector = new Vector4f((float) Mouse.getX() * 2 / Display.getWidth() - 1, (float) Mouse.getY() * 2 / Display.getHeight() - 1, -1, 1);

		//v.x = (2 * Mouse.getX() / width) - 1)/(Znear/width)
		//v.y = (2 * Mouse.getY() / height) - 1)/(Znear/height)
		//v.z = 1;

		//System.out.println(mVector);

		//Matrix4f tMatrix = new Matrix4f();
		//tMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
		//tMatrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
		//tMatrix.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
		
		FloatBuffer modMatrix = BufferUtils.createFloatBuffer(16);
		FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, modMatrix);
		glGetFloat(GL_PROJECTION_MATRIX, projMatrix);

		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f projectionMatrix = new Matrix4f();
		Matrix4f temp = new Matrix4f();

		//modelMatrix.load(modMatrix);
		projectionMatrix.load(projMatrix);

		Matrix4f.mul(modelMatrix, projectionMatrix, temp);
		Matrix4f.mul(temp, rMatrix, temp);
		temp.invert();
		
		//System.out.println(temp);
		//System.out.println(mVector);
		Matrix4f.transform(temp, mVector, mVector);

		//System.out.println(mVector);
		mVector.normalise();
		//System.out.println(mVector);

		//System.out.println("mVector: (" + mVector.x + ", " + mVector.y + ", " + mVector.z + ")");
		//System.out.println("Mouse Position: (" + Mouse.getX() + ", " + Mouse.getY() + ")");
		//System.out.println("Global Position: (" + position.x + ", " + position.y + ", " + position.z + ")");

		return mVector;
	}

	public static Plane computePlane(Vector3f a, Vector3f b, Vector3f c, Vector3f planePoint)
	{
		Vector3f normal = new Vector3f();
		float d;

		Vector3f.sub(a, b, b);
		Vector3f.sub(a, c, c);
		Vector3f.cross(b, c, normal);
		normal.normalise();
		d = Vector3f.dot(normal, a);

		System.out.println("Plane Normal: " + normal);
		System.out.println("Point on Plane: " + planePoint);

		return new Plane(normal, d, planePoint);
	}

	public static float linePlaneIntersection(Vector3f ray, Vector3f point, Plane plane)
	{
		float scalar = 0;
		float nDotPoint, nDotRay;
		Vector3f v = new Vector3f(0, 0, 0);;
		Vector3f.sub(plane.getPoint(), point, v);
		scalar = (Vector3f.dot(plane.getNormal(), v) / (Vector3f.dot(plane.getNormal(), ray)));

		return scalar;
	}

	public static Unit select(ArrayList<Unit> unitList, Unit level, Vector4f mVector, Vector4f position)
	{
		for (int i = 1; i < unitList.size(); i++)
		{
			Unit unit = unitList.get(i);

			System.out.println(unit.getName());
			Vector3f point1, point2, point3;
			point1 = new Vector3f(unit.getModel().xMin, unit.getModel().yMin, unit.getModel().zMin);
			point2 = new Vector3f(unit.getModel().xMin, unit.getModel().yMin, unit.getModel().zMax);
			point3 = new Vector3f(unit.getModel().xMax, unit.getModel().yMin, unit.getModel().zMin);
			Plane plane = computePlane(point1, point2, point3, unit.getPosition());
			float scalar = linePlaneIntersection(new Vector3f(mVector.x, mVector.y, mVector.z),
												new Vector3f(position.x, position.y, position.z),plane);

			System.out.println("Scalar: " + scalar);

			if (pointRectangleCollision(new Vector3f(-position.x + mVector.x * scalar, -position.y + mVector.y * scalar, -position.z + mVector.z * scalar), unit.getModel().xMin + unit.getPosition().x, unit.getModel().xMax + unit.getPosition().x, unit.getModel().zMin + unit.getPosition().z, unit.getModel().zMax + unit.getPosition().z) && scalar > 0)
			{
				return unit;
			}
			//System.out.println(position);
			//System.out.println(mVector);
			/*for (int j = 0; j < 5000; j++)
			{
				Vector3f point = new Vector3f((float) (-position.x + mVector.x * j * .1), (float) (-position.y + mVector.y * j * .1), (float) (-position.z + mVector.z * j * .1));
				//if (axisAlignedBoundingBoxCollision(unitList.get(i).getModel().xCollisionRadii, unitList.get(i).getModel().yCollisionRadii, unitList.get(i).getModel().zCollisionRadii, new Vector3f(point.x - unitList.get(i).getPosition().x, point.y - unitList.get(i).getPosition().y, point.z - unitList.get(i).getPosition().z)))
				if(axisAlignedBoundingBoxCollisionAltModel(point, unitList.get(i)))
				{
					return unitList.get(i);
				}
			}*/
		}
		
		return null;
	}
	
	public static Vector3f selectLevelPlane(Unit level, Vector4f mVector, Vector4f position)
	{
		System.out.println(level.getName());
		Vector3f point1, point2, point3;
		point1 = new Vector3f(level.getModel().xMin, level.getModel().yMin, level.getModel().zMin);
		point2 = new Vector3f(level.getModel().xMin, level.getModel().yMin, level.getModel().zMax);
		point3 = new Vector3f(level.getModel().xMax, level.getModel().yMin, level.getModel().zMin);
		Plane plane = computePlane(point1, point2, point3, level.getPosition());
		float scalar = linePlaneIntersection(new Vector3f(mVector.x, mVector.y, mVector.z),
											new Vector3f(position.x, position.y, position.z),plane);
		
		return new Vector3f(mVector.x * scalar, mVector.y * scalar, mVector.z * scalar);
	}

	/*
	 * public static float xzAngle(Model floor) { float angle; float magnitude = (float)(Math.sqrt(Math.pow(floor.x) + Math.pow(floor.y) + Math.pow(floor.z));
	 * 
	 * return angle; }
	 */
}
