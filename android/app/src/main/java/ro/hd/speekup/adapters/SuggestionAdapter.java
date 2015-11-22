package ro.hd.speekup.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import ro.hd.speekup.R;
import ro.hd.speekup.activities.EventDescriptionActivity;
import ro.hd.speekup.classes.ApiManager;
import ro.hd.speekup.classes.DateTimeHelper;
import ro.hd.speekup.classes.EventStartDateComparator;
import ro.hd.speekup.classes.SuggestionScoreComparator;
import ro.hd.speekup.entities.ListEvent;
import ro.hd.speekup.entities.Suggestion;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {
    public List<Suggestion> items;
    public List<Suggestion> filteredItems;

    private Set<String> existingIds;
    private Set<String> filteredIds;

    private String filterQuery = "";

    private Context context;
    private RecyclerView recyclerView;

    public SuggestionAdapter(Context ctx) {
        items = new ArrayList<>();
        filteredItems = new ArrayList<>();
        existingIds = new HashSet<>();
        filteredIds = new HashSet<>();
        context = ctx;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addItem(Suggestion item) {
        if (existingIds.contains(item.getId())) {
            updateItem(item);
        } else {
            items.add(item);
            existingIds.add(item.getId());
            filter(filterQuery);
        }
    }

    public void updateItem(Suggestion item) {
        String itemId = item.getId();
        for (int i = 0; i < items.size(); i++) {
            if (itemId.equals(items.get(i).getId())) {
                if (!items.get(i).equals(item)) {
                    items.set(i, item);
                    filter(filterQuery);
                }
            }
        }
    }

    public void removeAll() {
        items = new ArrayList<>();
        existingIds = new HashSet<>();
        filter("");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SuggestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggestion_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Suggestion suggestion = filteredItems.get(position);

        holder.text_score.setText(Integer.toString(suggestion.getScore()));
        holder.text_suggestion.setText(suggestion.getText());

        if (suggestion.hasVoted()) {
            holder.button_vote_up.setAlpha(0.4f);
            holder.button_vote_down.setAlpha(0.4f);
        } else {
            holder.button_vote_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vote(context, suggestion, true);
                }
            });
            holder.button_vote_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vote(context, suggestion, false);
                }
            });
        }

        holder.button_waze.setVisibility(View.GONE);
        holder.text_suggestion.setTypeface(Typeface.DEFAULT);
        holder.text_suggestion.setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
        holder.winner_sign.setVisibility(View.GONE);
        if (suggestion.getStatus().equals("winner")) {
            holder.text_suggestion.setTypeface(Typeface.DEFAULT_BOLD);
            holder.text_suggestion.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            holder.winner_sign.setVisibility(View.VISIBLE);

            if (suggestion.getType().equals("move")) {
                holder.button_waze.setVisibility(View.VISIBLE);

                holder.button_waze.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String url = "waze://?q=" + suggestion.getText();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                            context.startActivity(intent);
                        }
                    }
                });
            }
        }


    }

    private void vote(Context context, Suggestion suggestion, boolean positive) {
        ApiManager.vote(context, suggestion, positive, "SuggestionActivity");
        ApiManager.getSuggestions(context, suggestion.getEventId(), "SuggestionActivity");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    public Suggestion getItem(int position) {
        return filteredItems.get(position);
    }

    public void setFilter(String query) {
        if (!filterQuery.equals(query.toLowerCase())) {
            filterQuery = query.toLowerCase();
            filter(filterQuery);
            //recyclerView.smoothScrollToPosition(0);
            recyclerView.scrollToPosition(0);
        }
    }

    public String getFilter() {
        return filterQuery;
    }

    private void filter(String query) {
        List<Suggestion> filteredModelList = getFilteredItems(items, query);
        Collections.sort(filteredModelList, new SuggestionScoreComparator());
        animateTo(filteredModelList);
    }

    private List<Suggestion> getFilteredItems(List<Suggestion> items, String query) {
        return items;
    }

    public void animateTo(List<Suggestion> items) {
        recyclerView.getItemAnimator().endAnimations();

        //if an element was added before the first visible element, scroll the list to it
        View firstVisibleView = recyclerView.getChildAt(0);
        if (firstVisibleView != null) {
            final int firstVisiblePosition = recyclerView.getChildAdapterPosition(firstVisibleView);
            final int heightOfView = firstVisibleView.getHeight();
            final int initialSize = filteredItems.size();
            if (firstVisiblePosition == 0) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            Thread.sleep(250);
                        } catch (Exception e) {

                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        if (filteredItems.size() > initialSize) {
                            //Log.e("scroll", "one up");
                            recyclerView.smoothScrollBy(0, -heightOfView);
                        }
                        super.onPostExecute(aVoid);
                    }
                }.execute();
            }
        }

        applyChanges(items);
        for (int i = 0; i < filteredItems.size(); i++) {
            notifyItemChanged(i);
        }

        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);


    }

    private void applyAndAnimateRemovals(List<Suggestion> newItems) {
        Set<String> newIds = new HashSet<>();
        for (int i = 0; i < newItems.size(); i++) {
            newIds.add(newItems.get(i).getId());
        }

        for (int i = filteredItems.size() - 1; i >= 0; i--) {
            final Suggestion item = filteredItems.get(i);
            if (!newIds.contains(item.getId())) {
                removeFilteredItem(i);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Suggestion> newItems) {
        for (int toPosition = newItems.size() - 1; toPosition >= 0; toPosition--) {
            final Suggestion item = newItems.get(toPosition);
            final int fromPosition = filteredItems.indexOf(item);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveFilteredItem(fromPosition, toPosition);
                //if if the item was moved to or from the first position, scroll the view up on item
                if (fromPosition + toPosition == 1) {
                    View firstVisibleView = recyclerView.getChildAt(0);
                    if (firstVisibleView != null) {
                        final int heightOfView = firstVisibleView.getHeight();
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                try {
                                    Thread.sleep(250);
                                } catch (Exception e) {

                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                //Log.e("scroll", "because moved");
                                recyclerView.smoothScrollBy(0, -heightOfView);
                                super.onPostExecute(aVoid);
                            }
                        }.execute();
                    }
                }
            }
        }
    }

    private void applyAndAnimateAdditions(List<Suggestion> newItems) {
        for (int i = 0, count = newItems.size(); i < count; i++) {
            final Suggestion item = newItems.get(i);
            if (!filteredIds.contains(item.getId())) {
                addFilteredItem(i, item);
            }
        }
    }

    private void applyChanges(List<Suggestion> newItems) {
        for (int i = 0; i < newItems.size(); i++) {
            final Suggestion item = newItems.get(i);
            //check if the item is already in the list
            if (filteredIds.contains(item.getId())) {
                //search for the position of the item
                for (int j = 0; j < filteredItems.size(); j++) {
                    if (filteredItems.get(j).getId().equals(item.getId()) && !filteredItems.get(j).equals(item)) {
                        filteredItems.set(j, item);
                    }
                }
            }
        }
    }

    public Suggestion removeFilteredItem(int position) {
        final Suggestion item = filteredItems.remove(position);
        filteredIds.remove(item.getId());
        notifyItemRemoved(position);
        return item;
    }

    public void addFilteredItem(int position, Suggestion item) {
        filteredItems.add(position, item);
        filteredIds.add(item.getId());
        notifyItemInserted(position);
    }

    public void moveFilteredItem(int fromPosition, int toPosition) {
        final Suggestion item = filteredItems.remove(fromPosition);
        filteredItems.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_suggestion;
        public TextView text_score;

        public ImageView button_vote_up;
        public ImageView button_vote_down;

        public Button button_waze;
        public ImageView winner_sign;

        public LinearLayout item_layout;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            text_suggestion = (TextView) itemLayoutView.findViewById(R.id.text_suggestion);
            text_score = (TextView) itemLayoutView.findViewById(R.id.text_score);

            button_vote_up = (ImageView) itemLayoutView.findViewById(R.id.button_vote_up);
            button_vote_down = (ImageView) itemLayoutView.findViewById(R.id.button_vote_down);
            winner_sign = (ImageView) itemLayoutView.findViewById(R.id.winner_sign);

            button_waze = (Button) itemLayoutView.findViewById(R.id.button_waze);

            item_layout = (LinearLayout) itemLayoutView.findViewById(R.id.item_layout);
        }
    }
}



