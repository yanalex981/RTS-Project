import org.lwjgl.util.vector.Vector3f;


public class Marine extends Unit
{

	public Marine(Vector3f position, float speed, float collisionRadius, int sightRadius, Player owner, boolean active)
	{
		super(position, speed, collisionRadius, sightRadius, ModelReference.reactor, null, "Marine", owner, active);

		this.buildTime = 1500;
		this.hp = 100;
	}

}
