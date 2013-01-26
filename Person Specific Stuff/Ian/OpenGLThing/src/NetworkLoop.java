import java.io.IOException;
import java.net.DatagramPacket;


public class NetworkLoop extends Thread
{
	Player player;
	
	public NetworkLoop(Player player)
	{
		System.out.println("Heyo");
		this.player = player;
	}
	
	public void run()
	{
		
		while(true)
		{
			System.out.println("stuff");
			/*try
			{
				System.out.println("yo");
				player.packet = new DatagramPacket(player.data, 200);
				//player.socket.receive(player.packet);
				//player.updateNetworking();
			} catch (IOException e)
			{
				e.printStackTrace();
			}*/
		}
	}
}
