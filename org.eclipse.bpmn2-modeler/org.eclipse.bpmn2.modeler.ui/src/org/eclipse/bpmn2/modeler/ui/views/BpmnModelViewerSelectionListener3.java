package org.eclipse.bpmn2.modeler.ui.views;

import java.io.IOException;

import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

public class BpmnModelViewerSelectionListener3 implements ISelectionListener {
	private final ListActivityViewContentProvider contentProvider;
	private BPMN2Editor editor;
	private final TreeViewer viewer;

	public BpmnModelViewerSelectionListener3(TreeViewer viewer) {
		this.viewer = viewer;
		this.contentProvider = (ListActivityViewContentProvider) viewer.getContentProvider();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		Object bpmn2Editor = part.getAdapter(BPMN2Editor.class);
		if (bpmn2Editor instanceof BPMN2Editor) {
			editor = (BPMN2Editor)bpmn2Editor;
			try {
				ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(editor.getDiagramTypeProvider()
						.getDiagram().eResource());
				contentProvider.updateModel(modelHandler);
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




