package org.eclipse.bpmn2.modeler.ui.views;

import java.io.IOException;

import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

public class BpmnModelViewerSelectionListener2 implements ISelectionListener {
	private final ActivityViewContentProvider contentProvider;
	private BPMN2Editor editor;
	private final TreeViewer viewer;

	public BpmnModelViewerSelectionListener2(TreeViewer viewer) {
		this.viewer = viewer;
		this.contentProvider = (ActivityViewContentProvider) viewer.getContentProvider();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		Object bo = null;
		Object bpmn2Editor = part.getAdapter(BPMN2Editor.class);
		if (bpmn2Editor instanceof BPMN2Editor) {
			editor = (BPMN2Editor)bpmn2Editor;
			PictogramElement [] pes=editor.getSelectedPictogramElements();
			   bo = BPMNToolBehaviorProvider.featureProvider.getBusinessObjectForPictogramElement(pes[0]);
			try {
				ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(editor.getDiagramTypeProvider()
						.getDiagram().eResource());
				
				if (editor.currentInput.getName().startsWith("VP"))
				{contentProvider.updateModel(modelHandler);
				}
				else
					if  (editor.currentInput.getName().startsWith("VC"))
						contentProvider.updateModel1(modelHandler);
				
				viewer.refresh(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Object[] selected = contentProvider.getSelected(selection);
		if (selected != null) {
			viewer.setSelection(new StructuredSelection(selected), true);
		}
	}
}



