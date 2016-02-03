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
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
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
import org.eclipse.bpmn2.modeler.ui.wizards.DerivePattern3;
import org.eclipse.bpmn2.modeler.ui.wizards.UpdateMessageFlowNumber;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardAddPattern2;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardAddPattern3;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardSource;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardSource2;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardSource3;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardTarget2;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardTarget3;
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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class MyAdaptationPatternCase3 extends CustomShapeFeatureContainer {

	private final static String TYPE_VALUE = "AddInteractioncase#3";
	private final static String CUSTOM_TASK_ID = "org.eclipse.bpmn2.modeler.examples.customtask.MyPattern3";
	public static Boolean test3=false;
	public MyAdaptationPatternCase3() {
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
				//JOptionPane.showMessageDialog( null,"The version of collaboration is Stable. Please derive Derive working version", JOptionPane.ERROR_MESSAGE);
				JOptionPane.showMessageDialog(null, "The version of collaboration  is Stable.Derive stable version before use pattern", "Derive stable version before use pattern", JOptionPane.ERROR_MESSAGE);
				return null;
				
			}
			else
			{
			
			
			WizardSource3.context=(CreateContext) context;
			WizardSource3.diagram=getDiagram();
			WizardTarget3.context=(CreateContext) context;
			WizardTarget3.diagram=getDiagram();

			WizardAddPattern3 b = new WizardAddPattern3();

			WizardDialog dialog = new WizardDialog(getShell(), b);
			dialog.open();
			boolean cb = b.performCancel();

			if (b.a == true) {
				UpdateMessageFlowNumber st = new UpdateMessageFlowNumber();
				String stateP = st.selectEtatProcess(WizardTarget3.idvp);
				if (stateP.compareTo("Stable") == 0){
				
				test3=true;
				
				UpdateMessageFlowNumber upm = new UpdateMessageFlowNumber();
				String namePS = upm.selectNameProcess(WizardSource3.processStableS3);
				 String idS;
			  idS=  ump.SelectLastVProcess(namePS);
				
				String namePT = upm.selectNameProcess(WizardTarget3.idvp);

			    String idT;
			   idT=  ump.SelectLastVProcess(namePT);
			   upm.DeriveProcessPattern(WizardSource3.processStableS3, namePS);
				upm.DeriveProcessPattern(WizardTarget3.idvp, namePT);

				  try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());

						 pr2 =	 (Process) handler.findElement(WizardSource3.processStableS3);
						
						ModelHandler handler1 = ModelHandler
								.getInstance(getDiagram());
                         pr1 = (Process)handler1.findElement(WizardTarget3.idvp);

                       
                     
				  pr2.setId(idS);
				  pr1.setId(idT);
				

					} catch (IOException e) {
						Activator.logError(e);
					}
				}
				
				
				
				
				
				if(test3==true)
				{
				b.a = false;
				cb = false;

				PictogramElement pe, pe2, pes, pet;

				PictogramElement ab;
				Anchor s, t;
				Task element = null;
				CreateContext context2 = (CreateContext) context;
				String Process_1 = pr2.getId();
				System.out.println("07-12-2015"+Process_1);
			
				//String Process_1 = WizardSource3.idprocess;
				EObject o1 = null;
				UpdateMessageFlowNumber up= new UpdateMessageFlowNumber();
				DerivePattern3 derive= new DerivePattern3();

				try {
					ModelHandler handler = ModelHandler
							.getInstance(getDiagram());
					o1 = handler.findElement(WizardTarget3.activity_id);
					String stateProcess=up.selectEtatProcess(WizardTarget3.idvp);
					String nom=up.SelectLastVActivity(WizardTarget3.activity_name);

					String state=up.selectEtatActivity(WizardTarget3.activity_id);
					int comp= state.compareToIgnoreCase("Stable");
					int compProcess= stateProcess.compareToIgnoreCase("Stable");

					if ((comp == 0)&&(compProcess == 0))

					//	if (comp == 0)
					{
						up.DeriveVersionActivityP3();
						
						((Task)o1).setId(nom);
						up.deriveProcessP3(WizardTarget3.idvp);
						//	derive.DeriveVersionActivity();
							}
						else
						{
							String nb=up.selectTaskNumMsgFlow(WizardTarget3.activity_id,BPMN2Editor.currentInput.getName());

							int j = 0;
							j = Integer.parseInt(nb);
							j++;
							String n = Integer.toString(j);
							//up.InsertActivitymessage(WizardSource2.activity_id,WizardSource2.idprocess,n);
							up.InsertActivitymessage(n,WizardTarget3.activity_id,BPMN2Editor.currentInput.getName());

						
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
				element1 = (Task) createBusinessObject(context);
				pe2 = addGraphicalRepresentation(context, element1);
				if ((WizardSource3.newInteraction) == true) 
				{
					element1.setName(WizardSource3.newActivityNameSource);
					element1.setId("VA" + WizardSource3.ids3 + "-1");
					String idVersion = element1.getId();
					String nameVersion= element1.getName();
				   // up.InstertActivity(idVersion, nameVersion,WizardSource3.idprocess, "");
				    up.InstertActivity(idVersion, nameVersion,BPMN2Editor.currentInput.getName(), "");

					
				} 
				else 
				{
					element1.setId(WizardSource3.existingActivityNameSource);
					element1.setName(WizardSource3.activity_name);
					String idVersion = element1.getId();
					//String nb=up.selectTaskNumMsgFlow(idVersion,WizardSource3.idprocess);
					String nb=up.selectTaskNumMsgFlow(idVersion,BPMN2Editor.currentInput.getName());
					if (nb != "") {
					int j = 0;
					j = Integer.parseInt(nb);
					j++;
					String n = Integer.toString(j);
					//up.InsertActivitymessage(idVersion, WizardSource3.idprocess,n);
					up.InsertActivitymessage(n,idVersion, BPMN2Editor.currentInput.getName());
					} else {
						up.UpdateNbMsg(BPMN2Editor.currentInput.getName(),
								idVersion);
					}
				

				}
				try {
					ModelHandler handler = ModelHandler
							.getInstance(getDiagram());
					handler.addFlowElement(handler.findElement(Process_1),
							element1);
					
				} catch (IOException e) {
					Activator.logError(e);
				}

				
				try {
					ModelHandler handler = ModelHandler
							.getInstance(getDiagram());
					// chercher le participant2 dans l diagram
					o = handler.findElement(WizardTarget3.participant.getId());
				} catch (IOException e) {
					Activator.logError(e);
				}
				// chercher la représentation graphique du participant2
				List<PictogramElement> p = GraphitiUi.getLinkService()
						.getPictogramElements(getDiagram(), o);
				// (ContainerShape) p.get(0) c'est la figure qui représente le
				// participant 2
				

				try {
					ModelHandler handler = ModelHandler
							.getInstance(getDiagram());
					
					m = handler.createMessageFlow(element1,
							(InteractionNode) o1);

					
				} catch (IOException e) {
					Activator.logError(e);
				}

				

				t = GraphitiUi.getPeService().createChopboxAnchor(
						(ContainerShape) pe);
				s = GraphitiUi.getPeService().createChopboxAnchor(
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
				test3=false;
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
					String Process_1 = WizardSource3.idprocess;
					EObject o1 = null;
					UpdateMessageFlowNumber up= new UpdateMessageFlowNumber();
					DerivePattern3 derive= new DerivePattern3();

					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());
						o1 = handler.findElement(WizardTarget3.activity_id);
						
						String state=up.selectEtatActivity(WizardTarget3.activity_id);
						int comp= state.compareToIgnoreCase("Stable");
					//	if (state == "Stable")
							if (comp == 0)
						{
							
								derive.DeriveVersionActivity();
								}
							else
							{
								String nb=up.selectTaskNumMsgFlow(WizardTarget3.activity_id,BPMN2Editor.currentInput.getName());

								int j = 0;
								j = Integer.parseInt(nb);
								j++;
								String n = Integer.toString(j);
								//up.InsertActivitymessage(WizardSource2.activity_id,WizardSource2.idprocess,n);
								up.InsertActivitymessage(n,WizardTarget3.activity_id,BPMN2Editor.currentInput.getName());

							
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
					element1 = (Task) createBusinessObject(context);
					pe2 = addGraphicalRepresentation(context, element1);
					if ((WizardSource3.newInteraction) == true) 
					{
						element1.setName(WizardSource3.newActivityNameSource);
						element1.setId("VA" + WizardSource3.ids3 + "-1");
						String idVersion = element1.getId();
						String nameVersion= element1.getName();
					   // up.InstertActivity(idVersion, nameVersion,WizardSource3.idprocess, "");
					    up.InstertActivity(idVersion, nameVersion,BPMN2Editor.currentInput.getName(), "");

						
					} 
					else 
					{
						element1.setId(WizardSource3.existingActivityNameSource);
						element1.setName(WizardSource3.activity_name);
						String idVersion = element1.getId();
						//String nb=up.selectTaskNumMsgFlow(idVersion,WizardSource3.idprocess);
						String nb=up.selectTaskNumMsgFlow(idVersion,BPMN2Editor.currentInput.getName());
						if (nb != "") {
						int j = 0;
						j = Integer.parseInt(nb);
						j++;
						String n = Integer.toString(j);
						//up.InsertActivitymessage(idVersion, WizardSource3.idprocess,n);
						up.InsertActivitymessage(n,idVersion, BPMN2Editor.currentInput.getName());
						} else {
							up.UpdateNbMsg(BPMN2Editor.currentInput.getName(),
									idVersion);
						}
					

					}
					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());
						handler.addFlowElement(handler.findElement(Process_1),
								element1);
						
					} catch (IOException e) {
						Activator.logError(e);
					}

					
					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());
						// chercher le participant2 dans l diagram
						o = handler.findElement(WizardTarget3.participant.getId());
					} catch (IOException e) {
						Activator.logError(e);
					}
					// chercher la représentation graphique du participant2
					List<PictogramElement> p = GraphitiUi.getLinkService()
							.getPictogramElements(getDiagram(), o);
					// (ContainerShape) p.get(0) c'est la figure qui représente le
					// participant 2
					

					try {
						ModelHandler handler = ModelHandler
								.getInstance(getDiagram());
						
						m = handler.createMessageFlow(element1,
								(InteractionNode) o1);

						
					} catch (IOException e) {
						Activator.logError(e);
					}

					

					t = GraphitiUi.getPeService().createChopboxAnchor(
							(ContainerShape) pe);
					s = GraphitiUi.getPeService().createChopboxAnchor(
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
					return new Object[] { o1, element1, m, pe, pe2 };

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
