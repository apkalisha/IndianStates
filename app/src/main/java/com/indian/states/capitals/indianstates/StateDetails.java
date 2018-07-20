package com.indian.states.capitals.indianstates;

import java.util.ArrayList;

public class StateDetails {


    private String stateName;
    private String area;
    private String capital;
    private String history;
    private ArrayList<String> imageDetails;
    private ArrayList<String> imageLinks;
    private ArrayList<String> images;
    private String languages;
    private float literacyRate;
    private float literacyRateFemale;
    private float literacyRateMale;
    private String population;
    private String regionalDance;
    private int sexRatio;
    private String youTubeVideoLink;


    public StateDetails() {
    }

    public StateDetails(String area, String capital, String history, ArrayList<String> imageDetails,ArrayList<String> imageLinks, ArrayList<String> images, String languages, float literacyRate, float literacyRateFemale, float literacyRateMale, String population, String regionalDance, int sexRatio, String youTubeVideoLink) {
        this.area = area;
        this.capital = capital;
        this.history = history;
        this.imageDetails = imageDetails;
        this.imageLinks = imageLinks;
        this.images = images;
        this.languages = languages;
        this.literacyRate = literacyRate;
        this.literacyRateFemale = literacyRateFemale;
        this.literacyRateMale = literacyRateMale;
        this.population = population;
        this.regionalDance = regionalDance;
        this.sexRatio = sexRatio;
        this.youTubeVideoLink = youTubeVideoLink;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getImageDetails() {
        return imageDetails;
    }

    public void setImageDetails(ArrayList<String> imageDetails) {
        this.imageDetails = imageDetails;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public float getLiteracyRate() {
        return literacyRate;
    }

    public void setLiteracyRate(float literacyRate) {
        this.literacyRate = literacyRate;
    }

    public float getLiteracyRateFemale() {
        return literacyRateFemale;
    }

    public void setLiteracyRateFemale(float literacyRateFemale) {
        this.literacyRateFemale = literacyRateFemale;
    }

    public float getLiteracyRateMale() {
        return literacyRateMale;
    }

    public void setLiteracyRateMale(float literacyRateMale) {
        this.literacyRateMale = literacyRateMale;
    }

    public String getRegionalDance() {
        return regionalDance;
    }

    public void setRegionalDance(String regionalDance) {
        this.regionalDance = regionalDance;
    }

    public int getSexRatio() {
        return sexRatio;
    }

    public void setSexRatio(int sexRatio) {
        this.sexRatio = sexRatio;
    }

    public String getYouTubeVideoLink() {
        return youTubeVideoLink;
    }

    public void setYouTubeVideoLink(String youTubeVideoLink) {
        this.youTubeVideoLink = youTubeVideoLink;
    }

    public ArrayList<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ArrayList<String> imageLinks) {
        this.imageLinks = imageLinks;
    }
}
