/*******************************************************************************
 * Copyright (c) 2012 Original authors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Original authors and others - initial API and implementation
 ******************************************************************************/
package org.eclipse.nebula.widgets.nattable.test.fixture;


import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.ui.action.IMouseAction;
import org.eclipse.swt.events.MouseEvent;

public class MouseActionFixture implements IMouseAction {

	private boolean actionInvoked;

	public void run(NatTable natTable, MouseEvent event) {
		this.actionInvoked = true;
	}

	public boolean isActionInvoked() {
		return actionInvoked;
	}

	public void reset(){
		actionInvoked = false;
	}

}
