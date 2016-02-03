package org.eclipse.bpmn2.modeler.ui.views;

import java.util.ArrayList;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;

public class TreeObject2 implements IAdaptable {
	private String name;
	private TreeParent2 parent;
	private BaseElement f;
	

	public TreeObject2(final String name) {
		this.name = name;
		
	}
	
	public TreeObject2(final TaskImp ob) {
		this.f= (BaseElement)ob;
		
	
		this.name = ob.getTaskName();
	}

	public TreeObject2(final BaseElement f) {
		this.f = f;
		if (f instanceof FlowElement) {
			FlowElement flowElem = (FlowElement) f;
			name = flowElem.getName() == null ? "" : flowElem.getName(); //$NON-NLS-1$
			name += " (" + f.eClass().getName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			name = f.eClass().getName();
		}
	}

	public TreeObject2(String id_v, String state, BaseElement f2) {
		this.f = f2;
		if (f instanceof Task) {
			FlowElement flowElem = (FlowElement) f;
			name = flowElem.getName() == null ? "" : flowElem.getName()+" -"+id_v+"- "; //$NON-NLS-1$
			name += " (" + state + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			name = f.eClass().getName();
		}
	}
	
	
	public String getName() {
		return name;
	}
	
	

	public void setParent(final TreeParent2 parent) {
		this.parent = parent;
	}
/*	public void setParent(final TreeObject parent) {
		TreeParent t=new TreeParent(parent.getName());
		this.parent = t;
	}*/
	public TreeParent2 getParent() {
		return parent;
	}

	public String toString() {
		return getName();
	}

	public Object getAdapter(final Class key) {
		if (key.equals(EObject.class)) {
			return getBaseElement();
		}
		return null;
	}

	public void setBaseElement(final BaseElement f) {
		this.f = f;
	}

	public BaseElement getBaseElement() {
		return f;
	}
}
