package ro.hd.speekup.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import ro.hd.speekup.R;
import ro.hd.speekup.activities.EventDescriptionActivity;
import ro.hd.speekup.activities.SuggestionActivity;
import ro.hd.speekup.classes.DateTimeHelper;
import ro.hd.speekup.classes.EventStartDateComparator;
import ro.hd.speekup.entities.ListEvent;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    public List<ListEvent> items;
    public List<ListEvent> filteredItems;

    private Set<String> existingIds;
    private Set<String> filteredIds;

    private String filterQuery = "";

    private Context context;
    private RecyclerView recyclerView;

    public EventListAdapter(Context ctx) {
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

    public void addItem(ListEvent item) {
        if (existingIds.contains(item.getId())) {
            updateItem(item);
        } else {
            items.add(item);
            existingIds.add(item.getId());
            filter(filterQuery);
        }
    }

    public void updateItem(ListEvent event) {
        String itemId = event.getId();
        for (int i = 0; i < items.size(); i++) {
            if (itemId.equals(items.get(i).getId())) {
                if (!items.get(i).equals(event)) {
                    items.set(i, event);
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
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        ListEvent event = filteredItems.get(position);

            //set the message date
            if (event.getStartTime() > 0) {
                holder.text_time.setText(DateTimeHelper.writeTimeToday(event.getStartTime(), null));
                holder.text_date.setText(DateTimeHelper.writeDate(event.getStartTime(), context, null));
            } else {
                holder.text_time.setText("");
            }

            highlightText(holder.text_title, event.getTitle(), filterQuery, Color.RED); //cyan, direct linking doesn't work well
            highlightText(holder.text_subtitle, event.getLocationName(), filterQuery, Color.RED); //cyan, direct linking doesn't work well
            //add the avatar

            Glide.with(context)
                    .load(event.getAvatar())
                    .crossFade()
                    .centerCrop()
                    .into(holder.image);


        final ListEvent finalEvent = event;
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEvent(context, finalEvent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    public ListEvent getItem(int position) {
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


    private void highlightText(TextView view, String fulltext, String subtext, int color) {
        view.setText(fulltext);
        if (!subtext.isEmpty()) {
            int i = fulltext.toLowerCase().indexOf(subtext.toLowerCase());
            if (i >= 0) {
                view.setText(fulltext, TextView.BufferType.SPANNABLE);
                Spannable str = (Spannable) view.getText();
                str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void filter(String query) {
        List<ListEvent> filteredModelList = getFilteredItems(items, query);
        Collections.sort(filteredModelList, new EventStartDateComparator());
        animateTo(filteredModelList);
    }

    private List<ListEvent> getFilteredItems(List<ListEvent> items, String query) {
        List<ListEvent> filteredModelList = new ArrayList<>();
        query = query.toLowerCase();
        if (query.isEmpty()) {
            filteredModelList = items;
        } else {
            for (ListEvent item : items) {
                if (item.getTitle().toLowerCase().contains(query)
                        || item.getLocationName().contains(query)
                        ) {
                    filteredModelList.add(item);
                }
            }
        }
        return filteredModelList;
    }

    public void openEvent(Context context, ListEvent event) {
        Intent intent;
        if (event.isAttending() && event.getStartTime() <= System.currentTimeMillis() / 1000L) {
            intent = new Intent(context, SuggestionActivity.class);
        } else {
            intent = new Intent(context, EventDescriptionActivity.class);
        }
        Bundle b = new Bundle();
        b.putSerializable("event", event);
        intent.putExtras(b);
        ((Activity) context).startActivity(intent);
    }

    public void animateTo(List<ListEvent> items) {
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

    private void applyAndAnimateRemovals(List<ListEvent> newItems) {
        //List<Integer> newIds = new ArrayList<>();
        Set<String> newIds = new HashSet<>();
        for (int i = 0; i < newItems.size(); i++) {
            newIds.add(newItems.get(i).getId());
        }

        for (int i = filteredItems.size() - 1; i >= 0; i--) {
            final ListEvent item = filteredItems.get(i);
            if (!newIds.contains(item.getId())) {
                //Log.e("remove", item.getContact().getDisplayName() + " from " + Integer.toString(i));
                removeFilteredItem(i);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ListEvent> newItems) {
        for (int toPosition = newItems.size() - 1; toPosition >= 0; toPosition--) {
            final ListEvent item = newItems.get(toPosition);
            final int fromPosition = filteredItems.indexOf(item);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                //Log.e("move", newItems.get(toPosition).getContact().getDisplayName() + " to " + toPosition + " from " + fromPosition);
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

    private void applyAndAnimateAdditions(List<ListEvent> newItems) {
        for (int i = 0, count = newItems.size(); i < count; i++) {
            final ListEvent item = newItems.get(i);
            if (!filteredIds.contains(item.getId())) {
                //Log.e("add", item.getContact().getDisplayName() + " at " + Integer.toString(i));
                addFilteredItem(i, item);
            }
        }
    }

    private void applyChanges(List<ListEvent> newItems) {
        for (int i = 0; i < newItems.size(); i++) {
            final ListEvent item = newItems.get(i);
            //check if the item is already in the list
            if (filteredIds.contains(item.getId())) {
                //search for the position of the item
                for (int j = 0; j < filteredItems.size(); j++) {
                    if (filteredItems.get(j).getId().equals(item.getId()) && !filteredItems.get(j).equals(item)) {
                        filteredItems.set(j, item);
                        //Log.e("refresh", item.getContact().getDisplayName() + " at " + Integer.toString(j));
                    }
                }
            }
        }
    }

    public ListEvent removeFilteredItem(int position) {
        final ListEvent item = filteredItems.remove(position);
        filteredIds.remove(item.getId());
        notifyItemRemoved(position);
        return item;
    }

    public void addFilteredItem(int position, ListEvent item) {
        filteredItems.add(position, item);
        filteredIds.add(item.getId());
        notifyItemInserted(position);
    }

    public void moveFilteredItem(int fromPosition, int toPosition) {
        final ListEvent item = filteredItems.remove(fromPosition);
        filteredItems.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_title;
        public TextView text_subtitle;
        public TextView text_time;
        public TextView text_date;
        public CircleImageView image;
        public LinearLayout item_layout;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            text_title = (TextView) itemLayoutView.findViewById(R.id.text_title);
            text_subtitle = (TextView) itemLayoutView.findViewById(R.id.text_subtitle);
            text_time = (TextView) itemLayoutView.findViewById(R.id.text_time);
            text_date = (TextView) itemLayoutView.findViewById(R.id.text_date);
            image = (CircleImageView) itemLayoutView.findViewById(R.id.image);
            item_layout = (LinearLayout) itemLayoutView.findViewById(R.id.item_layout);
        }
    }
}



