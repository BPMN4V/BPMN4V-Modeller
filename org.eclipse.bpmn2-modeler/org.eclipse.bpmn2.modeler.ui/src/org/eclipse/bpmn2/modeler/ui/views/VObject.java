package org.eclipse.bpmn2.modeler.ui.views;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Documentation;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.ExtensionDefinition;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.bpmn2.Task;
public class VObject implements BaseElement {
private String name;
private String id;
private EClass eClass;	
private Class<Task> cl;
	public VObject(String name)
	{
		this.name=name;
		 eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName("Task");
		eClass.setInterface(true);
		
	}
	public String getName()
	{return name;}
	
	@Override
	public EList<Adapter> eAdapters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean eDeliver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void eNotify(Notification arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eSetDeliver(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public TreeIterator<EObject> eAllContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EClass eClass() {
		// TODO Auto-generated method stub
		
		
	
		
		return eClass;
	}

	@Override
	public EObject eContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EStructuralFeature eContainingFeature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EReference eContainmentFeature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EList<EObject> eContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EList<EObject> eCrossReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object eGet(EStructuralFeature arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object eGet(EStructuralFeature arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object eInvoke(EOperation arg0, EList<?> arg1)
			throws InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean eIsProxy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean eIsSet(EStructuralFeature arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Resource eResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eSet(EStructuralFeature arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eUnset(EStructuralFeature arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public FeatureMap getAnyAttribute() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Documentation> getDocumentation() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<ExtensionDefinition> getExtensionDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<ExtensionAttributeValue> getExtensionValues() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "Task1";
	}
	@Override
	public void setId(String arg0) {
		// TODO Auto-generated method stub
		id="Task1";
		
	}

}
