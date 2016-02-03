package org.eclipse.bpmn2.modeler.core.merrimac.dialogs;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TreeStructure;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeObjectEditor extends ObjectEditor {

	private Tree VH=null;
	private TreeItem item=null;
	private TreeItem subitem=null;
	private TreeItem subsubitem=null;
	public TreeObjectEditor(AbstractDetailComposite parent, EObject object,
			EStructuralFeature feature) {
		super(parent, object, feature);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected Control createControl(Composite composite, String label, int style) {
		createLabel(composite,label);
		VH=new Tree(parent, SWT.NO_SCROLL|SWT.MULTI);
		
	        VH.pack();
		
		
		
		
		return VH;
	}
	public TreeStructure<String>  createTree()
	{
		TreeStructure<String> root = new TreeStructure<String>("VAA1-1(V1)");
        root.addChild("VAA1-2(V2)");
        TreeStructure<String> childC = root.addChild("VAA1-3(V3)");
        root.addChild("VAA1-4(V4)");
         
        TreeStructure<String> childC1 = childC.addChild("VAA1-5(V5)");
        //TreeStructure<String> childC2 = childC1.addChild(null);
      //  System.out.println("root = " + childC.getRoot());                 // toString() method is invoked
        //System.out.println("Contains C = " + childC.contains("C"));
        //System.out.println("Contains D = " + childC.contains("D"));
         
        //System.out.println("root = " + childC1.getParent());              // toString() method is invoked
         return childC;
	}	
	public void BuildWidget(String data, int level)
	{ if (level==0)
	{
		item = new TreeItem(VH, SWT.NONE);
	       
 		item.setText(data);
	}
	else if (level==1)
	{
		subitem = new TreeItem(item, SWT.NONE);
	       
		subitem.setText(data);
	}
	else if (level==2)
	{subsubitem = new TreeItem(subitem, SWT.NONE);
    
	subsubitem.setText(data);
		
	}
	
	
	
	}
	public void setTreeWidget()
	{
		TreeStructure<String> tree =createTree();
		
		setTree(tree.parent,0);
	}
	
	public void setTree(TreeStructure<String> tree, int level)
	{ 
	
		 if (tree == null)
	            return; // Tree is empty, so leave.
	             
		 BuildWidget(tree.data, level)   ;
		 
	         
	        List<TreeStructure<String>> children2 = tree.children;
	        ++level; //increment the level
	         
	        Iterator<TreeStructure<String>> iterator = children2.iterator();
	        while (children2 != null && iterator.hasNext())
	        {
	        	TreeStructure<String> next = iterator.next();
	            if (next != null)
	            {
	            	setTree(next, level); //recursion
	            }
	             
	        }
	
	}
	public String getTree()
	{System.out.println(ModelUtil.getDisplayName(object, feature));
		return ModelUtil.getDisplayName(object, feature);}
	

	@Override
	protected boolean setValue(final Object result) {
		
		if (super.setValue(result)) {
			updateTree();
			return true;
		}
		// revert the change on error
		setTreeWidget();
	
				return false;
	}
	
	
	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}
public void notifyChanged(Notification notification) {
		
		
		if (notification.getEventType() == -1) {
			//setValue(getValue());
			
			VH.removeAll();
			
			setTreeWidget();
			super.notifyChanged(notification);
		}
		else if (object == notification.getNotifier()) {
			if (notification.getFeature() instanceof EStructuralFeature) {
				EStructuralFeature f = (EStructuralFeature)notification.getFeature();
				if (f!=null && (f.getName().equals(feature.getName()) ||
						f.getName().equals("mixed")) ) { // handle the case of FormalExpression.body //$NON-NLS-1$
					//setValue(getValue());
					/*TreeStructure<String> root=createTree();
					TreeStructure<String> parentTmp =root.parent;
					item = new TreeItem(VH, SWT.NONE);
			       
			 		item.setText(parentTmp.data);*/
					VH.removeAll();
					setTreeWidget();
					super.notifyChanged(notification);
				}
			}
		}
	}
protected void updateTree() {
	try {
		isWidgetUpdating = true;
		
		setTreeWidget();
		
			
		
	}
	finally {
		isWidgetUpdating = false;
	}
}


}
