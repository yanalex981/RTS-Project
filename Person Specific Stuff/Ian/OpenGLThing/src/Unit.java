import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Unit
{
	public static int WAIT = 0;
	public static int SETUP = 1;
	private int state;
	private Vector4f velocity = new Vector4f(0, 0, 0, 1);
	private Vector3f rotation = new Vector3f(0, 0, 0);
	private Vector3f position = new Vector3f(0, 0, 0);
	private Vector3f colour = new Vector3f(1, 1, 1);
	private Model model = new Model();
	private Animation setUpAnimation = new Animation();
	private int animationFrame;
	private long prevTime;
	private String objName;
	private Main m;

	public Unit(Model model, Animation setUpAnimation, Main m)
	{
		this.animationFrame = 0;
		this.model = model;
		this.setUpAnimation = setUpAnimation;
		this.state = WAIT;
		this.m = m;
	}

	public void draw(long time)
	{
		if (time - prevTime > 45)
			prevTime = Sys.getTime();
			
		if (state == SETUP)
		{
			if (setUpAnimation != null)
			{
				setUpAnimation.getFrame(animationFrame).draw(position, rotation, colour);
				
				//System.out.println(time - prevTime);
				if ((int)(Math.round(time - prevTime) / (1000 / 30)) == 1 || (int)time == 0)
				{
					prevTime = time;
					if (animationFrame < setUpAnimation.getFrames() - 1)
						animationFrame++;
					else
						animationFrame = 0;
				}
			} else
			{
				state = WAIT;
			}
		} else if (state == WAIT)
		{
			model.draw(position, rotation, colour);
		}
	}

	public void translate(float x, float y, float z)
	{
		position.x += x;
		position.y += y;
		position.z += z;
	}

	public void rotate(float x, float y, float z)
	{
		rotation.x += x;
		rotation.y += y;
		rotation.z += z;
	}

	public void setPosition(float x, float y, float z)
	{
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public void setColour(float r, float g, float b)
	{
		colour.x = r;
		colour.y = g;
		colour.z = b;
	}
	
	public void setName(String objName)
	{
		this.objName = objName;
	}
	
	public String getName()
	{
		return this.objName;
	}

	public Vector3f getPosition()
	{
		return position;
	}

	public Vector3f getRotation()
	{
		return rotation;
	}

	public Model getModel()
	{
		return model;
	}
	
	public Main getM()
	{
		return m;
	}

	public void setState(int state)
	{
		this.state = state;
	}
	
	public void keyOneCommand()
	{
		Unit unit = new Unit(ModelReference.reactor, null, m);
		unit.translate(Math.round(this.position.x), Math.round(this.position.y), Math.round(this.position.z + this.model.zCollisionRadii + 1));
		m.unitList.add(unit);
		m.selection = unit;
	}
	
	public void keyTwoCommand()
	{
		setPosition(m.cursorPosition.x, m.cursorPosition.y, m.cursorPosition.z);
	}

	public void update()
	{
		position.x += velocity.x;
		position.y += velocity.y;
		position.z += velocity.z;
		// loadMaskProperties();
	}
}
