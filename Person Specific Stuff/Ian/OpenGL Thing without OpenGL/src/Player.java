import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Player
{
	NetworkLoop nLoop;
	public UnitList masterList;
	public DatagramSocket socket;
	//private ArrayList<DatagramPacket> packets; 
	public DatagramPacket packet;
	private ArrayList<Unit> unitList;
	private ArrayList<Building> buildingList;
	private ArrayList<ControllableObject> selectionList;
	private Vector3f colour;
	private String playerName;
	public Map map;
	public byte[] data;

	public ArrayList[] controlGroup = new ArrayList[10];

	public Player(String playerName, Vector3f colour, Map map, Vector3f levelPosition)
	{
		nLoop = new NetworkLoop(this);
		nLoop.setPriority(Thread.MIN_PRIORITY);
		nLoop.start();
		try
		{
			this.socket = new DatagramSocket(666);
		} catch (SocketException e)
		{
			e.printStackTrace();
		}
		this.data = new byte[200];
		this.masterList = new UnitList();
		this.playerName = playerName;
		this.colour = colour;
		this.selectionList = new ArrayList<ControllableObject>();
		unitList = new ArrayList<Unit>();
		buildingList = new ArrayList<Building>();
		this.map = map;

		//init control groups
		for (int i = 0; i < controlGroup.length; i++)
			controlGroup[i] = new ArrayList<ControllableObject>(0);
	}

	public void setControlGroup(ArrayList<ControllableObject> units, int groupNumber)
	{
		controlGroup[groupNumber].clear();
		controlGroup[groupNumber].addAll(units);
	}

	public void addToControlGroup(ArrayList<ControllableObject> units, int groupNumber)
	{
		for (ControllableObject u : units)
			if (!controlGroup[groupNumber].contains(u))
				controlGroup[groupNumber].add(u);
	}

	public ArrayList<ControllableObject> getControlGroup(int groupNumber)
	{
		return controlGroup[groupNumber];
	}

	public void addUnit(Unit unit)
	{
		unitList.add(unit);
	}

	public void removeUnit(Unit unit)
	{
		unitList.remove(unit);
	}

	public Unit getUnit(int index)
	{
		return unitList.get(index);
	}

	public ArrayList<Unit> getUnitList()
	{
		return unitList;
	}

	public int getUnitListSize()
	{
		return unitList.size();
	}

	public void addBuilding(Building building)
	{
		buildingList.add(building);
	}

	public void removeBuilding(Building building)
	{
		buildingList.remove(building);
	}

	public Building getBuilding(int index)
	{
		return buildingList.get(index);
	}

	public ArrayList<Building> getBuildingList()
	{
		return this.buildingList;
	}

	public int getBuildingListSize()
	{
		return buildingList.size();
	}

	public void update(int delta, long time)
	{
		updateNetworking();

		for (int i = 0; i < buildingList.size(); i++)
		{
			buildingList.get(i).update(delta);
		}
		for (int i = 0; i < unitList.size(); i++)
		{
			if (unitList.get(i).getClass() == Worker.class)
				((Worker) unitList.get(i)).updateBuild(delta);
		}

		map.update(delta);
	}

	public void updateNetworking()
	{
		int offset = 0, instruction = -1;
		byte[] bytes = new byte[200];
		byte[] data = new byte[200];
		packet = new DatagramPacket(bytes, 200);
		data = packet.getData();
		offset++;
		
		try
		{
			instruction = DataInterpretor.getInstruction(data);
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

		if (instruction == DataFactory.PACKET_CONNECT)
		{

		}
		else if (instruction == DataFactory.PACKET_DISCONNECT)
		{

		}
		else if (instruction == DataFactory.PACKET_WIN)
		{

		}
		else if (instruction == DataFactory.PACKET_LOSE)
		{

		}
		else if (instruction == DataFactory.PACKET_MOVE)
		{

		}
		else if (instruction == DataFactory.PACKET_UPDATE_XY)
		{
			
		}
		else if (instruction == DataFactory.PACKET_ATTACK)
		{

		}
		else if (instruction == DataFactory.PACKET_UPDATE_HP)
		{

		}
		else if (instruction == DataFactory.PACKET_BUILD)
		{

		}
		else if (instruction == DataFactory.PACKET_ADD_UNIT)
		{

		}
		else if (instruction == DataFactory.PACKET_REMOVE_UNIT)
		{
			int unitID = 0;
			try
			{
				unitID = DataInterpretor.getIntData(data, offset);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			masterList.remove(unitID);
			bigloop:
			{
				for (int i = 0; i < unitList.size(); i++)
				{
					if (unitID == unitList.get(i).ID)
					{
						unitList.remove(i);
						break bigloop;
					}
				}
				for (int i = 0; i < buildingList.size(); i++)
				{
					if (unitID == buildingList.get(i).ID)
					{
						buildingList.remove(i);
						break bigloop;

					}
				}
			}
			for (int i = 0; i < selectionList.size(); i++)
			{
				if (selectionList.get(i).ID == unitID)
				{
					selectionList.remove(i);
				}
			}

		}
	}

	/*
	 * public void updateSelection(int delta) { if (Mouse.isButtonDown(0) && UI.getTotalDelta() > UI.getWaitDelta()) { if (selection != null) { selection.setShininess(10); } //System.out.println(cameraRotation); Vector4f mVector = Resource.rayPick(rMatrix); this.mVectorPosition = this.cameraPosition; selection = Resource.select(unitList, mVector, cameraPosition); this.mVector = mVector; if (selection != null) { System.out.println("We've got something!!!"); System.out.println(selection.getName());
	 * selection.setShininess(1); } UI.setTotalDelta(0); } if (Mouse.isButtonDown(1) && UI.getTotalDelta() > UI.getWaitDelta()) { Vector4f mVector = Resource.rayPick(rMatrix); cursorPosition = Resource.selectLevelPlane(unitList.get(0), mVector, cameraPosition); Vector3f.sub(cursorPosition, new Vector3f(cameraPosition.x, cameraPosition.y, cameraPosition.z), cursorPosition); //cursorPosition = new Vector3f(Math.round(cursorPosition.z), Math.round(cursorPosition.y), Math.round(cursorPosition.z));
	 * if(selection.owner.getCursorPosition().x >= 0 && selection.owner.getCursorPosition().x >= 0) { selection.setPath((selection.owner.map.findPath(selection.owner.map.getNode(Math.round(selection.position.x), Math.round(selection.position.z)), selection.owner.map.getNode(Math.round(selection.owner.getCursorPosition().x), Math.round(selection.owner.getCursorPosition().z)), selection.getCollisionRadius()))); //this.nextPosition(); } UI.setTotalDelta(0); } }
	 */
}
