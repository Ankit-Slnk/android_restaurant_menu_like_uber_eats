package com.ankit.restaurantmenudemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ankit.restaurantmenudemo.adapter.RestaurantMenuHeaderAdapter;
import com.ankit.restaurantmenudemo.models.MenuModel;
import com.ankit.restaurantmenudemo.models.MenuTabModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private boolean mIsTheTitleVisible = false;
    private AppBarLayout mAppBarLayout;
    LinearLayout llToolbar;
    TabLayout tabLayout;
    RecyclerView menuRecyclerView;
    RestaurantMenuHeaderAdapter restaurantMenuHeaderAdapter;
    RestaurantMenuHeaderAdapter restaurantMenuSearchAdapter;
    boolean isFavourite = false;
    LinearLayoutManager linearLayoutManager;
    private boolean isScrolled = false;
    private List<MenuTabModel> tabList = new ArrayList<>();
    ImageButton imgBtnFavourite;
    ImageView imgSearch;
    List<MenuModel> menuList = new ArrayList<>();
    static SearchView searchView;
    static LinearLayout llSearch;
    LinearLayout llEmptySearchView, llSearchView;
    RecyclerView searchMenuRecyclerView;
    ArrayList<MenuModel> filteredlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        setToolbar();

        mAppBarLayout = findViewById(R.id.main_appbar);
        tabLayout = findViewById(R.id.tab_layout);
        llToolbar = findViewById(R.id.llToolbar);
        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        imgBtnFavourite = findViewById(R.id.imgBtnFavourite);
        imgSearch = findViewById(R.id.imgSearch);
        llSearch = findViewById(R.id.llSearch);
        llEmptySearchView = findViewById(R.id.llEmptySearchView);
        searchView = findViewById(R.id.searchView);
        llSearchView = findViewById(R.id.llSearchView);
        searchMenuRecyclerView = findViewById(R.id.searchMenuRecyclerView);

        mAppBarLayout.addOnOffsetChangedListener(this);

        menuList.add(new MenuModel("", "", false, false));
        menuList.add(new MenuModel("Picked for you", "", true, false));
        menuList.add(new MenuModel("Crispy Chicken Burger", "Crispy Chicken, Mayo or Spicy Mayo and Lettuce", false, true));
        menuList.add(new MenuModel("Dummy Burger", "Dummy Burger Description", false, false));
        menuList.add(new MenuModel("Mayo Burger", "Mayo Burger", false, false));
        menuList.add(new MenuModel("Individual Rolls", "", true, false));
        menuList.add(new MenuModel("Hot Chips Roll", "Hot Chips Roll Tomato", false, false));
        menuList.add(new MenuModel("Dummy Roll", "Dummy Roll Description", false, true));
        menuList.add(new MenuModel("Drinks", "", true, false));
        menuList.add(new MenuModel("Can 375ml", "", false, false));
        menuList.add(new MenuModel("Coke 330ml", "Coca-Cola 330ml", false, false));
        menuList.add(new MenuModel("Bottom 660ml", "", false, false));

        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).isHeader) {
                tabList.add(new MenuTabModel(menuList.get(i).name, i));
            }
        }

        for (int i = 0; i < tabList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabList.get(i).name));
        }

        restaurantMenuHeaderAdapter = new RestaurantMenuHeaderAdapter(menuList, false);
        linearLayoutManager = new LinearLayoutManager(this);
        menuRecyclerView.setLayoutManager(linearLayoutManager);
        menuRecyclerView.setItemAnimator(new DefaultItemAnimator());
        menuRecyclerView.setAdapter(restaurantMenuHeaderAdapter);

        restaurantMenuSearchAdapter = new RestaurantMenuHeaderAdapter(filteredlist, true);
        searchMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchMenuRecyclerView.setAdapter(restaurantMenuSearchAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                scrollToMenuHeaderOnTabTap(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                scrollToMenuHeaderOnTabTap(tab.getPosition());
            }
        });

        slideUp(llToolbar);

        if (menuRecyclerView != null) {

            menuRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    if (newState == 0) {
                        isScrolled = false;
                    } else {
                        isScrolled = true;
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (isScrolled) {
                        int top = linearLayoutManager.findFirstVisibleItemPosition();
                        int bottom = linearLayoutManager.findLastVisibleItemPosition();

                        int pos = 0;
                        if (bottom == menuList.size() - 1) {
                            // First judge to slide to the bottom, the tab is positioned to the last
                            pos = tabList.size() - 1;
                        } else if (top == tabList.get(tabList.size() - 1).position) {
                            // If top is equal to the specified position, you can correspond to the tab,
                            pos = tabList.get(tabList.size() - 1).position;
                        } else {
                            // The loop traversal needs to compare the position of i + 1, so the loop length should be reduced by 1.
                            // If i <top <i + 1, then the tab should be positioned to the character at position i, whether it is sliding up or down
                            for (int i = 0; i < tabList.size() - 1; i++) {
                                if (top == tabList.get(i).position) {
                                    pos = i;
                                    break;
                                } else if (top > tabList.get(i).position && top < tabList.get(i + 1).position) {
                                    pos = i;
                                    break;
                                }
                            }
                        }

                        // Set the tab to the pos
                        tabLayout.setScrollPosition(pos, 0f, true);
                    }
                }
            });
        }

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMenu();
            }
        });

        llSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSearch.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                makeSearchEmpty();
                return false;
            }
        });
    }

    public static void slideUp(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                0,
                view.getHeight() == 0 ? -268 : -view.getHeight());
        animate.setDuration(500);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                -view.getHeight(),
                0);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    @Override
    public void onBackPressed() {
        if (llSearch.getVisibility() == View.VISIBLE) {
            llSearch.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void filter(String text) {
        // running a for loop to compare elements.
        if (text.isEmpty()) {
            makeSearchEmpty();
            return;
        }
        filteredlist.clear();
        for (MenuModel item : menuList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.name.toLowerCase().contains(text.toLowerCase()) || item.description.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            searchMenuRecyclerView.setVisibility(View.GONE);
            llEmptySearchView.setVisibility(View.VISIBLE);
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            restaurantMenuSearchAdapter.filterList(filteredlist);
            searchMenuRecyclerView.setVisibility(View.VISIBLE);
            llEmptySearchView.setVisibility(View.GONE);
        }
    }

    private void makeSearchEmpty() {
        restaurantMenuSearchAdapter.filterList(new ArrayList<>());
        searchMenuRecyclerView.setVisibility(View.GONE);
        llEmptySearchView.setVisibility(View.GONE);
    }

    public static void searchMenu() {
        llSearch.setVisibility(View.VISIBLE);
        searchView.requestFocus();
    }

    private void scrollToMenuHeaderOnTabTap(int tabPosition) {
        if (!isScrolled) {
            linearLayoutManager.scrollToPositionWithOffset(tabList.get(tabPosition).position, 280);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                slideDown(llToolbar);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                slideUp(llToolbar);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hungry Birds");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onFavouriteClick(View view) {
        isFavourite = !isFavourite;
        if (isFavourite) {
            imgBtnFavourite.setImageResource(R.drawable.ic_heart);
        } else {
            imgBtnFavourite.setImageResource(R.drawable.ic_heart_border);
        }
    }

    public void onBackClick(View view) {
        finish();
    }
}
