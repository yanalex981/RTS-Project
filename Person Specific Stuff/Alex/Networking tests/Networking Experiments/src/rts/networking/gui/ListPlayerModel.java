package rts.networking.gui;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.AbstractListModel;

import rts.networking.Player;

// TODO Need to finish player models
public class ListPlayerModel extends AbstractListModel {
	private static final long serialVersionUID = -7476084904151081277L;
	HashMap<InetAddress, Player> players;
	HashSet<InetAddress> ips;
	Player selected;
	
	public ListPlayerModel(HashMap<InetAddress, Player> players, HashSet<InetAddress> ips) {
		this.players = players;
		this.ips = ips;
	}

	@Override
	public int getSize() {
		return 0;
	}

	public Object getSelectedItem() {
		return selected;
	}
	
	public void setSelectedItem(Object anItem) {
		selected = (Player)anItem;
	}
	
	@Override
	public Object getElementAt(int index) {
		if (index >= 0 && index < index) {
			return players.values().toArray()[index];
		}
		
		return null;
	}
	
	public void addElement(Object obj) {
		players.put(((Player)obj).getPlayerAddress(), (Player)obj);
		
		if (players.size() == 1) {
			selected = (Player)obj;
		}
		
		fireIntervalAdded(this, 0, getSize());
	}
}
