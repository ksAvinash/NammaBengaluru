package com.avinash_ksworks.jci.nammabengaluru.adapter;



public class generic_adapter {

    private String image, name, description, bestSeason, contact, entryFee, additionalInformation;
    private int id;
    private Double latitude, longitude;

    public generic_adapter(int id, String image, String name, String description, String bestSeason, String contact, String entryFee, String additionalInformation, Double latitude, Double longitude) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.bestSeason = bestSeason;
        this.contact = contact;
        this.entryFee = entryFee;
        this.additionalInformation = additionalInformation;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBestSeason(String bestSeason) {
        this.bestSeason = bestSeason;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {

        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBestSeason() {
        return bestSeason;
    }

    public String getContact() {
        return contact;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public int getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
