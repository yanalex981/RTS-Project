import java.text.DecimalFormat;


public class ModelReference 
{
	static Model rocketTank;
	static Animation rocketTankAnimation = new Animation();
	static Model drake;
	static Model commandCenter;
	static Model level;
	static Model barracks;
	static Model die;
	static Model reactor;
	
	public static void loadModels()
	{
		DecimalFormat format = new DecimalFormat("000000");
		
		try
		{
			drake = Resource.loadModel("Drake Mirror.obj");
			drake.loadCollisionMask();
			rocketTank = Resource.loadModel("Vehicle.obj");
			for(int i = 1; i <= 41; i++)
			{
				rocketTankAnimation.addFrame(Resource.loadModel("Vehicle Animation\\Vehicle_" + format.format(i) + ".obj"));
			}
			commandCenter = Resource.loadModel("Base.obj");
			level = Resource.loadModel("Level.obj");
			barracks = Resource.loadModel("Barracks.obj");
			die = Resource.loadModel("Die.obj");
			reactor = Resource.loadModel("Reactor.obj");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}
