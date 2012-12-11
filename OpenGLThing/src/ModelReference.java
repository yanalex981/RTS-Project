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
	
	public static void loadModels()
	{
		DecimalFormat format = new DecimalFormat("000000");
		
		try
		{
			drake = Resource.loadModel("Drake Mirror.obj", false);
			drake.loadCollisionMask();
			rocketTank = Resource.loadModel("Vehicle.obj", false);
			for(int i = 1; i <= 41; i++)
			{
				rocketTankAnimation.addFrame(Resource.loadModel("Vehicle Animation\\Vehicle_" + format.format(i) + ".obj", false));
			}
			commandCenter = Resource.loadModel("Base.obj", false);
			level = Resource.loadModel("Level.obj", false);
			barracks = Resource.loadModel("Barracks.obj", false);
			die = Resource.loadModel("Die.obj", false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}
