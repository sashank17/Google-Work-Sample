package com.google;

import java.util.ArrayList;
import java.util.HashMap;

/** A class used to represent a Playlist */
class VideoPlaylist {

    private final String playlistTitle;
    private final ArrayList<Video> playlistVideos;

    VideoPlaylist(String playlistTitle, ArrayList<Video> playlistVideos) {
        this.playlistTitle = playlistTitle;
        this.playlistVideos = playlistVideos;
    }

    /** Returns the title of the playlist. */
    String getPlaylistTitle() {
        return playlistTitle;
    }

    /** Returns a list of videos in the playlist */
    ArrayList<Video> getPlaylistVideos() {
        return playlistVideos;
    }

    void addVideos(Video video) {
        playlistVideos.add(video);
    }

    boolean videoInPlaylist(Video video) {
        return playlistVideos.contains(video);
    }

    void removeVideos(Video video) {
        playlistVideos.remove(video);
    }

    void clear() {
        playlistVideos.clear();
    }

}
