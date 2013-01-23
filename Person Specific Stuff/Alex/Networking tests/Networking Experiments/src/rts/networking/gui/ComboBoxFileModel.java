package rts.networking.gui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

public class ComboBoxFileModel extends DefaultComboBoxModel {
	private static final long serialVersionUID = 7207990984195292199L;
	
	ArrayList<File> files = new ArrayList<File>();
	int index = 0;
	
	public ComboBoxFileModel(ArrayList<File> files) {
		this.files = files;
	}
	
	public int getSize() {
		return files.size();
	}
	
	public String getSelectedItem() {
		return files.get(index).getName();
	}
}
