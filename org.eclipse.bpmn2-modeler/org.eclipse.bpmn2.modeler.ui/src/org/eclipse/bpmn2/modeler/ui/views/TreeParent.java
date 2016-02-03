/******************************************************************************* 
 * Copyright (c) 2011, 2012 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.views;

import java.util.ArrayList;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.swt.widgets.TreeItem;

class TreeParent extends TreeObject {
	private ArrayList<TreeObject> children;
	//private ArrayList<TreeParent> children2;

	public TreeParent(String name) {
		super(name);
		children = new ArrayList<TreeObject>();
		//children2 = new ArrayList<TreeParent>();
	}

	public TreeParent(BaseElement elem) {
		super(elem);
		children = new ArrayList<TreeObject>();
	}
	
	public void addChild(TreeObject child) {
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
	
	public TreeObject[] getChildren() {
		return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
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
