import org.lwjgl.util.vector.Vector3f;


public class Barracks extends Building
{

	public Barracks(Vector3f position, Player owner, boolean active)
	{
		super(position, ModelReference.barracks, null, "Barracks", owner, active);
	}

	public void keyOneCommand()
	{
		Marine marine = new Marine(new Vector3f(this.position.x, -10, this.position.z + this.getModel().zCollisionRadii + ModelReference.reactor.zCollisionRadii), 0.05f, 1, 5, this.owner, false);
		owner.masterList.add(marine);
		marine.ID = owner.masterList.currentID;
		addToBuildQue(marine);
		owner.addUnit(marine);
	}
}
