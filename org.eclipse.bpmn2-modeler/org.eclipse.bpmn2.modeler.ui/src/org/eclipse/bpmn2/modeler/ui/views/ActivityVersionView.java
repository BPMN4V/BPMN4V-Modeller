package org.eclipse.bpmn2.modeler.ui.views;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class ActivityVersionView extends ViewPart {
	
	private final class BpmnModelDragSourceLisener extends DragSourceAdapter {
		@Override
		public void dragStart(DragSourceEvent event) {
			if (((IStructuredSelection) viewer.getSelection()).getFirstElement() instanceof TreeObject) {

				IStructuredSelection iStructuredSelection = (IStructuredSelection) viewer.getSelection();
				TreeObject treeObject = (TreeObject) iStructuredSelection.getFirstElement();
				//treeObject.getAdapter(getClass());
				BaseElement baseElement = treeObject.getBaseElement();
				LocalSelectionTransfer.getTransfer().setSelection(new StructuredSelection(baseElement));
			}

		}
	}

	
	
	public static TreeViewer viewer;
	private Tree activity;
	private ISelectionListener listener;

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		/*activity=new Tree(parent, SWT.NO_SCROLL|SWT.MULTI);
		TreeItem item = new TreeItem(activity, SWT.NONE);
	       
 		item.setText("salut");*/
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		final ActivityViewContentProvider contentProvider = new ActivityViewContentProvider();
		viewer.setContentProvider(contentProvider);
		
		viewer.setLabelProvider(new ViewLabelProvider());
		
		viewer.setInput(getViewSite());
		//viewer.setInput("imen");
		
		viewer.addDragSupport(DND.DROP_MOVE | DND.DROP_COPY, new Transfer[] { LocalSelectionTransfer.getTransfer() },
				new BpmnModelDragSourceLisener());

		listener = new BpmnModelViewerSelectionListener2(viewer);
		getSite().getPage().addSelectionListener(listener);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "org.eclipse.bpmn2.modeler.ui.viewer"); //$NON-NLS-1$
		
	}
	
	
	/*@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(listener);
		super.dispose();
	}
*/
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		//viewer.getControl().setFocus();
		
	}
	
	

}
