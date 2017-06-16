package com.mypinpad.metasnoop;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO;
import static android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY;
import static android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String NONE = "NONE";

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {

                PackageManager packageManager = getPackageManager();
                List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

                Log.i(TAG, packages.size() + " Packages installed");
                StringBuilder sb;
                for (PackageInfo packageInfo : packages) {
                    ApplicationInfo application = packageInfo.applicationInfo;

                    sb = new StringBuilder();
                    sb.append(String.format("\n\tPackage name: %s", application.packageName));
                    sb.append(String.format("\n\tApplication class: %s", application.className));
                    sb.append(String.format("\n\tData dir: %s", application.dataDir));
                    sb.append(String.format("\n\tNative library dir: %s", application.nativeLibraryDir));
                    sb.append(String.format("\n\tPermission required: %s", application.permission));
                    sb.append(String.format("\n\tPublic source dir: %s", application.publicSourceDir));
                    sb.append(String.format("\n\tTask affinity: %s", application.taskAffinity));
                    sb.append(String.format("\n\tTarget SDK version: %s", application.targetSdkVersion));
                    sb.append(String.format("\n\tVersion Name: %s", packageInfo.versionName));
                    sb.append(String.format("\n\tVersion Code: %d", packageInfo.versionCode));
                    sb.append(String.format("\n\tInstaller: %s", packageManager.getInstallerPackageName(application.packageName)));
                    sb.append(String.format("\n\tRequested features: %s", formatFeatures(packageInfo.reqFeatures)));
                    sb.append(String.format("\n\tRequested permissions: %s", formatRequestedPermissions(packageInfo.requestedPermissions)));
                    sb.append(String.format("\n\tPermissions: %s", formatPermissions(packageInfo.permissions)));
                    sb.append(String.format("\n\tInstall location: %s", formatInstallLocation(packageInfo.installLocation)));
                    sb.append(String.format("\n\tFirst install date: %s", formatDate(packageInfo.firstInstallTime)));
                    sb.append(String.format("\n\tLast update date: %s", formatDate(packageInfo.lastUpdateTime)));


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        sb.append(String.format("\n\tMinimum SDK version: %s", application.minSdkVersion));
                    }

                    Log.i(TAG, String.format("%s {%s\n}\n***\n\n", application.packageName, sb.toString()));
                }

            }

        });
    }

    private String formatPermissions(PermissionInfo[] permissions) {
        if (permissions == null || permissions.length == 0) {
            return NONE;
        }

        StringBuilder sb = new StringBuilder();
        for (PermissionInfo permission : permissions) {
            sb.append(String.format("\n%s,", permission));
        }
        return sb.toString();
    }

    private String formatRequestedPermissions(String[] requestedPermissions) {
        if(requestedPermissions == null || requestedPermissions.length == 0) {
            return NONE;
        }

        StringBuilder sb = new StringBuilder();
        for(String permission : requestedPermissions) {
            sb.append(String.format("\n%s,", permission));
        }
        return sb.toString();
    }

    private String formatFeatures(FeatureInfo[] reqFeatures) {
        if (reqFeatures == null || reqFeatures.length == 0) {
            return NONE;
        }

        StringBuilder sb = new StringBuilder();
        for (FeatureInfo featureInfo : reqFeatures) {
            sb.append(String.format("\n%s,", featureInfo.name));
        }
        return sb.toString();
    }

    private String formatInstallLocation(int installLocation) {
        switch (installLocation) {
            case INSTALL_LOCATION_AUTO:
                return "INSTALL_LOCATION_AUTO";
            case INSTALL_LOCATION_INTERNAL_ONLY:
                return "INSTALL_LOCATION_INTERNAL_ONLY";
            case INSTALL_LOCATION_PREFER_EXTERNAL:
                return "INSTALL_LOCATION_PREFER_EXTERNAL";
            default:
                return "UNKNOWN LOCATION : " + installLocation;
        }
    }

    private String formatDate(long millisTimestamp) {
        return dateFormatter.format(new Date(millisTimestamp));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
