package org.eclipse.bpmn2.modeler.core.merrimac.dialogs;


import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.help.IHelpContexts;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

public class VersionDragDialog extends AbstractObjectEditingDialog {
	protected EClass featureEType;
	
	public VersionDragDialog(DiagramEditor editor, EObject object) {
		super(editor, object);
		// TODO Auto-generated constructor stub
	}
	public VersionDragDialog(DiagramEditor editor, EObject object, EClass eclass) {
		super(editor, object);
		this.featureEType = eclass;
	}
	public void setFeatureEType(EClass eclass) {
		this.featureEType = eclass;
	}
	
	public EClass getFeatureEType() {
		return featureEType;
	}

	@Override
	public Composite createDialogContent(Composite parent) {
	//	Composite composite = (Composite)super.createDialogArea(parent);
		//composite.getChildren(); 
		//Composite composite =	PropertiesCompositeFactory.INSTANCE.createDialogComposite(
			//	featureEType, parent, SWT.NONE);
		//Label label = new Label(parent, SWT.WRAP);
      //   label.setText("ok");
      //   PlatformUI.getWorkbench().getHelpSystem().setHelp(getShell(), IHelpContexts.Property_Dialog);
		return parent;
	}
	

	@Override
	protected String getPreferenceKey() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int open() {
		title = null;
		if (object!=null)
			title = NLS.bind("Task version", ModelUtil.getLabel(object));
		//createDialogContent(dialogContent);
		//create();
		if (cancel)
			return Window.CANCEL;
		
		return super.open();
	}

}
