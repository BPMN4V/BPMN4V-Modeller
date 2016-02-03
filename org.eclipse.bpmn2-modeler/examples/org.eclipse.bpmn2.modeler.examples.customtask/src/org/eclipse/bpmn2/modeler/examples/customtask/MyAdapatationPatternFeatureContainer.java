package org.eclipse.bpmn2.modeler.examples.customtask;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.di.DIImport;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.core.features.BaseElementConnectionFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.BaseElementFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.ContextConstants;
import org.eclipse.bpmn2.modeler.core.features.IFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.IShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.Messages;
import org.eclipse.bpmn2.modeler.core.features.activity.task.ICustomElementFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskImageProvider.IconSize;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.examples.customtask.CustomTemporalDependencyFeatureContainer.TemporalDependencyFeatureContainer;
//import org.eclipse.bpmn2.modeler.examples.customtask.MyModel.AdaptationPatternCase1;
import org.eclipse.bpmn2.modeler.examples.customtask.MyModel.MyModelFactory;
import org.eclipse.bpmn2.modeler.examples.customtask.MyModel.MyModelPackage;
import org.eclipse.bpmn2.modeler.examples.customtask.MyModel.TemporalDependency;
//import org.eclipse.bpmn2.modeler.examples.customtask.MyModel.MyModelPackage;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.UpdateActivityDialog;
import org.eclipse.bpmn2.modeler.ui.diagram.UpdateProcessDialog;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomShapeFeatureContainer.CreateCustomShapeFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.MyCustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.flow.MessageFlowFeatureContainer.AddMessageFlowFeature;
import org.eclipse.bpmn2.modeler.ui.features.flow.MessageFlowFeatureContainer.CreateMessageFlowFeature;
import org.eclipse.bpmn2.modeler.ui.wizards.BPMN2DiagramWizard;
import org.eclipse.bpmn2.modeler.ui.wizards.BPMN2DiagramWizardPage2;
import org.eclipse.bpmn2.modeler.ui.wizards.NewCollaborationInterface;
import org.eclipse.bpmn2.modeler.ui.wizards.UpdateMessageFlowNumber;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardAddPattern;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardSource;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardTarget;
import org.eclipse.bpmn2.presentation.Bpmn2Editor;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.AreaContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class MyAdapatationPatternFeatureContainer extends
		CustomShapeFeatureContainer {

	private final static String TYPE_VALUE = "AddInteractioncase#1";
	private final static String CUSTOM_TASK_ID = "org.eclipse.bpmn2.modeler.examples.customtask.MyPattern1";
	public static Boolean p1 = false;
	public static Boolean test=false;

	public MyAdapatationPatternFeatureContainer() {
		// TODO Auto-generated constructor stub
	}

	
	
	
	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new MyCreateCustomShapeFeature(fp);

	}

	@Override
	public String getId(EObject object) {

		EStructuralFeature f = ModelUtil.getAnyAttribute(object, "type");
		if (f != null) {
			Object id = object.eGet(f);
			if (TYPE_VALUE.equals(id))
				return CUSTOM_TASK_ID;
		}

		return null;
	}
	
	
	
	
	public class MyCreateCustomShapeFeature extends CreateCustomShapeFeature {
		public MyCreateCustomShapeFeature(IFeatureProvider fp) {
			super(fp);
			// TODO Auto-generated constructor stub
		}

		private Shell getShell() {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
		}

		@Override
		public Object[] create(ICreateContext context) {
			
			UpdateMessageFlowNumber ump = new UpdateMessageFlowNumber();
 Process p1 = null,p2 = null;
			String stateC = ump.select_version_collaboration_state(BPMN2Editor.currentInput.getName());
			if (stateC.compareTo("Stable")==0){
				//JOptionPane.showMessageDialog( null,"The version of collaboration is Stable. Please derive Derive working version", JOptionPane.ERROR_MESSAGE);
				JOptionPane.showMessageDialog(null, "The version of collaboration  is Stable.Derive stable version before use pattern", "Derive stable version before use pattern", JOptionPane.ERROR_MESSAGE);
				return null;
				
			}
			else
			{
				
				WizardAddPattern b = new WizardAddPattern();
				WizardSource.context = (CreateContext) context;
				WizardSource.diagram = getDiagram();
				WizardTarget.context = (CreateContext) context;
				WizardTarget.diagram = getDiagram();
				WizardDialog dialog = new WizardDialog(getShell(), b);
				dialog.open();
				boolean cb = b.performCancel();

				if (b.a == true) {
				
					UpdateMessageFlowNumber st = new UpdateMessageFlowNumber();
					String stateP = st.selectEtatProcess(WizardTarget.idvp);
					if (stateP.compareTo("Stable") == 0){
					
					test=true;
					
					UpdateMessageFlowNumber upm = new UpdateMessageFlowNumber();
					String namePS = upm.selectNameProcess(WizardSource.processStableS);
					 String idS;
				  idS=  ump.SelectLastVProcess(namePS);
					
					String namePT = upm.selectNameProcess(WizardTarget.idvp);

				    String idT;
				   idT=  ump.SelectLastVProcess(namePT);
				   upm.DeriveProcessPattern(WizardSource.processStableS, namePS);
					upm.DeriveProcessPattern(WizardTarget.idvp, namePT);

					  try {
							ModelHandler handler = ModelHandler
									.getInstance(getDiagram());

							 p2 =	 (Process) handler.findElement(WizardSource.processStableS);
                //            BaseElement p1 = handler.findElement(WizardAddPattern.processStableT);
							
							ModelHandler handler1 = ModelHandler
									.getInstance(getDiagram());
                     //       BaseElement p1 = handler1.findElement(WizardTarget.idvp);
                             p1 = (Process)handler1.findElement(WizardTarget.idvp);

                           
                         
					  p2.setId(idS);
					  p1.setId(idT);
					

						} catch (IOException e) {
							Activator.logError(e);
						}
					}
					
					if(test==true)
					{
					
					b.a = false;
					cb = false;
					PictogramElement pe, pe2;

					Anchor s, t;

					Task element = null;
					CreateContext context2 = (CreateContext) context;

					element = (Task) createBusinessObject(context);

					UpdateMessageFlowNumber up = new UpdateMessageFlowNumber();

					if ((WizardSource.newInteraction) == true) {
						element.setName(WizardSource.newActivityNameSource);
						element.setId("VA" + WizardSource.ids + "-1");

						// int sq= element.getStartQuantity();
						// sq=sq+1;
						// element.setStartQuantity(sq);
						// String g=up.selectTaskNumMsgFlow(idVersion);
						String idVersion = element.getId();
						String nameVersion = element.getName();
						// up.InstertActivity(idVersion,
						// nameVersion,WizardSource.idprocess, "");
						up.InstertActivity(idVersion, nameVersion,
								BPMN2Editor.currentInput.getName(), "");

						// up.InsertActivitymessage(idVersion, "1");
						// System.out.println("hello"+up.selectTaskNumMsgFlow(id));

					} else {
						element.setId(WizardSource.existingActivityNameSource);
						element.setName(WizardSource.activity_name);
						String idVersion = element.getId();
						String nameVersion = element.getName();

						// nb=up.selectTaskNumMsgFlow(idVersion,BPMN2Editor.currentInput.getName());

						// int j = 0;
						// j = Integer.parseInt(nb);
						// j++;
						// String n = Integer.toString(j);
						// up.InsertActivitymessage(idVersion,
						// BPMN2Editor.currentInput.getName(),n);

						String nb = up.selectTaskNumMsgFlow(idVersion,
								BPMN2Editor.currentInput.getName());
						if (nb != "") {
							int j = 0;
							j = Integer.parseInt(nb);
							j++;
							String n = Integer.toString(j);
							// String bt=
							// up.selectIdVersionFromidvp(nameVersion,BPMN2Editor.currentInput.getName());
							up.InsertActivitymessage(n, idVersion,
									BPMN2Editor.currentInput.getName());

						} else {
							up.UpdateNbMsg(BPMN2Editor.currentInput.getName(),
									idVersion);
						}
					}
					//String Process_1 = WizardSource.idprocess;
					String Process_1 = p2.getId();
					System.out.println("09-11-2015hhh"+Process_1);
					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());

						handler.addFlowElement(handler.findElement(Process_1),
								element);

					} catch (IOException e) {
						Activator.logError(e);
					}
					pe = null;
					pe = addGraphicalRepresentation(context, element);

					MessageFlow m = null;
					EObject o = null;
					Task element1 = null;
					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());

						// chercher le participant2 dans le diagramme
						o = handler.findElement(WizardTarget.participant.getId());

					} catch (IOException e) {
						Activator.logError(e);
					}
					// chercher la représentation graphique du participant2
					List<PictogramElement> p = GraphitiUi.getLinkService()
							.getPictogramElements(getDiagram(), o);
					// (ContainerShape) p.get(0) c'est la figure qui représente le
					// participant 2
					context2.setTargetContainer((ContainerShape) p.get(0));
					// context2.setY( p.get(0).getGraphicsAlgorithm().getY());
					element1 = (Task) createBusinessObject(context2);
					if ((WizardTarget.newInteraction) == true) {
						element1.setName(WizardTarget.activityNameTarget);
						element1.setId("VA" + WizardTarget.idt + "-1");
						String idVersionT = element1.getId();
						String nameVersionT = element1.getName();
						// up.InstertActivity(idVersionT,
						// nameVersionT,WizardTarget.idvp, "");
						up.InstertActivity(idVersionT, nameVersionT,
								BPMN2Editor.currentInput.getName(), "");

						// up.InstertActivity(idVersionT, nameVersionT,"", "");

						// int sqt= element1.getStartQuantity();
						// sqt=sqt+1;
						// element1.setStartQuantity(sqt);

					} else {
						element1.setId(WizardTarget.existingActivityNameTarget);
						element1.setName(WizardTarget.activity_name);
						String idVersionT = element1.getId();
						String nameV = element1.getName();
						// String
						// nb=up.selectTaskNumMsgFlow(idVersionT,WizardTarget.idvp);
						String nb = up.selectTaskNumMsgFlow(idVersionT,
								BPMN2Editor.currentInput.getName());
						if (nb != "") {
						int j = 0;
						j = Integer.parseInt(nb);
						j++;
						String n = Integer.toString(j);
						// up.InsertActivitymessage(idVersionT,
						// WizardTarget.idvp,n);
					//	String bt = up.selectIdVersionFromidvp(nameV,		BPMN2Editor.currentInput.getName());
						up.InsertActivitymessage(n, idVersionT,
								BPMN2Editor.currentInput.getName());
						} else {
							up.UpdateNbMsg(BPMN2Editor.currentInput.getName(),
									idVersionT);
						}
						// int sqt= element1.getStartQuantity();
						// sqt=sqt+1;
						// element1.setStartQuantity(sqt);
					}
					//String Process_2 = WizardTarget.idvp;
					String Process_2 = p1.getId();
System.out.println("09-11-2015"+Process_2);
					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());

						handler.addFlowElement(handler.findElement(Process_2),
								element1);
						handler.getFlowElementContainer(element1);

						m = handler.createMessageFlow(element, element1);
						context2.setX(context.getX());
						context2.setY(context.getY() + 20);

					} catch (IOException e) {
						Activator.logError(e);
					}

					pe2 = addGraphicalRepresentation(context2, element1);

					s = GraphitiUi.getPeService().createChopboxAnchor(
							(ContainerShape) pe);
					t = GraphitiUi.getPeService().createChopboxAnchor(
							(ContainerShape) pe2);
					CreateConnectionContext c = new CreateConnectionContext();
					c.setSourceAnchor(s);
					c.setTargetAnchor(t);

					// add connection for business object
					AddConnectionContext addContext = new AddConnectionContext(
							c.getSourceAnchor(), c.getTargetAnchor());
					addContext.setNewObject(m);

					getFeatureProvider().addIfPossible(addContext);

					putBusinessObject(context, m);
					test=false;
					return new Object[] { element, element1, m, pe, pe2 };
					
				}
					
					
				else{
					b.a = false;
					cb = false;
					PictogramElement pe, pe2;

					Anchor s, t;

					Task element = null;
					CreateContext context2 = (CreateContext) context;

					element = (Task) createBusinessObject(context);

					UpdateMessageFlowNumber up = new UpdateMessageFlowNumber();

					if ((WizardSource.newInteraction) == true) {
						element.setName(WizardSource.newActivityNameSource);
						element.setId("VA" + WizardSource.ids + "-1");

						// int sq= element.getStartQuantity();
						// sq=sq+1;
						// element.setStartQuantity(sq);
						// String g=up.selectTaskNumMsgFlow(idVersion);
						String idVersion = element.getId();
						String nameVersion = element.getName();
						// up.InstertActivity(idVersion,
						// nameVersion,WizardSource.idprocess, "");
						up.InstertActivity(idVersion, nameVersion,
								BPMN2Editor.currentInput.getName(), "");

						// up.InsertActivitymessage(idVersion, "1");
						// System.out.println("hello"+up.selectTaskNumMsgFlow(id));

					} else {
						element.setId(WizardSource.existingActivityNameSource);
						element.setName(WizardSource.activity_name);
						String idVersion = element.getId();
						String nameVersion = element.getName();

						// nb=up.selectTaskNumMsgFlow(idVersion,BPMN2Editor.currentInput.getName());

						// int j = 0;
						// j = Integer.parseInt(nb);
						// j++;
						// String n = Integer.toString(j);
						// up.InsertActivitymessage(idVersion,
						// BPMN2Editor.currentInput.getName(),n);

						String nb = up.selectTaskNumMsgFlow(idVersion,
								BPMN2Editor.currentInput.getName());
						if (nb != "") {
							int j = 0;
							j = Integer.parseInt(nb);
							j++;
							String n = Integer.toString(j);
							// String bt=
							// up.selectIdVersionFromidvp(nameVersion,BPMN2Editor.currentInput.getName());
							up.InsertActivitymessage(n, idVersion,
									BPMN2Editor.currentInput.getName());

						} else {
							up.UpdateNbMsg(BPMN2Editor.currentInput.getName(),
									idVersion);
						}
					}
					String Process_1 = WizardSource.idprocess;
					//String Process_1 = p2.getId();
					System.out.println("09-11-2015hhh"+Process_1);
					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());

						handler.addFlowElement(handler.findElement(Process_1),
								element);

					} catch (IOException e) {
						Activator.logError(e);
					}
					pe = null;
					pe = addGraphicalRepresentation(context, element);

					MessageFlow m = null;
					EObject o = null;
					Task element1 = null;
					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());

						// chercher le participant2 dans le diagramme
						o = handler.findElement(WizardTarget.participant.getId());

					} catch (IOException e) {
						Activator.logError(e);
					}
					// chercher la représentation graphique du participant2
					List<PictogramElement> p = GraphitiUi.getLinkService()
							.getPictogramElements(getDiagram(), o);
					// (ContainerShape) p.get(0) c'est la figure qui représente le
					// participant 2
					context2.setTargetContainer((ContainerShape) p.get(0));
					// context2.setY( p.get(0).getGraphicsAlgorithm().getY());
					element1 = (Task) createBusinessObject(context2);
					if ((WizardTarget.newInteraction) == true) {
						element1.setName(WizardTarget.activityNameTarget);
						element1.setId("VA" + WizardTarget.idt + "-1");
						String idVersionT = element1.getId();
						String nameVersionT = element1.getName();
						// up.InstertActivity(idVersionT,
						// nameVersionT,WizardTarget.idvp, "");
						up.InstertActivity(idVersionT, nameVersionT,
								BPMN2Editor.currentInput.getName(), "");

						// up.InstertActivity(idVersionT, nameVersionT,"", "");

						// int sqt= element1.getStartQuantity();
						// sqt=sqt+1;
						// element1.setStartQuantity(sqt);

					} else {
						element1.setId(WizardTarget.existingActivityNameTarget);
						element1.setName(WizardTarget.activity_name);
						String idVersionT = element1.getId();
						String nameV = element1.getName();
						// String
						// nb=up.selectTaskNumMsgFlow(idVersionT,WizardTarget.idvp);
						String nb = up.selectTaskNumMsgFlow(idVersionT,
								BPMN2Editor.currentInput.getName());
						if (nb != "") {
						int j = 0;
						j = Integer.parseInt(nb);
						j++;
						String n = Integer.toString(j);
						// up.InsertActivitymessage(idVersionT,
						// WizardTarget.idvp,n);
					//	String bt = up.selectIdVersionFromidvp(nameV,		BPMN2Editor.currentInput.getName());
						up.InsertActivitymessage(n, idVersionT,
								BPMN2Editor.currentInput.getName());
						} else {
							up.UpdateNbMsg(BPMN2Editor.currentInput.getName(),
									idVersionT);
						}
						// int sqt= element1.getStartQuantity();
						// sqt=sqt+1;
						// element1.setStartQuantity(sqt);
					}
					String Process_2 = WizardTarget.idvp;
					//String Process_2 = p1.getId();
System.out.println("09-11-2015"+Process_2);
					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());

						handler.addFlowElement(handler.findElement(Process_2),
								element1);
						handler.getFlowElementContainer(element1);

						m = handler.createMessageFlow(element, element1);
						context2.setX(context.getX());
						context2.setY(context.getY() + 20);

					} catch (IOException e) {
						Activator.logError(e);
					}

					pe2 = addGraphicalRepresentation(context2, element1);

					s = GraphitiUi.getPeService().createChopboxAnchor(
							(ContainerShape) pe);
					t = GraphitiUi.getPeService().createChopboxAnchor(
							(ContainerShape) pe2);
					CreateConnectionContext c = new CreateConnectionContext();
					c.setSourceAnchor(s);
					c.setTargetAnchor(t);

					// add connection for business object
					AddConnectionContext addContext = new AddConnectionContext(
							c.getSourceAnchor(), c.getTargetAnchor());
					addContext.setNewObject(m);
					getFeatureProvider().addIfPossible(addContext);

					putBusinessObject(context, m);

					return new Object[] { element, element1, m, pe, pe2 };


				}}

				if (b.performCancel() == true) {
					cb = true;
					return null;
				} else {
					return null;
				}
				
			}
			
			

		}

	}

}
