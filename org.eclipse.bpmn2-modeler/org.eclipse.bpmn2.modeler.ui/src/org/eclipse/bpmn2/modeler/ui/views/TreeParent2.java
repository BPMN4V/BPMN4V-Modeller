package org.eclipse.bpmn2.modeler.ui.views;

import java.util.ArrayList;

import org.eclipse.bpmn2.BaseElement;

public class TreeParent2 extends TreeObject2 
{private ArrayList<TreeParent2> children;
//private ArrayList<TreeObject2> children2;

public TreeParent2(String name) {
	super(name);
	children = new ArrayList<TreeParent2>();
	//children2 = new ArrayList<TreeObject2>();
}

public TreeParent2(BaseElement elem) {
	super(elem);
	children = new ArrayList<TreeParent2>();
}

public void addChild(TreeParent2 child) {
	children.add(child);
	
	child.setParent(this);
}

/*public void addChild(TreeParent child) {
	children2.add(child);
	
	child.setParent(this);
}*/



public void removeChild(TreeObject child) {
	children.remove(child);
	
	child.setParent(null);
}

public TreeParent2[] getChildren() {
	return (TreeParent2[]) children.toArray(new TreeParent2[children.size()]);
}

/*public TreeParent[] getChildrenP() {
	return (TreeParent[]) children2.toArray(new TreeParent[children2.size()]);
}*/

public boolean hasChildren() {
	return children.size() > 0;
}

public void removeChildren() {
	children.clear();
}



}
