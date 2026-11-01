package com.cinebook.catalog.movie;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("movies")
public class Movie {

  @Id private String id;

  private String title;
  private int durationMins;
  private String language;
  private String posterUrl;

  public Movie(String title, int durationMins, String language, String posterUrl) {
    this.title = title;
    this.durationMins = durationMins;
    this.language = language;
    this.posterUrl = posterUrl;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public int getDurationMins() {
    return durationMins;
  }

  public String getLanguage() {
    return language;
  }

  public String getPosterUrl() {
    return posterUrl;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDurationMins(int durationMins) {
    this.durationMins = durationMins;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setPosterUrl(String posterUrl) {
    this.posterUrl = posterUrl;
  }
}
