package com.hekmatullahamin.plan.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Book;

import java.util.ArrayList;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Book> books;
    private DatabaseHandler databaseHandler;

    private Drawable[] colors;
    private ArrayList<View> booksViewArrayList;

    public BookRecyclerViewAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
        databaseHandler = new DatabaseHandler(context);
        booksViewArrayList = new ArrayList<>();

        colors = new Drawable[]{ContextCompat.getDrawable(context, R.drawable.gradient_cool_sky), ContextCompat.getDrawable(context, R.drawable.gradient_digital_water),
                ContextCompat.getDrawable(context, R.drawable.gradient_flare), ContextCompat.getDrawable(context, R.drawable.gradient_harvey),
                ContextCompat.getDrawable(context, R.drawable.gradient_instagram), ContextCompat.getDrawable(context, R.drawable.gradient_j_shine),
                ContextCompat.getDrawable(context, R.drawable.gradient_jonquil), ContextCompat.getDrawable(context, R.drawable.gradient_margo),
                ContextCompat.getDrawable(context, R.drawable.gradient_mega_tron), ContextCompat.getDrawable(context, R.drawable.gradient_memariani),
                ContextCompat.getDrawable(context, R.drawable.gradient_ohhappiness), ContextCompat.getDrawable(context, R.drawable.gradient_radar),
                ContextCompat.getDrawable(context, R.drawable.gradient_rastafari), ContextCompat.getDrawable(context, R.drawable.gradient_shahabi),
                ContextCompat.getDrawable(context, R.drawable.gradient_shore), ContextCompat.getDrawable(context, R.drawable.gradient_summer_dog),
                ContextCompat.getDrawable(context, R.drawable.gradient_ultra_voilet)};
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customBookRow = LayoutInflater.from(context).inflate(R.layout.custom_book_row, parent, false);
        return new ViewHolder(customBookRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        int randomColor = generateRandomNumber();
        holder.bookBackground.setBackground(colors[randomColor]);

        holder.bookNameTV.setText(book.getBookName());
        holder.bookPageTV.setText(book.getBookNumberOfPagesRead() + "/" + book.getBookTotalBookPages());
        double percentageOfComplete = getBookCompletePercentage(book.getBookNumberOfPagesRead(), book.getBookTotalBookPages());
        holder.bookCompletePercentageTV.setText(String.format("%.2f", percentageOfComplete) + "%");
        holder.progressBar.setProgress((int) percentageOfComplete);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bookNameTV, bookPageTV, bookCompletePercentageTV;
        private Button increaseNoOfPagesButton, decreaseNoOfPagesButton;
        private ImageButton deleteBook;
        private ProgressBar progressBar;
        private LinearLayout bookBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            adding every view in array then use the last which remain make width match parent
            booksViewArrayList.add(itemView);
            bookNameTV = itemView.findViewById(R.id.customBookRowBookNameTextViewId);
            bookPageTV = itemView.findViewById(R.id.customBookRowNumberOfPagesOfBookCountTextViewId);
            bookCompletePercentageTV = itemView.findViewById(R.id.customBookRowBookCompletePercentageTextViewId);
            increaseNoOfPagesButton = itemView.findViewById(R.id.customBookRowIncreaseNoOfPagesButtonId);
            decreaseNoOfPagesButton = itemView.findViewById(R.id.customBookRowDecreaseNoOfPagesButtonId);
            deleteBook = itemView.findViewById(R.id.customBookRowDeleteBookImageButtonId);
            progressBar = itemView.findViewById(R.id.customBookRowProgressBarId);
            bookBackground = itemView.findViewById(R.id.customBookRowMainLinearLayoutId);

            changeBookWidthToMatchParent();

            increaseNoOfPagesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Book book = books.get(getAdapterPosition());
                    int numberOfReadPages = book.getBookNumberOfPagesRead();
//                    number of pages should not exceed total pages
                    if (numberOfReadPages < book.getBookTotalBookPages()) {
                        numberOfReadPages++;
                        book.setBookNumberOfPagesRead(numberOfReadPages);
                        double percentageOfComplete = getBookCompletePercentage(numberOfReadPages, book.getBookTotalBookPages());
                        bookPageTV.setText(book.getBookNumberOfPagesRead() + "/" + book.getBookTotalBookPages());
                        bookCompletePercentageTV.setText(String.format("%.2f", percentageOfComplete) + "%");
                        progressBar.setProgress((int) percentageOfComplete);
                        databaseHandler.updateBook(book);
                    }
                }
            });

            decreaseNoOfPagesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Book book = books.get(getAdapterPosition());
                    int numberOfReadPages = book.getBookNumberOfPagesRead();
//                    number of pages should get less than zero
                    if (numberOfReadPages > 0) {
                        numberOfReadPages--;
                        book.setBookNumberOfPagesRead(numberOfReadPages);
                        double percentageOfComplete = getBookCompletePercentage(numberOfReadPages, book.getBookTotalBookPages());
                        bookPageTV.setText(book.getBookNumberOfPagesRead() + "/" + book.getBookTotalBookPages());
                        bookCompletePercentageTV.setText(String.format("%.2f", percentageOfComplete) + "%");
                        progressBar.setProgress((int) percentageOfComplete);
                        databaseHandler.updateBook(book);
                    }
                }
            });

            deleteBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Book book = books.get(position);
                    databaseHandler.deleteBook(book.getBookId());
                    books.remove(position);
                    booksViewArrayList.remove(position);
                    notifyItemRemoved(position);

//                    for hiding top linear layout of time table fragment
                    if (databaseHandler.totalBookCount() == 0) {
                        bookBackground.setVisibility(View.GONE);
                    } else {
                        bookBackground.setVisibility(View.VISIBLE);
                    }

                    if (databaseHandler.totalBookCount() == 1) {
                        changeBookWidthToMatchParent();
                    }
                }
            });
        }
    }

    private int generateRandomNumber() {
        int randomNumber = (int) (Math.random() * colors.length);
        return randomNumber;
    }

    private double getBookCompletePercentage(int numberOfReadPages, int totalPages) {
        double completePercentage = (double) (numberOfReadPages * 100) / totalPages;
        return completePercentage;
    }

    private void changeBookWidthToMatchParent() {
//            for making book match parent if there is only one book in recycler view
        if (databaseHandler.totalBookCount() == 1) {
            booksViewArrayList.get(0).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }
}
