import java.awt.geom.Point2D;

import org.lwjgl.util.vector.Vector3f;

public class Building extends ControllableObject
{
	private int queOneTime, queTwoTime, queThreeTime, queFourTime, queFiveTime;
	private Unit queOneUnit, queTwoUnit, queThreeUnit, queFourUnit, queFiveUnit;

	public Building(Point2D.Float position, String name, Player owner, boolean active)
	{
		super(position, name, owner, active);
	}
	
	public void killUnit()
	{
		owner.removeBuilding(this);
	}
	
	public void update(int delta)
	{
		if (hp == 0)
			killUnit();
		
		updateBuildQue(delta);
	}

	public void updateBuildQue(int delta)
	{

		if (queOneTime - delta <= 0)
		{
			build();
			queOneUnit = queTwoUnit;
			queTwoUnit = queThreeUnit;
			queThreeUnit = queFourUnit;
			queFourUnit = queFiveUnit;
			queFiveUnit = null;

			queOneTime = queTwoTime + (queOneTime - delta);
			queThreeTime = queTwoTime;
			queFourTime = queThreeTime;
			queFiveTime = queFourTime;
			queFiveTime = 0;
		}
		queOneTime -= delta;
	}

	public void build()
	{
		if (queOneUnit != null)
		{
			queOneUnit.setActive(true);
			owner.map.addUnit(queOneUnit);
		}
	}

	public void addToBuildQue(Unit unit)
	{
		if (queOneTime <= 0)
		{
			queOneUnit = unit;
			queOneTime = unit.getBuildTime();
		}
		else if (queTwoTime == 0)
		{
			queTwoUnit = unit;
			queOneTime = unit.getBuildTime();
		}
		else if (queThreeTime == 0)
		{
			queThreeUnit = unit;
			queOneTime = unit.getBuildTime();
		}
		else if (queFourTime == 0)
		{
			queFourUnit = unit;
			queOneTime = unit.getBuildTime();
		}
		else if (queFiveTime == 0)
		{
			queFiveUnit = unit;
			queOneTime = unit.getBuildTime();
		}
	}
}
