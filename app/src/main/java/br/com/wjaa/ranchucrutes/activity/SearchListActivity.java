package br.com.wjaa.ranchucrutes.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import br.com.wjaa.ranchucrutes.R;
import br.com.wjaa.ranchucrutes.adapter.SearchingListAdapter;
import br.com.wjaa.ranchucrutes.listener.RecyclerViewOnClickListenerHack;
import br.com.wjaa.ranchucrutes.service.RanchucrutesConstants;
import br.com.wjaa.ranchucrutes.view.SearchingListModel;

/**
 * Created by wagner on 02/10/15.
 */
public abstract class SearchListActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    protected SearchingListAdapter adapter;
    protected CoordinatorLayout clContainer;
    protected RecyclerView mRecyclerView;
    protected List<SearchingListModel> mList;
    protected List<SearchingListModel> mListFilter;

    private Toolbar toolbar;

    private MenuItem itemMenu;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        //==========================

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mList = bundle.getParcelableArrayList(RanchucrutesConstants.PARAM_LIST_SEARCH);
            mListFilter = cloneList(mList);
        }

        clContainer = (CoordinatorLayout) findViewById(R.id.cl_container);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager( this );
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        adapter = new SearchingListAdapter(this, mListFilter);
        mRecyclerView.setAdapter(adapter);

    }

    protected List<SearchingListModel> cloneList(List<SearchingListModel> mList) {
        List<SearchingListModel> clone = new ArrayList<>(mList.size());
        return cloneList(mList,clone);

    }

    protected List<SearchingListModel> cloneList(List<SearchingListModel> mList, List<SearchingListModel> clone) {
        for (SearchingListModel s : mList){
            clone.add(s);
        }
        return clone;

    }

    public abstract void filter( String q );



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        itemMenu = menu.findItem(R.id.search);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){
            searchView = (SearchView) itemMenu.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView( itemMenu );
        }

        ImageView closeButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close_white_18dp);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Pesquise aqui");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        searchView.onActionViewExpanded();
        //itemMenu.expandActionView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }
        /*else if( id == R.id.action_delete ){
            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                    SearchableProvider.AUTHORITY,
                    SearchableProvider.MODE);

            searchRecentSuggestions.clearHistory();

            Toast.makeText(this, "Cookies removidos", Toast.LENGTH_SHORT).show();
        }*/

        return true;
    }


    @Override
    public void onClickListener(View view, int position) {
        Intent intent = new Intent(this, SearchingListModel.class);
        intent.putExtra("car", mListFilter.get(position));

        // TRANSITIONS
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){

           /* View ivCar = view.findViewById(R.id.tviv_car);
            View tvModel = view.findViewById(R.id.tv_model);
            View tvBrand = view.findViewById(R.id.tv_brand);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    Pair.create(ivCar, "element1"),
                    Pair.create( tvModel, "element2" ),
                    Pair.create( tvBrand, "element3" ));*/

            //*startActivity(intent, options.toBundle() );
        }
        else{
            startActivity(intent);
        }
    }

    @Override
    public void onLongPressClickListener(View view, int position) {}


}
