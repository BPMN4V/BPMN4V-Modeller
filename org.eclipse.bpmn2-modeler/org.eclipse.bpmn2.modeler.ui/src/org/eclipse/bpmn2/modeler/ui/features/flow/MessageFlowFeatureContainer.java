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
package org.eclipse.bpmn2.modeler.ui.features.flow;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ConversationLink;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LoopCharacteristics;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.ResourceRole;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.BaseElementConnectionFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.DefaultDeleteBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.bendpoint.RemoveBendpointFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractAddFlowFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractReconnectFlowFeature;
import org.eclipse.bpmn2.modeler.core.features.label.UpdateLabelFeature;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.features.activity.DeleteActivityFeature;
import org.eclipse.bpmn2.modeler.ui.features.choreography.ChoreographyUtil;
import org.eclipse.bpmn2.modeler.ui.features.data.MessageFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.wizards.UpdateMessageFlowNumber;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardSource;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardTarget;
import org.eclipse.bpmn2.presentation.Bpmn2Editor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.AreaContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.features.context.impl.RemoveBendpointContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.graphiti.util.IColorConstant;
import org.eclipse.osgi.internal.framework.ContextFinder;
import org.eclipse.ui.actions.DeleteResourceAction;

import com.xhive.XhiveDriverFactory;
import com.xhive.anttasks.librarytasks.RemoveBinding;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class MessageFlowFeatureContainer extends
		BaseElementConnectionFeatureContainer {

	public final static String MESSAGE_REF = "message.ref"; //$NON-NLS-1$
	final static IPeService peService = Graphiti.getPeService();
	final static IGaService gaService = Graphiti.getGaService();
	private static IFeatureProvider ap;

	public Object getApplyObject(IContext context) {
		Object object = super.getApplyObject(context);
		if (context instanceof IPictogramElementContext) {
			PictogramElement pe = ((IPictogramElementContext) context)
					.getPictogramElement();
			if (ChoreographyUtil.isChoreographyMessageLink(pe))
				return null;
			MessageFlow mf = getMessageFlow(pe);
			if (mf != null)
				return mf;
		}
		return object;
	}

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof MessageFlow;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddMessageFlowFeature(fp);
	}

	@Override
	public ICreateConnectionFeature getCreateConnectionFeature(
			IFeatureProvider fp) {
		return new CreateMessageFlowFeature(fp);
	}

	@Override
	public IDeleteFeature getDeleteFeature(final IFeatureProvider fp) {
		return new DeleteMessageFromMessageFlowFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return new UpdateMessageFlowFeature(fp);
	}

	@Override
	public IReconnectionFeature getReconnectionFeature(IFeatureProvider fp) {
		return new ReconnectMessageFlowFeature(fp);
	}

	public static ContainerShape findMessageShape(Connection connection) {
		ConnectionDecorator d = findMessageDecorator(connection);
		if (d != null) {
			return BusinessObjectUtil.getFirstElementOfType(d,
					ContainerShape.class);
		}
		return null;
	}

	public static MessageFlow getMessageFlow(PictogramElement pe) {
		if (pe instanceof ContainerShape) {
			String id = peService.getPropertyValue(pe, MESSAGE_REF);
			if (id != null && !id.isEmpty()) {
				EObject o = pe.eContainer();
				while (!(o instanceof Diagram)) {
					o = o.eContainer();
				}
				if (o instanceof Diagram) {
					Diagram diagram = (Diagram) o;
					for (Connection connection : diagram.getConnections()) {
						MessageFlow messageFlow = BusinessObjectUtil
								.getFirstElementOfType(connection,
										MessageFlow.class);
						if (messageFlow != null) {
							if (id.equals(messageFlow.getId()))
								return messageFlow;
						}
					}
				}
			}
		} else if (pe instanceof Connection) {
			MessageFlow messageFlow = BusinessObjectUtil.getFirstElementOfType(
					pe, MessageFlow.class);
			return messageFlow;
		} else if (pe instanceof ConnectionDecorator) {
			pe = ((ConnectionDecorator) pe).getConnection();
			MessageFlow messageFlow = BusinessObjectUtil.getFirstElementOfType(
					pe, MessageFlow.class);
			return messageFlow;
		}
		return null;
	}

	protected static String messageToString(Message message) {
		if (message == null)
			return ""; //$NON-NLS-1$
		return message.getId();
	}

	protected static Connection getMessageFlowConnection(PictogramElement pe) {
		if (pe instanceof ContainerShape) {
			String id = peService.getPropertyValue(pe, MESSAGE_REF);
			if (id != null && !id.isEmpty()) {
				EObject o = pe.eContainer();
				while (!(o instanceof Diagram)) {
					o = o.eContainer();
				}
				if (o instanceof Diagram) {
					Diagram diagram = (Diagram) o;
					for (Connection connection : diagram.getConnections()) {
						MessageFlow messageFlow = BusinessObjectUtil
								.getFirstElementOfType(connection,
										MessageFlow.class);
						if (messageFlow != null) {
							if (id.equals(messageFlow.getId()))
								return connection;
						}
					}
				}
			}
		} else if (pe instanceof Connection) {
			MessageFlow messageFlow = BusinessObjectUtil.getFirstElementOfType(
					pe, MessageFlow.class);
			if (messageFlow != null)
				return (Connection) pe;
		} else if (pe instanceof ConnectionDecorator) {
			pe = ((ConnectionDecorator) pe).getConnection();
			MessageFlow messageFlow = BusinessObjectUtil.getFirstElementOfType(
					pe, MessageFlow.class);
			if (messageFlow != null)
				return (Connection) pe;
		}
		return null;
	}

	protected static ConnectionDecorator findMessageDecorator(
			Connection connection) {
		for (ConnectionDecorator d : connection.getConnectionDecorators()) {
			if (Graphiti.getPeService().getPropertyValue(d, MESSAGE_REF) != null) {
				return d;
			}
		}
		return null;
	}

	protected static boolean messageDecoratorMoved(Connection connection) {
		ContainerShape messageShape = findMessageShape(connection);
		if (messageShape != null) {
			ILocation loc = peService.getConnectionMidpoint(connection, 0.25);
			int w = MessageFeatureContainer.ENVELOPE_WIDTH / 2;
			int h = MessageFeatureContainer.ENVELOPE_HEIGHT / 2;
			int x = loc.getX() - w;
			int y = loc.getY() - h;
			ILocation shapeLoc = peService
					.getLocationRelativeToDiagram(messageShape);
			return x != shapeLoc.getX() || y != shapeLoc.getY();
		}
		return false;
	}

	protected static void adjustMessageDecorator(IFeatureProvider fp,
			Connection connection) {
		ContainerShape messageShape = findMessageShape(connection);
		if (messageShape != null) {
			// calculate new location: this will be 1/4 of the distance from
			// start of the connection line
			ILocation loc = peService.getConnectionMidpoint(connection, 0.25);
			int w = MessageFeatureContainer.ENVELOPE_WIDTH / 2;
			int h = MessageFeatureContainer.ENVELOPE_HEIGHT / 2;
			int x = loc.getX() - w;
			int y = loc.getY() - h;
			MoveShapeContext moveContext = new MoveShapeContext(messageShape);
			moveContext.setX(x);
			moveContext.setY(y);
			IMoveShapeFeature moveFeature = fp.getMoveShapeFeature(moveContext);
			moveFeature.moveShape(moveContext);
		}
	}

	protected static void addMessageDecorator(IFeatureProvider fp,
			Connection connection, Message message, Shape messageShape) {
		ILocation loc = peService.getConnectionMidpoint(connection, 0.25);
		Diagram diagram = peService.getDiagramForPictogramElement(connection);
		ConnectionDecorator decorator = peService.createConnectionDecorator(
				connection, true, 0.25, true);
		MessageFlow messageFlow = BusinessObjectUtil.getFirstElementOfType(
				connection, MessageFlow.class);

		int w = MessageFeatureContainer.ENVELOPE_WIDTH / 2;
		int h = MessageFeatureContainer.ENVELOPE_HEIGHT / 2;
		int x = loc.getX() - w;
		int y = loc.getY() - h;
		if (messageShape == null) {
			AddContext addContext = new AddContext(new AreaContext(), message);
			addContext.putProperty(MessageFeatureContainer.IS_REFERENCE,
					Boolean.TRUE);
			addContext.setX(x);
			addContext.setY(y);
			addContext.setTargetContainer(diagram);
			IAddFeature addFeature = fp.getAddFeature(addContext);
			messageShape = (Shape) addFeature.add(addContext);
		} else {
			MoveShapeContext moveContext = new MoveShapeContext(messageShape);
			moveContext.setLocation(x, y);
			moveContext.setSourceContainer(messageShape.getContainer());
			moveContext.setTargetContainer(messageShape.getContainer());
			IMoveShapeFeature moveFeature = fp.getMoveShapeFeature(moveContext);
			moveFeature.moveShape(moveContext);
		}
		fp.link(decorator, new Object[] { message, messageShape });
		peService.setPropertyValue(decorator, MESSAGE_REF, "true"); //$NON-NLS-1$
		// Set our MessageFlow ID in the Message shape. Sadly Graphiti shape
		// properties
		// can only hold Strings, so if the MessageFlow ID is null, we need to
		// assign
		// a new one to it.
		String id = messageFlow.getId();
		if (id == null || id.isEmpty())
			id = ModelUtil.setID(messageFlow);
		peService.setPropertyValue(messageShape, MESSAGE_REF, id);
		messageFlow.setMessageRef(message);
	}

	protected static void removeMessageDecorator(IFeatureProvider fp,
			Connection connection) {
		ConnectionDecorator decorator = findMessageDecorator(connection);
		if (decorator != null) {
			ContainerShape messageShape = findMessageShape(connection);
			if (messageShape != null) {
				peService.removeProperty(messageShape, MESSAGE_REF);
				DeleteContext deleteContext = new DeleteContext(messageShape);
				IDeleteFeature deleteFeature = fp
						.getDeleteFeature(deleteContext);
				deleteFeature.delete(deleteContext);
			}
			peService.deletePictogramElement(decorator);
			MessageFlow mf = BusinessObjectUtil.getFirstElementOfType(
					connection, MessageFlow.class);
			mf.setMessageRef(null);
		}
	}

	public static class AddMessageFlowFeature extends
			AbstractAddFlowFeature<MessageFlow> {
		public AddMessageFlowFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canAdd(IAddContext context) {
			if (context instanceof IAddConnectionContext) {
				IAddConnectionContext acc = (IAddConnectionContext) context;
				if (acc.getSourceAnchor() != null) {
					Object obj = BusinessObjectUtil.getFirstElementOfType(acc
							.getSourceAnchor().getParent(), BaseElement.class);
					if (obj instanceof StartEvent) {
						return false;
					}
				}
			}
			return super.canAdd(context);
		}

		@Override
		protected Polyline createConnectionLine(final Connection connection) {
			MessageFlow messageFlow = (MessageFlow) BusinessObjectUtil
					.getFirstBaseElement(connection);

			Polyline connectionLine = super.createConnectionLine(connection);
			connectionLine.setLineStyle(LineStyle.DASH);
			connectionLine.setLineWidth(2);

			ConnectionDecorator endDecorator = peService
					.createConnectionDecorator(connection, false, 1.0, true);
			ConnectionDecorator startDecorator = peService
					.createConnectionDecorator(connection, false, 0, true);

			int w = 5;
			int l = 10;

			Polyline arrowhead = gaService.createPolygon(endDecorator,
					new int[] { -l, w, 0, 0, -l, -w, -l, w });
			StyleUtil.applyStyle(arrowhead, messageFlow);
			arrowhead.setBackground(manageColor(IColorConstant.WHITE));

			Ellipse circle = gaService.createEllipse(startDecorator);
			gaService.setSize(circle, 10, 10);
			StyleUtil.applyStyle(circle, messageFlow);
			circle.setBackground(manageColor(IColorConstant.WHITE));

			return connectionLine;
		}

		@Override
		protected Class<? extends BaseElement> getBoClass() {
			return MessageFlow.class;
		}
	}

	public static class CreateMessageFlowFeature
			extends
			AbstractCreateFlowFeature<MessageFlow, InteractionNode, InteractionNode> {

		public CreateMessageFlowFeature(IFeatureProvider fp) {
			super(fp, Messages.MessageFlowFeatureContainer_Name,
					Messages.MessageFlowFeatureContainer_Description);
		}

		@Override
		public boolean isAvailable(IContext context) {
			if (!isModelObjectEnabled(Bpmn2Package.eINSTANCE.getMessageFlow()))
				return false;

			if (context instanceof ICreateConnectionContext) {
				ICreateConnectionContext ccc = (ICreateConnectionContext) context;
				if (ccc.getSourcePictogramElement() != null) {
					Object obj = BusinessObjectUtil.getFirstElementOfType(
							ccc.getSourcePictogramElement(), BaseElement.class);
					if (obj instanceof EndEvent) {
						List<EventDefinition> eventDefinitions = ((EndEvent) obj)
								.getEventDefinitions();
						for (EventDefinition eventDefinition : eventDefinitions) {
							if (eventDefinition instanceof MessageEventDefinition) {
								return true;
							}
						}
					} else if (obj instanceof StartEvent) {
						return false;
					}
				}
			}
			return super.isAvailable(context);
		}

		@Override
		public boolean canStartConnection(ICreateConnectionContext context) {
			if (ChoreographyUtil.isChoreographyParticipantBand(context
					.getSourcePictogramElement()))
				return false;
			return true;
		}

		@Override
		public boolean canCreate(ICreateConnectionContext context) {
			if (ChoreographyUtil.isChoreographyParticipantBand(context
					.getSourcePictogramElement()))
				return false;
			if (context.getTargetPictogramElement() != null) {
				if (ChoreographyUtil.isChoreographyParticipantBand(context
						.getTargetPictogramElement()))
					return false;
			}
			InteractionNode source = getSourceBo(context);
			InteractionNode target = getTargetBo(context);
			return super.canCreate(context)
					&& isDifferentParticipants(source, target);
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_MESSAGE_FLOW;
		}

		@Override
		protected Class<InteractionNode> getSourceClass() {
			return InteractionNode.class;
		}

		@Override
		protected Class<InteractionNode> getTargetClass() {
			return InteractionNode.class;
		}

		private boolean isDifferentParticipants(InteractionNode source,
				InteractionNode target) {
			if (source == null || target == null) {
				return true;
			}
			boolean different = false;
			try {
				ModelHandler handler = ModelHandler.getInstance(getDiagram());
				Participant sourceParticipant = handler.getParticipant(source);
				Participant targetParticipant = handler.getParticipant(target);
				if (sourceParticipant == null) {
					if (targetParticipant == null)
						return true;
					return false;
				}
				different = !sourceParticipant.equals(targetParticipant);
			} catch (IOException e) {
				Activator.logError(e);
			}
			return different;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.bpmn2.modeler.core.features.
		 * AbstractBpmn2CreateConnectionFeature#getBusinessObjectClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getMessageFlow();
		}
	}

	public static class DeleteMessageFromMessageFlowFeature extends
			DefaultDeleteBPMNShapeFeature {
private boolean deletesource=false;
private boolean deletecible=false;
		public DeleteMessageFromMessageFlowFeature(IFeatureProvider fp) {
			super(fp);
		}

		boolean canDeleteMessage = true;
		Connection messageFlowConnection;

		@Override
		public boolean canDelete(IDeleteContext context) {
			PictogramElement pe = context.getPictogramElement();
			if (getMessageFlow(pe) != null)
				return true;
			return false;
		}

		@Override
		public void delete(IDeleteContext context) {
			Message message = null;
			Task src = null;
			Task dest = null;
			// MyTaskImpl src = null;
			// MyTaskImpl dest = null;
			String state= select_version_collaboration_state(getDiagram().getName());
if (state.compareTo("Working")==0){
			PictogramElement pe = context.getPictogramElement();
			if (pe instanceof ContainerShape) {
				ContainerShape messageShape = (ContainerShape) pe;
				messageFlowConnection = getMessageFlowConnection(messageShape);
				message = BusinessObjectUtil.getFirstElementOfType(
						messageShape, Message.class);
			} else if (pe instanceof Connection) {
				messageFlowConnection = (Connection) pe;

				MessageFlow messageFlow = (MessageFlow) BusinessObjectUtil
						.getFirstBaseElement(messageFlowConnection);
				src = (Task) messageFlow.getSourceRef();

				try {
					ModelHandler handler = ModelHandler
							.getInstance(getDiagram());

				} catch (IOException e) {
					Activator.logError(e);
				}
				
				dest = (Task) messageFlow.getTargetRef();
				

				message = messageFlow.getMessageRef();

			}

			

			String idversion = src.getId();
			UpdateMessageFlowNumber up = new UpdateMessageFlowNumber();
			
			//String nb = up.selectTaskNumMsgFlow(idversion,WizardSource.idprocess);
			
			String nb = up.selectTaskNumMsgFlow(idversion,getDiagram().getName());
			System.out.println("bonsoir tt le monde"+getDiagram().getName());
			
			int jn = 0;
			jn = Integer.parseInt(nb);

			if (jn > 1)

			{
				System.out.println("impooooo");
				String ns = up.selectTaskNumMsgFlow(idversion,getDiagram().getName());

				int js = 0;
				js = Integer.parseInt(ns);
				js--;
				String n = Integer.toString(js);
				//up.InsertActivitymessage(idversion, WizardSource.idprocess, n);
				up.InsertActivitymessage( n,idversion, getDiagram().getName());

			} else {
				///delte squesence flow
				deletesource=true;
				
			}

			String idversionT = dest.getId();
			//String nbt = up.selectTaskNumMsgFlow(idversionT, WizardTarget.idvp);

			String nbt = up.selectTaskNumMsgFlow(idversionT,getDiagram().getName());
			int jnt = 0;
			jnt = Integer.parseInt(nbt);

			if (jnt > 1)

			{
				System.out.println("impooooo");
				
				String nt = up.selectTaskNumMsgFlow(idversionT,getDiagram().getName());
				int jt = 0;
				jt = Integer.parseInt(nt);
				jt--;
				String nd = Integer.toString(jt);
				//up.InsertActivitymessage(idversion, WizardTarget.idvp, nd);
				up.InsertActivitymessage(nd,idversionT, getDiagram().getName());

			} else {
				deletecible=true;
				}
			boolean derive1 = false ,derive2=false, derive3=false;
			Process ps=(Process) src.eContainer();
			String idp[]= ps.getId().split("-");
			Process pt=(Process) dest.eContainer();
			String idt[]= pt.getId().split("-");
			if (deletesource==true && deletecible==true && SelectProcessState(ps.getId()).compareTo("Stable")==0 && SelectProcessState(pt.getId()).compareTo("Stable")==0 )
			{ int reply =JOptionPane.showConfirmDialog(null,"Versions of "+ ps.getName()+"("+ps.getId()+")"+" and "+ pt.getName()+"("+pt.getId()+")"+" have to be derived since they are stable. Would you like to continue?", "Derive processes", JOptionPane.YES_NO_CANCEL_OPTION);
			if (reply == JOptionPane.YES_OPTION)	
			{int i=Integer.parseInt(idp[1])+1;
			String id=  Derive( ps.getId(),  ps.getName(),  idp[0]+"-"+i);
			ps.setId(id);
			 i=Integer.parseInt(idt[1])+1;
			  id= Derive( pt.getId(),  pt.getName(),  idt[0]+"-"+i);
			pt.setId(id);}
			else {derive1=true;}}
			else
				if (deletesource==true && deletecible==false && SelectProcessState(ps.getId()).compareTo("Stable")==0)
				{ int reply =JOptionPane.showConfirmDialog(null,"The Version of "+ ps.getName()+"("+ps.getId()+") have to be derived since it is stable. Would you like to continue?", "Derive processes", JOptionPane.YES_NO_CANCEL_OPTION);
				if (reply == JOptionPane.YES_OPTION)	
				{
				int i=Integer.parseInt(idp[1])+1;
				String id= Derive( ps.getId(),  ps.getName(),  idp[0]+"-"+i);
				ps.setId(id);}
				else {derive2=true;}}
			else 
				if (deletesource==false && deletecible==true && SelectProcessState(pt.getId()).compareTo("Stable")==0)
				{ int reply =JOptionPane.showConfirmDialog(null,"The Version of "+ pt.getName()+"("+pt.getId()+") have to be derived since it is stable. Would you like to continue?", "Derive processes", JOptionPane.YES_NO_CANCEL_OPTION);
				if (reply == JOptionPane.YES_OPTION)	
				{
				int i=Integer.parseInt(idp[1])+1;
				String id=Derive( pt.getId(),  pt.getName(),  idt[0]+"-"+i);
				ps.setId(id);}
				else {derive3=true;}}
			
			if (deletesource==true && deletecible==true && derive1==false)
			{ 
				if (message != null) {
				List<EObject> list = FeatureSupport.findMessageReferences(
						getDiagram(), message);
				if (list.size() > 2)
					canDeleteMessage = false;

				if (canDeleteMessage) {
					EcoreUtil.delete(message, true);
				}

				ConnectionDecorator decorator = findMessageDecorator(messageFlowConnection);
				if (decorator != null) {
					ContainerShape messageShape = BusinessObjectUtil
							.getFirstElementOfType(decorator,
									ContainerShape.class);
					if (messageShape != null) {
						ContainerShape labelShape = BusinessObjectUtil
								.getFirstElementOfType(messageShape,
										ContainerShape.class);
						if (labelShape != null)
							peService.deletePictogramElement(labelShape);
						peService.deletePictogramElement(messageShape);
					}
					peService.deletePictogramElement(decorator);
				}
				}
			
			super.delete(context);
			deletepattern( src,  dest);
			deletesource=false;
			deletecible=false;	
			}
			else
				if (deletesource==true && deletecible==false && derive2==false)
				{ 				
					if (message != null) {
					List<EObject> list = FeatureSupport.findMessageReferences(
							getDiagram(), message);
					if (list.size() > 2)
						canDeleteMessage = false;

					if (canDeleteMessage) {
						EcoreUtil.delete(message, true);
					}

					ConnectionDecorator decorator = findMessageDecorator(messageFlowConnection);
					if (decorator != null) {
						ContainerShape messageShape = BusinessObjectUtil
								.getFirstElementOfType(decorator,
										ContainerShape.class);
						if (messageShape != null) {
							ContainerShape labelShape = BusinessObjectUtil
									.getFirstElementOfType(messageShape,
											ContainerShape.class);
							if (labelShape != null)
								peService.deletePictogramElement(labelShape);
							peService.deletePictogramElement(messageShape);
						}
						peService.deletePictogramElement(decorator);
					}
				
					}
				super.delete(context);
				deletepattern( src,  null);	
				}
				else
					if (deletesource==false && deletecible==true &&derive3==false)
					{ 
						if (message != null) {
						List<EObject> list = FeatureSupport.findMessageReferences(
								getDiagram(), message);
						if (list.size() > 2)
							canDeleteMessage = false;

						if (canDeleteMessage) {
							EcoreUtil.delete(message, true);
						}

						ConnectionDecorator decorator = findMessageDecorator(messageFlowConnection);
						if (decorator != null) {
							ContainerShape messageShape = BusinessObjectUtil
									.getFirstElementOfType(decorator,
											ContainerShape.class);
							if (messageShape != null) {
								ContainerShape labelShape = BusinessObjectUtil
										.getFirstElementOfType(messageShape,
												ContainerShape.class);
								if (labelShape != null)
									peService.deletePictogramElement(labelShape);
								peService.deletePictogramElement(messageShape);
							}
							peService.deletePictogramElement(decorator);
						}
						}
					
					super.delete(context);
					deletepattern( null,  dest);
					deletesource=false;
					deletecible=false;	
					}
					else
						if (deletesource==false && deletecible==false && derive1==false && derive2==false && derive3==false)
						{ 
							if (message != null) {
							List<EObject> list = FeatureSupport.findMessageReferences(
									getDiagram(), message);
							if (list.size() > 2)
								canDeleteMessage = false;

							if (canDeleteMessage) {
								EcoreUtil.delete(message, true);
							}

							ConnectionDecorator decorator = findMessageDecorator(messageFlowConnection);
							if (decorator != null) {
								ContainerShape messageShape = BusinessObjectUtil
										.getFirstElementOfType(decorator,
												ContainerShape.class);
								if (messageShape != null) {
									ContainerShape labelShape = BusinessObjectUtil
											.getFirstElementOfType(messageShape,
													ContainerShape.class);
									if (labelShape != null)
										peService.deletePictogramElement(labelShape);
									peService.deletePictogramElement(messageShape);
								}
								peService.deletePictogramElement(decorator);
							}
						}
						
						super.delete(context);
						
						deletesource=false;
						deletecible=false;	
						}

}
			else
				JOptionPane.showMessageDialog(null, "This is a Stable version. It have to be derived before updating. ", "Derive stable version before use pattern", JOptionPane.ERROR_MESSAGE);
			
		}
		
		public String SelectProcessState(String id_v)
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
		 public String Derive(String id_dr, String name, String next_id_s)
		    {
			//String activities=SelectVersionProcessActivities(id_dr);
			String s2;
			int i=0;
					XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
			 if (driver.isInitialized()==false)
			driver.init();  
		      
		    XhiveSessionIf session = driver.createSession("xqapi-test");  
		    session.connect("Administrator", "imen", "vbpmn");  
		    session.begin(); 
		    int j=0; String s="";
		 
		
		    /*String idp[]= next_id_s.split("-");
			int i=Integer.parseInt(idp[1])+1;
			String next_id=idp[0]+"-"+i;*/
		    s2=   SelectLastVProcess(name);
		     i= SelectLastVprocess(name);
		    //String next_id_s="";
		    j=s2.indexOf("-");
		    int f=i+1;
		    next_id_s="'"+s2.substring(0,j)+"-"+f+"'";
		String path =select_version_process_path(id_dr);
		String path2=path.replace(id_dr, next_id_s);

		    try {  
		      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
		      // (1)
		     
		      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version> <id_v>"+s2+"</id_v><number>V"+i+"</number><creator>Imen</creator><creation_date>16/04/2014</creation_date><derived_from> <id_vd>"+id_dr+"</id_vd></derived_from><activities></activities><path></path><state>Working</state></version>where $p/name='"+name+"' return insert nodes  $i into $p/versions");
		      IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber let $o:="+next_id_s+" where $p/name='"+name+"' return replace value of node $p/id_vs with $o");
		      IterableIterator<? extends XhiveXQueryValueIf>  result2 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber let $o:='"+f+"' where $p/name='"+name+"' return replace value of node $p/id_vn with $o");
		     
		     session.commit();  
				    } finally {  
				      session.rollback();  
				    }
		    return s2;
		    
		}
		 public int SelectLastVprocess(String name)
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
		    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber where $p/name='"+name+"' return $p/id_vn");
		  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
		    while(result.hasNext()) {  
		 	   s2=result.next().toString();
		 	   }
		    s2=s2.substring(7);
		    j=s2.indexOf("<");
		   		 s2=s2.substring(0,j);
		   		
		    session.commit();  
				    } finally {  
				      session.rollback();  
				    } 
		     i=Integer.parseInt(s2);
			return i;
				
				
			}
		 public String SelectLastVProcess(String name)
			{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
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
		     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber where $p/name='"+name+"' return $p/id_vs");
		   //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
		     while(result.hasNext()) {  
		  	   s2=result.next().toString();
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
		 public String select_version_process_path(String s)
			{String dervivé=null; 
				XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
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
			      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $a in doc('Processes.xml')//Process/versions/version where $a/id_v='"+s+"' return <id> {$a/path} </id>");
					
						while(result.hasNext()) {  
					    	  String  s1=result.next().toString();
					    	  
								
									 j=s1.indexOf("<path>");
									
									 s1= s1.substring(j+6);
									
									j=s1.indexOf("<");
									dervivé=s1.substring(0, j);
								
									
							
								
								}	  
					
						
						
			        
			      
			      session.commit();  
			    } finally {  
			      session.rollback();  
			    }  
			    return dervivé;
			}

protected void deletepattern(Task src, Task dest)
{ if (src!=null)
	{List<SequenceFlow> sq=src.getIncoming();
	List<SequenceFlow> sqo=src.getOutgoing();
	Iterator<SequenceFlow> ii= sq.iterator();
	Connection c;
	
	List<SequenceFlow> s = new LinkedList<SequenceFlow>();
	while (ii.hasNext())
	{SequenceFlow f=ii.next();
		if (f!=null)
		 s.add(f);
	}
	ii=sqo.iterator();
	while (ii.hasNext())
	{SequenceFlow f=ii.next();
		if (f!=null)
		 s.add(f);
	}
	//stocker dans la liste s les sequence flow de la source srs les entrant et les sotant
	SequenceFlow f=null;
	Iterator<SequenceFlow> k=s.iterator();
	while (k.hasNext())
	{f=k.next();
		List<PictogramElement> l = Graphiti.getLinkService()
			.getPictogramElements(getDiagram(), f);
	Iterator<PictogramElement> j = l.iterator();
	
	while (j.hasNext()) {
		PictogramElement p = j.next();
		Graphiti.getPeService().deletePictogramElement(p);
		
	}
		deleteBusinessObject(f);
		
	}
	
		
	
	List<PictogramElement> l = Graphiti.getLinkService()
			.getPictogramElements(getDiagram(), src);
	Iterator<PictogramElement> i = l.iterator();
	while (i.hasNext()) {
		
		PictogramElement p = i.next();
	
		Graphiti.getPeService().deletePictogramElement(p);
		

	}
	
	deleteBusinessObject(src);

	}
if (dest!=null)
{List<SequenceFlow> sq=dest.getIncoming();
List<SequenceFlow> sqo=dest.getOutgoing();
Iterator<SequenceFlow> ii= sq.iterator();
Connection c;

List<SequenceFlow> s = new LinkedList<SequenceFlow>();
while (ii.hasNext())
{SequenceFlow f=ii.next();
	if (f!=null)
	 s.add(f);
}
ii=sqo.iterator();
while (ii.hasNext())
{SequenceFlow f=ii.next();
	if (f!=null)
	 s.add(f);
}
//stocker dans la liste s les sequence flow de la source srs les entrant et les sotant
SequenceFlow f=null;
Iterator<SequenceFlow> k=s.iterator();
while (k.hasNext())
{f=k.next();
	List<PictogramElement> l = Graphiti.getLinkService()
		.getPictogramElements(getDiagram(), f);
Iterator<PictogramElement> j = l.iterator();

while (j.hasNext()) {
	PictogramElement p = j.next();
	Graphiti.getPeService().deletePictogramElement(p);
	
}
	deleteBusinessObject(f);
	
}
List<PictogramElement> lt = Graphiti.getLinkService()
		.getPictogramElements(getDiagram(), dest);
Iterator<PictogramElement> j = lt.iterator();
while (j.hasNext()) {
	Graphiti.getPeService().deletePictogramElement(j.next());
}
deleteBusinessObject(dest);
}}

		@Override
		protected void deleteBusinessObject(Object bo) {
			if (bo instanceof Message && !canDeleteMessage)
				return;
			super.deleteBusinessObject(bo);
		}

		@Override
		public void postDelete(IDeleteContext context) {
			MessageFlow messageFlow = (MessageFlow) BusinessObjectUtil
					.getFirstBaseElement(messageFlowConnection);
			if (messageFlow != null) {
				messageFlow.setMessageRef(null);
				peService.setPropertyValue(messageFlowConnection, MESSAGE_REF,
						""); //$NON-NLS-1$
			}
		}
		public String select_version_collaboration_state(String id)
		{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
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
	      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Collaboration.xml')/Collaborations/Collaboration for $i in $o/versions/version where $i/id_v='"+id+"' return $i/state");
			
				while(result.hasNext()) {  
			    	   s2=result.next().toString();
			    	  j=s2.indexOf("<state>");
			    	  s2=s2.substring(j+7);
			    	  j=s2.indexOf("</state>");
			    	  s2=s2.substring(0, j);
			    	 }
	        
	      
	      session.commit();  
	    } finally {  
	      session.rollback();  
	    }  
			return s2;
			
		}

	}

	public static class UpdateMessageFlowFeature extends UpdateLabelFeature {

		boolean isUpdating = false;

		public UpdateMessageFlowFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public IReason updateNeeded(IUpdateContext context) {
			if (isUpdating)
				return Reason.createFalseReason();

			if (context.getPictogramElement() instanceof Connection) {
				Connection connection = (Connection) context
						.getPictogramElement();
				MessageFlow messageFlow = (MessageFlow) BusinessObjectUtil
						.getFirstBaseElement(connection);

				String oldMessageRef = peService.getPropertyValue(connection,
						MESSAGE_REF);
				if (oldMessageRef == null)
					oldMessageRef = ""; //$NON-NLS-1$

				String newMessageRef = messageToString(messageFlow
						.getMessageRef());

				if (!oldMessageRef.equals(newMessageRef)) {
					return Reason
							.createTrueReason(Messages.MessageFlowFeatureContainer_Ref_Changed);
				}

				// check if connection has been moved or reconnected
				if (messageDecoratorMoved(connection))
					return Reason
							.createTrueReason(Messages.MessageFlowFeatureContainer_Decorator_Moved);
			}
			return super.updateNeeded(context);
		}

		@Override
		public boolean update(IUpdateContext context) {
			try {
				isUpdating = true;
				Connection connection = (Connection) context
						.getPictogramElement();
				MessageFlow messageFlow = (MessageFlow) BusinessObjectUtil
						.getFirstBaseElement(connection);
				Message message = messageFlow.getMessageRef();
				String oldMessageRef = peService.getPropertyValue(connection,
						MESSAGE_REF);
				if (oldMessageRef == null)
					oldMessageRef = ""; //$NON-NLS-1$

				String newMessageRef = messageToString(messageFlow
						.getMessageRef());

				if (!oldMessageRef.equals(newMessageRef)) {
					removeMessageDecorator(getFeatureProvider(), connection);
					if (message != null) {
						Shape messageShape = (Shape) context
								.getProperty(MESSAGE_REF);
						addMessageDecorator(getFeatureProvider(), connection,
								message, messageShape);
					}
					peService.setPropertyValue(connection, MESSAGE_REF,
							newMessageRef);
				} else {
					// move the message decorator
					adjustMessageDecorator(getFeatureProvider(), connection);
				}

				return super.update(context);
			} finally {
				isUpdating = false;
			}
		}
	}

	public static class ReconnectMessageFlowFeature extends
			AbstractReconnectFlowFeature {

		public ReconnectMessageFlowFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		protected Class<? extends EObject> getTargetClass() {
			return InteractionNode.class;
		}

		@Override
		protected Class<? extends EObject> getSourceClass() {
			return InteractionNode.class;
		}
	}

	public static String selectTaskNumMsgFlow(String s) {
		XhiveDriverIf driver = XhiveDriverFactory
				.getDriver("xhive://localhost:1235");
		if (driver.isInitialized() == false)
			driver.init();

		XhiveSessionIf session = driver.createSession("xqapi-test");
		session.connect("Administrator", "imen", "vbpmn");
		session.begin();
		String s2 = "";
		try {
			XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();
			// (1)
			int j = 0;
			IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary
			// .executeXQuery("for $a in doc('Activities.xml')//Activity where $a/name='"
					.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version where $i/id_v='"
							+ s + "' return $i/");

			while (result.hasNext()) {
				s2 = result.next().toString();
				j = s2.indexOf("<nb_msg_flow>");
				s2 = s2.substring(j + 13);
				j = s2.indexOf("</nb_msg_flow>");
				s2 = s2.substring(0, j);
			}

			session.commit();
		} finally {
			session.rollback();
		}
		System.out.println("03-06-2015" + s2);
		return s2;

	}

}