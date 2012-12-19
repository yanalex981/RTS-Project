import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class UserInterface
{
	private boolean keyUP, keyDOWN, keyLEFT, keyRIGHT, keySPACE, keyLSHIFT;
	private boolean keyW, keyA, keyS, keyD;
	private boolean keyEsc, keyF2;
	private boolean key1, key2, key3, key4, key5, key6, key7, key8, key9, key0;
	private int totalDelta;

	public UserInterface()
	{

	}

	public void hotKeyUpdate(Unit selection, int delta)
	{
		key1 = Keyboard.isKeyDown(Keyboard.KEY_1);
		key2 = Keyboard.isKeyDown(Keyboard.KEY_2);
		key3 = Keyboard.isKeyDown(Keyboard.KEY_3);
		key4 = Keyboard.isKeyDown(Keyboard.KEY_4);
		key5 = Keyboard.isKeyDown(Keyboard.KEY_5);
		key6 = Keyboard.isKeyDown(Keyboard.KEY_6);
		key7 = Keyboard.isKeyDown(Keyboard.KEY_7);
		key8 = Keyboard.isKeyDown(Keyboard.KEY_8);
		key9 = Keyboard.isKeyDown(Keyboard.KEY_9);
		key0 = Keyboard.isKeyDown(Keyboard.KEY_0);
		totalDelta += delta;

		if (totalDelta > 100)
		{
			buildKeyUpdate(selection);
		}
	}

	public void buildKeyUpdate(Unit selection)
	{
		if (selection != null)
		{
			if (key1)
				selection.keyOneCommand();
			if (key2)
				selection.keyTwoCommand();
		}
	}

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

		keyboardMovementUpdate(tMatrix, rotation, delta);

		mouseMovementUpdate(rMatrix, tMatrix, rotation);
	}

	private void keyboardMovementUpdate(Matrix4f tMatrix, Vector3f rotation, float delta)
	{
		//Vector4f tVector = new Vector4f(0, 0, 0, 1);
		//Matrix4f rMatrix = new Matrix4f();

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
			tMatrix.translate(new Vector3f(0, 0.01f * delta, 0));
		}
		if (keySPACE)
		{
			tMatrix.translate(new Vector3f(0, -0.01f * delta, 0));
		}
		if (keyUP /* || (Mouse.getY() > Display.getHeight() - 50 && !Mouse.isGrabbed()) */)
		{
			tMatrix.translate(new Vector3f((float) (delta * 0.01 * 0.707), 0, (float) (delta * 0.01 * 0.707)));
		}
		if (keyDOWN /* || (Mouse.getY() < 50 && !Mouse.isGrabbed()) */)
		{
			tMatrix.translate(new Vector3f((float) (delta * -0.01 * 0.707), 0, (float) (delta * -0.01 * 0.707)));
		}
		if (keyRIGHT /* || (Mouse.getX() > Display.getWidth() - 50 && !Mouse.isGrabbed()) */)
		{
			tMatrix.translate(new Vector3f((float) (delta * 0.01 * 0.707), 0, (float) (delta * -0.01 * 0.707)));
		}
		if (keyLEFT /* || (Mouse.getX() < 50 && !Mouse.isGrabbed()) */)
		{
			tMatrix.translate(new Vector3f((float) (delta * -0.01 * 0.707), 0, (float) (delta * 0.01 * 0.707)));
		}
		if (keyEsc)
		{
			System.exit(0);
		}
	}

	private void mouseMovementUpdate(Matrix4f rMatrix, Matrix4f tMatrix, Vector3f rotation)
	{
		//System.out.println(rotation);
		//System.out.println(rMatrix);
		if (totalDelta > 100)
		{
			if (Mouse.isButtonDown(2) && !Mouse.isGrabbed())
				Mouse.setGrabbed(true);
			else if (Mouse.isButtonDown(2) && Mouse.isGrabbed())
				Mouse.setGrabbed(false);
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
			//rMatrix.rotate((float)Math.toRadians(-mouseDX), new Vector3f(0, 1, 0));

			if (rotation.x - mouseDY > 85)
				rotation.x = 85;
			else if (rotation.x - mouseDY < -85)
				rotation.x = -85;
			else
			{
				rotation.x -= mouseDY;
			}
			//rMatrix.rotate((float)Math.toRadians(mouseDY), new Vector3f(1, 0, 0));
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
}
