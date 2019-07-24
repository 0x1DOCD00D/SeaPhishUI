package example.com.seaphish.uicarise.hierachyviewerlibrary;

/**
 * Created by uicarise on 12/20/16.
 */

/* Self defined Tree Structure. Helper class */
public class Tree<T> {

    private TreeNode<T> rootGUIElement;

    public Tree(TreeNode<T> root) {
        this.rootGUIElement = root;
    }

    public TreeNode<T> getRootGUIElement(){
        return this.rootGUIElement;
    }

    public void setRootGUIElement(TreeNode<T> rootElement){
        this.rootGUIElement = rootElement;
    }


}
