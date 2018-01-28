package com.nuchwezi.nuscribes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AK1N Nemesis Fixx on 1/28/2018.
 */

class NuScribesAPI {
    public static final String NUSCRIBES_BOOKS_API_URL = "https://nuscribes.com/api/books/";;

    public static String getBookTitle(JSONObject bookObject) {
        try {
            return bookObject.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getBookSummary(JSONObject bookObject) {
        try {
            return bookObject.getString("summary");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getBookID(JSONObject bookObject) {
        try {
            return bookObject.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getBookReadURL(JSONObject bookObject) {

        try {
            return bookObject.getString("read_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getBookAuthor(JSONObject bookObject) {

        try {
            return bookObject.getString("author");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getBookCoverURL(JSONObject bookObject) {
        try {
            String url = bookObject.getString("cover_image_url");
            if(url.trim().length() == 0)
                return "https://s26.postimg.org/ylhkzigbd/book.png";
            else
                return url;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "https://s26.postimg.org/ylhkzigbd/book.png";
    }

    public static boolean hasDownload(JSONObject bookObject) {
        try {
            String url = bookObject.getString("uri_epub");
            if(url.trim().length() == 0)
                return false;
            else
                return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getBookDownloadURL(JSONObject bookObject) {
        try {
            return bookObject.getString("uri_epub");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
