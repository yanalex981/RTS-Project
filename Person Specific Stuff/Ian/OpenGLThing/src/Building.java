
public class Building extends Unit
{
	private int queOneTime, queTwoTime, queThreeTime, queFourTime, queFiveTime;
	private Unit queOneUnit, queTwoUnit, queThreeUnit, queFourUnit, queFiveUnit;
	
	public Building(Model model, Animation animation, String name, Main m)
	{
		super(model, animation, name, m);
		
	}
	
	public void updateBuild(int delta)
	{
		
		if(queOneTime - delta<= 0)
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
			queOneTime -= delta;
			build();
		}
	}
	
	public void build()
	{
		queOneUnit.setActive(true);
	}
	
	public void addToQue(Unit unit)
	{
		if(queOneTime == 0)
		{
			queOneUnit = unit;
		}
		else if(queTwoTime == 0)
		{
			queTwoUnit = unit;
		}
		else if(queThreeTime == 0)
		{
			queThreeUnit = unit;
		}
		else if(queFourTime == 0)
		{
			queFourUnit = unit;
		}
		else if(queFiveTime == 0)
		{
			queFiveUnit = unit;
		}
		else
		{
			
		}
	}
	
	public void keyOneCommand()
	{
		Unit unit = new Unit(ModelReference.reactor, null, "Reactor", m);
		unit.translate(Math.round(this.position.x), Math.round(this.position.y), Math.round(this.position.z + this.model.zCollisionRadii + 1));
		m.unitList.add(unit);
		m.selection = unit;
	}
}
