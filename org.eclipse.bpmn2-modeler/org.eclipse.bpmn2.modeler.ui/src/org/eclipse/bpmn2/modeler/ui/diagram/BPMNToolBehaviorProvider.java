/******************************************************************************* 
 * Copyright (c) 2011, 2012 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.diagram;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Group;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.features.CompoundCreateFeature;
import org.eclipse.bpmn2.modeler.core.features.CompoundCreateFeaturePart;
import org.eclipse.bpmn2.modeler.core.features.IBpmn2AddFeature;
import org.eclipse.bpmn2.modeler.core.features.IBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.core.features.ShowPropertiesFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.ActivitySelectionBehavior;
import org.eclipse.bpmn2.modeler.core.features.command.ICustomCommandFeature;
import org.eclipse.bpmn2.modeler.core.features.event.EventSelectionBehavior;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.DateObjectEditor;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ModelSubclassSelectionDialog;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditingDialog;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.bpmn2.modeler.core.preferences.ModelEnablements;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.runtime.ToolPaletteDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.ToolPaletteDescriptor.CategoryDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.ToolPaletteDescriptor.ToolDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.ToolPaletteDescriptor.ToolPart;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.core.validation.ValidationStatusAdapter;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.FeatureMap;
import org.eclipse.bpmn2.modeler.ui.IConstants;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomConnectionFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomElementFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomShapeFeatureContainer;

import org.eclipse.bpmn2.modeler.ui.features.activity.task.TaskFeatureContainer.CreateTaskFeature;
import org.eclipse.bpmn2.modeler.ui.features.choreography.ChoreographySelectionBehavior;
import org.eclipse.bpmn2.modeler.ui.features.choreography.ChoreographyUtil;
import org.eclipse.bpmn2.modeler.ui.property.dialogs.SchemaSelectionDialog;
import org.eclipse.bpmn2.modeler.ui.wizards.BPMN2DiagramWizard;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.graphiti.IExecutionInfo;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.FeatureCheckerAdapter;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureAndContext;
import org.eclipse.graphiti.features.IFeatureChecker;
import org.eclipse.graphiti.features.IFeatureCheckerHolder;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDoubleClickContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.AddBendpointContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.context.impl.MoveBendpointContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.IToolEntry;
import org.eclipse.graphiti.palette.impl.ConnectionCreationToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.platform.IPlatformImageConstants;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.tb.ContextButtonEntry;
import org.eclipse.graphiti.tb.ContextMenuEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;
import org.eclipse.graphiti.tb.IContextMenuEntry;
import org.eclipse.graphiti.tb.IDecorator;
import org.eclipse.graphiti.tb.IImageDecorator;
import org.eclipse.graphiti.tb.ImageDecorator;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.util.ILocationInfo;
import org.eclipse.graphiti.util.LocationInfo;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;



public class BPMNToolBehaviorProvider extends DefaultToolBehaviorProvider implements IFeatureCheckerHolder {

	public static BPMN2Editor editor;
	protected TargetRuntime targetRuntime;
	public static BPMNFeatureProvider featureProvider;
	protected ModelEnablements modelEnablements;
	protected Boolean entre=false;
	protected Hashtable<String, PaletteCompartmentEntry> categories = new Hashtable<String, PaletteCompartmentEntry>();
	protected List<IPaletteCompartmentEntry> palette;
	protected AbstractBpmn2PropertySection section;
	protected AbstractDetailComposite parent;
	protected boolean changesDone = false;
	protected VersionDialog dialog;
	public static boolean boutonentry=false, menuentry=false, trouve;
	public  static  String state, activity_state ,process_state;
	public static String  id_v, name_process, id_va, name_activity;
	private TreeItem item=null;
	private TreeItem subitem=null;
	
	private String dervivé="";
	protected class ProfileSelectionToolEntry extends ToolEntry {
		BPMN2Editor editor;
		
		ProfileSelectionToolEntry(BPMN2Editor editor, String label) {
			super(label, null, null, null, null);
			this.editor = editor;
			//id_v=editor.getCurrentInput().getName();
			id_v=editor.bpmnDiagram.getName();
		}
		
		public Tool createTool() {
			String profile = getLabel();
			Bpmn2DiagramType diagramType = ModelUtil.getDiagramType(editor);
			TargetRuntime rt = editor.getTargetRuntime();
			editor.getPreferences().setDefaultToolProfile(rt, diagramType, profile);
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					editor.updatePalette();
				}
				
			});
			return null;
		}

		@Override
		public ImageDescriptor getLargeIcon() {
			return super.getSmallIcon();
		}

		@Override
		public ImageDescriptor getSmallIcon() {
			Bpmn2DiagramType diagramType = ModelUtil.getDiagramType(editor);
			TargetRuntime rt = editor.getTargetRuntime();
			String profile = editor.getPreferences().getDefaultToolProfile(rt, diagramType);
			if (getLabel().equals(profile))
				return Activator.getDefault().getImageDescriptor(IConstants.ICON_CHECKBOX_CHECKED_16);
			return Activator.getDefault().getImageDescriptor(IConstants.ICON_CHECKBOX_UNCHECKED_16);
		}
	}
	
	protected abstract class ObjectDragVersion extends ObjectEditor {

		public ObjectDragVersion(AbstractDetailComposite parent,
				EObject object, EStructuralFeature feature) {
			super(parent, object, feature);
			this.parent=parent;
			// TODO Auto-generated constructor stub
		}
		
	}
	
	public BPMNToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		
	}

	public void createPaletteProfilesGroup(BPMN2Editor editor, PaletteRoot paletteRoot) {
		TargetRuntime rt = editor.getTargetRuntime();
	
		Bpmn2DiagramType diagramType = ModelUtil.getDiagramType(editor);

		PaletteDrawer drawer = new PaletteDrawer(Messages.BPMNToolBehaviorProvider_Profiles_Drawer_Label, null);
		int size = 0;

		for (String profile : editor.getPreferences().getAllToolProfiles(rt, diagramType)) {
			drawer.add(new ProfileSelectionToolEntry(editor, profile));
			++size;
		}
		if (size>1) {
			drawer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
			paletteRoot.add(1,drawer);
		}
	}
	
	@Override
	public IPaletteCompartmentEntry[] getPalette() {

		editor = (BPMN2Editor)getDiagramTypeProvider().getDiagramEditor();
		targetRuntime = editor.getTargetRuntime();
		modelEnablements = editor.getModelEnablements();
		featureProvider = (BPMNFeatureProvider)getFeatureProvider();

		palette = new ArrayList<IPaletteCompartmentEntry>();
		Bpmn2DiagramType diagramType = ModelUtil.getDiagramType(editor.getBpmnDiagram());
		String profile = editor.getPreferences().getDefaultToolProfile(targetRuntime, diagramType);
		
		PaletteCompartmentEntry compartmentEntry = null;
		categories.clear();
		ToolPaletteDescriptor toolPaletteDescriptor = targetRuntime.getToolPalette(diagramType, profile);
		if (toolPaletteDescriptor!=null) {
			boolean needCustomTaskDrawer = true;
			for (CategoryDescriptor category : toolPaletteDescriptor.getCategories()) {
				if (ToolPaletteDescriptor.DEFAULT_PALETTE_ID.equals(category.getId())) {
					createDefaultpalette();
					needCustomTaskDrawer = false;
					continue;
				}
				
				category = getRealCategory(targetRuntime, category);
				compartmentEntry = categories.get(category.getName());
				for (ToolDescriptor tool : category.getTools()) {
					tool = getRealTool(targetRuntime, tool);
					IFeature feature = getCreateFeature(tool);
				
					if (feature!=null) {
						if (compartmentEntry==null) {
							compartmentEntry = new PaletteCompartmentEntry(category.getName(), category.getIcon());
							compartmentEntry.setInitiallyOpen(false);
							categories.put(category.getName(), compartmentEntry);
						}
						createEntry(feature, compartmentEntry);
					}
				}
				// if there are no tools defined for this category, check if it will be
				// used for only Custom Tasks. If so, create the category anyway.
				if (compartmentEntry==null) {
					for (CustomTaskDescriptor tc : targetRuntime.getCustomTasks()) {
						if (category.getName().equals(tc.getCategory())) {
							compartmentEntry = new PaletteCompartmentEntry(category.getName(), category.getIcon());
							compartmentEntry.setInitiallyOpen(false);
							categories.put(category.getName(), compartmentEntry);
							palette.add(compartmentEntry);
							break;
						}
					}
				}
				else if (compartmentEntry.getToolEntries().size()>0)
					palette.add(compartmentEntry);
			}
			if (needCustomTaskDrawer)
				createCustomTasks(palette);
		}
		else
		{
			// create a default toolpalette
			createDefaultpalette();
		}
		
		return palette.toArray(new IPaletteCompartmentEntry[palette.size()]);
	}
	
	private CategoryDescriptor getRealCategory(TargetRuntime rt, CategoryDescriptor category) {
		String fromPalette = category.getFromPalette();
		String id = category.getId();
		if (fromPalette!=null && id!=null) {
			for (TargetRuntime otherRt : TargetRuntime.getAllRuntimes()) {
				if (otherRt!=rt) {
					for (ToolPaletteDescriptor tp : otherRt.getToolPalettes()) {
						if ( fromPalette.equals(tp.getId())) {
							for (CategoryDescriptor c : tp.getCategories()) {
								if (id.equals(c.getId())) {
									return c;
								}
							}
						}
					}
				}
			}
		}
		return category;
	}
	
	private ToolDescriptor getRealTool(TargetRuntime rt, ToolDescriptor tool) {
		String fromPalette = tool.getFromPalette();
		String id = tool.getId();
		if (fromPalette!=null && id!=null) {
			for (TargetRuntime otherRt : TargetRuntime.getAllRuntimes()) {
				if (otherRt!=rt) {
					for (ToolPaletteDescriptor tp : otherRt.getToolPalettes()) {
						if ( fromPalette.equals(tp.getId())) {
							for (CategoryDescriptor c : tp.getCategories()) {
								for (ToolDescriptor t : c.getTools()) {
									if (id.equals(t.getId())) {
										return t;
									}
								}
							}
						}
					}
				}
			}
		}
		return tool;
	}
	
	private void createDefaultpalette() {
		createConnectors(palette);
		createTasksCompartments(palette);
		createGatewaysCompartments(palette);
		createEventsCompartments(palette);
		createEventDefinitionsCompartments(palette);
		createDataCompartments(palette);
		createOtherCompartments(palette);
		createCustomTasks(palette);
	}
	
	public List<IToolEntry> getTools() {
		List<IToolEntry> tools = new ArrayList<IToolEntry>();
		if (palette==null)
			getPalette();
		
		for (IPaletteCompartmentEntry ce : palette) {
			for (IToolEntry te : ce.getToolEntries()) {
				tools.add(te);
			}
		}
		return tools;
	}

	public IPaletteCompartmentEntry getCategory(IToolEntry tool) {
		if (palette==null)
			getPalette();
		
		for (IPaletteCompartmentEntry ce : palette) {
			for (IToolEntry te : ce.getToolEntries()) {
				if (te == tool)
					return ce;
			}
		}
		return null;
	}
	
	private IFeature getCreateFeature(ToolDescriptor tool) {
		if (tool.getToolParts().size()==1)
			return getCreateFeature(tool, null, null, tool.getToolParts().get(0));
		else {
			CompoundCreateFeature compoundFeature = null;
			for (ToolPart tp : tool.getToolParts()) {
				if (compoundFeature==null)
					compoundFeature = new CompoundCreateFeature(featureProvider,tool);
				getCreateFeature(tool, compoundFeature, null, tp);
			}
			return compoundFeature;
		}
	}
	
	private IFeature getCreateFeature(ToolDescriptor tool, CompoundCreateFeature root, CompoundCreateFeaturePart node, ToolPart toolPart) {
		IFeature parentFeature = null;
		String name = toolPart.getName();
		//System.out.println("namebbbbbbbbbb"+name);
		EClassifier eClass = targetRuntime.getModelDescriptor().getClassifier(name);
		if (eClass!=null) {
			parentFeature = featureProvider.getCreateFeatureForBusinessObject(eClass.getInstanceClass());
		}
		if (root!=null) {
			if (node!=null) {
				CompoundCreateFeaturePart n = node.addChild(parentFeature);
				if (toolPart.hasProperties()) {
					n.setProperties(toolPart.getProperties());
				}
			}
			else {
				node = root.addChild(parentFeature);
				if (toolPart.hasProperties()) {
					node.setProperties(toolPart.getProperties());
				}
			}
		}
		else if (toolPart.hasProperties()) {
			root = new CompoundCreateFeature(featureProvider, tool);
			node = root.addChild(parentFeature);
			node.setProperties(toolPart.getProperties());
			parentFeature = root;
		}
		
		for (ToolPart childToolPart : toolPart.getChildren()) {
			if (root==null) {
				root = new CompoundCreateFeature(featureProvider, tool);
				node = root.addChild(parentFeature);
				parentFeature = root;
			}
			getCreateFeature(tool, root, node, childToolPart);
		}
		
		return parentFeature;
	}
	
	private void createEventsCompartments(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(Messages.BPMNToolBehaviorProvider_Events_Drawer_Label, null);

		createEntries(FeatureMap.EVENTS, compartmentEntry);

		if (compartmentEntry.getToolEntries().size()>0)
			palette.add(compartmentEntry);
	}

	private void createOtherCompartments(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(Messages.BPMNToolBehaviorProvider_Other_Drawer_Label, null);
		compartmentEntry.setInitiallyOpen(false);

		createEntries(FeatureMap.OTHER, compartmentEntry);

		if (compartmentEntry.getToolEntries().size()>0)
			palette.add(compartmentEntry);
	}

	private void createDataCompartments(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(Messages.BPMNToolBehaviorProvider_Data_Items_Drawer_Label, null);
		compartmentEntry.setInitiallyOpen(false);

		createEntries(FeatureMap.DATA, compartmentEntry);

		if (compartmentEntry.getToolEntries().size()>0)
			palette.add(compartmentEntry);
	}

	private void createEventDefinitionsCompartments(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(Messages.BPMNToolBehaviorProvider_Event_Definitions_Drawer_Label, null);
		compartmentEntry.setInitiallyOpen(false);

		createEntries(FeatureMap.EVENT_DEFINITIONS, compartmentEntry);

		if (compartmentEntry.getToolEntries().size()>0)
			palette.add(compartmentEntry);
	}

	private void createGatewaysCompartments(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(Messages.BPMNToolBehaviorProvider_Gateways_Drawer_Label, null);

		createEntries(FeatureMap.GATEWAYS, compartmentEntry);

		if (compartmentEntry.getToolEntries().size()>0)
			palette.add(compartmentEntry);
	}

	private void createTasksCompartments(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(Messages.BPMNToolBehaviorProvider_Tasks_Drawer_Label, null);

		createEntries(FeatureMap.TASKS, compartmentEntry);

		if (compartmentEntry.getToolEntries().size()>0)
			palette.add(compartmentEntry);
	}

	private void createConnectors(List<IPaletteCompartmentEntry> palette) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(Messages.BPMNToolBehaviorProvider_Connectors_Drawer_Label, null);

		createEntries(FeatureMap.CONNECTORS, compartmentEntry);

		if (compartmentEntry.getToolEntries().size()>0)
			palette.add(compartmentEntry);
	}

	private void createEntries(List<Class> neededEntries, PaletteCompartmentEntry compartmentEntry) {
		for (Object o : neededEntries) {
			if (o instanceof Class) {
				createEntry((Class)o, compartmentEntry);
			}
		}
	}
	
	private boolean isEnabled(String className) {
		return modelEnablements.isEnabled(className);
	}
	
	private void createEntry(Class c, PaletteCompartmentEntry compartmentEntry) {
		if (isEnabled(c.getSimpleName())) {
			IFeature feature = featureProvider.getCreateFeatureForBusinessObject(c);
			if (feature instanceof ICreateFeature) {
				ICreateFeature cf = (ICreateFeature)feature;
				ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(cf.getCreateName(),
					cf.getCreateDescription(), cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
				compartmentEntry.addToolEntry(objectCreationToolEntry);
			}
			else if (feature instanceof ICreateConnectionFeature) {
				ICreateConnectionFeature cf = (ICreateConnectionFeature)feature;
				ConnectionCreationToolEntry connectionCreationToolEntry = new ConnectionCreationToolEntry(
						cf.getCreateName(), cf.getCreateDescription(), cf.getCreateImageId(),
						cf.getCreateLargeImageId());
				connectionCreationToolEntry.addCreateConnectionFeature(cf);
				compartmentEntry.addToolEntry(connectionCreationToolEntry);
			}
		}
	}
	
	private void createEntry(IFeature feature, PaletteCompartmentEntry compartmentEntry) {
		if (modelEnablements.isEnabled(feature) || feature instanceof CompoundCreateFeature) {
			IFeature targetFeature = feature;
			if (feature instanceof CompoundCreateFeature) {
				CompoundCreateFeature cf = (CompoundCreateFeature)feature;
				targetFeature = ((CompoundCreateFeaturePart)cf.getChildren().get(0)).getFeature();
			}
			if (targetFeature instanceof ICreateFeature) {
				ICreateFeature cf = (ICreateFeature)feature;
				ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(cf.getCreateName(),
					cf.getCreateDescription(), cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
				compartmentEntry.addToolEntry(objectCreationToolEntry);
			}
			else if (targetFeature instanceof ICreateConnectionFeature) {
				ICreateConnectionFeature cf = (ICreateConnectionFeature)feature;
				ConnectionCreationToolEntry connectionCreationToolEntry = new ConnectionCreationToolEntry(
						cf.getCreateName(), cf.getCreateDescription(), cf.getCreateImageId(),
						cf.getCreateLargeImageId());
				connectionCreationToolEntry.addCreateConnectionFeature(cf);
				compartmentEntry.addToolEntry(connectionCreationToolEntry);
			}
		}
	}

	private void createCustomTasks(List<IPaletteCompartmentEntry> ret) {

		PaletteCompartmentEntry compartmentEntry = null;
		BPMN2Editor editor = (BPMN2Editor) getDiagramTypeProvider().getDiagramEditor();
		TargetRuntime rt = editor.getTargetRuntime();
		
		try {
			for (IPaletteCompartmentEntry e : ret) {
				categories.put(e.getLabel(), (PaletteCompartmentEntry) e);
			}
			
			for (CustomTaskDescriptor tc : rt.getCustomTasks()) {
				CustomElementFeatureContainer container = (CustomElementFeatureContainer)tc.getFeatureContainer();
				if (!container.isAvailable(featureProvider))
					continue;

				IToolEntry toolEntry = null;
				String id = tc.getId();
				container.setId(id);
				featureProvider.addFeatureContainer(id, container);
				if (container instanceof CustomShapeFeatureContainer) {
					ICreateFeature cf = ((CustomShapeFeatureContainer)container).getCreateFeature(featureProvider);
					ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(tc.getName(),
							cf.getCreateDescription(), cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
					ObjectCreationToolEntry objectCreationToolEntry2 = new ObjectCreationToolEntry(tc.getName(),
							cf.getCreateDescription(), cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
					toolEntry = objectCreationToolEntry;
				}
				else if (container instanceof CustomConnectionFeatureContainer) {
					ICreateConnectionFeature cf = ((CustomConnectionFeatureContainer)container).getCreateConnectionFeature(featureProvider);
					ConnectionCreationToolEntry connectionCreationToolEntry = new ConnectionCreationToolEntry(
							cf.getCreateName(), cf.getCreateDescription(), cf.getCreateImageId(),
							cf.getCreateLargeImageId());
					connectionCreationToolEntry.addCreateConnectionFeature(cf);
					toolEntry = connectionCreationToolEntry;
				}
				
				String category = tc.getCategory();
				if (category==null || category.isEmpty())
					category = Messages.BPMNToolBehaviorProvider_Custom_Tasks_Drawer_Label;
				
				compartmentEntry = categories.get(category);
				if (compartmentEntry==null) {
					compartmentEntry = new PaletteCompartmentEntry(category, null);
					compartmentEntry.setInitiallyOpen(false);
					ret.add(compartmentEntry);
					categories.put(category, compartmentEntry);
				}
				
				compartmentEntry.addToolEntry(toolEntry);
			}
			
		
		} catch (Exception ex) {
			Activator.logError(ex);
		}
	}
	private static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	@Override
	public IFeatureChecker getFeatureChecker() {
		return new FeatureCheckerAdapter(false) {
			@Override
			public boolean allowAdd(IContext context) {
				return super.allowAdd(context);
			}

			@Override
			public boolean allowCreate() {
				return super.allowCreate();
			}
		};
	}

	@Override
	public GraphicsAlgorithm[] getClickArea(PictogramElement pe) {
		if (ActivitySelectionBehavior.canApplyTo(pe)) {
			return ActivitySelectionBehavior.getClickArea(pe);
		} else if (EventSelectionBehavior.canApplyTo(pe)) {
			return EventSelectionBehavior.getClickArea(pe);
		} else if (ChoreographySelectionBehavior.canApplyTo(pe)) {
			return ChoreographySelectionBehavior.getClickArea(pe);
		}
		else {
			if (pe instanceof ContainerShape) {
				BaseElement be = BusinessObjectUtil.getFirstBaseElement((ContainerShape)pe);
			}
		}
		return super.getClickArea(pe);
	}

	@Override
	public int getLineSelectionWidth(Polyline polyline) {
		PictogramElement pe = polyline.getPictogramElement();
		if (pe!=null && BusinessObjectUtil.getFirstBaseElement(pe) instanceof Group)
			return 20;
		return super.getLineSelectionWidth(polyline);
	}

	@Override
	public GraphicsAlgorithm getSelectionBorder(PictogramElement pe) {
		if (ActivitySelectionBehavior.canApplyTo(pe)) {
			return ActivitySelectionBehavior.getSelectionBorder(pe);
		} else if (EventSelectionBehavior.canApplyTo(pe)) {
			return EventSelectionBehavior.getSelectionBorder(pe);
		} else if (ChoreographySelectionBehavior.canApplyTo(pe)) {
			return ChoreographySelectionBehavior.getSelectionBorder(pe);
		}
		else if (pe instanceof ContainerShape) {
			if (((ContainerShape)pe).getChildren().size()>0) {
				GraphicsAlgorithm ga = ((ContainerShape)pe).getChildren().get(0).getGraphicsAlgorithm();
				if (!(ga instanceof AbstractText) && !(ga instanceof Polyline))
					return ga;
				ga = ((ContainerShape)pe).getGraphicsAlgorithm();
				if (ga.getGraphicsAlgorithmChildren().size()>0)
					return ga.getGraphicsAlgorithmChildren().get(0);
				return ga;
			}
		}
		else if (pe instanceof Shape) {
			return ((Shape)pe).getGraphicsAlgorithm();
		}
		return super.getSelectionBorder(pe);
	}

	public static Point getMouseLocation(IFeatureProvider fp) {
		DiagramBehavior db = (DiagramBehavior) fp.getDiagramTypeProvider().getDiagramBehavior();
		org.eclipse.draw2d.geometry.Point p = db.getMouseLocation();
		p = db.calculateRealMouseLocation(p);
		Point point = GraphicsUtil.createPoint(p.x, p.y);
		return point;
	}
	
	@Override
	public String getTitleToolTip()
	{
		return "working";}
	
	@Override
	public IContextButtonPadData getContextButtonPad(final IPictogramElementContext context) {
		IContextButtonPadData data = super.getContextButtonPad(context);
		PictogramElement pe = context.getPictogramElement();
		final IFeatureProvider fp = getFeatureProvider();

		if (pe instanceof Shape && FeatureSupport.isLabelShape((Shape)pe)) {
			// labels don't have a buttonpad
			setGenericContextButtons(data, pe, 0);
			return data;
		}

		if( pe.getGraphicsAlgorithm()!= null && pe.getGraphicsAlgorithm().getWidth() < 40 ){
		    ILocation origin = getAbsoluteLocation(pe.getGraphicsAlgorithm());
		    data.getPadLocation().setRectangle(origin.getX(), origin.getY(), 40, 40);
		}
		
		// 1. set the generic context buttons
		// Participant bands can only be removed from the choreograpy task
		int genericButtons = CONTEXT_BUTTON_DELETE;
		if (ChoreographyUtil.isChoreographyParticipantBand(pe)) {
			genericButtons |= CONTEXT_BUTTON_REMOVE;
		}
		setGenericContextButtons(data, pe, genericButtons);

		// 2. set the expand & collapse buttons
		CustomContext cc = new CustomContext(new PictogramElement[] { pe });
		for (ICustomFeature cf : fp.getCustomFeatures(cc)) {
			if (cf.canExecute(cc)) {
				ContextButtonEntry button = new ContextButtonEntry(cf, cc);
				button.setText(cf.getName()); //$NON-NLS-1$
				button.setIconId(cf.getImageId());
				button.setDescription(cf.getDescription());
			
				data.getDomainSpecificContextButtons().add(button);
			}
		}

		// 3. add one domain specific context-button, which offers all
		// available connection-features as drag&drop features...

		// 3.a. create new CreateConnectionContext
		CreateConnectionContext ccc = new CreateConnectionContext();
		ccc.setSourcePictogramElement(pe);
		Anchor anchor = null;
		if (pe instanceof Anchor) {
			anchor = (Anchor) pe;
		} else if (pe instanceof AnchorContainer) {
			// assume, that our shapes always have chopbox anchors
			anchor = Graphiti.getPeService().getChopboxAnchor((AnchorContainer) pe);
		}
		ccc.setSourceAnchor(anchor);

		// 3.b. create context button and add "Create Connections" feature
		ContextButtonEntry button = new ContextButtonEntry(null, context);
		button.setText("Create Connection"); //$NON-NLS-1$
		String description = null;
		ArrayList<String> names = new ArrayList<String>();
		button.setIconId(ImageProvider.IMG_16_SEQUENCE_FLOW);
		for (IToolEntry te : getTools()) {
			if (te instanceof ConnectionCreationToolEntry) {
				ConnectionCreationToolEntry cte = (ConnectionCreationToolEntry)te;
				for (IFeature f : cte.getCreateConnectionFeatures()) {
					ICreateConnectionFeature ccf = (ICreateConnectionFeature)f;
					if (ccf.isAvailable(ccc) && ccf.canStartConnection(ccc)) {
						button.addDragAndDropFeature(ccf);
						
						
						names.add(ccf.getCreateName());
					}
				}
				
			}
		}
		
		// 3.c. build a reasonable description for the context button action 
		for (int i=0; i<names.size(); ++i) {
			if (description==null)
				description = Messages.BPMNToolBehaviorProvider_Click_Drag_Prompt;
			description += names.get(i);
			if (i+2 == names.size())
				description += Messages.BPMNToolBehaviorProvider_Click_Drag_Prompt_Last_Separator;
			else if (i+1 < names.size())
				description += Messages.BPMNToolBehaviorProvider_Click_Drag_Prompt_Separator;
		}
		button.setDescription(description);

		// 3.d. add context button, button only if it contains at least one feature
		if (button.getDragAndDropFeatures().size() > 0) {
			data.getDomainSpecificContextButtons().add(button);
			
			
			
			
			
			
			
			
			
			
			
				
		/*DefaultDetailComposite parent=new DefaultDetailComposite(section);
		
			if ( parent.getShell().toString()!=null)
			{SchemaSelectionDialog dialog = new SchemaSelectionDialog(parent.getShell(), businessObject); 
			if (dialog.open() == Window.OK) System.out.println("ok&é");}*/
				
			
		}
		
		boutonentry=true; menuentry=false;
		
			CustomContext cc1 = new CustomContext(new PictogramElement[] { pe });
		CreateNewVersion cv =new CreateNewVersion(fp);
		ContextButtonEntry button1 = new ContextButtonEntry(cv, cc1);
		//cv.execute(cc);
	
		button1.setText("Handle versions");
		System.out.println("ImageProvider.IMG_16_MESSAGE"+ImageProvider.IMG_16_VERSIONS);
		button1.setIconId(ImageProvider.IMG_16_VERSIONS);
		button1.setDescription("Handle versions");
		data.getDomainSpecificContextButtons().add(button1);
		
		CustomContext cc2 = new CustomContext(new PictogramElement[] { pe });
		ShowResourceRole sr =new ShowResourceRole(fp);
		ContextButtonEntry button2 = new ContextButtonEntry(sr, cc2);
		//cv.execute(cc);
	
		button2.setText("ResourceRole Versions");
		
		button2.setIconId(ImageProvider.IMG_16_USER_TASK);
		button2.setDescription("ResourceRole Versions");
		data.getDomainSpecificContextButtons().add(button2);
		
		/*Shell shell = new Sh
		 * ell();
		VersionDialog1 dialog =new VersionDialog1(shell);
		dialog.create();
		
		//dialog.createDialogArea(shell);
		dialog.setBlockOnOpen(false);
		if (dialog.open() == Window.OK)
			changesDone = true;
		else
			changesDone = false;*/
	
		//entre=true;
	/*	EObject businessObject = BusinessObjectUtil.getBusinessObjectForPictogramElement(pe);
		VersionDragDialog dialog =
				new VersionDragDialog(editor, businessObject);
		dialog.open();
		/*if (dialog.open() == Window.OK)
			changesDone = dialog.hasDoneChanges();
		else
			changesDone = false;*/
		//VersionDragDialog dialog =
			//	new VersionDragDialog(editor, businessObject);
		//ObjectDragVersion o=new ObjectDragVersion();
	//	if (dialog.open() == Window.OK)
		//dialog.createDialogContent(parent)	;
		//dialog.open();
			//if ( dialog.open() == Window.OK)
				
		//	 {dialog.create();
		
		

		return data;
	}
