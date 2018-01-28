package com.nuchwezi.nuscribes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An activity representing a single Book detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link BookListActivity}.
 */
public class BookDetailActivity extends AppCompatActivity {

    private BookDetailFragment bookDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Share this Book", Snackbar.LENGTH_LONG)
                        .setAction("Share", null).show();

                bookDetailFragment.share();
            }
        });

        FloatingActionButton fab_download = (FloatingActionButton) findViewById(R.id.fab_download);
        fab_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Download this Book", Snackbar.LENGTH_LONG)
                        .setAction("Download", null).show();

                bookDetailFragment.download();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            String itemSRC = getIntent().getStringExtra(BookDetailFragment.ARG_ITEM_SRC);
            try {
                JSONObject bookObject = new JSONObject(itemSRC);
                if(!NuScribesAPI.hasDownload(bookObject)){
                    fab_download.setVisibility(View.INVISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arguments.putString(BookDetailFragment.ARG_ITEM_SRC, itemSRC
                    );
            bookDetailFragment = new BookDetailFragment();
            bookDetailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.book_detail_container, bookDetailFragment)
                    .commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, BookListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
