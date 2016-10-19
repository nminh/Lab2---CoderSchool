package com.codepath.android.booksearch.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.adapter.BookAdapter;
import com.codepath.android.booksearch.api.BookApi;
import com.codepath.android.booksearch.custom.DividerItemDecoration;
import com.codepath.android.booksearch.custom.EndlessScrollListener;
import com.codepath.android.booksearch.model.Book;
import com.codepath.android.booksearch.model.SearchRequest;
import com.codepath.android.booksearch.model.SearchResult;
import com.codepath.android.booksearch.utils.RetrofitUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListActivity extends AppCompatActivity {
    private SearchRequest mSearchRequest;
    private BookAdapter mBookAdapter;
    private BookApi mBookApi;
    private LinearLayoutManager mLayoutManager;
    private MenuItem miActionProgressItem, miSearch;

    @BindView(R.id.lvBooks)
    RecyclerView lvBooks;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);
        setUpApi();
        setUpViews();
    }

    private void setUpApi() {
        mSearchRequest = new SearchRequest();
        mBookApi = RetrofitUtils.get().create(BookApi.class);
    }

    private void setUpViews() {
        setSupportActionBar(toolbar);
        mBookAdapter = new BookAdapter();
        mBookAdapter.setListener(new BookAdapter.Listener() {
            @Override
            public void onItemClick(Book book) {
                Toast.makeText(BookListActivity.this, book.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lvBooks.setAdapter(new SlideInBottomAnimationAdapter(mBookAdapter));
        lvBooks.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        lvBooks.setLayoutManager(mLayoutManager);
        lvBooks.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mSearchRequest.setPage(page + 1);
                fetchMoreBooks();
            }
        });
    }

    private void fetchMoreBooks() {
        miActionProgressItem.setVisible(true);
        miSearch.setVisible(false);
        mBookApi.search(mSearchRequest.toQueryMay()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.body() != null) {
                    mBookAdapter.addBooks(response.body().getBooks());
                }
                handleComplete();
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("Error", t.getMessage());
                handleComplete();
            }
        });
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks() {
        miActionProgressItem.setVisible(true);
        miSearch.setVisible(false);
        mBookApi.search(mSearchRequest.toQueryMay()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                handleResponse(response.body());
                handleComplete();
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("Error", t.getMessage());
                handleComplete();
            }
        });
    }

    private void handleComplete() {
        miActionProgressItem.setVisible(false);
        miSearch.setVisible(true);
    }

    private void handleResponse(SearchResult searchResult) {
        if (searchResult != null) {
            mBookAdapter.setBooks(searchResult.getBooks());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        miSearch = menu.findItem(R.id.miSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(miSearch);
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setHint("Search...");
        et.setHintTextColor(Color.parseColor("#50FFFFFF"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                mSearchRequest.setQuery(query);
                fetchBooks();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        fetchBooks();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
