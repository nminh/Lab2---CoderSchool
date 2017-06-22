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
    private List<Book> mBooks;

    public BookAdapter() {
        this.mBooks = new ArrayList<>();
    }

    public void setBooks(List<Book> books) {
        // TODO: Insert your code here

    }

    public void addBooks(List<Book> books) {
         int startPosition = getItemCount();
        // TODO: Insert your code here
        mBooks.addAll(startPosition, books);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: Insert your code here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // TODO: Insert your code here
        if(getItemCount() != 0 && position == getItemCount() -1){
            return;
        }
        ViewHolder holder1 = (ViewHolder) holder;
        Book book = mBooks.get(position);
        holder1.tvAuthor.setText(book.getAuthor());
        holder1.tvTitle.setText(book.getTitle());
        Picasso.with(holder.itemView.getContext()).load(book.getCoverUrl()).into(holder1.ivCover);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        // TODO: Update this function
        return mBooks.size();
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
