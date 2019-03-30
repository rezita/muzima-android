/*
 * Copyright (c) The Trustees of Indiana University, Moi University
 * and Vanderbilt University Medical Center. All Rights Reserved.
 *
 * This version of the code is licensed under the MPL 2.0 Open Source license
 * with additional health care disclaimer.
 * If the user is an entity intending to commercialize any application that uses
 *  this code in a for-profit venture,please contact the copyright holder.
 */

package com.muzima.view;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.muzima.MuzimaApplication;
import com.muzima.R;
import com.muzima.controller.SmartCardController;

public class BaseActivity extends AppCompatActivity {

    private DefaultMenuDropDownHelper dropDownHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        dropDownHelper = new DefaultMenuDropDownHelper(this);
    }

    private void setupActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public void onUserInteraction() {
        ((MuzimaApplication) getApplication()).restartTimer();
        super.onUserInteraction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MuzimaApplication) getApplication()).setCurrentActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = dropDownHelper.onOptionsItemSelected(item);
        return result || super.onOptionsItemSelected(item);
    }

    void removeSettingsMenu(Menu menu) {
        dropDownHelper.removeSettingsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem syncSHRMenuItem = menu.findItem(R.id.menu_SHR_data_sync);
        if(syncSHRMenuItem != null) {
            try {
                int count = ((MuzimaApplication)getApplicationContext()).getSmartCardController().getSmartCardRecordWithNonUploadedData().size();
                if(count > 0){
                    syncSHRMenuItem.setVisible(true);
                    syncSHRMenuItem.setTitle(getString(R.string.menu_SHR_data_sync, count));
                } else {
                    syncSHRMenuItem.setVisible(false);
                }
            } catch (SmartCardController.SmartCardRecordFetchException e) {
                Log.e(BaseFragmentActivity.class.getSimpleName(), "Error fetching smartcard records");
            }
        }
        return true;
    }
}
