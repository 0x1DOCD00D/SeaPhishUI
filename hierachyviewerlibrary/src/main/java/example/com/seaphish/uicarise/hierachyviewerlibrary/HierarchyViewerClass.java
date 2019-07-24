package example.com.seaphish.uicarise.hierachyviewerlibrary;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by uicarise on 12/19/16.
 */

/* this class fetches the hierarchy of GUI elements in an android app */
public class HierarchyViewerClass
{
    public static TreeNode<String> root;
    public static int countElements = 0;
    public static int index = -1;

    /* This class contains generic functions which can be used to fetch the UI hierarchy for any Android app */
    public static View debugViewIds(View view, String logtag) {

        /* We traverse through the elements - starting from the parent and
         * then try to fetch all the children for each element */
        countElements = 0;
        /* maintain the log of the traversing node. */
        Log.v(logtag, "traversing: " + view.getClass().getSimpleName() + ", id: " + view.getId());

        root = new TreeNode<String>(view.getClass().getSimpleName());
        //System.out.println("Adding root: "+view.getClass().getSimpleName() +" node");
        if (view.getParent() != null && (view.getParent() instanceof ViewGroup)) {
            return debugViewIds((View)view.getParent(), logtag);
        }
        else {
            /* fetch all the child nodes for each parent*/
            debugChildViewIds1(view, logtag, 0);
            /* total GUI elements in each interface.*/
            System.out.println("Total Count: "+countElements);
            return view;
        }

    }

    /* this method is not in use currently - but if we want to construct a tree for the GUI elements
     * we can use this method called from debugViewIds. This method will be called recursively to fetch all the child
      * elements of a node and add them in a tree-structure. */
    private static void debugChildViewIds(View view, String logtag, int spaces)
    {
        index++;
        System.out.println("Start of debug child views with index: "+index+ " space count: "+spaces+" current node: "+view.getClass().getSimpleName());

        if (view instanceof ViewGroup)
        {
            ViewGroup group = (ViewGroup)view;
            for (int i = 0; i < group.getChildCount(); i++)
            {
                View child = group.getChildAt(i);
                //System.out.println("Group name: "+group.getClass().getSimpleName());
                TreeNode<String> childNode = new TreeNode<String>(child.getClass().getSimpleName());
                Log.v(logtag, padString("view: " + child.getClass().getSimpleName() + "(" + child.getId() + ")" , spaces));
                if(spaces == 0)
                {
                    //System.out.println("Adding node: "+child.getClass().getSimpleName() +" at root");
                    root.addChildAt(index,childNode);
                }
                else
                {
                    System.out.println("Get the parent node: "+view.getClass().getSimpleName());
                    if(root.getGUIElement().equals(view.getClass().getSimpleName()))
                    {
                        System.out.println("adding to the root : " + root.getGUIElement() + " the child: " + childNode.getGUIElement()+" with i:"+i);
                        root.addChildAt(i, childNode);
                    }
                    else
                    {
                        TreeNode<String> getCurrentRoot = root.getChildWithString(view.getClass().getSimpleName());
                        if (getCurrentRoot != null) {
                            System.out.println("Adding node: " + child.getClass().getSimpleName() + " with root node as: " + getCurrentRoot.getGUIElement()+" with i: "+i);
                            getCurrentRoot.addChildAt(i, childNode);
                            root = getCurrentRoot;
                        } else {
                            System.out.println("adding to the root : " + root.getGUIElement() + " the child: " + childNode.getGUIElement()+" with i: "+i);
                            root.addChildAt(i, childNode);
                        }
                    }
                }
                //root = childNode;
                debugChildViewIds(child, logtag, spaces + 1);
            }
        }
    }

    /* This method is used to print and store the hierarchy of GUI elements in a log file
     * Each time we maintain the parent - child hierarchy by appending spaces before we print the child node
      * and the GUIElement - defines the boundary for each element.*/

    /* The location property does not fetches proper information and may fail for different versions of android */
    private static void debugChildViewIds1(View view, String logtag, int spaces) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            countElements+=group.getChildCount();
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                Log.v(logtag, padString("<GUIObject><Name>" + child.getClass().getSimpleName() +"</Name><Id>"+ child.getId() +"</Id><Location x=\""+child.getX()+"\" y=\""+child.getY()+"\" bottom=\""+child.getBottom()+"\" right=\""+child.getRight()+"\" />", spaces));
                debugChildViewIds1(child, logtag, spaces + 1);
                Log.v(logtag,padString("</GUIObject>",spaces));
            }
        }
    }


    private static String padString(String str, int noOfSpaces)
    {
        if (noOfSpaces <= 0)
        {
            return str;
        }
        StringBuilder builder = new StringBuilder(str.length() + noOfSpaces);
        for (int i = 0; i < noOfSpaces; i++) {
            builder.append(' ');
        }
        return builder.append(str).toString();
    }
}
