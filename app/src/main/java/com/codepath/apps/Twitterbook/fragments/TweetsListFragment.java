package com.codepath.apps.Twitterbook.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.activity.TweetsDetailActivity;
import com.codepath.apps.Twitterbook.adapters.TimelineRecyclerViewAdapter;
import com.codepath.apps.Twitterbook.models.TweetModel;
import com.codepath.apps.Twitterbook.models.UserProfileModel;
import com.codepath.apps.Twitterbook.network.TwitterClient;
import com.codepath.apps.Twitterbook.utils.DividerItemDecoration;
import com.codepath.apps.Twitterbook.utils.EndlessScrollListener;
import com.codepath.apps.Twitterbook.utils.ItemClickSupport;
import com.codepath.apps.Twitterbook.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TweetsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TweetsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TweetsListFragment extends Fragment implements ComposeTweetFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String SCREEN_NAME = "screen_name";
    private int mPage;
    private String screenName;

    private ArrayList<TweetModel> tweets;
    private TimelineRecyclerViewAdapter timelineAdapter;
    private EndlessScrollListener endlessScrollListener;

    private TwitterClient twitterClient;
    private UserProfileModel userProfileModel;
    private int pageNo = 1;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.rvTimeline)
    RecyclerView rvTimeline;

    private OnFragmentInteractionListener mListener;

    public TweetsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @return A new instance of fragment TweetsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TweetsListFragment newInstance(int page, String screenName) {
        TweetsListFragment fragment = new TweetsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(SCREEN_NAME, screenName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
            screenName = getArguments().getString(SCREEN_NAME);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        ButterKnife.bind(this, view);

        twitterClient = new TwitterClient(getActivity());

        initializeRecyclerView();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTweets(0,screenName, true);
            }
        });
        loadTweets(pageNo,screenName, true);

        loadUserProfile();

        ItemClickSupport.addTo(rvTimeline).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                TweetModel currentTweet = tweets.get(position);
                Intent intent = new Intent(getActivity(), TweetsDetailActivity.class);
                intent.putExtra("tweet", Parcels.wrap(currentTweet));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onUserProfileLoaded(UserProfileModel userProfileModel);
        void onTweetListLoadedUpdated(ArrayList<TweetModel> tweetModels);
    }

    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        rvTimeline.setLayoutManager(linearLayoutManager);
        endlessScrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                pageNo++;
                loadTweets(pageNo,"pranayairan", false);
            }
        };

        rvTimeline.addOnScrollListener(endlessScrollListener);
        timelineAdapter = new TimelineRecyclerViewAdapter(tweets, getActivity());
        rvTimeline.setAdapter(timelineAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvTimeline.addItemDecoration(itemDecoration);
    }

    private void loadUserProfile() {
        twitterClient.getUsersProfile(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                userProfileModel = new UserProfileModel(json);
                timelineAdapter.setUserProfileModel(userProfileModel);
                if (mListener != null) {
                    mListener.onUserProfileLoaded(userProfileModel);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void loadTweets(final int page, String screenName, final boolean isfirstLoad) {
        if (Utils.isOnline()) {
            switch (mPage) {
                case 0:
                    loadHomeTimeline(page, isfirstLoad);
                    break;
                case 1:
                    loadMentionsTimeline(page, isfirstLoad);
                    break;
                case 3:
                    loadUserTweets(page,screenName, isfirstLoad);
                    break;
                case 4:
                    loadFavoriteTweets(page, screenName, isfirstLoad);
                    break;
            }
        } else {
            Snackbar.make(rvTimeline, "Please check internet connection", Snackbar.LENGTH_SHORT).show();
            timelineAdapter.setTweetsList(getOffLineTweets());
            timelineAdapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        }

    }

    @Override
    public void onTweetSuccess(TweetModel tweetModel) {
        timelineAdapter.addTweetAtStart(tweetModel);
        timelineAdapter.notifyDataSetChanged();
        rvTimeline.scrollToPosition(0);
    }

    private List<TweetModel> getOffLineTweets() {
        return SQLite.select().from(TweetModel.class).queryList();
    }

    private void loadHomeTimeline(final int page, final boolean isfirstLoad) {
        twitterClient.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweets = TweetModel.fromJson(json);
                if (isfirstLoad) {
                    timelineAdapter.setTweetsList(tweets);
                } else {
                    timelineAdapter.addAll(tweets);
                }
                swipeContainer.setRefreshing(false);
                timelineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (statusCode == 429) {
                    loadTweets(page,"", isfirstLoad);
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void loadMentionsTimeline(final int page, final boolean isfirstLoad) {
        twitterClient.getMentionsTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweets = TweetModel.fromJson(json);
                if (isfirstLoad) {
                    timelineAdapter.setTweetsList(tweets);
                } else {
                    timelineAdapter.addAll(tweets);
                }
                swipeContainer.setRefreshing(false);
                timelineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (statusCode == 429) {
                    loadTweets(page,"", isfirstLoad);
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void loadFavoriteTweets(final int page,final String screenName, final boolean isfirstLoad) {
        twitterClient.getFavoriteTweets(page,screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweets = TweetModel.fromJson(json);
                if (isfirstLoad) {
                    timelineAdapter.setTweetsList(tweets);
                } else {
                    timelineAdapter.addAll(tweets);
                }
                swipeContainer.setRefreshing(false);
                timelineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (statusCode == 429) {
                    loadTweets(0,screenName, isfirstLoad);
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void loadUserTweets(final int page, final String screenName, final boolean isfirstLoad) {
        twitterClient.getUsersTweets(page, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweets = TweetModel.fromJson(json);
                if (isfirstLoad) {
                    timelineAdapter.setTweetsList(tweets);
                } else {
                    timelineAdapter.addAll(tweets);
                }
                swipeContainer.setRefreshing(false);
                timelineAdapter.notifyDataSetChanged();
                mListener.onTweetListLoadedUpdated(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (statusCode == 429) {
                    loadTweets(page,"", isfirstLoad);
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
