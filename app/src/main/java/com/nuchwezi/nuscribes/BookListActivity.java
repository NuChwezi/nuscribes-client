package com.nuchwezi.nuscribes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An activity representing a list of Books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BookListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        final View recyclerView = findViewById(R.id.book_list);
        assert recyclerView != null;


        initContent((RecyclerView) recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refresh Books", Snackbar.LENGTH_LONG)
                        .setAction("Refresh", null).show();

                initContent((RecyclerView) recyclerView);
            }
        });

        if (findViewById(R.id.book_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void initContent(RecyclerView recyclerView) {
        setupRecyclerView(recyclerView);
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {


        Utility.getHTTP(this, NuScribesAPI.NUSCRIBES_BOOKS_API_URL, new ParametricCallback() {
            @Override
            public void call(String data) {

                try {
                    JSONArray books = new JSONArray(data);
                    recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(books));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final JSONArray mValues;

        public SimpleItemRecyclerViewAdapter(JSONArray items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            try {
                holder.mItem = mValues.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.mTitleView.setText(NuScribesAPI.getBookTitle(holder.mItem));
            holder.mSubTitleView.setText(String.format("by %s", NuScribesAPI.getBookAuthor(holder.mItem)));
            holder.mContentView.loadDataWithBaseURL("", NuScribesAPI.getBookSummary(holder.mItem), "text/html", "UTF-8", "");
            holder.mContentView.setDownloadListener(new DownloadListener() {
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            Picasso.with(BookListActivity.this)
                    .load(NuScribesAPI.getBookCoverURL(holder.mItem))
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.erorr)
                    .into(holder.mImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(BookDetailFragment.ARG_ITEM_SRC, holder.mItem.toString());
                        BookDetailFragment fragment = new BookDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.book_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, BookDetailActivity.class);
                        intent.putExtra(BookDetailFragment.ARG_ITEM_SRC, holder.mItem.toString());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitleView,mSubTitleView;
            public final ImageView mImageView;
            public final WebView mContentView;
            public JSONObject mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.title);
                mSubTitleView = (TextView) view.findViewById(R.id.subtitle);
                mContentView = (WebView) view.findViewById(R.id.content);
                mContentView.getSettings().setJavaScriptEnabled(false);
                mImageView = (ImageView) view.findViewById(R.id.contentImage);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }
        }
    }
}
