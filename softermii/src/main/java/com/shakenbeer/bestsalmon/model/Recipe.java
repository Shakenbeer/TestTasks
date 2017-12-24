package com.shakenbeer.bestsalmon.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Parceler works better with non-private fields
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Parcel
public class Recipe {

    String uri;

    String label;

    String image;

    String source;

    String url;

    List<String> ingredientLines;

    public Recipe() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }
}
