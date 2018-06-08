package udacity.com.tamtommovie.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.tamtommovie.R;
import udacity.com.tamtommovie.model.Review;

/**
 * Created by omaraltamimi on 5/22/18.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    List<Review> mReviews;

    public ReviewsAdapter(List<Review> reviews) {
        mReviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        if (position == RecyclerView.NO_POSITION)
            return;
        holder.bind(mReviews.get(position));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author)
        TextView tvAuthorName;
        @BindView(R.id.content)
        TextView tvContent;


        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Review review) {
            tvAuthorName.setText(review.getAuthor());
            tvContent.setText(review.getContent());
        }

    }
}
