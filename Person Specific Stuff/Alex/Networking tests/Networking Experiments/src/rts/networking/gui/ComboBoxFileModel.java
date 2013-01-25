package rts.networking.gui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
//import javax.swing.DefaultComboBoxModel;
import javax.swing.MutableComboBoxModel;

public class ComboBoxFileModel extends AbstractListModel implements MutableComboBoxModel {
	private static final long serialVersionUID = 1099660775670519617L;
	ArrayList<File> files;
	File selected;
	
	public ComboBoxFileModel(ArrayList<File> files) {
		if (files != null) {
			this.files = new ArrayList<File>(files.size());
			
			for (File f: files) {
				this.files.add(f);
			}
		}
		else {
			files = new ArrayList<File>(1);
		}
	}
	
	public int getSize() {
		return files.size();
	}
	
	public Object getElementAt(int index) {
		return files.get(index);
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selected = (File)anItem;
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	@Override
	public void addElement(Object obj) {
		files.add((File)obj);
		
		if (files.size() == 1) {
			selected = (File)obj;
		}
		
		fireIntervalAdded(this, 0, getSize());
	}

	@Override
	public void removeElement(Object obj) {
		files.remove(obj);
		fireIntervalRemoved(this, 0, getSize());
	}

	@Override
	public void insertElementAt(Object obj, int index) {
		files.add(index, (File)obj);
		fireIntervalAdded(this, 0, getSize());
	}

	@Override
	public void removeElementAt(int index) {
		files.remove(index);
		fireIntervalRemoved(this, 0, getSize());
	}
}

//
//import java.io.File;
//import java.util.ArrayList;
//
//import javax.swing.DefaultComboBoxModel;
//
//public class ComboBoxFileModel extends DefaultComboBoxModel {
//	private static final long serialVersionUID = -5538888608157101156L;
//
//	ArrayList<File> files;
//	ArrayList<String> fileNames;
//	int index = -1;
//	
//	public ComboBoxFileModel(ArrayList<File> files) {
//		this.files = files;
//		
//		for (File f: files) {
//			fileNames.add(f.getName());
//		}
//	}
//	
//	public int getSize() {
//		if (files == null) {
//			return 0;
//		}
//		
//		return files.size();
//	}
//
//	public Object getElementAt(int index) {
//		if (index > files.size() || index < 0) {
//			return "";
//		}
//		
//		return fileNames.get(index);
//	}
//}
