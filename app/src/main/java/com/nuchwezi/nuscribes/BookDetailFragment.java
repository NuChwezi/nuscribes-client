package com.nuchwezi.nuscribes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a single Book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_SRC = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private JSONObject mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_SRC)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            try {
                mItem = new JSONObject(getArguments().getString(ARG_ITEM_SRC));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(NuScribesAPI.getBookTitle(mItem));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            WebView mWebview = ((WebView) rootView.findViewById(R.id.book_detail));

            mWebview.clearCache(true);

            mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

            mWebview.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
                }
            });

            mWebview .loadUrl(NuScribesAPI.getBookReadURL(mItem));
        }

        return rootView;
    }

    public void share() {
        if (mItem != null) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, NuScribesAPI.getBookTitle(mItem));
            share.putExtra(Intent.EXTRA_TEXT, NuScribesAPI.getBookReadURL(mItem));

            startActivity(Intent.createChooser(share, "Share BOOK!"));
        }
    }

    public void download() {
        if (mItem != null) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse( NuScribesAPI.getBookDownloadURL(mItem)));
            startActivity(i);
        }
    }
}
