package org.eclipse.bpmn2.modeler.ui.views;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;

public class VersionTreeObject implements IAdaptable {
	private String name;
	private TreeParent parent;
	private BaseElement f;

	public VersionTreeObject(final String name) {
		this.name = name;
	}

	public VersionTreeObject(final BaseElement f) {
		this.f = f;
		if (f instanceof FlowElement) {
			FlowElement flowElem = (FlowElement) f;
			name = flowElem.getName() == null ? "" : flowElem.getName(); //$NON-NLS-1$
			name += " (" + f.eClass().getName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			name = f.eClass().getName();
		}
	}

	public String getName() {
		return name;
	}

	public void setParent(final TreeParent parent) {
		this.parent = parent;
	}

	public TreeParent getParent() {
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

