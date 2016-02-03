package org.eclipse.bpmn2.modeler.ui.views;


import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class HierarchyView extends ViewPart {

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

		final HierarchyViewContentProvider contentProvider = new HierarchyViewContentProvider();
		viewer.setContentProvider(contentProvider);
		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.NO_RECREATE);
		viewer.setLabelProvider(new HierarchyViewLabelProvider());
		
		viewer.setInput(getViewSite());
		//viewer.setInput("imen");
		
	//viewer.addDragSupport(DND.DROP_MOVE | DND.DROP_COPY, new Transfer[] { LocalSelectionTransfer.getTransfer() },
				//new BpmnModelDragSourceLisener());

		listener = new BpmnModelViewerSelectionListener4(viewer);
		getSite().getPage().addSelectionListener(listener);

viewer.expandAll();
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
