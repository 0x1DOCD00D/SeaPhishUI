package example.com.seaphish.uicarise.writenodesxml;

import android.app.IntentService;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

/* This is a sample intent service - running in background
 *  */
public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    /* This method will be called once the service is started
     * we call another function here - GetHashCodeforApps */
    @Override
    protected void onHandleIntent(Intent intent) {
        GetHashCodeforApps();
    }

    /* This function fetches the list of all the app installed on the smartphone
     * and fetch all the certificate and signature details */
    private void GetHashCodeforApps()
    {
        ContextWrapper contextWrapper = new ContextWrapper(this.getApplicationContext());
        StringBuilder sb = new StringBuilder();

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);

        /* Start : Here this code snipper - I am trying to fetch the app as a file and store this file
        * on the phone's memory. This is a partial function - still needs to be tested. */
        File file = null;
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = getPackageManager().queryIntentActivities(mainIntent, 0);
        for (ResolveInfo info : apps) {

            /* fetch the apk file only for one of the apps - example if the smartphone has Chase Bank app
             * we try to save that apk as a file in the phones memory - which requires no permission */
            if(info != null && info.activityInfo != null && info.activityInfo.taskAffinity != null &&
                    info.activityInfo.taskAffinity.contains("chase"))
            {
                file = new File(info.activityInfo.applicationInfo.publicSourceDir,"apkfile");
                break;
            }

        }

        /* End : Here this code snipper - I am trying to fetch the app as a file and store this file
        * on the phone's memory. This is a partial function - still needs to be tested. */


        /* get the PackageManager instance and fetch the package list- i.e. the apps list */
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
}
