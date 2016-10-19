package com.codepath.android.booksearch.model;

import android.text.TextUtils;

import com.codepath.android.booksearch.utils.Constant;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Book {

    @SerializedName("edition_key")
    private List<String> editionKeys;

    @SerializedName("cover_edition_key")
    private String coverEditionKey;

    @SerializedName("author_name")
    private List<String> authors;

    @SerializedName("title_suggest")
    private String title;

    public String getOpenLibraryId() {
        if (editionKeys != null && editionKeys.size() > 0) {
            return editionKeys.get(0);
        }
        return coverEditionKey;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        if (authors != null) {
            return TextUtils.join(", ", authors);
        }
        return "No author";
    }

    public String getCoverUrl() {
        return Constant.STATIC_BASE_URL + getOpenLibraryId() + "-L.jpg?default=false";
    }
}
