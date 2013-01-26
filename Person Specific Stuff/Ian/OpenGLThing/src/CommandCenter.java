import org.lwjgl.util.vector.Vector3f;

public class CommandCenter extends Building
{

	public CommandCenter(Vector3f position, Player owner, boolean active)
	{
		super(position, ModelReference.commandCenter, null, "Command Center", owner, active);
	}

	public void keyOneCommand()
	{
		Worker worker = new Worker(new Vector3f(this.position.x, -9, this.position.z + this.getModel().zCollisionRadii + ModelReference.die.zCollisionRadii), 0.03f, 1, 5, this.owner, false);
		owner.masterList.add(worker);
		worker.ID = owner.masterList.currentID;
		addToBuildQue(worker);
		owner.addUnit(worker);
	}

}
