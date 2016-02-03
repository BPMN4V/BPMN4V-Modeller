package org.eclipse.bpmn2.modeler.ui.views;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
 
public class TreeStructure implements IAdaptable
{
    public String data;
    public TreeStructure parent;
    public List<TreeStructure> children;
     
    public TreeStructure(final String data)
    {
        this.data = data;
        children = new ArrayList<TreeStructure>();
    }
     
    /**
     * Adds a new child node to the parent node returns the added Child
     */
    public void addChild(TreeStructure childData)
    {
       // TreeStructure childNode = new TreeStructure(childData.getName());
    	childData.parent = this;
        this.children.add(childData);
        
    }
     
    /**
     * Removes a child node from a parent.
     */
    public void removeChild(String childData)
    {
        TreeStructure childNode = new TreeStructure(childData);
        this.children.remove(childNode);
    }
    public void removeChildren()
    {children.clear();}
     
    /**
     * Returns the parent
     */
    public TreeStructure getParent()
    {
        return this.parent;
    }
     
    /**
     * Returns the parent
     */
  /*  public TreeStructure<T> getRoot()
    {
        if (this == null)
        {
            return this;
        }
         
        TreeStructure<T> parentTmp = this.parent;
        TreeStructure<T> root = this;
         
        //iteration
        while (parentTmp != null)
        {
            parentTmp = root.parent;
            if (parentTmp != null)
            {
                root = parentTmp;
            }
             
        }
         
        return root;
    }*/
     
  public TreeStructure[] getChildren()
  {
	  
	  return (TreeStructure[]) children.toArray(new TreeStructure[children.size()]);}
  public boolean hasChildren()
  {return children.size() > 0;}
 
	public String getName() {
		return data;
	}
	
	public String toString() {
		return getName();
	}
	
	

	public void setParent(final TreeStructure parent) {
		this.parent = parent;
	}
/*	public void setParent(final TreeObject parent) {
		TreeParent t=new TreeParent(parent.getName());
		this.parent = t;
	}*/

	@Override
	public Object getAdapter(Class key) {
		if (key.equals(EObject.class)) {
			return getBaseElement();
		}
		return null;
	}

	private Object getBaseElement() {
		// TODO Auto-generated method stub
		return this;
	}
	
    
    /**
     * Searches the tree for a given type
     */
  /*  public boolean contains(T data)
    {
         
        if (this == null)
        {
            return false;
        }
         
        if (this.data.equals(data))
        {
            return true;
        }
         
        List<TreeStructure<T>> children2 = this.children;
        Iterator<TreeStructure<T>> iterator = children2.iterator();
        while (children2 != null && iterator.hasNext())
        {
            TreeStructure<T> next = iterator.next();
            if (next != null)
            {
                next.contains(data); //recursion
            }
             
        }
         
        return false;
    }
     
    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder("[");
        helpToString(this, output, 0);
        output.append("]");
        return output.toString();
    }
     
    private void helpToString(TreeStructure<T> tree, StringBuilder output, int level)
    {
        if (tree == null)
            return; // Tree is empty, so leave.
             
        output.append(getSpaces(level) + tree.data);
         
        List<TreeStructure<T>> children2 = tree.children;
        ++level; //increment the level
         
        Iterator<TreeStructure<T>> iterator = children2.iterator();
        while (children2 != null && iterator.hasNext())
        {
        	TreeStructure<T> next = iterator.next();
            if (next != null)
            {
                helpToString(next, output, level); //recursion
            }
             
        }
         
    }
     
    private String getSpaces(int level)
    {
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < level; i++)
        {
            sb.append("--");
        }
         
        return sb.toString();
    }*/
     
}
     
