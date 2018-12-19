package com.metropolitan.nemanja.projekat_nmd.Activities.Fragments;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.metropolitan.nemanja.projekat_nmd.Activities.Adapter.MoviesAdapter;
import com.metropolitan.nemanja.projekat_nmd.Activities.Api.Client;
import com.metropolitan.nemanja.projekat_nmd.Activities.Api.Service;
import com.metropolitan.nemanja.projekat_nmd.Activities.Model.Movie;
import com.metropolitan.nemanja.projekat_nmd.Activities.Model.MoviesResponse;
import com.metropolitan.nemanja.projekat_nmd.BuildConfig;
import com.metropolitan.nemanja.projekat_nmd.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    public static final String LOG_TAG = MoviesAdapter.class.getName();



    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.popular_activity_main, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Fetching movies...");
        pd.setCancelable(false);
        pd.show();

        movieList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new MoviesAdapter(getActivity(), movieList);


        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_green_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                getFragmentManager()
                        .beginTransaction()
                        .detach(UpcomingFragment.this)
                        .attach(UpcomingFragment.this)
                        .commit();
                Toast.makeText(getActivity(), "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });


        loadJSON();

        return view;
    }
    private void loadJSON() {

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getActivity().getApplicationContext(), "Please obtain API Key firstly from themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }

            Client Client = new Client();
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.getUpcoming(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getActivity().getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()){
                        swipeContainer.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(getActivity(), "Error Fetching Data!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
