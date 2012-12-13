import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class UserInterface
{
	private boolean keyEsc, keyUP, keyDOWN, keyLEFT, keyRIGHT, keySPACE, keyLSHIFT, keyW, keyA, keyS, keyD;

	public UserInterface()
	{

	}

	public void movementUpdate(Matrix4f tMatrix, Vector3f rotation, float delta)
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

		keyboardUpdate(tMatrix, rotation, delta);
		mouseUpdate(tMatrix, rotation);

	}

	private void keyboardUpdate(Matrix4f tMatrix, Vector3f rotation, float delta)
	{
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
		if (keySPACE)
		{
			System.out.println("These are words");
			tMatrix.translate(new Vector3f(0, 0.01f * delta, 0));
		}
		if (keyLSHIFT)
		{
			tMatrix.translate(new Vector3f(0, -0.01f * delta, 0));
		}
		if (keyUP || (Mouse.getY() > Display.getHeight() - 50 && !Mouse.isGrabbed()))
		{
			tMatrix.translate(new Vector3f((float) (delta * -0.01 * 0.707), 0, (float) (delta * 0.01 * 0.707)));
		}
		if (keyDOWN || (Mouse.getY() < 50 && !Mouse.isGrabbed()))
		{
			tMatrix.translate(new Vector3f((float) (delta * 0.01 * 0.707), 0, (float) (delta * -0.01 * 0.707)));
		}
		if (keyRIGHT || (Mouse.getX() > Display.getWidth() - 50 && !Mouse.isGrabbed()))
		{
			tMatrix.translate(new Vector3f((float) (delta * -0.01 * 0.707), 0, (float) (delta * -0.01 * 0.707)));
		}
		if (keyLEFT || (Mouse.getX() < 50 && !Mouse.isGrabbed()))
		{
			tMatrix.translate(new Vector3f((float) (delta * 0.01 * 0.707), 0, (float) (delta * 0.01 * 0.707)));
		}
		if (keyEsc)
		{
			System.exit(0);
		}
	}

	private void mouseUpdate(Matrix4f tMatrix, Vector3f rotation)
	{
		if (Mouse.isButtonDown(1))
			Mouse.setGrabbed(true);
		else if (Mouse.isButtonDown(2))
			Mouse.setGrabbed(false);

		if (Mouse.isGrabbed())
		{
			float mouseDX = Mouse.getDX() * 0.32f;
			float mouseDY = Mouse.getDY() * 0.32f;

			if (rotation.y + mouseDX >= 360)
				rotation.y = rotation.y + mouseDX - 360;
			else if (rotation.y + mouseDX < 0)
				rotation.y = 360 - rotation.y + mouseDX;
			else
				rotation.y += mouseDX;

			if (rotation.x - mouseDY > 85)
				rotation.x = 85;
			else if (rotation.x - mouseDY < -85)
				rotation.x = -85;
			else
				rotation.x -= mouseDY;
		}

		int mouseDWheel = Mouse.getDWheel();
		if (mouseDWheel != 0)
		{
			System.out.println(Mouse.getDWheel());
			tMatrix.translate(new Vector3f((float) (mouseDWheel * -0.01 * Math.sin(Math.toRadians(rotation.y))), (float) (mouseDWheel * 0.01 * Math.sin(Math.toRadians(rotation.x))), (float) (mouseDWheel * 0.01 * Math.cos(Math.toRadians(rotation.y)))));
		}
	}
}
