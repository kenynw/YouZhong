package com.liaopeikun.youzhong.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.liaopeikun.youzhong.SearchActivity;
import com.liaopeikun.youzhong.SendTextToClipboard;
import com.liaopeikun.youzhong.AddTorrentActivity;
import com.liaopeikun.youzhong.R;
import com.liaopeikun.youzhong.core.Torrent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (c) 2017/8/8. LiaoPeiKun Inc. All rights reserved.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context mContext;

    private List<Torrent> mTorrentList;

    public SearchAdapter(Context context, List<Torrent> torrentList) {
        mContext = context;
        mTorrentList = torrentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_torrent_search, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Torrent torrent = mTorrentList.get(position);
        holder.mTvTorrentName.setText(torrent.getName());
        holder.mTvTorrentSize.setText(torrent.getTotalSize());
        holder.mTvTorrentDate.setText(torrent.getUpdateTime());
        holder.mBtnTorrentCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SendTextToClipboard.class);
                intent.putExtra(Intent.EXTRA_TEXT, torrent.getDownloadPath());
                mContext.startActivity(intent);
            }
        });
        holder.mBtnTorrentXunlei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(torrent.getDownloadPath()));
                intent.addCategory("android.intent.category.DEFAULT");
                mContext.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, AddTorrentActivity.class);
                i.putExtra(AddTorrentActivity.TAG_URI, Uri.parse(torrent.getDownloadPath()));
                ((AppCompatActivity) mContext).startActivityForResult(i, SearchActivity.ADD_TORRENT_REQUEST);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTorrentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_torrent_name)
        TextView mTvTorrentName;

        @BindView(R.id.btn_torrent_copy)
        Button mBtnTorrentCopy;

        @BindView(R.id.btn_torrent_download)
        Button mBtnTorrentDownload;

        @BindView(R.id.btn_torrent_xunlei)
        Button mBtnTorrentXunlei;

        @BindView(R.id.tv_torrent_size)
        TextView mTvTorrentSize;

        @BindView(R.id.tv_torrent_date)
        TextView mTvTorrentDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