public void opendialog(IContext c)
{
Shell shell = new Shell();

VersionDialog dialog =new VersionDialog(shell);
dialog.createDialogArea(shell);
dialog.setBlockOnOpen(false);
dialog.open();


}
	@Override
	public void postExecute(IExecutionInfo executionInfo) {
		BPMN2Editor editor = (BPMN2Editor)getDiagramTypeProvider().getDiagramEditor();
		for (IFeatureAndContext fc : executionInfo.getExecutionList()) {
			IContext context = fc.getContext();
			IFeature feature = fc.getFeature();
			if (context instanceof AddContext) {
				if (feature instanceof IBpmn2AddFeature) {
					((IBpmn2AddFeature)feature).postExecute(executionInfo);
				}
			}
			else if (context instanceof CreateContext) {
				if (feature instanceof IBpmn2CreateFeature) {
					((IBpmn2CreateFeature)feature).postExecute(executionInfo);
				}
			}
			else if (context instanceof UpdateContext) {
				PictogramElement pe = ((UpdateContext)context).getPictogramElement();
				if (!(pe instanceof Connection)) {
					editor.setPictogramElementForSelection(pe);
				}
				editor.refresh();
			}
			else if (context instanceof MoveShapeContext) {
				PictogramElement pe = ((MoveShapeContext)context).getPictogramElement();
				editor.setPictogramElementForSelection(pe);
				editor.refresh();
			}
			else if (context instanceof AddBendpointContext) {
				PictogramElement pe = ((AddBendpointContext)context).getConnection();
				editor.setPictogramElementForSelection(pe);
				editor.refresh();
			}
			else if (context instanceof MoveBendpointContext) {
				PictogramElement pe = ((MoveBendpointContext)context).getConnection();
				editor.setPictogramElementForSelection(pe);
				editor.refresh();
			}
		}
	}

	@Override
	public ICustomFeature getDoubleClickFeature(IDoubleClickContext context) {
		ICustomFeature[] cf = getFeatureProvider().getCustomFeatures(context);
		for (int i = 0; i < cf.length; i++) {
			ICustomFeature iCustomFeature = cf[i];
			if (iCustomFeature instanceof ShowPropertiesFeature &&
					iCustomFeature.canExecute(context)) {
				return iCustomFeature;
			}
		}
		// temp debugging stuff to dump connection routing info
		for (PictogramElement pe : context.getPictogramElements()) {
			String id = Graphiti.getPeService().getPropertyValue(pe, "ROUTING_NET_CONNECTION"); //$NON-NLS-1$
			if (pe instanceof FreeFormConnection) {
				System.out.println("id="+id); //$NON-NLS-1$
				FreeFormConnection c = (FreeFormConnection)pe;
				int i=0;
				ILocation loc = Graphiti.getPeService().getLocationRelativeToDiagram(c.getStart());
				System.out.println("0: "+loc.getX()+","+loc.getY()); //$NON-NLS-1$ //$NON-NLS-2$
				for (Point p : c.getBendpoints()) {
					System.out.println(++i+": "+p.getX()+","+p.getY()); //$NON-NLS-1$ //$NON-NLS-2$
				}
				loc = Graphiti.getPeService().getLocationRelativeToDiagram(c.getEnd());
				System.out.println(++i+": "+loc.getX()+","+loc.getY()); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return null;
	}

	@Override
	public GraphicsAlgorithm getChopboxAnchorArea(PictogramElement pe) {
		return super.getChopboxAnchorArea(pe);
	}
	
	 @Override
	    public String getToolTip(GraphicsAlgorithm ga) {
	        EObject bo = (EObject) getFeatureProvider().getBusinessObjectForPictogramElement(ga.getPictogramElement());
	        if (bo != null && bo instanceof BPMNDiagram ) {
	            
	           
	            return "Stable";
	        }
	        else 
	        	 if (bo != null && bo instanceof Task ) {
	 	            
	  	           
	 	            return "Stable";
	 	        }
	        return super.getTitleToolTip();
	    }

    @Override
    public IDecorator[] getDecorators(PictogramElement pe) {
        List<IDecorator> decorators = new ArrayList<IDecorator>();

        // labels should not be decorated
		String labelProperty = Graphiti.getPeService().getPropertyValue(pe, GraphicsUtil.LABEL_PROPERTY);
		//System.out.println("labelProperty"+labelProperty);
		if (!Boolean.parseBoolean(labelProperty)) {
	        IFeatureProvider featureProvider = getFeatureProvider();
	        Object bo = featureProvider.getBusinessObjectForPictogramElement(pe);
	        if (bo!=null) {
		        ValidationStatusAdapter statusAdapter = (ValidationStatusAdapter) EcoreUtil.getRegisteredAdapter((EObject) bo,
		                ValidationStatusAdapter.class);
		        if (statusAdapter != null) {
		            final IImageDecorator decorator;
		            final IStatus status = statusAdapter.getValidationStatus();
		            switch (status.getSeverity()) {
		            case IStatus.INFO:
		                decorator = new ImageDecorator(IPlatformImageConstants.IMG_ECLIPSE_INFORMATION_TSK);
		                break;
		            case IStatus.WARNING:
		                decorator = new ImageDecorator(IPlatformImageConstants.IMG_ECLIPSE_WARNING_TSK);
		                break;
		            case IStatus.ERROR:
		                decorator = new ImageDecorator(IPlatformImageConstants.IMG_ECLIPSE_ERROR_TSK);
		                break;
		            default:
		                decorator = null;
		                break;
		            }
		            if (decorator != null) {
		                GraphicsAlgorithm ga = getSelectionBorder(pe);
		                if (ga == null) {
		                    ga = pe.getGraphicsAlgorithm();
		                }
		                decorator.setX(-5);
		                decorator.setY(-5);
		                decorator.setMessage(status.getMessage());
		                decorators.add(decorator);
		            }
		        }
	        }
		}
		
        return decorators.toArray(new IDecorator[decorators.size()]);
    }

	@Override
	public ICustomFeature getCommandFeature(CustomContext context, String hint) {
		for (ICustomFeature cf : getFeatureProvider().getCustomFeatures(context)) {
			if (cf instanceof ICustomCommandFeature && ((ICustomCommandFeature)cf).isAvailable(hint)) {
				context.putProperty(ICustomCommandFeature.COMMAND_HINT, hint);
				return cf;
			}
		}
		return super.getCommandFeature(context, hint);
	}

	public ILocationInfo getLocationInfo(PictogramElement pe, ILocationInfo locationInfo) {
		if (locationInfo==null) {
			if (pe instanceof ContainerShape) {
				ContainerShape shape = (ContainerShape) pe;
				locationInfo = new LocationInfo(shape, shape.getGraphicsAlgorithm());
			}
		}
		return locationInfo;
	}
	
	
	 @Override
	    public IContextMenuEntry[] getContextMenu(ICustomContext context) {
	        // create a sub-menu for all custom features
		 getPictogramElement();
		 IFeatureProvider featureProvider = getFeatureProvider();
		 PictogramElement[] pes = context.getPictogramElements();
		 IContextMenuEntry ret[] = null;
		 Object  bo = featureProvider.getBusinessObjectForPictogramElement(pes[0]);
		// id_v=editor.getCurrentInput().getName();
		 id_v=editor.bpmnDiagram.getName();
			if (id_v.startsWith("VP"))
			{
			name_process=SelectName(id_v);
			process_state=Selectstate(id_v);}
			else
			{
				name_process=SelectNameC(id_v);
				process_state=SelectstateC(id_v);}
			
		/*TransactionalEditingDomain domain =getDiagramEditor().getEditingDomain();
		boolean success = ModelUtil.setValue(domain, object, feature, result);*/
		//System.out.println(bo.getClass().);
        if (bo instanceof Task  ) {
			 boutonentry=false; menuentry=true;
			 id_va=((Task) bo).getId();
			 state=SelectActivitystate(id_va);
			 activity_state=state;
			 trouve=Trouve_idva(id_va);
			 name_activity=SelectActivityName(id_va);
			 System.out.println("name activity"+name_activity);
			  ContextMenuEntry subMenu0 = new ContextMenuEntry(null, context);
		        subMenu0.setText("Handle versions");
		        subMenu0.setSubmenu(true);
			final IFeatureProvider fp = getFeatureProvider();
			UpdateVersionActivity ua =new UpdateVersionActivity(fp);
	        // display sub-menu hierarchical or flat
		  ContextMenuEntry subMenu = new ContextMenuEntry(ua, context);
	        subMenu.setText("Update");
	        DervieVersionActivity da= new DervieVersionActivity(fp);
	        ContextMenuEntry subMenu1 = new ContextMenuEntry(da, context);
	        subMenu1.setText("Derive");
	        ValidateVersionActivity va =new ValidateVersionActivity(fp);
	        ContextMenuEntry subMenu2 = new ContextMenuEntry(va, context);
	        subMenu2.setText("Validate");
	        DefineActivityInformation dai= new DefineActivityInformation(fp);
	        ContextMenuEntry subMenu3 = new ContextMenuEntry(dai, context);
	        subMenu3.setText("Define Information");
	        DefineActivityRole dar= new DefineActivityRole(fp);
	        ContextMenuEntry subMenu4 = new ContextMenuEntry(dar, context);
	        subMenu4.setText("Define Role");
	        subMenu0.add(subMenu);
	        subMenu0.add(subMenu1);
	        subMenu0.add(subMenu2);
	       // subMenu.add(subMenu3);
	        //subMenu.add(subMenu4);
	        ret = new IContextMenuEntry[] { subMenu0 };
	       
         }
        else
        	 if (bo instanceof Participant  ) {
        		Participant p= (Participant) bo;
        		 id_v=p.getProcessRef().getId();
        		 state=Selectstate(p.getProcessRef().getId());
        			//	process_state=state;
        				name_process=SelectName(p.getProcessRef().getId());
        				System.out.println("31-05-2015 id_v"+ p.getProcessRef().getId()+ " name "+name_process+ " state "+state);
        		    	 boutonentry=false; menuentry=true;
        				  ContextMenuEntry subMenu0 = new ContextMenuEntry(null, context);
        			        subMenu0.setText("Handle versions");
        			        subMenu0.setSubmenu(true);
        			       
        				final IFeatureProvider fp = getFeatureProvider();
        				
        				UpdateVersionProcessParticipant up=new UpdateVersionProcessParticipant(fp);
        				
        		        // display sub-menu hierarchical or flat
        			  ContextMenuEntry subMenu = new ContextMenuEntry(up, context);
        		        subMenu.setText("Update");
        		        DeriveVersionProcessParticipant dp=new DeriveVersionProcessParticipant(fp);
        		        subMenu0.add(subMenu);
        		        ContextMenuEntry subMenu1 = new ContextMenuEntry(dp, context);
        		        subMenu1.setText("Derive");
        		        subMenu0.add(subMenu1);
        		        ValidateVersionProcessParticipant vp =new ValidateVersionProcessParticipant(fp);
        		        ContextMenuEntry subMenu2 = new ContextMenuEntry(vp, context);
        		        subMenu2.setText("Validate");
        		        subMenu0.add(subMenu2);
        		        
        		      //  subMenu.add(subMenu4);
        		         ret = new IContextMenuEntry[] { subMenu0 };
    	       
             }
         else 
	     if (bo instanceof BPMNDiagram)  
	     {int j=0;
	   
	// System.out.println("23-05-2015"+((BPMNDiagram) bo).getId()+ " "+  ((BPMNDiagram) bo).getName());
	 //  if (((BPMNDiagram) bo).getId().startsWith("VP"))
	    
	   		System.out.println("this.editor.getModelFile().getName()"+  editor.getCurrentInput().getName());
				//id_v=editor.getCurrentInput().getName();
	   		id_v=editor.bpmnDiagram.getName();
	   		System.out.println("15-01-2016"+editor.bpmnDiagram.getName());
			if (id_v.startsWith("VP"))
			// this.editor.getBpmnDiagram().setName("pro");
			{	 
	
			 
		//	j=id_v.indexOf(".");
			//id_v=id_v.substring(0,j);
			state=Selectstate(id_v);
		//	process_state=state;
			name_process=SelectName(id_v);
			System.out.println(" id_v"+ id_v+ " name "+name_process+ " state "+state);
	    	 boutonentry=false; menuentry=true;
			  ContextMenuEntry subMenu0 = new ContextMenuEntry(null, context);
		        subMenu0.setText("Handle versions");
		        subMenu0.setSubmenu(true);
		       
			final IFeatureProvider fp = getFeatureProvider();
			
			UpdateVersionProcess up=new UpdateVersionProcess(fp);
			
	        // display sub-menu hierarchical or flat
		  ContextMenuEntry subMenu = new ContextMenuEntry(up, context);
	        subMenu.setText("Update");
	        DeriveVersionProcess dp=new DeriveVersionProcess(fp);
	        subMenu0.add(subMenu);
	        ContextMenuEntry subMenu1 = new ContextMenuEntry(dp, context);
	        subMenu1.setText("Derive");
	        subMenu0.add(subMenu1);
	        ValidateVersionProcess vp =new ValidateVersionProcess(fp);
	        ContextMenuEntry subMenu2 = new ContextMenuEntry(vp, context);
	        subMenu2.setText("Validate");
	        subMenu0.add(subMenu2);
	        DefineProcessActivities dpa =new DefineProcessActivities(fp);
	        ContextMenuEntry subMenu3 = new ContextMenuEntry(dpa, context);
	        subMenu3.setText("Define Activities");
	       // subMenu.add(subMenu3);
	        DefineProcessEvents dpe =new DefineProcessEvents(fp);
	        ContextMenuEntry subMenu4 = new ContextMenuEntry(dpe, context);
	        subMenu4.setText("Define Events");
	      //  subMenu.add(subMenu4);
	         ret = new IContextMenuEntry[] { subMenu0 };
			}
			else
				if (id_v.startsWith("VC"))
					// this.editor.getBpmnDiagram().setName("pro");
				{	 
		
				 
			//	j=id_v.indexOf(".");
				//id_v=id_v.substring(0,j);
				state=SelectstateC(id_v);
			//	process_state=state;
				name_process=SelectNameC(id_v);
				System.out.println(" id_v"+ id_v+ " name "+name_process+ " state "+state);
		    	 boutonentry=false; menuentry=true;
				  ContextMenuEntry subMenu0 = new ContextMenuEntry(null, context);
			        subMenu0.setText("Handle versions");
			        subMenu0.setSubmenu(true);
			       
				final IFeatureProvider fp = getFeatureProvider();
				
				UpdateVersionCollaboration up=new UpdateVersionCollaboration(fp);
				
		      //   display sub-menu hierarchical or flat
			  ContextMenuEntry subMenu = new ContextMenuEntry(up, context);
		        subMenu.setText("Update");
		        DeriveVersionCollaboration dp=new DeriveVersionCollaboration(fp);
		        subMenu0.add(subMenu);
		        ContextMenuEntry subMenu1 = new ContextMenuEntry(dp, context);
		        subMenu1.setText("Derive");
		        subMenu0.add(subMenu1);
		        ValidateVersionCollaboration vp =new ValidateVersionCollaboration(fp);
		        ContextMenuEntry subMenu2 = new ContextMenuEntry(vp, context);
		        subMenu2.setText("Validate");
		        subMenu0.add(subMenu2);
		       
		        //subMenu.add(subMenu4);
		         ret = new IContextMenuEntry[] { subMenu0 };
				}

	     }
	     
	        return ret;
	 
	       
	 }
	  
	 public String Selectstate(String id_v)
		{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		int i=0;
		 if (driver.isInitialized()==false)
		driver.init();  
	    
	  XhiveSessionIf session = driver.createSession("xqapi-test");  
	  session.connect("Administrator", "imen", "vbpmn");  
	  session.begin();  
	  String s2="";
	  try {  
	    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	    // (1)
	    int j=0;
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Processes.xml')/Processes/Process for $i in $o/versions/version where $i/id_v='"+id_v+"' return $i/state");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	   System.out.println(j+"s2"+s2);
	 	   j++;
	 	   }
	    s2=s2.substring(7);
	    j=s2.indexOf("<");
	   		 s2=s2.substring(0,j);
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    
		return s2;
			
			
		}
	 public String SelectstateC(String id_v)
		{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		int i=0;
		 if (driver.isInitialized()==false)
		driver.init();  
	    
	  XhiveSessionIf session = driver.createSession("xqapi-test");  
	  session.connect("Administrator", "imen", "vbpmn");  
	  session.begin();  
	  String s2="";
	  try {  
	    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	    // (1)
	    int j=0;
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Collaboration.xml')/Collaborations/Collaboration for $i in $o/versions/version where $i/id_v='"+id_v+"' return $i/state");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	   System.out.println(j+"s2"+s2);
	 	   j++;
	 	   }
	    s2=s2.substring(7);
	    j=s2.indexOf("<");
	   		 s2=s2.substring(0,j);
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    
		return s2;
			
			
		}
	 public String SelectActivitystate(String id_v)
		{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		int i=0;
		 if (driver.isInitialized()==false)
		driver.init();  
	    
	  XhiveSessionIf session = driver.createSession("xqapi-test");  
	  session.connect("Administrator", "imen", "vbpmn");  
	  session.begin();  
	  String s2="";
	  try {  
	    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	    // (1)
	    int j=0;
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version where $i/id_v='"+id_v+"' return $i/state");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	 //  System.out.println(j+"s2"+s2);
	 	  s2=s2.substring(7);
		    j=s2.indexOf("<");
		   		 s2=s2.substring(0,j);
	 	   j++;
	 	   }
	   
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    if (s2.compareTo("")==0)
	    	return "Working";
	    else
		return s2;
			
			
		}
	 public String SelectName(String id_v)
		{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		int i=0;
		 if (driver.isInitialized()==false)
		driver.init();  
	    
	  XhiveSessionIf session = driver.createSession("xqapi-test");  
	  session.connect("Administrator", "imen", "vbpmn");  
	  session.begin();  
	  String s2="";
	  try {  
	    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	    // (1)
	    int j=0;
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Processes.xml')/Processes/Process where $o/versions/version/id_v='"+id_v+"' return $o/name");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	   
	 	   }
	    s2=s2.substring(6);
	    j=s2.indexOf("<");
	   		 s2=s2.substring(0,j);
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    
		return s2;
			
			
		}
	 public String SelectNameC(String id_v)
		{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		int i=0;
		 if (driver.isInitialized()==false)
		driver.init();  
	    
	  XhiveSessionIf session = driver.createSession("xqapi-test");  
	  session.connect("Administrator", "imen", "vbpmn");  
	  session.begin();  
	  String s2="";
	  try {  
	    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	    // (1)
	    int j=0;
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Collaboration.xml')/Collaborations/Collaboration where $o/versions/version/id_v='"+id_v+"' return $o/name");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	   
	 	   }
	    s2=s2.substring(6);
	    j=s2.indexOf("<");
	   		 s2=s2.substring(0,j);
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    
		return s2;
			
			
		}
	 public String SelectActivityName(String id_v)
		{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		int i=0;
		 if (driver.isInitialized()==false)
		driver.init();  
	    
	  XhiveSessionIf session = driver.createSession("xqapi-test");  
	  session.connect("Administrator", "imen", "vbpmn");  
	  session.begin();  
	  String s2="";
	  try {  
	    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	    // (1)
	    int j=0;
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity where $o/versions/version/id_v='"+id_v+"' return $o/name");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	  s2=s2.substring(6);
		    j=s2.indexOf("<");
		   		 s2=s2.substring(0,j);
	 	   }
	   
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    
		return s2;
			
			
		}
	 
	 public void getPictogramElement()
	 { IFeatureProvider featureProvider = getFeatureProvider();
	 Object []  b = null;

	 PictogramElement[] pes = editor.getSelectedPictogramElements();
	 
	 
	
		//   b = featureProvider.getBusinessObjectForPictogramElement(pes[0]);
			//.out.println("this.currentInput.getPersistable().toString()"+b.toString());
			// Graphiti.getLinkService().getPictogramElements((Diagram) editor.bpmnDiagram, (EObject) b);
		b=	 Graphiti.getPeService().getAllContainedPictogramElements(pes[0]).toArray();

		 
	 }
	 
		private boolean Trouve_idva(String idva)
	  	{boolean trouve=false;
	  	XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
	  	int i=0;
	  	 if (driver.isInitialized()==false)
	  	driver.init();  
	      
	    XhiveSessionIf session = driver.createSession("xqapi-test");  
	    session.connect("Administrator", "imen", "vbpmn");  
	    session.begin();  
	    String s2="";
	    try {  
	      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	      // (1)
	      int j=0;
	      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity where $o/versions/version/id_v='"+idva+"' return $o/name");
	    //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	      while(result.hasNext()) {  
	    	  s2=result.next().toString();
	    	  trouve=true;
	   	   }
	     
	     		
	      session.commit();  
	  		    } finally {  
	  		      session.rollback();  
	  		    } 
	      
	  	return trouve;
	  		
	  		
	  	}

	
}