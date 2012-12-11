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
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Resource 
{
	public static Model loadModel(String st, boolean loadBoundingBox) throws FileNotFoundException, IOException
	{
		Scanner s = new Scanner(new File(st));
		Model m = new Model();
		String line;
		
		while(s.hasNextLine())
		{
			line = s.nextLine();
			if(line.startsWith("v "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				m.verticies.add(new Vector4f(x, y, z, 1));
			}
			else if(line.startsWith("vn "))
			{
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				m.normals.add(new Vector4f(x, y, z, 1));
			}
			else if(line.startsWith("f "))
			{
				if(line.split(" ").length == 4)
				{
					Vector3f vertexIndicies = new Vector3f(Float.valueOf(line.split(" ")[1].split("//")[0]), Float.valueOf(line.split(" ")[2].split("//")[0]), Float.valueOf(line.split(" ")[3].split("//")[0]));
					Vector3f normalIndicies = new Vector3f(Float.valueOf(line.split(" ")[1].split("//")[1]), Float.valueOf(line.split(" ")[2].split("//")[1]), Float.valueOf(line.split(" ")[3].split("//")[1]));
					m.triFaces.add(new TriangleFace(vertexIndicies, normalIndicies));
				}
				if(line.split(" ").length == 5)
				{
					Vector4f vertexIndicies = new Vector4f(Float.valueOf(line.split(" ")[1].split("//")[0]), Float.valueOf(line.split(" ")[2].split("//")[0]), Float.valueOf(line.split(" ")[3].split("//")[0]), Float.valueOf(line.split(" ")[4].split("//")[0]));
					Vector4f normalIndicies = new Vector4f(Float.valueOf(line.split(" ")[1].split("//")[1]), Float.valueOf(line.split(" ")[2].split("//")[1]), Float.valueOf(line.split(" ")[3].split("//")[1]), Float.valueOf(line.split(" ")[4].split("//")[1]));
					m.quadFaces.add(new QuadFace(vertexIndicies, normalIndicies));
				}
			}
		}
		s.close();
		
		return m;
	}
	
	public static void initLightAspect(int i, int j, float a, float b, float c) 
	{   
        glLight(i, j, asFloatBuffer(new float[]{a, b, c, 1f}));   
	}
	
	public static FloatBuffer asFloatBuffer(float[] values)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}
	
	public static boolean axisAlignedBoundingBoxCollision(Unit box, Vector3f point)
	{
		//System.out.println("Box: Min(" + (box.position.x + box.xMin) + ", " + (box.position.y + box.yMin) + ", " + (box.position.z + box.zMin) + ") Max(" + (box.position.x + box.xMax) + ", "+ (box.position.y + box.yMax) + ", " + (box.position.z + box.zMax) + ")");
		//System.out.println("Point: (" + point.x + ", " + point.y + ", " + point.z + ")");
		
		Vector4f bPos = box.getPosition();
		
		if(point.x > box.getModel().xMin + box.getPosition().x && point.x < box.getModel().xMax + box.getPosition().x && point.y > box.getModel().yMin + box.getPosition().y && point.y < box.getModel().yMax + box.getPosition().y && point.z > box.getModel().zMin + box.getPosition().z && point.z < box.getModel().zMax + box.getPosition().z)
			return true;
		
		return false;
	}
	
	public static boolean planeCollision(Unit thing, Unit floor)
	{
		//System.out.println("Thing:(" + thing.position.x + ", " + thing.position.y + ", " + thing.position.z + ")");
		//System.out.println("Floor: Min(" + (floor.position.x + floor.xMin) + ", "+ (floor.position.z + floor.zMin) + ") Max(" + (floor.position.x + floor.xMax) + ", " + (floor.position.z + floor.zMax) + ")");
		
		Vector4f tPos = thing.getPosition();
		Vector4f fPos = floor.getPosition();
		
		//if(tPos.x > fPos.xMin + floor.position.x && thing.position.x < floor.xMax + floor.position.x && thing.position.z > floor.zMin + floor.position.z && thing.position.z < floor.zMax + floor.position.z)
			//return true;
	
		return false;
	}
	
	public static Vector3f mouseVector(Vector3f rotation)
	{
		Vector3f mVector = new Vector3f(1, 1, 1);
		
		mVector.x = (float)(Mouse.getX() - Display.getWidth()/2);
		mVector.y = (float)(Mouse.getY() - Display.getHeight()/2);
		mVector.z = (float)(-(Math.sqrt(Math.pow(Display.getHeight(), 2) + Math.pow(Display.getWidth(), 2))));
		float magnitude = (float)(Math.sqrt(Math.pow(mVector.x, 2) + Math.pow(mVector.y, 2) + Math.pow(mVector.z, 2)));
		mVector = new Vector3f(mVector.x/magnitude, mVector.y/magnitude, mVector.z/magnitude);

		/*
		rotationMatrix.m00 = (float)(Math.cos(rotation.y) * Math.cos(rotation.x));
		rotationMatrix.m01 = (float)(-Math.sin(rotation.y));
		rotationMatrix.m02 = (float)(Math.sin(rotation.x));
		rotationMatrix.m10 = (float)(Math.sin(rotation.x));
		rotationMatrix.m11 = (float)(Math.cos(rotation.y) * Math.cos(rotation.y));
		rotationMatrix.m12 = (float)(-Math.sin(rotation.y));
		rotationMatrix.m20 = (float)(-Math.sin(rotation.x));
		rotationMatrix.m21 = (float)(-Math);
		rotationmatrix.m22 = (float)();
		*/
		
		mVector.x = (float)(Math.cos(Math.toRadians(rotation.y)) * mVector.x);
		mVector.y = (float)(Math.cos(Math.toRadians(rotation.x)) * mVector.y);
		mVector.z = (float)(Math.cos(Math.toRadians(rotation.y)) * mVector.z);
		
		//System.out.println("mVector: (" + mVector.x + ", " + mVector.y + ", " + mVector.z + ")");
		
		return mVector;
	}
	
	public static Unit select(ArrayList<Unit> unitList, Vector3f mVector, Vector3f globalPosition)
	{
		for(int i = 0; i < unitList.size() - 1; i++)
		{
			for(int j = 1; j < 50; j++)
			{
				Vector3f temp = new Vector3f(globalPosition.x + mVector.x * j, globalPosition.y + mVector.y * j, globalPosition.z + mVector.z * j);
				if(axisAlignedBoundingBoxCollision(unitList.get(i), temp))
				{
					return unitList.get(i);
				}
			}
		}
		return null;
	}
	
	/*public static float xzAngle(Model floor)
	{
		float angle;
		float magnitude = (float)(Math.sqrt(Math.pow(floor.x) + Math.pow(floor.y) + Math.pow(floor.z));
		
		return angle;
	}*/
}
