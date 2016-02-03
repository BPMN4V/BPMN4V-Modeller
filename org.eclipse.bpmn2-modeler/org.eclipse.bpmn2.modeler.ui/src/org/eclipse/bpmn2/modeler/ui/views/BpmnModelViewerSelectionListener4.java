package org.eclipse.bpmn2.modeler.ui.views;

import java.io.IOException;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

public class BpmnModelViewerSelectionListener4 implements ISelectionListener {
	private final HierarchyViewContentProvider contentProvider;
	private BPMN2Editor editor;
	private final TreeViewer viewer;

	public BpmnModelViewerSelectionListener4(TreeViewer viewer) {
		this.viewer = viewer;
		this.contentProvider = (HierarchyViewContentProvider) viewer.getContentProvider();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		
Object b[];Object bo = null;
//Object[] selected = null ;
		Object bpmn2Editor = part.getAdapter(BPMN2Editor.class);
		if (bpmn2Editor instanceof BPMN2Editor) {
			editor = (BPMN2Editor)bpmn2Editor;
			PictogramElement [] pes=editor.getSelectedPictogramElements();
			   bo = BPMNToolBehaviorProvider.featureProvider.getBusinessObjectForPictogramElement(pes[0]);
			try {
			//	String[] selected = contentProvider.getSelectedS(selection);
				//Object[] selected = contentProvider.getSelected(selection);
				
					//selected[0]=bo;
				//b=	 Graphiti.getPeService().getAllContainedPictogramElements(pes[0]).toArray();
			//	Graphiti.getPeService().getDiagramForPictogramElement(pes[0]);
				//((Task) b[0]);
			//	b[0].
				
				//System.out.println("bo.toString()"+((Task) bo).getId());
				//editor
				//pe[0].getLink().getPictogramElement().g
				ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(editor.getDiagramTypeProvider()
						.getDiagram().eResource());
				if (bo instanceof Task)
				contentProvider.updateModel(modelHandler,((Task) bo).getId(), "Task");
				else 
					if (bo instanceof BPMNDiagram)
						contentProvider.updateModel(modelHandler,editor.getCurrentInput().getName(), "Process");
					else 
						if (bo instanceof Participant)
							{Participant p=((Participant) bo);
							contentProvider.updateModel(modelHandler,p.getProcessRef().getId(), "Participant");
							
							}
				viewer.refresh(true);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Object[] selected = contentProvider.getSelected(selection);
		//selected[selected.length+1]=bo;
	
		//((Task) selected[0]);
		if (selected != null) {
			viewer.setSelection(new StructuredSelection(selected), true);
		}
	}
}
