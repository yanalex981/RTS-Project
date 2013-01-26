import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ControllableObject
{
	public static final int WAITING = 0;

	public int ID;

	protected boolean active;

	protected int animationFrame;

	protected int buildTime;

	protected Vector3f colour = new Vector3f(1, 1, 1);

	/**
	 * max health, current health, damage negated each time this unit is attacked
	 */
	protected int maxHp, hp, armour;

	protected Model model = new Model();

	protected String objName;

	protected Player owner;

	/** The current position of the Unit */
	protected Vector3f position = new Vector3f(0, 0, 0);

	protected long prevTime;

	/** The angle of the Unit, in degrees */
	protected Vector3f rotation = new Vector3f(0, 0, 0);

	protected Animation setUpAnimation = new Animation();

	protected float shininess = 10;

	/** The sight radius of the Unit, in game units */
	protected int sightRadius;

	protected int state;

	protected Vector4f velocity = new Vector4f(0, 0, 0, 1);

	public ControllableObject(Vector3f position, Model model, Animation setUpAnimation, String objName, Player owner, boolean active)
	{
		this.animationFrame = 0;
		this.model = model;
		this.setUpAnimation = setUpAnimation;
		this.state = WAITING;
		this.objName = objName;
		this.active = active;
		this.position = position;
		this.owner = owner;
	}

	public void draw(long time)
	{
		if (active)
		{
			if (time - prevTime > 45)
				prevTime = Sys.getTime();

			if (state == 10)
			{
				if (setUpAnimation != null)
				{
					setUpAnimation.getFrame(animationFrame).draw(position, rotation, colour, shininess);

					// System.out.println(time - prevTime);
					if ((int) (Math.round(time - prevTime) / (1000 / 30)) == 1 || (int) time == 0)
					{
						prevTime = time;
						if (animationFrame < setUpAnimation.getFrames() - 1)
							animationFrame++;
						else
							animationFrame = 0;
					}
				}
			}
			else
			{
				model.draw(position, rotation, colour, shininess);
			}
		}
	}

	public int getBuildTime()
	{
		return buildTime;
	}

	public Model getModel()
	{
		return model;
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

	public float getShininess()
	{
		return this.shininess;
	}

	public void keyEightCommand()
	{

	}

	public void keyFiveCommand()
	{

	}

	public void keyFourCommand()
	{

	}

	public void keyNineCommand()
	{

	}

	public void keyOneCommand()
	{

	}

	public void keySevenCommand()
	{

	}

	public void keySixCommand()
	{

	}

	public void keyThreeCommand()
	{

	}

	public void keyTwoCommand()
	{

	}

	public void rotate(float x, float y, float z)
	{
		rotation.x += x;
		rotation.y += y;
		rotation.z += z;
	}

	public void setActive(boolean status)
	{
		active = status;
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

	public void setPosition(float x, float y, float z)
	{
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public void setShininess(float shininess)
	{
		this.shininess = shininess;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public void takeDamage(int damage)
	{
		hp -= damage - armour;

		if (hp < 0)
			hp = 0;
	}

	public void translate(float x, float y, float z)
	{
		position.x += x;
		position.y += y;
		position.z += z;
	}
}
