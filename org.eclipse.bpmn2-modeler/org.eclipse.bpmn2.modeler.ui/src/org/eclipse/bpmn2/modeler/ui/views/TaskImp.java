package org.eclipse.bpmn2.modeler.ui.views;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.impl.TaskImpl;
//import org.eclipse.emf.common.util.EList;


import java.util.Collection;







import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;

public class TaskImp extends TaskImpl implements Task {/**
* The default value of the '{@link #getTaskName() <em>Task Name</em>}' attribute.
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @see #getTaskName()
* @generated
* @ordered
*/
protected static final String TASK_NAME_EDEFAULT = null;
/**
* The cached value of the '{@link #getTaskName() <em>Task Name</em>}' attribute.
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @see #getTaskName()
* @generated
* @ordered
*/
protected String taskName = TASK_NAME_EDEFAULT;
/**
* The default value of the '{@link #getDisplayName() <em>Display Name</em>}' attribute.
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @see #getDisplayName()
* @generated
* @ordered
*/
protected static final String DISPLAY_NAME_EDEFAULT = null;
/**
* The cached value of the '{@link #getDisplayName() <em>Display Name</em>}' attribute.
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @see #getDisplayName()
* @generated
* @ordered
*/
protected String displayName = DISPLAY_NAME_EDEFAULT;
/**
* The default value of the '{@link #getIcon() <em>Icon</em>}' attribute.
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @see #getIcon()
* @generated
* @ordered
*/
protected static final String ICON_EDEFAULT = null;
/**
* The cached value of the '{@link #getIcon() <em>Icon</em>}' attribute.
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @see #getIcon()
* @generated
* @ordered
*/
protected String icon = ICON_EDEFAULT;
/**
* The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @see #getParameters()
* @generated
* @ordered
*/

public TaskImp() {
super();
}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
@Override
protected EClass eStaticClass() {
	EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName("Task");
		eClass.setInterface(true);
		
return Bpmn2Package.eINSTANCE.getTask();
}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
public String getTaskName() {
return taskName;
}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
public void setTaskName(String newTaskName) {
String oldTaskName = taskName;
taskName = newTaskName;

}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
public String getDisplayName() {
return displayName;
}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
public void setDisplayName(String newDisplayName) {
String oldDisplayName = displayName;
displayName = "Task1";

}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
public String getIcon() {
return icon;
}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
public void setIcon(String newIcon) {
String oldIcon = icon;
icon = newIcon;

}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/

/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/

/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
@Override
public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
return null;


}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
@Override
public Object eGet(int featureID, boolean resolve, boolean coreType) {

return getTaskName();


}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
@SuppressWarnings("unchecked")
@Override
public void eSet(int featureID, Object newValue) {

setTaskName((String)newValue);

}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
@Override
public void eUnset(int featureID) {

setTaskName(TASK_NAME_EDEFAULT);
return;

}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
@Override
public boolean eIsSet(int featureID) {

return TASK_NAME_EDEFAULT == null ? taskName != null : !TASK_NAME_EDEFAULT.equals(taskName);

}
/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated
*/
@Override
public String toString() {
if (eIsProxy()) return super.toString();
StringBuffer result = new StringBuffer(super.toString());
result.append(" (taskName: ");
result.append(taskName);
result.append(", displayName: ");
result.append(displayName);
result.append(", icon: ");
result.append(icon);
result.append(')');
return result.toString();
}

}
