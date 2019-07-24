package example.com.seaphish.uicarise.writenodesxml;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import example.com.seaphish.uicarise.hierachyviewerlibrary.HierarchyViewerClass;


/* This is an activity which does not have any XML layout file
 * This is used to generate the phishing activity and is called from SimpleActivity.java when a button
   * is clicked to generate the phishing GUI*/
public class GeneratedActivity extends AppCompatActivity {

    Random rand = new Random();
    Integer newId = 0;
    private static int offset_y = 100;

    private GenerateMedata[] customObjects = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String newFileName = "";

        /* fetch the intent to get the app that needs to be generated. */
        Intent myIntent = getIntent();
        if (myIntent != null && myIntent.getExtras() != null)
            newFileName = myIntent.getExtras().getString("PkgName");

        System.out.println("New File Name: " + newFileName);

        /* call the function to create the GUI - paint each element as they appear from the stored file. */
        FrameLayout ll = CallToCreate(newFileName, this.getApplicationContext());

        setContentView(ll);

        View thisView = this.findViewById(android.R.id.content);

        if(thisView != null)
        {
            System.out.println("View id by content: " + thisView);
            System.out.println("View class name: "+thisView.getClass().getSimpleName());
            //HierarchyViewerClass.debugViewIds(thisView,"newcheck");
            HierarchyViewerClass.debugViewIds(getWindow().getDecorView(), newFileName);
        }
        else
        {
            System.out.println("View decor: "+getWindow().getDecorView());
            HierarchyViewerClass.debugViewIds(getWindow().getDecorView(), newFileName);
        }
    }

    /* this method is used to create the action bar seperately for the app */
    LinearLayout createActionBar(GenerateMedata[] actionObject, final Context ctx , LinearLayout actionLL, Toolbar tb)
    {
        System.out.println("Inside create action bar");
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        for(int objCount = 0 ; objCount < actionObject.length ; objCount++)
        {
            if (actionObject[objCount] != null && actionObject[objCount].getClassName() != null &&
                    (!actionObject[objCount].getClassName().equals("")) &&
                    actionObject[objCount].getClassName().toLowerCase().contains("textview"))
            {
                TextView tt = new TextView(this);

                if (actionObject[objCount].getText() != null &&
                        (!actionObject[objCount].getText().equals("")) &&
                        (!actionObject[objCount].getText().equals("null"))) {
                    tt.setText(actionObject[objCount].getText());
                    System.out.println("Custom Text View : " + actionObject[objCount].getText());
                }
                if (actionObject[objCount].getContentDescription() != null && (!actionObject[objCount].getContentDescription().equals("")) && (!actionObject[objCount].getContentDescription().equals("null"))) {
                    tt.setContentDescription(actionObject[objCount].getContentDescription());
                    System.out.println("Custom Text View : content description " + actionObject[objCount].getContentDescription());
                }

                String[] tempRect = actionObject[objCount].getBoundsInScreen().split("\\(");
                String[] tempRect2 = tempRect[1].split(",");
                String[] tempRect3 = tempRect2[1].split("-");

                tt.setX(Integer.parseInt(tempRect2[0].trim()));
                System.out.println("Custom Text View Set X: " + tt.getX());


                tt.setY((Integer.parseInt(tempRect3[0].trim()) - 300));
                System.out.println("Custom Text View Set Y - 250: " + tt.getY());

                if(tt.getX() < 250){
                    tt.setTypeface(null, Typeface.BOLD);
                }

                //actionLL.addView(tt);
                //tb.addView(tt);
                tb.setTitle(actionObject[objCount].getText());
                //this.setTitle(actionObject[objCount].getText());
            }
            else if (actionObject[objCount] != null && actionObject[objCount].getClassName() != null
                    && (!actionObject[objCount].getClassName().equals("")) &&
                    actionObject[objCount].getClassName().toLowerCase().contains(".button"))
            {
                final Button bt = new Button(this);

                if (actionObject[objCount].getText() != null && (!actionObject[objCount].getText().equals("")) && (!actionObject[objCount].getText().equals("null")))
                {
                    bt.setText(actionObject[objCount].getText());
                    System.out.println("Custom Button Text : " + actionObject[objCount].getText());
                }
                if (actionObject[objCount].getContentDescription() != null && (!actionObject[objCount].getContentDescription().equals("")) && (!actionObject[objCount].getContentDescription().equals("null")))
                {
                    System.out.println("Custom Button Text : Content Description " + actionObject[objCount].getContentDescription());
                    bt.setContentDescription(actionObject[objCount].getContentDescription());
                    System.out.println("Custom button : text: "+bt.getText());

                    if(bt.getText().equals("")){
                        bt.setText(actionObject[objCount].getContentDescription());
                        System.out.println("Custom button : after text: "+bt.getText());
                    }
                    if (actionObject[objCount].getText() != null && (actionObject[objCount].getText().equals("") || actionObject[objCount].getText().equals("null"))) {
                        bt.setText(actionObject[objCount].getContentDescription());
                    }
                }

                String[] tempRect = actionObject[objCount].getBoundsInScreen().split("\\(");
                String[] tempRect2 = tempRect[1].split(",");
                String[] tempRect3 = tempRect2[1].split("-");


                bt.setX(Integer.parseInt(tempRect2[0].trim()));

                System.out.println("Custom Button Set X: " + bt.getX());


                bt.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));

                System.out.println("Custom Button Text Set Y - 250: " + bt.getY());


                bt.setEnabled(actionObject[objCount].getEnabled());
                bt.setFocusable(actionObject[objCount].getFocusable());
                bt.setClickable(actionObject[objCount].getClickable());

                int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                System.out.println("Custom Button : get width: " + tempWidth);
                System.out.println("Custom Button: get height: " + tempHeight);


                FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                bt.setLayoutParams(params1);

                //actionLL.addView(bt);
                tb.addView(bt);
            }
            else if (actionObject[objCount] != null && actionObject[objCount].getClassName() != null &&
                    (!actionObject[objCount].getClassName().equals("")) &&
                    actionObject[objCount].getClassName().toLowerCase().contains("imagebutton"))
            {
                ImageButton imgBtn = new ImageButton(this);


                String[] tempRect = actionObject[objCount].getBoundsInScreen().split("\\(");
                String[] tempRect2 = tempRect[1].split(",");
                String[] tempRect3 = tempRect2[1].split("-");


                imgBtn.setX(Integer.parseInt(tempRect2[0].trim()));
                System.out.println("Custom ImageButton Set X: " + imgBtn.getX());

                if(Integer.parseInt(tempRect3[0].trim()) > 250)
                {
                    imgBtn.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                    System.out.println("Custom ImageButton Text Set Y - 250: " + imgBtn.getY());
                }
                else
                {
                    imgBtn.setY((Integer.parseInt(tempRect3[0].trim()) ));
                    System.out.println("Custom ImageButton Text Set Y : " + imgBtn.getY());
                }

                String tempID = actionObject[objCount].getViewIdResName();
                imgBtn.setId(getID(tempID));

                imgBtn.setEnabled(actionObject[objCount].getEnabled());
                imgBtn.setFocusable(actionObject[objCount].getFocusable());
                imgBtn.setClickable(actionObject[objCount].getClickable());

                int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                System.out.println("Custom ImageButton : get width: " + tempWidth);
                System.out.println("Custom ImageButton: get height: " + tempHeight);


                FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                imgBtn.setLayoutParams(params1);

                //imgBtn.setImageResource(R.mipmap.ic_launcher);

                //actionLL.addView(imgBtn);
                tb.addView(imgBtn);
            }

            else if (actionObject[objCount] != null && actionObject[objCount].getClassName() != null &&
                    (!actionObject[objCount].getClassName().equals("")) &&
                    actionObject[objCount].getClassName().toLowerCase().contains("image"))
            {
                ImageView imgView = new ImageView(this);


                String[] tempRect = actionObject[objCount].getBoundsInScreen().split("\\(");
                String[] tempRect2 = tempRect[1].split(",");
                String[] tempRect3 = tempRect2[1].split("-");


                imgView.setX(Integer.parseInt(tempRect2[0].trim()));
                System.out.println("Custom ImageView Set X: " + imgView.getX());

                if(Integer.parseInt(tempRect3[0].trim()) > 250)
                {
                    imgView.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                    System.out.println("Custom ImageView Text Set Y - 250: " + imgView.getY());
                }
                else
                {
                    imgView.setY((Integer.parseInt(tempRect3[0].trim()) ));
                    System.out.println("Custom ImageView Text Set Y : " + imgView.getY());
                }

                String tempID = actionObject[objCount].getViewIdResName();
                imgView.setId(getID(tempID));


                imgView.setEnabled(actionObject[objCount].getEnabled());
                imgView.setFocusable(actionObject[objCount].getFocusable());
                imgView.setClickable(actionObject[objCount].getClickable());

                int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                System.out.println("Custom ImageView : get widht: " + tempWidth);
                System.out.println("Custom ImageView: get height: " + tempHeight);


                FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                imgView.setLayoutParams(params1);

                //imgView.setImageResource(R.mipmap.ic_launcher);

                //actionLL.addView(imgView);
                tb.addView(imgView);
            }

        }
        /*if(ab != null)
        {
            System.out.println("Inside action bar no null");
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            //ab.setCustomView(tb);
            ab.setCustomView(actionLL);
        }
        else
        {
            System.out.println("Action bar is null");
        }*/

        return actionLL;

    }

    /* generate an ID - unique for each GUI element.
    * By default if no ID is given to a GUI element in android,
    * the default value will be -1. This may make any other app suspicious about the phishing apps elements
    * thus we generate a unique ID for each element on the basis of the ID either fetched from the log or
    * we randomly assign  some value */
    Integer getID(String viewIdResName)
    {
        //int newId = 0 ;
        if(viewIdResName != null && !viewIdResName.equals("null") && !viewIdResName.equals("") &&
                viewIdResName.contains("//")){
            String[] ids = viewIdResName.split("//");
            if(ids.length > 1)
            {
                int i = 0;
                while( i < ids[1].length()) {
                    newId *= 10;
                    newId += ids[i].charAt(i++) - '0'; //Minus the ASCII code of '0' to get the value of the charAt(i++).
                }
                System.out.println("ViewIdResName: "+viewIdResName+" newId: "+newId );
                return newId;
            }
        }
        if(newId == 0){
            //Random rand = new Random();
            newId = rand.nextInt(10000000) + 1;
        }
        else{
            newId = newId + 2;
        }
        System.out.println("Without ViewIdResName: "+viewIdResName+" random newId: "+newId );
        return newId;
    }

    /* Bind each element to the GUI as traced by the accessibility service. */
    FrameLayout CallToCreate(String filename, final Context ctx)
    {
        int customCount = - 1;

        FrameLayout ll = new FrameLayout(ctx);

        GenerateMedata[] objects = new GenerateMedata[100];
        customObjects = new GenerateMedata[10];

        String filepath = "MyFileStorage";
        File myInternalFile = null;

        ContextWrapper contextWrapper = new ContextWrapper(this.getApplicationContext());
        File directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);
        System.out.println("Directory for reading file data: "+directory.getAbsolutePath());

        Toolbar tb = new Toolbar(this);
        FrameLayout.LayoutParams tbparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tb.setLayoutParams(tbparams);
        tb.setPopupTheme(R.style.customTheme);
        tb.setTitle("Test");

        this.setSupportActionBar(tb);
        ll.addView(tb);


        LinearLayout tbLL = new LinearLayout(this);
        //tbLL.setLayoutParams(tbparams);
        //tb.addView(tbLL);

        /* fetch the file name and then read the contents of this file */
        if (!filename.equals(""))
        {
            System.out.println("New file found : "+filename);
            myInternalFile = new File(directory, filename);

            int count = -1;

            try
            {
                FileInputStream fis = new FileInputStream(myInternalFile);
                InputStreamReader in = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(in);
                String strLine;

                /* Read the entire file and set the properties for each GUI element */
                while ((strLine = br.readLine()) != null)
                {
                    System.out.println("Data :" + strLine);

                    if (!strLine.equals("")) {
                        count++;
                        objects[count] = new GenerateMedata();
                        String[] tempArray = strLine.split(";");
                        String[] tempSplit;
                        if (tempArray != null && tempArray.length > 0)
                        {
                            for (int i = 0; i < tempArray.length; i++)
                            {
                                // System.out.println("Temp Array Split value: "+tempArray[i].toLowerCase().trim());
                                if (tempArray[i].toLowerCase().contains("android.view.accessibility.accessibilitynodeinfo"))
                                {
                                    tempSplit = tempArray[i].split("@");
                                    objects[count].setNodeId(tempSplit[1].toString().trim());

                                }
                                else if (tempArray[i].toLowerCase().contains("boundsinscreen"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setBoundsInScreen(tempSplit[1].toString().trim());
                                }
                                else if (tempArray[i].toLowerCase().contains("packagename"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setPackageName(tempSplit[1].toString().trim());
                                }
                                else if (tempArray[i].toLowerCase().contains("classname"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setClassName(tempSplit[1].toString().trim());
                                }
                                else if (tempArray[i].toLowerCase().contains("contentdescription"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setContentDescription(tempSplit[1].toString().trim());

                                    if (tempSplit[1].toString().toLowerCase().trim().contains("remember") || tempSplit[1].toString().toLowerCase().trim().contains("link"))
                                    {
                                        System.out.println("Content for chkbox or link: " + tempSplit[1].toString().trim());
                                    }

                                    if(tempSplit.length > 2)
                                    {
                                        for(int len = 2 ; len < tempSplit.length ; len++)
                                        {
                                            objects[count].setContentDescription(objects[count].getContentDescription() + " " + tempSplit[len].toString().trim());
                                        }
                                    }
                                }
                                else if (tempArray[i].trim().contains("viewIdResName:"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setViewIdResName(tempSplit[1].toString().trim());
                                }
                                else if (tempArray[i].trim().contains("text:"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setText(tempSplit[1].toString().trim());

                                    if(tempSplit.length>=2)
                                    {
                                        for(int len = 2 ; len < tempSplit.length ; len++)
                                        {
                                            //System.out.println("_________"+tempSplit[len].toString().trim());
                                            objects[count].setText(objects[count].getText() + " " + tempSplit[len].toString().trim());
                                        }
                                    }
                                }
                                else if (tempArray[i].toLowerCase().contains("checkable:"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setCheckable(Boolean.parseBoolean(tempSplit[1].toString().trim()));
                                }
                                else if (tempArray[i].toLowerCase().contains("checked:"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setChecked(Boolean.parseBoolean(tempSplit[1].toString().trim()));
                                }
                                else if (tempArray[i].toLowerCase().contains("focusable:"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setFocusable(Boolean.parseBoolean(tempSplit[1].toString().trim()));
                                }
                                else if (tempArray[i].toLowerCase().contains("focused:"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setFocused(Boolean.parseBoolean(tempSplit[1].toString().trim()));
                                }
                                else if (tempArray[i].toLowerCase().contains("selected:"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setSelected(Boolean.parseBoolean(tempSplit[1].toString().trim()));
                                }
                                else if (tempArray[i].toLowerCase().contains("clickable:"))
                                {
                                    tempSplit = tempArray[i].split(":");

                                    if (tempSplit[0].trim().equals("clickable"))
                                    {
                                        objects[count].setClickable(Boolean.parseBoolean(tempSplit[1].toString().trim()));

                                    }
                                }
                                else if (tempArray[i].toLowerCase().contains("enabled:"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setEnabled(Boolean.parseBoolean(tempSplit[1].toString().trim()));
                                }
                                else if (tempArray[i].toLowerCase().contains("scrollable:"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setScrollable(Boolean.parseBoolean(tempSplit[1].toString().trim()));
                                }
                                else if(tempArray[i].toLowerCase().contains("password:"))
                                {
                                    tempSplit = tempArray[i].split(":");
                                    objects[count].setPassword(Boolean.parseBoolean(tempSplit[1].toString().trim()));
                                }
                            }
                        }

                    }
                }

            }
            catch (Exception ex)
            {
                System.out.println("in file reading : exception:" + ex);
            }

            File checkFiles = contextWrapper.getFilesDir();

            if (checkFiles != null)
                System.out.println("files in get files in dir: " + checkFiles.getPath());

            List<String> addedNodes = new ArrayList();

            /* Once all the GUI elements information is traced , we can now bind each GUI element
             * as per their properties and class. The method used here to bind the GUI is code-behind methodology
              * we don't use XML which is generally used to bind the GUI for android app
              * It is easy to track the elements properties and bind as they appear at the specific location*/
            if (objects.length > 0)
            {
                for (int objCount = 0; objCount < count; objCount++)
                {
                    if (addedNodes.size() == 0 || (!(addedNodes.contains(objects[objCount].getNodeId()))))
                    {
                        addedNodes.add(objects[objCount].getNodeId());

                        if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("textview"))
                        {
                            final TextView tt = new TextView(this);

                            if (objects[objCount].getText() != null &&
                                    (!objects[objCount].getText().equals("")) && (!objects[objCount].getText().equals("null")))
                            {
                                tt.setText(objects[objCount].getText(), TextView.BufferType.SPANNABLE);
                                System.out.println("Text View getText: " + objects[objCount].getText());
                            }
                            if (objects[objCount].getContentDescription() != null
                                    && (!objects[objCount].getContentDescription().equals(""))
                                    && (!objects[objCount].getContentDescription().equals("null")))
                            {
                                tt.setContentDescription(objects[objCount].getContentDescription());
                                if(tt.getText() == null ||  tt.getText().equals("") || tt.getText().equals("null"))
                                {
                                    tt.setText(objects[objCount].getContentDescription(), TextView.BufferType.SPANNABLE);
                                    System.out.println("Text View getText : " + objects[objCount].getText() +" from content description");
                                }
                                System.out.println("Text View : content description " + objects[objCount].getContentDescription());
                            }

                            String tempID = objects[objCount].getViewIdResName();

                            tt.setId(getID(tempID));

                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");

                            tt.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("Text View Set X: " + tt.getX());

                            if(objects[objCount].getClickable())
                            {
                                //tt.setClickable(true);

                                String[] tempSplit = filename.split(".txt");

                                final File chkTxtEvent = new File(directory,tempSplit[0]+"_click_"+tt.getText().toString().replace(" ","")+".txt");
                                boolean exists = chkTxtEvent.exists();

                                System.out.println("File for textview : "+tt.getText().toString()+" with file name :"+chkTxtEvent.getName()+" exists: "+exists);
                                if(exists)
                                {
                                    tt.setMovementMethod(LinkMovementMethod.getInstance());
                                    Spannable mySpannable = (Spannable)tt.getText();
                                    ClickableSpan myClickableSpan = new ClickableSpan()
                                    {
                                        @Override
                                        public void onClick(View widget) {
                                            System.out.println("In myClickableSpan event for text view:"+ tt.getText()+" with filename: "+chkTxtEvent.getName());
                                            FrameLayout ll = CallToCreate(chkTxtEvent.getName(),ctx);
                                            setContentView(ll);
                                        }
                                    };
                                    mySpannable.setSpan(myClickableSpan, 0, tt.getText().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                    /*tt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view)
                                        {
                                            System.out.println("In setOnClickListener event for text view:"+ tt.getText()+" with filename: "+chkTxtEvent.getName());
                                            FrameLayout ll = CallToCreate(chkTxtEvent.getName(),ctx);
                                            setContentView(ll);
                                        }
                                    });*/
                                }
                            }
                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                tt.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("Text View Set Y - 250: " + tt.getY());
                                ll.addView(tt);
                            }
                            else
                            {
                                System.out.println("Part of custom action bar");
                                customCount++;
                                customObjects[customCount] = new GenerateMedata();
                                customObjects[customCount] = objects[objCount];
                                tt.setY((Integer.parseInt(tempRect3[0].trim())));
                                System.out.println("Text View Set Y : " + tt.getY());
                            }

                        }
                        else if(objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("textinputlayout"))
                        {

                            TextInputLayout txtInputLayout = new TextInputLayout(this);
                            EditText editText = new EditText(this);
                            // Add an ID to it

                            if (objects[objCount].getText() != null && (!objects[objCount].getText().equals("")) && (!objects[objCount].getText().equals("null")))
                            {
                                //et.setText(objects[objCount].getText());
                                System.out.println("InputLayout Edit Text : " + objects[objCount].getText());
                                editText.setHint(objects[objCount].getText());
                            }
                            else if (objects[objCount].getContentDescription() != null && (!objects[objCount].getContentDescription().equals("")) && (!objects[objCount].getContentDescription().equals("null")))
                            {
                                //et.setText(objects[objCount].getContentDescription());
                                editText.setHint(objects[objCount].getContentDescription());
                                System.out.println("InputLayout Edit Text : Content Description " + objects[objCount].getContentDescription());
                                editText.setContentDescription(objects[objCount].getContentDescription());
                            }

                            String[] tempRect_1 = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2_1 = tempRect_1[1].split(",");
                            String[] tempRect3_1 = tempRect2_1[1].split("-");

                            String tempID = objects[objCount+1].getViewIdResName();
                            editText.setId(getID(tempID));

                            String[] tempRect = objects[objCount+1].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");

//                            System.out.println("InputLayout Edit Text Left : SET X: " + tempRect2[0].trim());
//                            System.out.println("InputLayout Edit Text Right: " + tempRect3[1].trim());
//                            System.out.println("InputLayout Edit Text Top: SET Y " + tempRect3[0].trim());
//                            System.out.println("InputLayout Edit Text Bottom: " + tempRect2[2].replace(")", "").trim());

                            txtInputLayout.setX(Integer.parseInt(tempRect2_1[0].trim()));
                            editText.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("InputLayout EditText Set X: " + editText.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                editText.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("InputLayout EditText Set Y - 250: " + editText.getY());
                            }
                            else
                            {
                                editText.setY((Integer.parseInt(tempRect3[0].trim())));
                                txtInputLayout.setY((Integer.parseInt(tempRect3[0].trim())));
                                System.out.println("InputLayout EditText Set Y: " + editText.getY());
                            }

                            if(Integer.parseInt(tempRect3_1[0].trim()) > 250)
                            {
                                txtInputLayout.setY((Integer.parseInt(tempRect3_1[0].trim()) - offset_y));
                                System.out.println("InputLayout EditText Set Y - 250: " + txtInputLayout.getY());
                            }
                            else
                            {
                                txtInputLayout.setY((Integer.parseInt(tempRect3_1[0].trim())));
                                System.out.println("InputLayout EditText Set Y: " + txtInputLayout.getY());
                            }


                            int tempWidth_1 = Integer.parseInt(tempRect3_1[1].trim()) - Integer.parseInt(tempRect2_1[0].trim());
                            int tempHeight_1 = Integer.parseInt((tempRect2_1[2].replace(")", "").trim())) - Integer.parseInt(tempRect3_1[0].trim());

                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            FrameLayout.LayoutParams params1_1 = new FrameLayout.LayoutParams(tempWidth_1, tempHeight_1);
                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);

                            editText.setLayoutParams(params1);
                            txtInputLayout.setLayoutParams(params1_1);
                            System.out.println("InputLayout Edit text: get widht: " + tempWidth);
                            System.out.println("InputLayout Edit text: get height: " + tempHeight);

                            if(objects[objCount+1].getPassword())
                            {
                                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                editText.setHint("Password");
                                //editText.setSelection(editText.getText().length());
                            }

                            //txtInputLayout.addView(editText);
                            ll.addView(txtInputLayout);
                            ll.addView(editText);
                            objCount++;
                        }
                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("edittext"))
                        {
                            EditText et = new EditText(this);

                            if (objects[objCount].getText() != null && (!objects[objCount].getText().equals("")) && (!objects[objCount].getText().equals("null"))) {
                                //et.setText(objects[objCount].getText());
                                System.out.println("Edit Text : " + objects[objCount].getText());
                                et.setHint(objects[objCount].getText());
                            } else if (objects[objCount].getContentDescription() != null && (!objects[objCount].getContentDescription().equals("")) && (!objects[objCount].getContentDescription().equals("null"))) {
                                //et.setText(objects[objCount].getContentDescription());
                                et.setHint(objects[objCount].getContentDescription());
                                System.out.println("Edit Text : Content Description " + objects[objCount].getContentDescription());
                                et.setContentDescription(objects[objCount].getContentDescription());
                            }

                            String tempID = objects[objCount].getViewIdResName();
                            et.setId(getID(tempID));

                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");


//                            System.out.println("Edit Text Left: " + tempRect2[0].trim());
//                            System.out.println("Edit Text Right: " + tempRect3[1].trim());
//                            System.out.println("Edit Text Top: " + tempRect3[0].trim());
//                            System.out.println("Edit Text Bottom: " + tempRect2[2].replace(")", "").trim());

                            et.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("EditText Set X: " + et.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                et.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("EditText Set Y - 250: " + et.getY());
                            }
                            else
                            {
                                et.setY((Integer.parseInt(tempRect3[0].trim())));
                                System.out.println("EditText Set Y: " + et.getY());
                            }


                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());


                            et.setEnabled(true);
                            et.setActivated(true);

                            if(objects[objCount].getPassword())
                            {
                                et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                et.setHint("Password");
                                //editText.setSelection(editText.getText().length());
                            }

                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);

                            et.setLayoutParams(params1);
                            System.out.println("Edit text: get width: " + tempWidth);
                            System.out.println("Edit text: get heigth: " + tempHeight);

                            ll.addView(et);

                        }
                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains(".button"))
                        {
                            final Button bt = new Button(this);

                            if (objects[objCount].getText() != null && (!objects[objCount].getText().equals("")) && (!objects[objCount].getText().equals("null")))
                            {
                                bt.setText(objects[objCount].getText());
                                System.out.println("Button Text : " + objects[objCount].getText());
                            }
                            if (objects[objCount].getContentDescription() != null && (!objects[objCount].getContentDescription().equals("")) && (!objects[objCount].getContentDescription().equals("null")))
                            {
                                System.out.println("Button Text : Content Description " + objects[objCount].getContentDescription());
                                bt.setContentDescription(objects[objCount].getContentDescription());

                                if (objects[objCount].getText() != null && (objects[objCount].getText().equals("") || objects[objCount].getText().equals("null"))) {
                                    bt.setText(objects[objCount].getContentDescription());
                                }
                            }

                            String tempID = objects[objCount].getViewIdResName();
                            bt.setId(getID(tempID));

                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");

                            bt.setEnabled(objects[objCount].getEnabled());
                            bt.setClickable(objects[objCount].getClickable());
                            bt.setFocusable(objects[objCount].getFocusable());

                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            System.out.println("Button : get widht: " + tempWidth);
                            System.out.println("Button: get height: " + tempHeight);


                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                            bt.setLayoutParams(params1);

                            String[] tempSplit = filename.split(".txt");

                            final File chkBtnEvent = new File(directory,tempSplit[0]+"_click_"+bt.getText().toString().replace(" ","")+".txt");
                            boolean exists = chkBtnEvent.exists();

                            System.out.println("File for button : "+bt.getText()+" with filename :"+chkBtnEvent.getName().toString().replace(" ","")+" exists: "+exists);
                            if(exists)
                            {
                                bt.setOnClickListener(new View.OnClickListener() {

                                    public void onClick(View view) {
                                        System.out.println("In click event for button:"+ bt.getText()+" with filename: "+chkBtnEvent.getName());
                                        FrameLayout clickll = CallToCreate(chkBtnEvent.getName(),ctx);
                                        setContentView(clickll);
                                    }
                                });
                            }

                            bt.setX(Integer.parseInt(tempRect2[0].trim()));

                            System.out.println("Button Set X: " + bt.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                bt.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("Button Text Set Y - 250: " + bt.getY());
                                ll.addView(bt);
                            }
                            else
                            {
                                System.out.println("Part of custom action bar");
                                customCount++;
                                customObjects[customCount] = new GenerateMedata();
                                customObjects[customCount] = objects[objCount];
                                bt.setY((Integer.parseInt(tempRect3[0].trim())));
                                System.out.println("Button Text Set Y : " + bt.getY());
                            }



                        }
                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("checkbox"))
                        {
                            CheckBox chkBox = new CheckBox(this);

                            if (objects[objCount].getText() != null && (!objects[objCount].getText().equals("")) && (!objects[objCount].getText().equals("null")))
                            {
                                chkBox.setText(objects[objCount].getText());
                                System.out.println("Check Box Text : " + objects[objCount].getText());
                            }
                            if (objects[objCount].getContentDescription() != null && (!objects[objCount].getContentDescription().equals("")) && (!objects[objCount].getContentDescription().equals("null"))) {
                                System.out.println("CheckBox Text : Content Description " + objects[objCount].getContentDescription());
                                chkBox.setContentDescription(objects[objCount].getContentDescription());
                            }

                            String tempID = objects[objCount].getViewIdResName();
                            chkBox.setId(getID(tempID));

                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");


                            chkBox.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("CheckBox Set X: " + chkBox.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                chkBox.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("CheckBox Text Set Y - 250: " + chkBox.getY());
                            }
                            else
                            {
                                chkBox.setY((Integer.parseInt(tempRect3[0].trim()) ));
                                System.out.println("CheckBox Text Set Y : " + chkBox.getY());
                            }


                            chkBox.setFocusable(objects[objCount].getFocusable());
                            chkBox.setEnabled(objects[objCount].getEnabled());
                            chkBox.setClickable(objects[objCount].getClickable());
                            chkBox.setChecked(objects[objCount].getChecked());

                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            System.out.println("CheckBox : get widht: " + tempWidth);
                            System.out.println("CheckBox: get height: " + tempHeight);


                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                            chkBox.setLayoutParams(params1);

                            ll.addView(chkBox);

                        }

                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("listview"))
                        {
                            ListView lstView = new ListView(this);


                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");


                            lstView.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("ListView Set X: " + lstView.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                lstView.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("ListView Text Set Y - 250: " + lstView.getY());
                            }
                            else
                            {
                                lstView.setY((Integer.parseInt(tempRect3[0].trim()) ));
                                System.out.println("ListView Text Set Y : " + lstView.getY());
                            }


                            lstView.setEnabled(objects[objCount].getEnabled());
                            lstView.setFocusable(objects[objCount].getFocusable());
                            lstView.setClickable(objects[objCount].getClickable());

                            String tempID = objects[objCount].getViewIdResName();
                            lstView.setId(getID(tempID));


                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            System.out.println("ListView : get widht: " + tempWidth);
                            System.out.println("ListView: get height: " + tempHeight);


                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                            lstView.setLayoutParams(params1);

                            ll.addView(lstView);

                        }
                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("imagebutton"))
                        {
                            ImageButton imgBtn = new ImageButton(this);


                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");


                            imgBtn.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("ImageButton Set X: " + imgBtn.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                imgBtn.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("ImageButton Text Set Y - 250: " + imgBtn.getY());
                            }
                            else
                            {
                                imgBtn.setY((Integer.parseInt(tempRect3[0].trim()) ));
                                System.out.println("ImageButton Text Set Y : " + imgBtn.getY());
                            }

                            String tempID = objects[objCount].getViewIdResName();
                            imgBtn.setId(getID(tempID));

                            imgBtn.setEnabled(objects[objCount].getEnabled());
                            imgBtn.setFocusable(objects[objCount].getFocusable());
                            imgBtn.setClickable(objects[objCount].getClickable());

                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            System.out.println("ImageButton : get widht: " + tempWidth);
                            System.out.println("ImageButton: get height: " + tempHeight);


                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                            imgBtn.setLayoutParams(params1);

                            //imgBtn.setImageResource(R.mipmap.ic_launcher);


                            ll.addView(imgBtn);
                        }

                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("image"))
                        {
                            ImageView imgView = new ImageView(this);


                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");


                            imgView.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("ImageView Set X: " + imgView.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                imgView.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("ImageView Text Set Y - 250: " + imgView.getY());
                            }
                            else
                            {
                                imgView.setY((Integer.parseInt(tempRect3[0].trim()) ));
                                System.out.println("ImageView Text Set Y : " + imgView.getY());
                            }

                            String tempID = objects[objCount].getViewIdResName();
                            imgView.setId(getID(tempID));


                            imgView.setEnabled(objects[objCount].getEnabled());
                            imgView.setFocusable(objects[objCount].getFocusable());
                            imgView.setClickable(objects[objCount].getClickable());

                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            System.out.println("ImageView : get widht: " + tempWidth);
                            System.out.println("ImageView: get height: " + tempHeight);


                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                            imgView.setLayoutParams(params1);

                            //imgView.setImageResource(R.mipmap.ic_launcher);

                            ll.addView(imgView);

                        }
                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("radiobutton"))
                        {
                            RadioButton rbt = new RadioButton(this);

                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");


                            rbt.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("RadioButton Set X: " + rbt.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                rbt.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("RadioButton Text Set Y - 250: " + rbt.getY());
                            }
                            else
                            {
                                rbt.setY((Integer.parseInt(tempRect3[0].trim()) ));
                                System.out.println("FrameLayout Text Set Y : " + rbt.getY());
                            }

                            String tempID = objects[objCount].getViewIdResName();
                            rbt.setId(getID(tempID));


                            rbt.setEnabled(objects[objCount].getEnabled());
                            rbt.setClickable(objects[objCount].getClickable());
                            rbt.setFocusable(objects[objCount].getFocusable());

                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            System.out.println("RadioButton : get widht: " + tempWidth);
                            System.out.println("RadioButton: get height: " + tempHeight);


                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                            rbt.setLayoutParams(params1);

                            ll.addView(rbt);
                        }
                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("frameLayout"))
                        {
                            FrameLayout childFL = new FrameLayout(this);


                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");


                            childFL.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("FrameLayout Set X: " + childFL.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                childFL.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("FrameLayout Text Set Y - 250: " + childFL.getY());
                            }
                            else
                            {
                                childFL.setY((Integer.parseInt(tempRect3[0].trim()) ));
                                System.out.println("FrameLayout Text Set Y : " + childFL.getY());
                            }

                            String tempID = objects[objCount].getViewIdResName();
                            childFL.setId(getID(tempID));


                            childFL.setEnabled(objects[objCount].getEnabled());
                            childFL.setClickable(objects[objCount].getClickable());
                            childFL.setFocusable(objects[objCount].getFocusable());

                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            System.out.println("FrameLayout : get widht: " + tempWidth);
                            System.out.println("FrameLayout: get height: " + tempHeight);


                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                            childFL.setLayoutParams(params1);

                            ll.addView(childFL);
                        }
                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("drawerLayout"))
                        {
                            DrawerLayout childDL = new DrawerLayout(this);


                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");


                            childDL.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("DrawerLayout Set X: " + childDL.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                childDL.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("DrawerLayout Text Set Y - 250: " + childDL.getY());
                            }
                            else
                            {
                                childDL.setY((Integer.parseInt(tempRect3[0].trim()) ));
                                System.out.println("DrawerLayout Text Set Y : " + childDL.getY());
                            }

                            String tempID = objects[objCount].getViewIdResName();
                            childDL.setId(getID(tempID));


                            childDL.setEnabled(objects[objCount].getEnabled());
                            childDL.setClickable(objects[objCount].getClickable());
                            childDL.setFocusable(objects[objCount].getFocusable());

                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            System.out.println("DrawerLayout : get width: " + tempWidth);
                            System.out.println("DrawerLayout: get height: " + tempHeight);


                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                            childDL.setLayoutParams(params1);

                            ll.addView(childDL);
                        }
                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("relativeLayout"))
                        {
                            RelativeLayout childRL = new RelativeLayout(this);


                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");


                            childRL.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("RelativeLayout Set X: " + childRL.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                childRL.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("RelativeLayout Text Set Y - 250: " + childRL.getY());
                            }
                            else
                            {
                                childRL.setY((Integer.parseInt(tempRect3[0].trim()) ));
                                System.out.println("RelativeLayout Text Set Y : " + childRL.getY());
                            }

                            String tempID = objects[objCount].getViewIdResName();
                            childRL.setId(getID(tempID));


                            childRL.setEnabled(objects[objCount].getEnabled());
                            childRL.setClickable(objects[objCount].getClickable());
                            childRL.setFocusable(objects[objCount].getFocusable());

                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            System.out.println("RelativeLayout : get width: " + tempWidth);
                            System.out.println("RelativeLayout: get height: " + tempHeight);


                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                            childRL.setLayoutParams(params1);

                            ll.addView(childRL);
                        }
                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("scrollview"))
                        {
                            ScrollView sl = new ScrollView(this);

                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");


                            sl.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("ScrollView Set X: " + sl.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                sl.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("ScrollView Text Set Y - 250: " + sl.getY());
                            }
                            else
                            {
                                sl.setY((Integer.parseInt(tempRect3[0].trim()) ));
                                System.out.println("ScrollView Text Set Y : " + sl.getY());
                            }

                            String tempID = objects[objCount].getViewIdResName();
                            sl.setId(getID(tempID));


                            sl.setEnabled(objects[objCount].getEnabled());
                            sl.setClickable(objects[objCount].getClickable());
                            sl.setFocusable(objects[objCount].getFocusable());

                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            System.out.println("ScrollView : get widht: " + tempWidth);
                            System.out.println("ScrollView: get height: " + tempHeight);


                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                            sl.setLayoutParams(params1);

                            ll.addView(sl);
                        }
                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().toLowerCase().contains("linearlayout"))
                        {
                            LinearLayout childlL = new LinearLayout(this);


                            String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                            String[] tempRect2 = tempRect[1].split(",");
                            String[] tempRect3 = tempRect2[1].split("-");


                            childlL.setX(Integer.parseInt(tempRect2[0].trim()));
                            System.out.println("LinearLayout Set X: " + childlL.getX());

                            if(Integer.parseInt(tempRect3[0].trim()) > 250)
                            {
                                childlL.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                System.out.println("LinearLayout Text Set Y - 250: " + childlL.getY());
                            }
                            else
                            {
                                childlL.setY((Integer.parseInt(tempRect3[0].trim()) ));
                                System.out.println("LinearLayout Text Set Y : " + childlL.getY());
                            }

                            String tempID = objects[objCount].getViewIdResName();
                            childlL.setId(getID(tempID));


                            childlL.setEnabled(objects[objCount].getEnabled());
                            childlL.setClickable(objects[objCount].getClickable());
                            childlL.setFocusable(objects[objCount].getFocusable());

                            int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                            int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                            System.out.println("LinearLayout : get widht: " + tempWidth);
                            System.out.println("LinearLayout: get height: " + tempHeight);


                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                            childlL.setLayoutParams(params1);

                            ll.addView(childlL);
                        }

                        else if (objects[objCount] != null && objects[objCount].getClassName() != null &&
                                (!objects[objCount].getClassName().equals("")) &&
                                objects[objCount].getClassName().equals("android.view.View"))
                        {
                            System.out.println("View : getContentDescription: " + objects[objCount].getContentDescription());

                            System.out.println("View : getText: " + objects[objCount].getText());

                            System.out.println("View : getClickable: " + objects[objCount].getClickable());

                            if (objects[objCount].getClickable())
                            {
                                if (objects[objCount].getContentDescription() != null && (!objects[objCount].getContentDescription().equals("")) && (!objects[objCount].getContentDescription().equals("null")))
                                {
                                    if (objects[objCount].getContentDescription().toLowerCase().contains("checkbox"))
                                    {
                                        objCount++;
                                        CheckBox chkBox = new CheckBox(this);

                                        if (objects[objCount].getText() != null && (!objects[objCount].getText().equals("")) && (!objects[objCount].getText().equals("null")))
                                        {
                                            chkBox.setText(objects[objCount].getText());
                                            System.out.println("View Check Box Text : " + objects[objCount].getText());
                                        }
                                        else
                                        {
                                            System.out.println("View CheckBox Text : Content Description " + objects[objCount].getContentDescription());
                                            chkBox.setContentDescription(objects[objCount].getContentDescription());
                                            chkBox.setText(objects[objCount].getContentDescription());
                                        }

                                        String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                                        String[] tempRect2 = tempRect[1].split(",");
                                        String[] tempRect3 = tempRect2[1].split("-");

                                        chkBox.setX(Integer.parseInt(tempRect2[0].trim()));
                                        System.out.println("View CheckBox Set X: " + chkBox.getX());

                                        if(Integer.parseInt(tempRect3[0].trim()) > 250)
                                        {
                                            chkBox.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                            System.out.println("View CheckBox Text Set Y - 250: " + chkBox.getY());
                                        }
                                        else
                                        {
                                            chkBox.setY((Integer.parseInt(tempRect3[0].trim())));
                                            System.out.println("View CheckBox Text Set Y : " + chkBox.getY());

                                        }

                                        String tempID = objects[objCount].getViewIdResName();
                                        chkBox.setId(getID(tempID));

                                        chkBox.setEnabled(objects[objCount].getEnabled());
                                        chkBox.setFocusable(objects[objCount].getFocusable());
                                        chkBox.setClickable(objects[objCount].getClickable());
                                        chkBox.setChecked(objects[objCount].getChecked());

                                        int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                                        int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                                        System.out.println("View CheckBox : get widht: " + tempWidth);
                                        System.out.println("View CheckBox: get height: " + tempHeight);


                                        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                                        chkBox.setLayoutParams(params1);

                                        ll.addView(chkBox);
                                    }
                                    else if (objects[objCount].getContentDescription().toLowerCase().contains(".listview"))
                                    {
                                        objCount++;
                                        ListView lstView = new ListView(this);

                                        String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                                        String[] tempRect2 = tempRect[1].split(",");
                                        String[] tempRect3 = tempRect2[1].split("-");

                                        lstView.setX(Integer.parseInt(tempRect2[0].trim()));
                                        System.out.println("View ListView Set X: " + lstView.getX());

                                        if(Integer.parseInt(tempRect3[0].trim()) > 250)
                                        {
                                            lstView.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                            System.out.println("View ListView Text Set Y - 250: " + lstView.getY());
                                        }
                                        else
                                        {
                                            lstView.setY((Integer.parseInt(tempRect3[0].trim())));
                                            System.out.println("View ListView Text Set Y : " + lstView.getY());

                                        }
                                        String tempID = objects[objCount].getViewIdResName();
                                        lstView.setId(getID(tempID));


                                        lstView.setEnabled(objects[objCount].getEnabled());
                                        lstView.setFocusable(objects[objCount].getFocusable());
                                        lstView.setClickable(objects[objCount].getClickable());

                                        int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                                        int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                                        System.out.println("View ListView : get widht: " + tempWidth);
                                        System.out.println("View ListView: get height: " + tempHeight);


                                        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                                        lstView.setLayoutParams(params1);

                                        ll.addView(lstView);
                                    }

                                    else if (objects[objCount].getContentDescription().toLowerCase().contains(".imagebutton"))
                                    {
                                        objCount++;
                                        ImageButton imgBtn = new ImageButton(this);

                                        String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                                        String[] tempRect2 = tempRect[1].split(",");
                                        String[] tempRect3 = tempRect2[1].split("-");

                                        imgBtn.setX(Integer.parseInt(tempRect2[0].trim()));
                                        System.out.println("View ImageButton Set X: " + imgBtn.getX());

                                        if(Integer.parseInt(tempRect3[0].trim()) > 250)
                                        {
                                            imgBtn.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                            System.out.println("View ImageButton Text Set Y - 250: " + imgBtn.getY());
                                        }
                                        else
                                        {
                                            imgBtn.setY((Integer.parseInt(tempRect3[0].trim())));
                                            System.out.println("View ImageButton Text Set Y : " + imgBtn.getY());
                                        }

                                        String tempID = objects[objCount].getViewIdResName();
                                        imgBtn.setId(getID(tempID));

                                        imgBtn.setEnabled(objects[objCount].getEnabled());
                                        imgBtn.setFocusable(objects[objCount].getFocusable());
                                        imgBtn.setClickable(objects[objCount].getClickable());

                                        int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                                        int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                                        System.out.println("View ImageButton : get widht: " + tempWidth);
                                        System.out.println("View ImageButton: get height: " + tempHeight);


                                        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                                        imgBtn.setLayoutParams(params1);

                                        ll.addView(imgBtn);
                                    }

                                    else if (objects[objCount].getContentDescription().toLowerCase().contains(".image"))
                                    {
                                        objCount++;
                                        ImageView imgView = new ImageView(this);

                                        String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                                        String[] tempRect2 = tempRect[1].split(",");
                                        String[] tempRect3 = tempRect2[1].split("-");

                                        imgView.setX(Integer.parseInt(tempRect2[0].trim()));
                                        System.out.println("View ImageView Set X: " + imgView.getX());

                                        if(Integer.parseInt(tempRect3[0].trim()) > 250)
                                        {
                                            imgView.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                            System.out.println("View ImageView Text Set Y - 250: " + imgView.getY());
                                        }
                                        else
                                        {
                                            imgView.setY((Integer.parseInt(tempRect3[0].trim())));
                                            System.out.println("View ImageView Text Set Y : " + imgView.getY());
                                        }

                                        String tempID = objects[objCount].getViewIdResName();
                                        imgView.setId(getID(tempID));

                                        imgView.setEnabled(objects[objCount].getEnabled());
                                        imgView.setFocusable(objects[objCount].getFocusable());
                                        imgView.setClickable(objects[objCount].getClickable());

                                        int tempWidth = Integer.parseInt(tempRect3[1].trim()) - Integer.parseInt(tempRect2[0].trim());
                                        int tempHeight = Integer.parseInt((tempRect2[2].replace(")", "").trim())) - Integer.parseInt(tempRect3[0].trim());

                                        System.out.println("View ImageView : get width: " + tempWidth);
                                        System.out.println("View ImageView: get height: " + tempHeight);


                                        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(tempWidth, tempHeight);
                                        imgView.setLayoutParams(params1);

                                        //imgView.setImageResource(R.mipmap.ic_launcher);

                                        ll.addView(imgView);
                                    }

                                    else if (objects[objCount].getContentDescription().toLowerCase().contains("separator"))
                                    {
                                        //objCount++;
                                        TextView tt = new TextView(this);

                                        System.out.println("View Text View : content description: " + objects[objCount].getContentDescription());
                                        tt.setContentDescription(objects[objCount].getContentDescription());
                                        tt.setText("|");

                                        String tempID = objects[objCount].getViewIdResName();
                                        tt.setId(getID(tempID));

                                        String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                                        String[] tempRect2 = tempRect[1].split(",");
                                        String[] tempRect3 = tempRect2[1].split("-");


                                        tt.setX(Integer.parseInt(tempRect2[0].trim()));
                                        System.out.println("View Text Separator Set X: " + tt.getX());

                                        if(Integer.parseInt(tempRect3[0].trim()) > 250)
                                        {
                                            tt.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                            System.out.println("View Text Separator Set Y - 250: " + tt.getY());
                                        }
                                        else
                                        {
                                            tt.setY((Integer.parseInt(tempRect3[0].trim())));
                                            System.out.println("View Text Separator Set Y: " + tt.getY());
                                        }

                                        ll.addView(tt);

                                    }
                                    else
                                    {
                                        TextView tt = new TextView(this);

                                        if (objects[objCount].getText() != null && (!objects[objCount].getText().equals("")) && (!objects[objCount].getText().equals("null"))) {
                                            tt.setText(objects[objCount].getText());
                                            System.out.println("View Text View : " + objects[objCount].getText());
                                        } else {
                                            tt.setContentDescription(objects[objCount].getContentDescription());
                                            System.out.println("View Text View : content description " + objects[objCount].getContentDescription());
                                            tt.setText(objects[objCount].getContentDescription());
                                        }

                                        String tempID = objects[objCount].getViewIdResName();
                                        tt.setId(getID(tempID));

                                        String[] tempRect = objects[objCount].getBoundsInScreen().split("\\(");
                                        String[] tempRect2 = tempRect[1].split(",");
                                        String[] tempRect3 = tempRect2[1].split("-");


                                        tt.setX(Integer.parseInt(tempRect2[0].trim()));
                                        System.out.println("View TextView Set X: " + tt.getX());

                                        if(Integer.parseInt(tempRect3[0].trim()) > 250)
                                        {
                                            tt.setY((Integer.parseInt(tempRect3[0].trim()) - offset_y));
                                            System.out.println("View TextView Set Y - 250: " + tt.getY());
                                            ll.addView(tt);
                                        }
                                        else
                                        {
                                            tt.setY((Integer.parseInt(tempRect3[0].trim())));
                                            System.out.println("Part of custom action bar");
                                            customCount++;
                                            customObjects[customCount] = new GenerateMedata();
                                            customObjects[customCount] = objects[objCount];
                                            tt.setY((Integer.parseInt(tempRect3[0].trim())));
                                            System.out.println("View TextView Set Y : " + tt.getY());
                                        }
                                    }
                                }
                            }

                    /*System.out.println("View : getContentDescription: " +objects[objCount].getContentDescription());
                    System.out.println("View : getEnabled: " +objects[objCount].getEnabled());
                    System.out.println("View : getFocusable: " +objects[objCount].getFocusable());
                    System.out.println("View : getCheckable: " +objects[objCount].getCheckable());
                    System.out.println("View : getText: " +objects[objCount].getText());
                    System.out.println("View : getSelected: " +objects[objCount].getSelected());*/

                        }
                    }
                    else if (addedNodes.equals(objects[objCount].getNodeId()))
                    {
                        System.out.println("Node with Id: " + objects[objCount].getNodeId() + " is already added in UI");

                    }
                }
            }

        }


        System.out.println("Custom Count: "+customCount);

        if(customCount > -1){
            createActionBar(customObjects,ctx,tbLL,tb);
        }

        return  ll;
    }
}

