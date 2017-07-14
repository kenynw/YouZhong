package org.proninyaroslav.libretorrent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.proninyaroslav.libretorrent.core.utils.Utils;

/**
 * Copyright (c) 2017/7/13. LiaoPeiKun Inc. All rights reserved.
 */

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Utils.isDarkTheme(getApplicationContext())) {
            setTheme(R.style.AppTheme_Dark);
        }



    }

}
