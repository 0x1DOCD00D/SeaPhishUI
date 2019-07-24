package example.com.seaphish.uicarise.hierachyviewerlibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uicarise on 12/20/16.
 * Helper class which can be used to store the GUI elements of an android app in a Tree Structure.
 */

public class TreeNode<T> {

    private T guiElement;
    private List<TreeNode<T>> childrenGUIElement;
    private TreeNode<T> parentGUIElement;

    public TreeNode(T element)
    {
        this.guiElement = element;
        this.childrenGUIElement = new ArrayList<TreeNode<T>>();
    }

    public void addChildElements(TreeNode<T> childElement){
        childElement.setParent(this);
        this.childrenGUIElement.add(childElement);
    }

    public void setParent(TreeNode<T> parent) {
        this.parentGUIElement = parent;
    }

    public TreeNode<T> getParent() {
        return this.parentGUIElement;
    }

    public List<TreeNode<T>> getChildren() {
        return this.childrenGUIElement;
    }

    public void setChildren(List<TreeNode<T>> children) {
        for (TreeNode<T> child : children)
            child.setParent(this);

        this.childrenGUIElement = children;
    }

    public T getGUIElement() {
        return this.guiElement;
    }

    public void setGUIElement(T data) {
        this.guiElement = data;
    }

    public void addChildAt(int index, TreeNode<T> child) {
        child.setParent(this);
        this.childrenGUIElement.add(index, child);
    }

    public TreeNode<T> getChildAt(int index) {
        return this.childrenGUIElement.get(index);
    }

    public TreeNode<T> getChildWithString(T checkElement ){

        System.out.println("CheckElement: "+checkElement);
        if(this.childrenGUIElement != null)
            System.out.println("Check size: "+this.childrenGUIElement.size());

        for(int i =0 ; i < this.childrenGUIElement.size();i++)
        {
            System.out.println("Child element at index "+i+" value: "+this.childrenGUIElement.get(i).guiElement);
            if(this.childrenGUIElement.get(i).guiElement.equals(checkElement))
            {
                System.out.println("Match found at index: "+i);
                return this.childrenGUIElement.get(i);
            }
        }
        return null;
    }
}
