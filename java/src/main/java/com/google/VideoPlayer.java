package com.google;

import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;

  ArrayList<String> ids = new ArrayList<>();
  ArrayList<String> titles = new ArrayList<>();
  ArrayList<List<String>> tags = new ArrayList<>();

  ArrayList<VideoPlaylist> videoPlaylists = new ArrayList<>();

  ArrayList<String> flaggedVideos = new ArrayList<>();

  private boolean currentlyPlaying, currentlyPaused;
  private String currentlyPlayingVideo;



  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();

    for (Video video: videoLibrary.getVideos()) {
      titles.add(video.getTitle());
      ids.add(video.getVideoId());
      tags.add(video.getTags());
    }

    currentlyPlaying = false;
    currentlyPaused = false;
  }



  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    ArrayList<String> sortedTitles = new ArrayList<>();
    sortedTitles.addAll(titles);
    Collections.sort(sortedTitles);
    for (String title: sortedTitles) {
      for (Video video: videoLibrary.getVideos()) {
        if (video.getTitle().equals(title)) {
          System.out.print("\t" + title + " (" + video.getVideoId() + ") [" + String.join(" ", video.getTags()) + "]");
          if (flaggedVideos.contains(title)) {
            System.out.println(" - FLAGGED " + "(reason: " + video.getReason() + ")");
          }
          else {
            System.out.println();
          }
        }
      }
    }
  }

  public void playVideo(String videoId) {
    if (ids.contains(videoId)) {
      if (!flaggedVideos.contains(videoLibrary.getVideo(videoId).getTitle())) {
        if (currentlyPlaying) {
          stopVideo();
        }
        currentlyPlaying = true;
        currentlyPaused = false;
        currentlyPlayingVideo = titles.get(ids.indexOf(videoId));
        System.out.println("Playing video: " + currentlyPlayingVideo);
      }
      else {
        System.out.println("Cannot play video: Video is currently flagged (reason: " + videoLibrary.getVideo(videoId).getReason() + ")");
      }
    }
    else {
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  public void stopVideo() {
    if (currentlyPlaying) {
      System.out.println("Stopping video: " + currentlyPlayingVideo);
      currentlyPlaying = false;
      currentlyPlayingVideo = "";
    }
    else {
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  public void playRandomVideo() {
    Random rand = new Random();
    String playId;
    if (flaggedVideos.size() != videoLibrary.getVideos().size()) {
      do {
        playId = ids.get(rand.nextInt(5));
      } while (flaggedVideos.contains(videoLibrary.getVideo(playId).getTitle()));
      playVideo(playId);
    }
    else {
      System.out.println("No videos available");
    }
  }

  public void pauseVideo() {
    if (currentlyPlaying) {
      if (!currentlyPaused) {
        System.out.println("Pausing video: " + currentlyPlayingVideo);
        currentlyPaused = true;
      }
      else {
        System.out.println("Video already paused: " + currentlyPlayingVideo);
      }
    }
    else {
      System.out.println("Cannot pause video: No video is currently playing");
    }
  }

  public void continueVideo() {
    if (currentlyPlaying) {
      if (currentlyPaused) {
        System.out.println("Continuing video: " + currentlyPlayingVideo);
        currentlyPaused = false;
      }
      else {
        System.out.println("Cannot continue video: Video is not paused");
      }
    }
    else {
      System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  public void showPlaying() {
    if (currentlyPlaying) {
      System.out.print("Currently playing: " + currentlyPlayingVideo + " (" + ids.get(titles.indexOf(currentlyPlayingVideo)) + ") [" + String.join(" ", tags.get(titles.indexOf(currentlyPlayingVideo))) + "]");
      if (currentlyPaused) {
        System.out.println(" - PAUSED");
      }
      else {
        System.out.println();
      }
    }
    else {
      System.out.println("No video is currently playing");
    }
  }




  private VideoPlaylist playlistExists(String playlistName) {
    for (VideoPlaylist playlists: videoPlaylists) {
      if (playlists.getPlaylistTitle().equalsIgnoreCase(playlistName)) {
        return playlists;
      }
    }
    return null;
  }

  public void createPlaylist(String playlistName) {
    if (playlistExists(playlistName) == null) {
      videoPlaylists.add(new VideoPlaylist(playlistName, new ArrayList<>()));
      System.out.println("Successfully created new playlist: " + playlistName);
    }
    else {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = playlistExists(playlistName);
    if (playlist != null) {
      if (!ids.contains(videoId)) {
        System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
      }
      else if (!flaggedVideos.contains(videoLibrary.getVideo(videoId).getTitle())) {
        if (playlist.videoInPlaylist(videoLibrary.getVideo(videoId))) {
          System.out.println("Cannot add video to " + playlistName + ": Video already added");
        }
        else {
          playlist.addVideos(videoLibrary.getVideo(videoId));
          System.out.println("Added video to " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
        }
      }
      else {
        System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " + videoLibrary.getVideo(videoId).getReason() + ")");
      }
    }
    else {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    }

  }

  public void showAllPlaylists() {
    if (videoPlaylists.isEmpty()) {
      System.out.println("No playlists exist yet");
    }
    else {
      System.out.println("Showing all playlists:");
      ArrayList<String> playlistTitles = new ArrayList<>();
      for(VideoPlaylist playlist: videoPlaylists) {
        playlistTitles.add(playlist.getPlaylistTitle());
      }
      Collections.sort(playlistTitles);
      for(String playlistTitle: playlistTitles) {
        System.out.println("\t" + playlistTitle);
      }
    }
  }

  public void showPlaylist(String playlistName) {
    VideoPlaylist playlist = playlistExists(playlistName);
    if (playlist != null) {
      System.out.println("Showing playlist: " + playlistName);
      if (playlist.getPlaylistVideos().isEmpty()) {
        System.out.println("\tNo videos here yet");
      }
      else {
        for (Video video: playlist.getPlaylistVideos()) {
          System.out.print("\t" + video.getTitle() + " (" + video.getVideoId() + ") [" + String.join(" ", video.getTags()) + "]");
          if (flaggedVideos.contains(video.getTitle())) {
            System.out.println(" - FLAGGED " + "(reason: " + video.getReason() + ")");
          }
          else {
            System.out.println();
          }
        }
      }
    }
    else {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = playlistExists(playlistName);
    if (playlist != null) {
      if (playlist.videoInPlaylist(videoLibrary.getVideo(videoId))) {
        playlist.removeVideos(videoLibrary.getVideo(videoId));
        System.out.println("Removed video from " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
      }
      else if (!ids.contains(videoId)) {
        System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      }
      else {
        System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
      }
    }
    else {
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    }
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist playlist = playlistExists(playlistName);
    if (playlist != null) {
      playlist.clear();
      System.out.println("Successfully removed all videos from " + playlistName);
    }
    else {
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist playlist = playlistExists(playlistName);
    if (playlist != null) {
      videoPlaylists.remove(playlist);
      System.out.println("Deleted playlist: " + playlistName);
    }
    else {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    }
  }




  public void searchVideos(String searchTerm) {
    ArrayList<String> foundVideos = new ArrayList<>();
    ArrayList<String> foundVideosIds = new ArrayList<>();

    for (Video video : videoLibrary.getVideos()) {
      if (video.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) && !flaggedVideos.contains(video.getTitle())) {
        foundVideos.add(video.getTitle());
      }
    }

    displayFoundVideos(searchTerm, foundVideos, foundVideosIds);
  }

  public void searchVideosWithTag(String videoTag) {
    ArrayList<String> foundVideos = new ArrayList<>();
    ArrayList<String> foundVideosIds = new ArrayList<>();

    for (Video video : videoLibrary.getVideos()) {
      if (video.getTags().contains(videoTag.toLowerCase()) && !flaggedVideos.contains(video.getTitle())) {
        foundVideos.add(video.getTitle());
      }
    }

    displayFoundVideos(videoTag, foundVideos, foundVideosIds);
  }

  private void displayFoundVideos(String videoTag, ArrayList<String> foundVideos, ArrayList<String> foundVideosIds) {
    if (!foundVideos.isEmpty()) {
      Collections.sort(foundVideos);
      System.out.println("Here are the results for " + videoTag + ":");

      for (String title : foundVideos) {
        for (Video video : videoLibrary.getVideos()) {
          if (video.getTitle().equalsIgnoreCase(title)) {
            foundVideosIds.add(video.getVideoId());
            System.out.println("\t" + (foundVideos.indexOf(title) + 1) + ") " + title + " (" + video.getVideoId() + ") [" + String.join(" ", video.getTags()) + "]");
          }
        }
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.\nIf your answer is not a valid number, we will assume it's a no.");
      Scanner sc = new Scanner(System.in);
      String input = sc.next();
      if (isNumeric(input) && Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= foundVideos.size()) {
        playVideo(foundVideosIds.get(Integer.parseInt(input)-1));
      }
    }
    else  {
      System.out.println("No search results for " + videoTag);
    }
  }

  private boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    try {
      double d = Integer.parseInt(strNum);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }



  public void flagVideo(String videoId) {
    if (ids.contains(videoId)) {
      if (videoLibrary.getVideo(videoId).getTitle().equals(currentlyPlayingVideo)) {
        stopVideo();
      }
      if (!flaggedVideos.contains(videoLibrary.getVideo(videoId).getTitle())) {
        flaggedVideos.add(videoLibrary.getVideo(videoId).getTitle());
        videoLibrary.getVideo(videoId).reasonFlagged("Not supplied");
        System.out.println("Successfully flagged video: " + videoLibrary.getVideo(videoId).getTitle() + " (reason: Not supplied)");
      }
      else {
        System.out.println("Cannot flag video: Video is already flagged");
      }
    }
    else {
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void flagVideo(String videoId, String reason) {
    if (ids.contains(videoId)) {
      if (videoLibrary.getVideo(videoId).getTitle().equals(currentlyPlayingVideo)) {
        stopVideo();
      }
      if (!flaggedVideos.contains(videoLibrary.getVideo(videoId).getTitle())) {
        flaggedVideos.add(videoLibrary.getVideo(videoId).getTitle());
        videoLibrary.getVideo(videoId).reasonFlagged(reason);
        System.out.println("Successfully flagged video: " + videoLibrary.getVideo(videoId).getTitle() + " (reason: " + reason + ")");
      }
      else {
        System.out.println("Cannot flag video: Video is already flagged");
      }
    }
    else {
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void allowVideo(String videoId) {
    if (ids.contains(videoId)) {
      if (flaggedVideos.contains(videoLibrary.getVideo(videoId).getTitle())) {
        flaggedVideos.remove(videoLibrary.getVideo(videoId).getTitle());
        System.out.println("Successfully removed flag from video: " + videoLibrary.getVideo(videoId).getTitle());
      }
      else {
        System.out.println("Cannot remove flag from video: Video is not flagged");
      }
    }
    else {
      System.out.println("Cannot remove flag from video: Video does not exist");
    }
  }
}