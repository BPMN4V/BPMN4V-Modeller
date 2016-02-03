/*******************************************************************************
 * Copyright (c) 2011, 2012, 2013 Red Hat, Inc.
 * All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * 	Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.examples.customtask;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomShapeFeatureContainer.CreateCustomShapeFeature;
import org.eclipse.bpmn2.modeler.ui.wizards.UpdateMessageFlowNumber;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardAddPattern;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardAddPattern2;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardSource;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardSource2;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardTarget;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardTarget2;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.func.ICreate;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class MyAdaptationPatternCase2 extends CustomShapeFeatureContainer {

	private final static String TYPE_VALUE = "AddInteractioncase#2";
	private final static String CUSTOM_TASK_ID = "org.eclipse.bpmn2.modeler.examples.customtask.MyPattern2";
	public String aa;
	public static Boolean test2=false;

	public MyAdaptationPatternCase2() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new MyCreateCustomShapeFeature(fp);

	}

	@Override
	public String getId(EObject object) {
		// This is where we inspect the object to determine what its custom task
		// ID should be.
		// In this case, the "type" attribute will have a value of
		// "MyBoundaryEvent".
		// If found, return the TEMPORAL_DEPENDENCY_ID string.
		//
		// Note that the object inspection can be arbitrarily complex and may
		// include several
		// object features. This simple case just demonstrates what needs to
		// happen here.
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
			Process  pr1 = null,pr2 = null;

			String stateC = ump.select_version_collaboration_state(BPMN2Editor.currentInput.getName());
			if (stateC.compareTo("Stable")==0){
				JOptionPane.showMessageDialog(null, "The version of collaboration  is Stable.Derive stable version before use pattern", "Derive stable version before use pattern", JOptionPane.ERROR_MESSAGE);
				return null;
				
			}
			else
			{
			
			
			WizardSource2.context=(CreateContext) context;
			WizardSource2.diagram=getDiagram();
			WizardTarget2.context=(CreateContext) context;
			WizardTarget2.diagram=getDiagram();
			WizardAddPattern2 b = new WizardAddPattern2();
			WizardDialog dialog = new WizardDialog(getShell(), b);
			dialog.open();
			EObject o2 = null;
			String Process_1 = WizardSource2.idprocess;

			boolean cb = b.performCancel();

			if (b.a == true) {
				UpdateMessageFlowNumber st = new UpdateMessageFlowNumber();
				String stateP = st.selectEtatProcess(WizardTarget2.idvp);
				if (stateP.compareTo("Stable") == 0){
				
				test2=true;
				
				UpdateMessageFlowNumber upm = new UpdateMessageFlowNumber();
				String namePS = upm.selectNameProcess(WizardSource2.processStableS2);
				 String idS;
			  idS=  ump.SelectLastVProcess(namePS);
				
				String namePT = upm.selectNameProcess(WizardTarget2.idvp);

			    String idT;
			   idT=  ump.SelectLastVProcess(namePT);
			   upm.DeriveProcessPattern(WizardSource2.processStableS2, namePS);
				upm.DeriveProcessPattern(WizardTarget2.idvp, namePT);

				  try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());

						 pr2 =	 (Process) handler.findElement(WizardSource2.processStableS2);
						
						ModelHandler handler1 = ModelHandler
								.getInstance(getDiagram());
                         pr1 = (Process)handler1.findElement(WizardTarget2.idvp);

                       
                     
				  pr2.setId(idS);
				  pr1.setId(idT);
				

					} catch (IOException e) {
						Activator.logError(e);
					}
				}
				
				if(test2==true)
				{
				
				
				
				b.a = false;
				cb = false;

				PictogramElement pe, pe2, pes, pet;
				PictogramElement ab;
				Anchor s, t;
				Task element = null;
				CreateContext context2 = (CreateContext) context;
				EObject o1 = null;
				UpdateMessageFlowNumber up= new UpdateMessageFlowNumber();


				try {
					ModelHandler handler = ModelHandler
							.getInstance(getDiagram());

					o1 = handler.findElement(WizardSource2.activity_id);

					String stateActivity=up.selectEtatActivity(WizardSource2.activity_id);
					String stateProcess=up.selectEtatProcess(WizardSource2.idprocess);
					String nom=up.SelectLastVActivity(WizardSource2.activityName);
					int comp= stateActivity.compareToIgnoreCase("Stable");
					int compProcess= stateProcess.compareToIgnoreCase("Stable");

						if ((comp == 0)&&(compProcess == 0))
					{
						
							up.DeriveVersionActivity();
							
							((Task)o1).setId(nom);
							up.DeriveProcess(WizardSource2.idprocess);
							}
						else
						{
							String nb=up.selectTaskNumMsgFlow(WizardSource2.activity_id,BPMN2Editor.currentInput.getName());

							int j = 0;
							j = Integer.parseInt(nb);
							j++;
							String n = Integer.toString(j);
							up.InsertActivitymessage(n,WizardSource2.activity_id,BPMN2Editor.currentInput.getName());

						
					}
					
				

				} catch (IOException e) {
					Activator.logError(e);
				}
				pe = null;

				List<PictogramElement> p1 = GraphitiUi.getLinkService()
						.getPictogramElements(getDiagram(), o1);
				pe = p1.get(1);

				MessageFlow m = null;
				EObject o = null;
				Task element1 = null;
				try {
					ModelHandler handler = ModelHandler
							.getInstance(getDiagram());
					// chercher le participant2 dans l diagram
					o = handler.findElement(WizardTarget2.participant.getId());

				} catch (IOException e) {
					Activator.logError(e);
				}
				// chercher la représentation graphique du participant2
				List<PictogramElement> p = GraphitiUi.getLinkService()
						.getPictogramElements(getDiagram(), o);
				// (ContainerShape) p.get(0) c'est la figure qui représente le
				// participant 2
				context2.setTargetContainer((ContainerShape) p.get(0));
				element1 = (Task) createBusinessObject(context2);
				if ((WizardTarget2.newInteraction) == true) {
					element1.setName(WizardTarget2.activityNameTarget2);
					element1.setId("VA" + WizardTarget2.idt2 + "-1");
					String idVersionT2 = element1.getId();
					String nameVersionT2= element1.getName();
				
					up.InstertActivity(idVersionT2, nameVersionT2,BPMN2Editor.currentInput.getName(), "");


				} else {
					element1.setId(WizardTarget2.existingActivityNameTarget);
					element1.setName(WizardTarget2.activity_name);
					
					String idVersionT2 = element1.getId();
					String nb=up.selectTaskNumMsgFlow(idVersionT2,BPMN2Editor.currentInput.getName());
					if (nb != "") {
					int j = 0;
					j = Integer.parseInt(nb);
					j++;
					String n = Integer.toString(j);
					up.InsertActivitymessage(n,idVersionT2, BPMN2Editor.currentInput.getName());
					} else {
						up.UpdateNbMsg(BPMN2Editor.currentInput.getName(),
								idVersionT2);
					}
	
				}
				String Process_2 = pr1.getId();
				System.out.println("07-12-2015"+Process_2);
			//	String Process_2 = WizardTarget2.idvp;

				try {
					ModelHandler handler = ModelHandler
							.getInstance(getDiagram());
					handler.addFlowElement(handler.findElement(Process_2),
							element1);
					handler.getFlowElementContainer(element1);

					m = handler.createMessageFlow((InteractionNode) o1,
							element1);
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
				AddConnectionContext addContext = new AddConnectionContext(
						c.getSourceAnchor(), c.getTargetAnchor());
				addContext.setNewObject(m);
				getFeatureProvider().addIfPossible(addContext);
				putBusinessObject(context, m);
				test2=false;
				return new Object[] { o1, element1, m, pe, pe2 };
			}
				else{

					b.a = false;
					cb = false;

					PictogramElement pe, pe2, pes, pet;
					PictogramElement ab;
					Anchor s, t;
					Task element = null;
					CreateContext context2 = (CreateContext) context;
					EObject o1 = null;
					UpdateMessageFlowNumber up= new UpdateMessageFlowNumber();


					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());

						o1 = handler.findElement(WizardSource2.activity_id);

						String stateActivity=up.selectEtatActivity(WizardSource2.activity_id);
						String stateProcess=up.selectEtatProcess(WizardSource2.idprocess);
						String nom=up.SelectLastVActivity(WizardSource2.activityName);
						int comp= stateActivity.compareToIgnoreCase("Stable");
						int compProcess= stateProcess.compareToIgnoreCase("Stable");

					
							if ((comp == 0)&&(compProcess == 0))
						{
							
								up.DeriveVersionActivity();
								
								((Task)o1).setId(nom);
								up.DeriveProcess(WizardSource2.idprocess);
								}
							else
							{
								String nb=up.selectTaskNumMsgFlow(WizardSource2.activity_id,BPMN2Editor.currentInput.getName());

								int j = 0;
								j = Integer.parseInt(nb);
								j++;
								String n = Integer.toString(j);
								up.InsertActivitymessage(n,WizardSource2.activity_id,BPMN2Editor.currentInput.getName());

							
						}
						
					

					} catch (IOException e) {
						Activator.logError(e);
					}
					pe = null;

					List<PictogramElement> p1 = GraphitiUi.getLinkService()
							.getPictogramElements(getDiagram(), o1);
					pe = p1.get(1);

					MessageFlow m = null;
					EObject o = null;
					Task element1 = null;
					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());
						// chercher le participant2 dans l diagram
						o = handler.findElement(WizardTarget2.participant.getId());

					} catch (IOException e) {
						Activator.logError(e);
					}
					// chercher la représentation graphique du participant2
					List<PictogramElement> p = GraphitiUi.getLinkService()
							.getPictogramElements(getDiagram(), o);
					// (ContainerShape) p.get(0) c'est la figure qui représente le
					// participant 2
					context2.setTargetContainer((ContainerShape) p.get(0));
					element1 = (Task) createBusinessObject(context2);
					if ((WizardTarget2.newInteraction) == true) {
						element1.setName(WizardTarget2.activityNameTarget2);
						element1.setId("VA" + WizardTarget2.idt2 + "-1");
						String idVersionT2 = element1.getId();
						String nameVersionT2= element1.getName();
					
						up.InstertActivity(idVersionT2, nameVersionT2,BPMN2Editor.currentInput.getName(), "");

					} else
					{
						element1.setId(WizardTarget2.existingActivityNameTarget);
						element1.setName(WizardTarget2.activity_name);
						
						String idVersionT2 = element1.getId();
						String nb=up.selectTaskNumMsgFlow(idVersionT2,BPMN2Editor.currentInput.getName());
						if (nb != "") {
						int j = 0;
						j = Integer.parseInt(nb);
						j++;
						String n = Integer.toString(j);
						up.InsertActivitymessage(n,idVersionT2, BPMN2Editor.currentInput.getName());
						} else {
							up.UpdateNbMsg(BPMN2Editor.currentInput.getName(),
									idVersionT2);
						}
						

					}

					String Process_2 = WizardTarget2.idvp;

					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());
						handler.addFlowElement(handler.findElement(Process_2),
								element1);
						handler.getFlowElementContainer(element1);

						m = handler.createMessageFlow((InteractionNode) o1,
								element1);
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
					AddConnectionContext addContext = new AddConnectionContext(
							c.getSourceAnchor(), c.getTargetAnchor());
					addContext.setNewObject(m);
					getFeatureProvider().addIfPossible(addContext);
					putBusinessObject(context, m);
					
					return new Object[] { o1, element1, m, pe, pe2 };
				}
					
				}
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

