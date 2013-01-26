import org.lwjgl.util.vector.Vector3f;

public class Worker extends Unit
{
	int buildTime;
	Building buildObject;
	boolean building;

	public Worker(Vector3f position, float speed, float collisionRadius, int sightRadius, Player owner, boolean active)
	{
		super(position, speed, collisionRadius, sightRadius, ModelReference.die, null, "Worker", owner, active);

		building = false;
		this.hp = 50;
	}

	public void build(Building buildObject)
	{
		this.buildObject = buildObject;
		building = true;
		buildTime = 1000;
	}

	public void updateBuild(int delta)
	{
		buildTime -= delta;
		if (buildTime <= 0 && building)
		{
			buildObject.setActive(true);
			building = false;
		}
	}

	public void keyOneCommand()
	{
		build(new Barracks(new Vector3f(this.position.x, -10, this.position.z + ModelReference.barracks.zCollisionRadii + this.getModel().zCollisionRadii), this.owner, false));
		owner.masterList.add(buildObject);
		buildObject.ID = owner.masterList.currentID;
		owner.addBuilding(buildObject);
	}

	public void keyTwoCommand()
	{
		build(new CommandCenter(new Vector3f(this.position.x, -10, this.position.z + ModelReference.commandCenter.zCollisionRadii + this.getModel().zCollisionRadii), this.owner, false));
		owner.addBuilding(buildObject);
	}
}
