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
package org.eclipse.nebula.widgets.nattable.search.command;

import java.util.Comparator;

import org.eclipse.nebula.widgets.nattable.command.ILayerCommand;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayerListener;
import org.eclipse.nebula.widgets.nattable.search.strategy.ISearchStrategy;


public class SearchCommand implements ILayerCommand {
	
	private ILayer context;
	private final ISearchStrategy searchStrategy;
	private final String searchText;
	private final boolean isWrapSearch;
	private final boolean isCaseSensitive;
	private final String searchDirection;
	private final Comparator<?> comparator;
	private ILayerListener searchEventListener;
	
	public SearchCommand(ILayer layer, ISearchStrategy searchStrategy, String searchDirection, boolean isWrapSearch, boolean isCaseSensitive, Comparator<?> comparator) {
		this(null, layer, searchStrategy, searchDirection, isWrapSearch, isCaseSensitive, comparator);
	}
	
	public SearchCommand(String searchText, ILayer layer, ISearchStrategy searchStrategy, String searchDirection, boolean isWrapSearch, boolean isCaseSensitive, Comparator<?> comparator) {
		this.context = layer;
		this.searchStrategy = searchStrategy;
		this.searchText = searchText;
		this.isWrapSearch = isWrapSearch;
		this.isCaseSensitive = isCaseSensitive;
		this.searchDirection = searchDirection;
		this.comparator = comparator;
	}
	
	protected SearchCommand(SearchCommand command) {
		this.context = command.context;
		this.searchStrategy = command.searchStrategy;
		this.searchText = command.searchText;
		this.isWrapSearch = command.isWrapSearch;
		this.isCaseSensitive = command.isCaseSensitive;
		this.searchDirection = command.searchDirection;
		this.comparator = command.comparator;
		this.searchEventListener = command.searchEventListener;
	}
	
	public ILayer getContext() {
		return context;
	}
	
	public ISearchStrategy getSearchStrategy() {
		return searchStrategy;
	}
	
	public String getSearchText() {
		return searchText;
	}
	
	public String getSearchDirection() {
		return searchDirection;
	}
	
	public boolean isWrapSearch() {
		return isWrapSearch;
	}
	
	public boolean isCaseSensitive() {
		return isCaseSensitive;
	}
	
	public ILayerListener getSearchEventListener() {
		return searchEventListener;
	}
	
	public void setSearchEventListener(ILayerListener listener) {
		this.searchEventListener = listener;
	}
	
	public Comparator<?> getComparator() {
		return comparator;
	}
	
	public boolean convertToTargetLayer(ILayer targetLayer) {
		context = targetLayer;
		return true;
	}
	
	public SearchCommand cloneCommand() {
		return new SearchCommand(this);
	}
	
}
