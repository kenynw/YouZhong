package com.liaopeikun.youzhong;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.liaopeikun.youzhong.adapters.SearchAdapter;
import com.liaopeikun.youzhong.core.Torrent;
import com.liaopeikun.youzhong.core.utils.Utils;
import com.liaopeikun.youzhong.customviews.RecyclerViewDividerDecoration;
import com.liaopeikun.youzhong.dialogs.SpinnerProgressDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (c) 2017/7/13. LiaoPeiKun Inc. All rights reserved.
 */

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SwipeRefreshLayout.OnRefreshListener {

    @SuppressWarnings("unused")
    public static final String TAG = SearchActivity.class.getSimpleName();

    private static final String TAG_DIALOG_PROGRESS = "progress";

    public static final int ADD_TORRENT_REQUEST = 1;
    @BindView(R.id.swipe_layout_search)
    SwipeRefreshLayout mSwipeLayout;

    @BindView(R.id.recy_search_results)
    RecyclerView mRecySearchResults;

    private SearchView mSearchView;

    private SpinnerProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Utils.isDarkTheme(getApplicationContext())) {
            setTheme(R.style.AppTheme_Dark);
        }

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Utils.showColoredStatusBar_KitKat(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mSwipeLayout.setOnRefreshListener(this);
        mRecySearchResults.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecySearchResults.setHasFixedSize(true);
        TypedArray a = obtainStyledAttributes(new TypedValue().data, new int[]{R.attr.divider});
        mRecySearchResults.addItemDecoration(new RecyclerViewDividerDecoration(a.getDrawable(0)));
        a.recycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        mSearchView.onActionViewExpanded();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.hint_search));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public boolean onQueryTextSubmit(final String s) {
        new AsyncTask<Void, Void, List<Torrent>>() {

            @Override
            protected void onPreExecute() {
                showProgress();
            }

            @Override
            protected void onPostExecute(List<Torrent> torrents) {
                mRecySearchResults.setAdapter(new SearchAdapter(SearchActivity.this, torrents));
                hideProgress();
            }

            @Override
            protected List<Torrent> doInBackground(Void... params) {
                List<Torrent> list = new ArrayList<>();
                try {
                    Document document = Jsoup.connect("https://www.torrentkitty.tv/search/" + s).get();
                    Element result = document.getElementById("archiveResult");
                    Elements tr = result.getElementsByTag("tr");

                    for (int i = 1; i < tr.size(); i++) {
                        Element element = tr.get(i);
                        Elements tds = element.getElementsByTag("td");
//                        Log.d(TAG, "name: " + tds.get(0).text() + ", size: " + tds.get(1).text() + ", date: " + tds.get(2).text());

                        Torrent torrent = new Torrent();
                        torrent.setName(tds.select(".name").text());
                        torrent.setTotalSize(tds.get(1).text());
                        torrent.setUpdateTime(tds.get(2).text());
                        String url = tds.select("a[rel=magnet]").attr("href");
                        Log.d(TAG, url);
                        torrent.setDownloadPath(url);

                        list.add(torrent);
//                        torrent.totalBytes = Long.valueOf(tds.get(1).text());
                    }
//                    new Handler().
//                    Log.d(TAG, result.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return list;
            }

        }.execute();

        mSearchView.clearFocus();

        return true;
    }

    @Override
    public boolean onQueryTextChange(final String s) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TORRENT_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data.hasExtra(AddTorrentActivity.TAG_RESULT_TORRENT)) {
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mProgress = (SpinnerProgressDialog) getFragmentManager().findFragmentByTag(TAG_DIALOG_PROGRESS);
    }

    private void showProgress() {
        mProgress = SpinnerProgressDialog.newInstance(
                R.string.decode_torrent_progress_title,
                getString(R.string.decode_torrent_default_message),
                0,
                true,
                true
        );
        mProgress.show(getFragmentManager(), TAG_DIALOG_PROGRESS);
    }

    private void hideProgress() {
        if (mProgress != null) {
            try {
                mProgress.dismiss();
            } catch (Exception e) {

            }
        }
        mProgress = null;
    }

}
