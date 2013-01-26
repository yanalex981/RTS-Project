/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description: Defines a model in terms of its polygon data and drawing data. 
 * This class usually defines a model segment and is part of a larger structure.
 */

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glMaterialf;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;


public class Mesh
{
	public List<Vector3f> verticies = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<TriangleFace> triFaces = new ArrayList<TriangleFace>();
	public List<QuadFace> quadFaces = new ArrayList<QuadFace>();
	private Vector3f Ka = new Vector3f();
	private Vector3f Kd = new Vector3f();
	private Vector3f Ks = new Vector3f();
	int list;
	private float shininess;
	String name;
	Model parent;
	
	public Mesh(String name, Model parent)
	{
		this.name = name;
		this.parent = parent;
		this.shininess = 1;
	}
	
	/**
	 * Clones the current mesh in a new object not referencing this one.
	 * 
	 * @return The clone of this mesh.
	 */
	public Mesh cloneMesh()
	{
		try
		{
			return (Mesh)(this.clone());
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
	
	/**
	 * Generates drawlists which define an object. this list is referred to every time the object is rendered.
	 */
	public void generateDrawList()
	{
		//System.out.println(name);
		list = glGenLists(1);
		//System.out.println("Tri Faces: " + triFaces.size());
		//System.out.println("Quad Faces: " + quadFaces.size());
		//System.out.println("Verticies: " + verticies.size());
		//System.out.println("Normals: " + normals.size());
		glNewList(list, GL_COMPILE);
		{
			glBegin(GL_TRIANGLES);
			{
				for (int i = 0; i < triFaces.size(); i++)
				{
					Vector3f n1 = parent.normals.get((int) (triFaces.get(i).normal.x - 1));
					glNormal3f(n1.x, n1.y, n1.z);
					Vector3f v1 = parent.verticies.get((int) (triFaces.get(i).vertex.x - 1));
					glVertex3f(v1.x, v1.y, v1.z);
					Vector3f n2 = parent.normals.get((int) (triFaces.get(i).normal.y - 1));
					glNormal3f(n2.x, n2.y, n2.z);
					Vector3f v2 = parent.verticies.get((int) (triFaces.get(i).vertex.y - 1));
					glVertex3f(v2.x, v2.y, v2.z);
					Vector3f n3 = parent.normals.get((int) (triFaces.get(i).normal.z - 1));
					glNormal3f(n3.x, n3.y, n3.z);
					Vector3f v3 = parent.verticies.get((int) (triFaces.get(i).vertex.z - 1));
					glVertex3f(v3.x, v3.y, v3.z);
				}
			}
			glEnd();

			glBegin(GL_QUADS);
			{
				for (int i = 0; i < quadFaces.size(); i++)
				{
					Vector3f n1 = parent.normals.get((int) (quadFaces.get(i).normal.x - 1));
					glNormal3f(n1.x, n1.y, n1.z);
					Vector3f v1 = parent.verticies.get((int) (quadFaces.get(i).vertex.x - 1));
					glVertex3f(v1.x, v1.y, v1.z);
					Vector3f n2 = parent.normals.get((int) (quadFaces.get(i).normal.y - 1));
					glNormal3f(n2.x, n2.y, n2.z);
					Vector3f v2 = parent.verticies.get((int) (quadFaces.get(i).vertex.y - 1));
					glVertex3f(v2.x, v2.y, v2.z);
					Vector3f n3 = parent.normals.get((int) (quadFaces.get(i).normal.z - 1));
					glNormal3f(n3.x, n3.y, n3.z);
					Vector3f v3 = parent.verticies.get((int) (quadFaces.get(i).vertex.z - 1));
					glVertex3f(v3.x, v3.y, v3.z);
					Vector3f n4 = parent.normals.get((int) (quadFaces.get(i).normal.w - 1));
					glNormal3f(n4.x, n4.y, n4.z);
					Vector3f v4 = parent.verticies.get((int) (quadFaces.get(i).vertex.w - 1));
					glVertex3f(v4.x, v4.y, v4.z);
				}
			}
			glEnd();
		}
		glEndList();
	}
	
	/**
	 * Draws the object to the screen.
	 * 
	 * @param colour: the colour of the object.
	 * @param shininess: the shininess of the object used for specular lighting.
	 */
	public void draw(Vector3f colour, float shininess)
	{
		glColor3f(colour.x, colour.y, colour.z);
		glMaterialf(GL_FRONT, GL_SHININESS, shininess + this.shininess);
		
		glCallList(list);
	}
	
	public void setKa(Vector3f Ka)
	{
		this.Ka = Ka;
	}
	
	public void setKd(Vector3f Kd)
	{
		this.Kd = Kd;
	}
	
	public void setKs(Vector3f Ks)
	{
		this.Ks = Ks;
	}
	
	public void setShininess(float shininess)
	{
		this.shininess = shininess;
	}
}
