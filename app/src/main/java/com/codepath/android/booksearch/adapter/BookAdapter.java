package com.codepath.android.booksearch.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.model.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int FOOTER = 1;
    private final int ITEM = 0;
    private List<Book> mBooks;
    private Listener mListener;

    public interface Listener {
        void onItemClick(Book book);
    }

    public BookAdapter() {
        this.mBooks = new ArrayList<>();
    }

    public void setListener(Listener istener) {
        mListener = istener;
    }

    public void setBooks(List<Book> books) {
        mBooks = books;
        notifyDataSetChanged();
    }

    public void addBooks(List<Book> books) {
        int startPosition = getItemCount();
        mBooks.addAll(books);
        notifyItemRangeInserted(startPosition, books.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case FOOTER:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_loading, parent, false);
                return new FooterViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_book, parent, false);
                return new ViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final Book book = mBooks.get(position);
            viewHolder.tvTitle.setText(book.getTitle());
            viewHolder.tvAuthor.setText(book.getAuthor());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) mListener.onItemClick(book);
                }
            });
            Picasso.with(holder.itemView.getContext())
                    .load(book.getCoverUrl())
                    .placeholder(R.drawable.ic_nocover)
                    .into(viewHolder.ivCover);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mBooks.size()) {
            return FOOTER;
        }
        return ITEM;
    }

    @Override
    public int getItemCount() {
        if (mBooks.size() > 0) {
            return mBooks.size() + 1;
        }
        return 0;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivCover)
        ImageView ivCover;

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvAuthor)
        TextView tvAuthor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
