import static org.lwjgl.opengl.GL11.*;

import java.util.List;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Model
{
	public List<Vector3f> verticies = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<TriangleFace> triFaces = new ArrayList<TriangleFace>();
	public List<QuadFace> quadFaces = new ArrayList<QuadFace>();
	public List<Vector3f> maskVerticies = new ArrayList<Vector3f>();
	public List<Vector3f> maskNormals = new ArrayList<Vector3f>();
	public List<QuadFace> maskFaces = new ArrayList<QuadFace>();
	private int shininess = 10;
	int list;

	float xCollisionRadii, yCollisionRadii, zCollisionRadii;
	float xMin = 32768, xMax = -32768, yMin = 32768, yMax = -32768, zMin = 32768, zMax = -32768;
	String name;

	public Model()
	{

	}
	
	public void setShininess(int shininess)
	{
		this.shininess = shininess;
	}

	public void initializeDisplayList()
	{
		list = glGenLists(1);
		glNewList(list, GL_COMPILE);
		{
			glBegin(GL_TRIANGLES);
			{
				// glColor3f(1.0f, 1.0f, 0.0f);
				for (int i = 0; i < triFaces.size(); i++)
				{
					// glColor3f(1.0f, 0.0f, 0.0f);
					Vector3f n1 = normals.get((int) (triFaces.get(i).normal.x - 1));
					glNormal3f(n1.x, n1.y, n1.z);
					Vector3f v1 = verticies.get((int) (triFaces.get(i).vertex.x - 1));
					glVertex3f(v1.x, v1.y, v1.z);
					// glColor3f(0.0f, 1.0f, 0.0f);
					Vector3f n2 = normals.get((int) (triFaces.get(i).normal.y - 1));
					glNormal3f(n2.x, n2.y, n2.z);
					Vector3f v2 = verticies.get((int) (triFaces.get(i).vertex.y - 1));
					glVertex3f(v2.x, v2.y, v2.z);
					// glColor3f(0.0f, 0.0f, 1.0f);
					Vector3f n3 = normals.get((int) (triFaces.get(i).normal.z - 1));
					glNormal3f(n3.x, n3.y, n3.z);
					Vector3f v3 = verticies.get((int) (triFaces.get(i).vertex.z - 1));
					glVertex3f(v3.x, v3.y, v3.z);
				}
			}
			glEnd();

			glBegin(GL_QUADS);
			{
				// glColor3f(1.0f, 0.0f, 0.0f);
				for (int i = 0; i < quadFaces.size(); i++)
				{
					// glColor3f(1.0f, 0.0f, 0.0f);
					Vector3f n1 = normals.get((int) (quadFaces.get(i).normal.x - 1));
					glNormal3f(n1.x, n1.y, n1.z);
					Vector3f v1 = verticies.get((int) (quadFaces.get(i).vertex.x - 1));
					glVertex3f(v1.x, v1.y, v1.z);
					// glColor3f(0.0f, 1.0f, 0.0f);
					Vector3f n2 = normals.get((int) (quadFaces.get(i).normal.y - 1));
					glNormal3f(n2.x, n2.y, n2.z);
					Vector3f v2 = verticies.get((int) (quadFaces.get(i).vertex.y - 1));
					glVertex3f(v2.x, v2.y, v2.z);
					// glColor3f(0.0f, 0.0f, 1.0f);
					Vector3f n3 = normals.get((int) (quadFaces.get(i).normal.z - 1));
					glNormal3f(n3.x, n3.y, n3.z);
					Vector3f v3 = verticies.get((int) (quadFaces.get(i).vertex.z - 1));
					glVertex3f(v3.x, v3.y, v3.z);
					Vector3f n4 = normals.get((int) (quadFaces.get(i).normal.w - 1));
					glNormal3f(n4.x, n4.y, n4.z);
					Vector3f v4 = verticies.get((int) (quadFaces.get(i).vertex.w - 1));
					glVertex3f(v4.x, v4.y, v4.z);
				}
			}
			glEnd();
		}
		glEndList();
	}

	public void loadCollisionMask()
	{
		// float minX = verticies.get(1).x, minY = verticies.get(1).y, minZ =
		// verticies.get(1).z, maxX = verticies.get(1).x, maxY =
		// verticies.get(1).y, maxZ = verticies.get(1).z;

		for (int i = 0; i < verticies.size(); i++)
		{
			if (verticies.get(i).x < xMin)
				xMin = verticies.get(i).x;
			else if (verticies.get(i).x > xMax)
				xMax = verticies.get(i).x;

			if (verticies.get(i).y < yMin)
				yMin = verticies.get(i).y;
			else if (verticies.get(i).y > yMax)
				yMax = verticies.get(i).y;

			if (verticies.get(i).z < zMin)
				zMin = verticies.get(i).z;
			else if (verticies.get(i).z > zMax)
				zMax = verticies.get(i).z;
		}
		//System.out.println("min:(" + xMin + ", " + yMin + ", " + zMin + ")   max:(" + xMax + ", " + yMax + ", " + zMax + ")");

		maskVerticies.add(new Vector3f(xMin, yMin, zMin));// Left-Back-Bottom
		maskVerticies.add(new Vector3f(xMin, yMax, zMin));// Left-Back-Top
		maskVerticies.add(new Vector3f(xMin, yMin, zMax));// Left-Front-Bottom
		maskVerticies.add(new Vector3f(xMin, yMax, zMax));// Left-Front-Top
		maskVerticies.add(new Vector3f(xMax, yMin, zMin));// Right-Back-Bottom
		maskVerticies.add(new Vector3f(xMax, yMax, zMin));// Right-Back-Top
		maskVerticies.add(new Vector3f(xMax, yMin, zMax));// Right-Front-Bottom
		maskVerticies.add(new Vector3f(xMax, yMax, zMax));// Right-Front-Top

		//System.out.println(maskVerticies.size());
		maskNormals.add(new Vector3f(-1, -1, -1));// Left-Back-Bottom
		maskNormals.get(0).normalise();
		maskNormals.add(new Vector3f(-1, 1, -1));// Left-Back-Top
		maskNormals.get(1).normalise();
		maskNormals.add(new Vector3f(-1, -1, 1));// Left-Front-Bottom
		maskNormals.get(2).normalise();
		maskNormals.add(new Vector3f(-1, 1, 1));// Left-Front-Top
		maskNormals.get(3).normalise();
		maskNormals.add(new Vector3f(1, -1, -1));// Right-Back-Bottom
		maskNormals.get(4).normalise();
		maskNormals.add(new Vector3f(1, 1, -1));// Right-Back-Top
		maskNormals.get(5).normalise();
		maskNormals.add(new Vector3f(1, -1, 1));// Right-Front-Bottom
		maskNormals.get(6).normalise();
		maskNormals.add(new Vector3f(1, 1, 1));// Right-Front-Top
		maskNormals.get(7).normalise();
		//System.out.println(maskNormals.get(0).x);

		maskFaces.add(new QuadFace(new Vector4f(2, 3, 1, 0), new Vector4f(0, 1, 3, 2)));
		maskFaces.add(new QuadFace(new Vector4f(4, 5, 7, 6), new Vector4f(4, 5, 7, 6)));
		maskFaces.add(new QuadFace(new Vector4f(0, 1, 5, 4), new Vector4f(0, 1, 5, 4)));
		maskFaces.add(new QuadFace(new Vector4f(6, 7, 3, 2), new Vector4f(2, 3, 7, 6)));
		maskFaces.add(new QuadFace(new Vector4f(4, 6, 2, 0), new Vector4f(0, 2, 6, 4)));
		maskFaces.add(new QuadFace(new Vector4f(1, 3, 7, 5), new Vector4f(1, 3, 7, 5)));
		//System.out.println(maskFaces.size());

		xCollisionRadii = Math.abs(xMax - xMin) / 2;
		yCollisionRadii = Math.abs(yMax - yMin) / 2;
		zCollisionRadii = Math.abs(zMax - zMin) / 2;
	}

	public void loadMaskProperties()
	{
		xMin = maskVerticies.get(0).x;
		yMin = maskVerticies.get(0).y;
		zMin = maskVerticies.get(0).z;
		xMax = maskVerticies.get(0).x;
		yMax = maskVerticies.get(0).y;
		zMax = maskVerticies.get(0).z;

		for (int i = 0; i < maskVerticies.size(); i++)
		{
			if (maskVerticies.get(i).x < xMin)
				xMin = maskVerticies.get(i).x;
			else if (maskVerticies.get(i).x > xMax)
				xMax = maskVerticies.get(i).x;

			if (maskVerticies.get(i).y < yMin)
				yMin = maskVerticies.get(i).y;
			else if (maskVerticies.get(i).y > yMax)
				yMax = maskVerticies.get(i).y;

			if (maskVerticies.get(i).z < zMin)
				zMin = maskVerticies.get(i).z;
			else if (maskVerticies.get(i).z > zMax)
				zMax = maskVerticies.get(i).z;
		}
		// System.out.println("min:(" + xMin + ", " + yMin + ", " + zMin +
		// ")   max:(" + xMax + ", " + yMax + ", " + zMax + ")");
	}

	public void draw(Vector3f position, Vector3f rotation, Vector3f colour)
	{
		glPushMatrix();
		glTranslatef(position.x, position.y, position.z);
		glRotatef(rotation.x, 1, 0, 0);
		glRotatef(rotation.y, 0, 1, 0);
		glRotatef(rotation.z, 0, 0, 1);
		glColor3f(colour.x, colour.y, colour.z);
		glMaterialf(GL_FRONT, GL_SHININESS, shininess);

		glCallList(list);
		//render();
		//renderMask();
		glPopMatrix();
	}

	private void render()
	{
		glBegin(GL_TRIANGLES);
		{
			// glColor3f(1.0f, 1.0f, 0.0f);
			for (int i = 0; i < triFaces.size(); i++)
			{
				// glColor3f(1.0f, 0.0f, 0.0f);
				Vector3f n1 = normals.get((int) (triFaces.get(i).normal.x - 1));
				glNormal3f(n1.x, n1.y, n1.z);
				Vector3f v1 = verticies.get((int) (triFaces.get(i).vertex.x - 1));
				glVertex3f(v1.x, v1.y, v1.z);
				// glColor3f(0.0f, 1.0f, 0.0f);
				Vector3f n2 = normals.get((int) (triFaces.get(i).normal.y - 1));
				glNormal3f(n2.x, n2.y, n2.z);
				Vector3f v2 = verticies.get((int) (triFaces.get(i).vertex.y - 1));
				glVertex3f(v2.x, v2.y, v2.z);
				// glColor3f(0.0f, 0.0f, 1.0f);
				Vector3f n3 = normals.get((int) (triFaces.get(i).normal.z - 1));
				glNormal3f(n3.x, n3.y, n3.z);
				Vector3f v3 = verticies.get((int) (triFaces.get(i).vertex.z - 1));
				glVertex3f(v3.x, v3.y, v3.z);
			}
		}
		glEnd();

		glBegin(GL_QUADS);
		{
			// glColor3f(1.0f, 0.0f, 0.0f);
			for (int i = 0; i < quadFaces.size(); i++)
			{
				// glColor3f(1.0f, 0.0f, 0.0f);
				Vector3f n1 = normals.get((int) (quadFaces.get(i).normal.x - 1));
				glNormal3f(n1.x, n1.y, n1.z);
				Vector3f v1 = verticies.get((int) (quadFaces.get(i).vertex.x - 1));
				glVertex3f(v1.x, v1.y, v1.z);
				// glColor3f(0.0f, 1.0f, 0.0f);
				Vector3f n2 = normals.get((int) (quadFaces.get(i).normal.y - 1));
				glNormal3f(n2.x, n2.y, n2.z);
				Vector3f v2 = verticies.get((int) (quadFaces.get(i).vertex.y - 1));
				glVertex3f(v2.x, v2.y, v2.z);
				// glColor3f(0.0f, 0.0f, 1.0f);
				Vector3f n3 = normals.get((int) (quadFaces.get(i).normal.z - 1));
				glNormal3f(n3.x, n3.y, n3.z);
				Vector3f v3 = verticies.get((int) (quadFaces.get(i).vertex.z - 1));
				glVertex3f(v3.x, v3.y, v3.z);
				Vector3f n4 = normals.get((int) (quadFaces.get(i).normal.w - 1));
				glNormal3f(n4.x, n4.y, n4.z);
				Vector3f v4 = verticies.get((int) (quadFaces.get(i).vertex.w - 1));
				glVertex3f(v4.x, v4.y, v4.z);
			}
		}
		glEnd();
	}

	private void renderMask()
	{
		glBegin(GL_QUADS);
		{
			// glColor3f(1.0f, 0.0f, 0.0f);
			for (int i = 0; i < maskFaces.size(); i++)
			{
				Vector3f n1 = maskNormals.get((int) (maskFaces.get(i).normal.x));
				glNormal3f(n1.x, n1.y, n1.z);
				Vector3f v1 = maskVerticies.get((int) (maskFaces.get(i).vertex.x));
				glVertex3f(v1.x, v1.y, v1.z);
				Vector3f n2 = maskNormals.get((int) (maskFaces.get(i).normal.y));
				glNormal3f(n2.x, n2.y, n2.z);
				Vector3f v2 = maskVerticies.get((int) (maskFaces.get(i).vertex.y));
				glVertex3f(v2.x, v2.y, v2.z);
				Vector3f n3 = maskNormals.get((int) (maskFaces.get(i).normal.z));
				glNormal3f(n3.x, n3.y, n3.z);
				Vector3f v3 = maskVerticies.get((int) (maskFaces.get(i).vertex.z));
				glVertex3f(v3.x, v3.y, v3.z);
				Vector3f n4 = maskNormals.get((int) (maskFaces.get(i).normal.w));
				glNormal3f(n4.x, n4.y, n4.z);
				Vector3f v4 = maskVerticies.get((int) (maskFaces.get(i).vertex.w));
				glVertex3f(v4.x, v4.y, v4.z);
			}
		}
		glEnd();
	}
}
