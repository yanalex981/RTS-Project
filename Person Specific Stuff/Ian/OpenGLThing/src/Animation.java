import java.util.ArrayList;

public class Animation
{
	public final int FPS = 30;
	private int frames;
	private ArrayList<Model> models = new ArrayList<Model>();

	public Animation()
	{
		frames = 0;
	}

	public void addFrame(Model model)
	{
		models.add(model);
		frames += 1;
	}

	public Model getFrame(int index)
	{
		return models.get(index);
	}

	public int getFrames()
	{
		return frames;
	}
}
