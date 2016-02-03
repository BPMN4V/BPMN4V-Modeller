package org.eclipse.bpmn2.modeler.core.merrimac.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
public class TreeStructure<T>
{
    public T data;
    public TreeStructure<T> parent;
    public List<TreeStructure<T>> children;
     
    public TreeStructure(T data)
    {
        this.data = data;
        children = new ArrayList<TreeStructure<T>>();
    }
     
    /**
     * Adds a new child node to the parent node returns the added Child
     */
    public TreeStructure<T> addChild(T childData)
    {
        TreeStructure<T> childNode = new TreeStructure<T>(childData);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }
     
    /**
     * Removes a child node from a parent.
     */
    public void removeChild(T childData)
    {
        TreeStructure<T> childNode = new TreeStructure<T>(childData);
        this.children.remove(childNode);
    }
     
    /**
     * Returns the parent
     */
    public TreeStructure<T> getParent()
    {
        return this.parent;
    }
     
    /**
     * Returns the parent
     */
    public TreeStructure<T> getRoot()
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
    }
     
    /**
     * Searches the tree for a given type
     */
    public boolean contains(T data)
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
    }
     
}
     
