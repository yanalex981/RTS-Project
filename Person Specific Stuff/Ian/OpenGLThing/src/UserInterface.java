/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description: executes responses to user input.
 */

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class UserInterface
{
	private boolean keyUP, keyDOWN, keyLEFT, keyRIGHT, keySPACE, keyLSHIFT, keyLCTRL;
	private boolean keyW, keyA, keyS, keyD;
	private boolean keyEsc, keyF2;
	private boolean[] key = new boolean[10];
	private int totalDelta, waitDelta = 100;

	public UserInterface()
	{
	}

	/**
	 * Input Check for hotkeys.
	 * 
	 * @param selection : The unit that the user has selected.
	 * @param delta : The change in time.
	 */
	public void hotKeyUpdate(ArrayList<ControllableObject> selectionList, int delta)
	{
		key[1] = Keyboard.isKeyDown(Keyboard.KEY_1);
		key[2] = Keyboard.isKeyDown(Keyboard.KEY_2);
		key[3] = Keyboard.isKeyDown(Keyboard.KEY_3);
		key[4] = Keyboard.isKeyDown(Keyboard.KEY_4);
		key[5] = Keyboard.isKeyDown(Keyboard.KEY_5);
		key[6] = Keyboard.isKeyDown(Keyboard.KEY_6);
		key[7] = Keyboard.isKeyDown(Keyboard.KEY_7);
		key[8] = Keyboard.isKeyDown(Keyboard.KEY_8);
		key[9] = Keyboard.isKeyDown(Keyboard.KEY_9);
		key[0] = Keyboard.isKeyDown(Keyboard.KEY_0);

		if (totalDelta > waitDelta)
			buildKeyUpdate(selectionList);
	}

	/**
	 * Update for keys dedicated to building.
	 * 
	 * @param selection : The unit that the player has selected.
	 */
	public void buildKeyUpdate(ArrayList<ControllableObject> selectionList)
	{
		for (int i = 0; i < selectionList.size(); i++)
		{
			if (key[1])
			{
				selectionList.get(i).keyOneCommand();
				totalDelta = 0;
			}
			if (key[2])
			{
				selectionList.get(i).keyTwoCommand();
				totalDelta = 0;
			}
		}
	}

	public void updateSelection(int delta, ArrayList<ControllableObject> selectionList, Player owner, Matrix4f rMatrix, Vector4f cameraPosition)
	{
		keyLSHIFT = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		keyLCTRL = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);

		if (getTotalDelta() > getWaitDelta())
		{
			for (int i = 0; i < selectionList.size(); i++)
			{
				selectionList.get(i).setShininess(10);
			}

			if (Mouse.isButtonDown(0))
			{
				Vector4f mVector = Resource.rayPick(rMatrix);
				owner.setMVectorPosition(cameraPosition);
				ControllableObject selection = Resource.select((ArrayList) owner.getUnitList(), mVector, cameraPosition);
				if (selection == null)
				{
					selection = Resource.select((ArrayList) owner.getBuildingList(), mVector, cameraPosition);
				}
				owner.setMVector(mVector);

				// unit selection code
				if (selection != null)
				{
					if (selection.getClass() == Building.class || !keyLSHIFT)
					{
						selectionList.clear();
					}
					selectionList.add(selection);
				}
				else
				{
					selectionList.clear();
				}

				//setTotalDelta(0);
			}

			// control group code
			for (int i = 0; i < key.length; i++)
			{
				if (key[i])
				{
					if (keyLCTRL)
					{
						owner.setControlGroup(selectionList, i);
						System.out.println("set group #" + i);
					}
					else if (keyLSHIFT)
					{
						owner.addToControlGroup(selectionList, i);
						System.out.println("added to group #" + i);
					}
					else
					{
						selectionList.clear();
						selectionList.addAll(owner.getControlGroup(i));
						System.out.println("retreived group #" + i);
					}

					break;
				}
			}

			if (selectionList != null)
			{
				for (int i = 0; i < selectionList.size(); i++)
					selectionList.get(i).setShininess(1);
			}
		}
	}

	public void updateUnitMovement(ArrayList<ControllableObject> selectionList, Player owner, Vector4f cameraPosition, Vector3f cursorPosition, Matrix4f rMatrix)
	{
		if (Mouse.isButtonDown(1) && getTotalDelta() > getWaitDelta())
		{
			Vector4f mVector = Resource.rayPick(rMatrix);
			cursorPosition = Resource.selectLevelPlane(owner.level, mVector, cameraPosition, owner.levelPosition);
			System.out.println("Cursor Position: " + cursorPosition);
			System.out.println("CameraPosition: " + cameraPosition);
			Vector3f.sub(cursorPosition, new Vector3f(cameraPosition.x, cameraPosition.y, cameraPosition.z), cursorPosition);
			System.out.println("Cursor Position: " + cursorPosition);
			// cursorPosition = new Vector3f(Math.round(cursorPosition.z),
			// Math.round(cursorPosition.y), Math.round(cursorPosition.z));
			if (owner.getCursorPosition().x >= 0 && owner.getCursorPosition().x >= 0)
			{
				for (int i = 0; i < selectionList.size(); i++)
				{
					((Unit) selectionList.get(i)).setPath((owner.map.findPath(selectionList.get(i).getPosition().x, selectionList.get(i).getPosition().z, cursorPosition.x, cursorPosition.z, ((Unit) selectionList.get(i)).getCollisionRadius())));
				}
			}
			setTotalDelta(0);
		}
	}

	/**
	 * Updates input for movement keys.
	 * 
	 * @param tMatrix : The translation matrix.
	 * @param rMatrix : The rotation matrix.
	 * @param rotation : The rotation values specified for each axis.
	 * @param delta : The change in time.
	 */
	public void movementUpdate(Matrix4f tMatrix, Matrix4f rMatrix, Vector3f rotation, float delta)
	{
		keyW = Keyboard.isKeyDown(Keyboard.KEY_W);
		keyS = Keyboard.isKeyDown(Keyboard.KEY_S);
		keyA = Keyboard.isKeyDown(Keyboard.KEY_A);
		keyD = Keyboard.isKeyDown(Keyboard.KEY_D);
		keyUP = Keyboard.isKeyDown(Keyboard.KEY_UP);
		keyDOWN = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		keyLEFT = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		keyRIGHT = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		keySPACE = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		keyLSHIFT = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		keyEsc = Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);

		if (totalDelta > waitDelta)
			keyboardMovementUpdate(tMatrix, rotation, delta);

		mouseMovementUpdate(rMatrix, tMatrix, rotation);
	}

	/**
	 * Updates keyboard based movement.
	 * 
	 * @param tMatrix : The translation Matrix.
	 * @param rotation : The axis specific rotation values.
	 * @param delta : The change in time.
	 */
	private void keyboardMovementUpdate(Matrix4f tMatrix, Vector3f rotation, float delta)
	{
		// Vector4f tVector = new Vector4f(0, 0, 0, 1);
		// Matrix4f rMatrix = new Matrix4f();

		if (keyA)
		{
			tMatrix.translate(new Vector3f((float) (delta * 0.01 * Math.sin(Math.toRadians(rotation.y + 90))), 0f, (float) (delta * -0.01 * Math.cos(Math.toRadians(rotation.y + 90)))));
		}
		if (keyD)
		{
			tMatrix.translate(new Vector3f((float) (delta * -0.01 * Math.sin(Math.toRadians(rotation.y + 90))), 0f, (float) (delta * 0.01 * Math.cos(Math.toRadians(rotation.y + 90)))));
		}
		if (keyW)
		{
			tMatrix.translate(new Vector3f((float) (delta * -0.01 * Math.sin(Math.toRadians(rotation.y))), (float) (delta * 0.01 * Math.sin(Math.toRadians(rotation.x))), (float) (delta * 0.01 * Math.cos(Math.toRadians(rotation.y)))));
		}
		if (keyS)
		{
			tMatrix.translate(new Vector3f((float) (delta * 0.01 * Math.sin(Math.toRadians(rotation.y))), (float) (delta * -0.01 * Math.sin(Math.toRadians(rotation.x))), (float) (delta * -0.01 * Math.cos(Math.toRadians(rotation.y)))));
		}
		if (keyLSHIFT)
		{
			// tMatrix.translate(new Vector3f(0, 0.01f * delta, 0));
		}
		if (keySPACE)
		{
			tMatrix.translate(new Vector3f(0, -0.01f * delta, 0));
		}
		if ((keyLEFT || (Mouse.getX() < 50 && !Mouse.isGrabbed())))
		{
			tMatrix.translate(new Vector3f((float) (delta * 0.01 * 0.707), 0, (float) (delta * 0.01 * 0.707)));
		}
		if ((keyRIGHT || (Mouse.getX() > Display.getWidth() - 50 && !Mouse.isGrabbed())))
		{
			tMatrix.translate(new Vector3f((float) (delta * -0.01 * 0.707), 0, (float) (delta * -0.01 * 0.707)));
		}
		if ((keyDOWN || (Mouse.getY() < 50 && !Mouse.isGrabbed())))
		{
			tMatrix.translate(new Vector3f((float) (delta * 0.01 * 0.707), 0, (float) (delta * -0.01 * 0.707)));
		}
		if ((keyUP || (Mouse.getY() > Display.getHeight() - 50 && !Mouse.isGrabbed())))
		{
			tMatrix.translate(new Vector3f((float) (delta * -0.01 * 0.707), 0, (float) (delta * 0.01 * 0.707)));
		}
		if (keyEsc)
		{
			System.exit(0);
		}
	}

	/**
	 * Updates mouse based movement updates.
	 * 
	 * @param rMatrix : The rotation matrix.
	 * @param tMatrix : The translation matrix.
	 * @param rotation : THe axis specific rotation angles.
	 */
	private void mouseMovementUpdate(Matrix4f rMatrix, Matrix4f tMatrix, Vector3f rotation)
	{
		// System.out.println(rotation);
		// System.out.println(rMatrix);
		if (totalDelta > waitDelta * 2)
		{
			if (Mouse.isButtonDown(2) && !Mouse.isGrabbed())
			{
				System.out.println("Mouse Down");
				Mouse.setGrabbed(true);
				totalDelta = 0;
			}
			else if (Mouse.isButtonDown(2) && Mouse.isGrabbed())
			{
				System.out.println("Mouse Up");
				Mouse.setGrabbed(false);
				totalDelta = 0;
			}
		}

		if (Mouse.isGrabbed())
		{
			float mouseDY = Mouse.getDY() * 0.32f;
			float mouseDX = Mouse.getDX() * 0.32f;

			if (rotation.y + mouseDX >= 360)
				rotation.y = rotation.y + mouseDX - 360;
			else if (rotation.y + mouseDX < 0)
				rotation.y = 360 + rotation.y + mouseDX;
			else
			{
				rotation.y += mouseDX;
			}
			// rMatrix.rotate((float)Math.toRadians(-mouseDX), new Vector3f(0,
			// 1, 0));

			if (rotation.x - mouseDY > 85)
				rotation.x = 85;
			else if (rotation.x - mouseDY < -85)
				rotation.x = -85;
			else
			{
				rotation.x -= mouseDY;
			}
			// rMatrix.rotate((float)Math.toRadians(mouseDY), new Vector3f(1, 0,
			// 0));
		}

		rMatrix.setIdentity();
		rMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
		rMatrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));

		int mouseDWheel = Mouse.getDWheel();
		if (mouseDWheel != 0)
		{
			System.out.println(Mouse.getDWheel());
			tMatrix.translate(new Vector3f((float) (mouseDWheel * -0.01 * Math.sin(Math.toRadians(rotation.y))), (float) (mouseDWheel * 0.01 * Math.sin(Math.toRadians(rotation.x))), (float) (mouseDWheel * 0.01 * Math.cos(Math.toRadians(rotation.y)))));
		}
	}

	public int getTotalDelta()
	{
		return totalDelta;
	}

	public void setTotalDelta(int totalDelta)
	{
		this.totalDelta = totalDelta;
	}

	public void addTotalDelta(int delta)
	{
		this.totalDelta += delta;
	}

	public int getWaitDelta()
	{
		return waitDelta;
	}
}
