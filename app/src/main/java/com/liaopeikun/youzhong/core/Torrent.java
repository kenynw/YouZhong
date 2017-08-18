/*
 * Copyright (C) 2016 Yaroslav Pronin <proninyaroslav@mail.ru>
 *
 * This file is part of LibreTorrent.
 *
 * LibreTorrent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreTorrent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreTorrent.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.liaopeikun.youzhong.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * The class encapsulates the torrent file and its meta information.
 */

public class Torrent implements Parcelable, Comparable<Torrent>
{
    /* Usually torrent SHA-1 hash */
    private String id;
    /* Path to the torrent file */
    private String torrentFile;
    private String downloadPath;
    /*
     * The index position in array must be
     * equal to the priority position in array
     */
    private List<Integer> filePriorities;
    private String torrentName;
    private String totalSize; // 文件大小
    private String updateTime; // 上传时间
    private boolean sequentialDownload = false;
    private boolean finished = false;
    private boolean paused = false;

    public Torrent(String id, String torrentName,
                   Collection<Integer> filePriorities,
                   String downloadPath)
    {
        this(id, null, torrentName, filePriorities, downloadPath);
    }

    public Torrent(String id, String torrentFile,
                   String torrentName,
                   Collection<Integer> filePriorities,
                   String downloadPath)
    {
        this.id = id;
        this.torrentFile = torrentFile;
        this.torrentName = torrentName;
        this.filePriorities = new ArrayList<>(filePriorities);
        this.downloadPath = downloadPath;
    }

    public Torrent() {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return torrentName;
    }

    public void setName(String name)
    {
        torrentName = name;
    }

    public String getTorrentFilePath()
    {
        return torrentFile;
    }

    public void setTorrentFilePath(String path)
    {
        torrentFile = path;
    }

    public List<Integer> getFilePriorities()
    {
        return filePriorities;
    }

    public void setFilePriorities(Collection<Integer> priorities)
    {
        filePriorities = new ArrayList<>(priorities);
    }

    public String getDownloadPath()
    {
        return downloadPath;
    }

    public void setDownloadPath(String path)
    {
        downloadPath = path;
    }

    public void setSequentialDownload(boolean sequential)
    {
        sequentialDownload = sequential;
    }

    public boolean isSequentialDownload()
    {
        return sequentialDownload;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public void setFinished(boolean finished)
    {
        this.finished = finished;
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(boolean paused)
    {
        this.paused = paused;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int compareTo(Torrent another)
    {
        return torrentName.compareTo(another.getName());
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof Torrent && (o == this || id.equals(((Torrent) o).id));
    }

    @Override
    public String toString()
    {
        return "Torrent{" +
                "id='" + id + '\'' +
                ", torrentFile='" + torrentFile + '\'' +
                ", downloadPath='" + downloadPath + '\'' +
                ", filePriorities=" + filePriorities +
                ", torrentName='" + torrentName + '\'' +
                ", sequentialDownload=" + sequentialDownload +
                ", finished=" + finished +
                ", paused=" + paused +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.torrentFile);
        dest.writeString(this.downloadPath);
        dest.writeList(this.filePriorities);
        dest.writeString(this.torrentName);
        dest.writeString(this.totalSize);
        dest.writeString(this.updateTime);
        dest.writeByte(this.sequentialDownload ? (byte) 1 : (byte) 0);
        dest.writeByte(this.finished ? (byte) 1 : (byte) 0);
        dest.writeByte(this.paused ? (byte) 1 : (byte) 0);
    }

    protected Torrent(Parcel in) {
        this.id = in.readString();
        this.torrentFile = in.readString();
        this.downloadPath = in.readString();
        this.filePriorities = new ArrayList<Integer>();
        in.readList(this.filePriorities, Integer.class.getClassLoader());
        this.torrentName = in.readString();
        this.totalSize = in.readString();
        this.updateTime = in.readString();
        this.sequentialDownload = in.readByte() != 0;
        this.finished = in.readByte() != 0;
        this.paused = in.readByte() != 0;
    }

    public static final Creator<Torrent> CREATOR = new Creator<Torrent>() {
        @Override
        public Torrent createFromParcel(Parcel source) {
            return new Torrent(source);
        }

        @Override
        public Torrent[] newArray(int size) {
            return new Torrent[size];
        }
    };
}
