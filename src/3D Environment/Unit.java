import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Unit {
	
	public static int WAIT = 0;
	public static int SETUP = 1;
	private int state;
	private Vector4f velocity = new Vector4f(0, 0, 0, 1);
	private Vector4f rotation = new Vector4f(0, 0, 0, 1);
	private Vector4f position = new Vector4f(0, 0, 0, 1);
	private Vector3f colour = new Vector3f(1, 1, 1);
	private Model model = new Model();
	private Animation setUpAnimation = new Animation();
	private int animationFrame;
	
	public Unit(Model model, Animation setUpAnimation)
	{
		this.animationFrame = 0;
		this.model = model;
		this.setUpAnimation = setUpAnimation;
		this.state = WAIT;
	}
	public void draw()
	{
		if(state == SETUP)
		{
			if(setUpAnimation != null)
			{
				setUpAnimation.getFrame(animationFrame).draw(position, rotation, colour);
		
				if(animationFrame < setUpAnimation.getFrames() - 1)
					animationFrame++;
				else
					animationFrame = 0;
			}
			else
			{
				state = WAIT;
			}
		}
		else if(state == WAIT)
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
	
	public Vector4f getPosition()
	{
		return position;
	}
	
	public Vector4f getRotation()
	{
		return rotation;
	}
	
	public Model getModel()
	{
		return model;
	}
	
	public void setState(int state)
	{
		this.state = state;
	}
	
	public void update()
	{
		position.x += velocity.x;
		position.y += velocity.y;
		position.z += velocity.z;
		//loadMaskProperties();
	}
}
