package com.ankit.restaurantmenudemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ankit.restaurantmenudemo.R;
import com.ankit.restaurantmenudemo.models.MenuModel;

import java.util.List;

import static com.ankit.restaurantmenudemo.MainActivity.searchMenu;

public class RestaurantMenuHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TOP_VIEW = 1;
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 3;
    List<MenuModel> menuList;
    boolean isSearch = false;

    public RestaurantMenuHeaderAdapter(List<MenuModel> menuList, boolean isSearch) {
        this.menuList = menuList;
        this.isSearch = isSearch;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP_VIEW) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.restaurant_top_item_view, parent, false);
            return new VHTopItem(itemView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.restaurant_menu_item_view, parent, false);
            return new VHItem(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.restaurant_menu_header_item_view, parent, false);
            return new VHHeader(itemView);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MenuModel dataItem = getItem(position);
        if (holder instanceof VHTopItem) {
            ((VHTopItem) holder).imgSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchMenu();
                }
            });
        } else if (holder instanceof VHItem) {
            ((VHItem) holder).tvMenuName.setText(dataItem.name);
            ((VHItem) holder).tvDescription.setText(dataItem.description);

            if (!dataItem.isImage) {
                ((VHItem) holder).menuImage.setVisibility(View.GONE);
            }
            if (dataItem.description.isEmpty()) {
                ((VHItem) holder).tvDescription.setVisibility(View.GONE);
            }
        } else if (holder instanceof VHHeader) {
            ((VHHeader) holder).tvHeader.setText(dataItem.name);
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MenuModel dataItem = getItem(position);

        if (position == 0 && !isSearch) {
            return TYPE_TOP_VIEW;
        } else if (dataItem.isHeader) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }

    }

    public void filterList(List<MenuModel> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    private MenuModel getItem(int position) {
        return menuList.get(position);
    }

    static class VHTopItem extends RecyclerView.ViewHolder {
        ImageView imgSearch;

        public VHTopItem(View itemView) {
            super(itemView);
            imgSearch = itemView.findViewById(R.id.imgSearch);
        }
    }

    static class VHItem extends RecyclerView.ViewHolder {
        TextView tvMenuName;
        TextView tvDescription;
        ImageView menuImage;

        public VHItem(View itemView) {
            super(itemView);
            tvMenuName = itemView.findViewById(R.id.tvMenuName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            menuImage = itemView.findViewById(R.id.menuImage);
        }
    }

    static class VHHeader extends RecyclerView.ViewHolder {
        TextView tvHeader;

        public VHHeader(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvHeader);
        }
    }
}