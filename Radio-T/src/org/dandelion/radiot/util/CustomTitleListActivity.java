package org.dandelion.radiot.util;

import org.dandelion.radiot.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public abstract class CustomTitleListActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupTitleBar();
    }

    private void setupTitleBar() {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        TextView titleText = (TextView) findViewById(R.id.titlebar_title);
        titleText.setText(getTitle());
    }

}
