package com.free.top.tvshows.tube.api;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.free.top.tvshows.tube.Application;
import com.free.top.tvshows.tube.api.model.Person;
import com.free.top.tvshows.tube.api.model.BaseModel;
import com.free.top.tvshows.tube.api.model.Cast;
import com.free.top.tvshows.tube.api.model.CastCredit;
import com.free.top.tvshows.tube.api.model.CrewCredit;
import com.free.top.tvshows.tube.api.model.Genre;
import com.free.top.tvshows.tube.api.model.Movie;
import com.free.top.tvshows.tube.api.model.TVCastCredit;
import com.free.top.tvshows.tube.api.model.TVCrewCredit;
import com.free.top.tvshows.tube.api.model.TVSeason;
import com.free.top.tvshows.tube.api.model.TVShow;
import com.free.top.tvshows.tube.api.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TMDb {

    private static final String API_KEY = "92a13ac5f3007ace6e990a6eb73de5e2";
    private static final String API_URL = "https://api.themoviedb.org/3";

    public static class Discover {

        public static void movieDiscover(int page, int sort, int yearFrom, int yearTo, int genreId, final Listener<Movie> listener) {
            String sortBy;
            switch (sort) {
                case 0:
                    sortBy = "popularity.desc";
                    break;
                case 1:
                    sortBy = "vote_average.desc";
                    break;
                case 2:
                    sortBy = "primary_release_date.desc";
                    break;
                case 3:
                    sortBy = "revenue.desc";
                    break;
                default:
                    sortBy = "popularity.desc";
                    break;
            }
            Locale locale = Locale.getDefault();
            String url = API_URL + "/discover/movie" +
                    "?api_key=" + API_KEY +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase() +
                    "&page=" + page +
                    "&sort_by=" + sortBy +
                    "&primary_release_date.gte=" + yearFrom + "-01-01" +
                    "&primary_release_date.lte=" + yearTo + "-12-31" +
                    (genreId != -1 ? "&with_genres=" + genreId : "");

            Log.d("TMDb.Discover", url);

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<Movie> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new Movie.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Discover", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }

        public static void tvShowDiscover(int page, int sort, int yearFrom, int yearTo, int genreId, final Listener<TVShow> listener) {
            String sortBy;
            switch (sort) {
                case 0:
                    sortBy = "popularity.desc";
                    break;
                case 1:
                    sortBy = "vote_average.desc";
                    break;
                case 2:
                    sortBy = "first_air_date.desc";
                    break;
                default:
                    sortBy = "popularity.desc";
                    break;
            }
            Locale locale = Locale.getDefault();
            String url = API_URL + "/discover/tv" +
                    "?api_key=" + API_KEY +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase() +
                    "&page=" + page +
                    "&sort_by=" + sortBy +
                    "&air_date.gte=" + yearFrom + "-01-01" +
                    "&air_date.lte=" + yearTo + "-12-31" +
                    (genreId != -1 ? "&with_genres=" + genreId : "");

            Log.d("TMDb.Discover", url);

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<TVShow> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new TVShow.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Discover", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }
    }

    public static class Genres {
        public static void getMovieList(final Listener<Genre> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/genre/movie/list" +
                    "?api_key=" + API_KEY +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jGenres = response.getJSONArray("genres");
                        List<Genre> data = new ArrayList<>();
                        for (int i = 0; i < jGenres.length(); i++) {
                            data.add(new Genre.Builder().withJSONObject(jGenres.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, 1, 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Genres", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);

        }

        public static void getTvList(final Listener<Genre> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/genre/tv/list" +
                    "?api_key=" + API_KEY +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jGenres = response.getJSONArray("genres");
                        List<Genre> data = new ArrayList<>();
                        for (int i = 0; i < jGenres.length(); i++) {
                            data.add(new Genre.Builder().withJSONObject(jGenres.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, 1, 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Genres", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);

        }
    }

    public static class Movies {

        public static void getDetails(int id, final MovieDetailsListener listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/movie/" + id +
                    "?api_key=" + API_KEY +
                    "&language=" + locale.getLanguage() +
                    "&append_to_response=credits,videos,recommendations,reviews,similar&include_image_language=en";

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Movie movie = new Movie.Builder().withJSONObject(response).build();

                        JSONObject jCredits = response.getJSONObject("credits");
                        JSONArray jCast = jCredits.getJSONArray("cast");
                        ArrayList<Cast> casts = new ArrayList<>();
                        for (int i = 0; i < jCast.length(); i ++) {
                            casts.add(new Cast.Builder().withJSONObject(jCast.getJSONObject(i)).build());
                        }

                        ArrayList<Video> videos = new ArrayList<>();
                        if (response.has("videos")) {
                            JSONObject jVideos = response.getJSONObject("videos");
                            JSONArray jResults = jVideos.getJSONArray("results");
                            for (int i = 0; i < jResults.length(); i++) {
                                Video video = new Video.Builder().withJSONObject(jResults.getJSONObject(i)).build();
                                if (video.getSite() != null && video.getSite().equals("YouTube"))
                                    videos.add(video);
                            }
                        }

                        ArrayList<Movie> recommendations = new ArrayList<>();
                        if (response.has("recommendations")) {
                            JSONObject jRecommendations = response.getJSONObject("recommendations");
                            JSONArray jResults = jRecommendations.getJSONArray("results");
                            for (int i = 0; i < jResults.length(); i++) {
                                recommendations.add(new Movie.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                            }
                        }

                        ArrayList<Movie> similar = new ArrayList<>();
                        if (response.has("similar")) {
                            JSONObject jSimilar = response.getJSONObject("similar");
                            JSONArray jResults = jSimilar.getJSONArray("results");
                            for (int i = 0; i < jResults.length(); i++) {
                                similar.add(new Movie.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                            }
                        }

                        listener.onResponse(movie, casts, videos, similar, recommendations);

                    } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError();
                }

                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);


        }

        public static void getNowPlaying(int page, final Listener<Movie> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/movie/now_playing" +
                    "?api_key=" + API_KEY +
                    "&page=" + page +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<Movie> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new Movie.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }

        public static void getPopular(int page, final Listener<Movie> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/movie/popular" +
                    "?api_key=" + API_KEY +
                    "&page=" + page +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<Movie> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new Movie.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }

        public static void getTopRated(int page, final Listener<Movie> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/movie/top_rated" +
                    "?api_key=" + API_KEY +
                    "&page=" + page +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<Movie> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new Movie.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }

        public static void getUpcoming(int page, final Listener<Movie> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/movie/upcoming" +
                    "?api_key=" + API_KEY +
                    "&page=" + page +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<Movie> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new Movie.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }
    }

    public static class People {

        public static void getDetails(int id, final ActorDetailsListener listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/person/" + id +
                    "?api_key=" + API_KEY +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase() +
                    "&append_to_response=images,tagged_images,movie_credits,tv_credits,external_ids";

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Person actor = new Person.Builder().withJSONObject(response).build();

                        ArrayList<String> images = new ArrayList<>();
                        if (response.has("images")) {
                            JSONObject jImages = response.getJSONObject("images");
                            JSONArray jProfiles = jImages.getJSONArray("profiles");
                            for (int i = 0; i < jProfiles.length(); i++) {
                                JSONObject jProfile = jProfiles.getJSONObject(i);
                                if (jProfile.has("file_path"))
                                    images.add(jProfile.getString("file_path"));
                            }
                        }

                        ArrayList<String> taggedImages = new ArrayList<>();
                        if (response.has("tagged_images")) {
                            JSONObject jTaggedImages = response.getJSONObject("tagged_images");
                            JSONArray jResults = jTaggedImages.getJSONArray("results");
                            for (int i = 0; i < jResults.length(); i++) {
                                JSONObject jImage = jResults.getJSONObject(i);
                                if (jImage.has("file_path"))
                                    taggedImages.add(jImage.getString("file_path"));
                            }
                        }

                        ArrayList<CastCredit> movieCast = new ArrayList<>();
                        ArrayList<CrewCredit> movieCrew = new ArrayList<>();
                        if (response.has("movie_credits")) {
                            JSONObject jMovieCredits = response.getJSONObject("movie_credits");
                            JSONArray jCast = jMovieCredits.getJSONArray("cast");
                            for (int i = 0; i < jCast.length(); i++) {
                                movieCast.add(new CastCredit.Builder().withJSONObject(jCast.getJSONObject(i)).build());
                            }
                            JSONArray jCrew = jMovieCredits.getJSONArray("crew");
                            for (int i = 0; i < jCrew.length(); i++) {
                                movieCrew.add(new CrewCredit.Builder().withJSONObject(jCrew.getJSONObject(i)).build());
                            }
                        }

                        Collections.sort(movieCast);
                        Collections.sort(movieCrew);

                        ArrayList<TVCastCredit> tvCast = new ArrayList<>();
                        ArrayList<TVCrewCredit> tvCrew = new ArrayList<>();
                        if (response.has("tv_credits")) {
                            JSONObject jTvCredits = response.getJSONObject("tv_credits");
                            JSONArray jCast = jTvCredits.getJSONArray("cast");
                            for (int i = 0; i < jCast.length(); i++) {
                                tvCast.add(new TVCastCredit.Builder().withJSONObject(jCast.getJSONObject(i)).build());
                            }
                            JSONArray jCrew = jTvCredits.getJSONArray("crew");
                            for (int i = 0; i < jCrew.length(); i++) {
                                tvCrew.add(new TVCrewCredit.Builder().withJSONObject(jCrew.getJSONObject(i)).build());
                            }
                        }

                        Collections.sort(tvCast);
                        Collections.sort(tvCrew);

                        listener.onResponse(actor, images, taggedImages, movieCast, movieCrew, tvCast, tvCrew);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }

        public static void getPopular(int page, final Listener<Person> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/person/popular" +
                    "?api_key=" + API_KEY +
                    "&page=" + page +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<Person> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new Person.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }
    }

    public static class Search {

        public static void searchMovies(String query, int page, final Listener<Movie> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/search/movie" +
                    "?api_key=" + API_KEY +
                    "&language=" + locale.getLanguage() +
                    "&page=" + page +
                    "&query=" + Uri.encode(query);

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<Movie> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new Movie.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Search", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }

        public static void searchTVShows(String query, int page, final Listener<TVShow> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/search/tv" +
                    "?api_key=" + API_KEY +
                    "&language=" + locale.getLanguage() +
                    "&page=" + page +
                    "&query=" + Uri.encode(query);

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<TVShow> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new TVShow.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Search", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }

        public static void searchPeople(String query, int page, final Listener<Person> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/search/person" +
                    "?api_key=" + API_KEY +
                    "&language=" + locale.getLanguage() +
                    "&page=" + page +
                    "&query=" + Uri.encode(query);

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<Person> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new Person.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Search", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }
    }

    public static class TV {

        public static void getDetails(int id, final TVShowDetailsListener listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/tv/" + id +
                    "?api_key=" + API_KEY +
                    "&language=" + locale.getLanguage() +
                    "&append_to_response=credits,videos,recommendations,similar,external_ids";

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        TVShow tvShow = new TVShow.Builder().withJSONObject(response).build();

                        JSONObject jCredits = response.getJSONObject("credits");
                        JSONArray jCast = jCredits.getJSONArray("cast");
                        ArrayList<Cast> casts = new ArrayList<>();
                        for (int i = 0; i < jCast.length(); i ++) {
                            casts.add(new Cast.Builder().withJSONObject(jCast.getJSONObject(i)).build());
                        }

                        ArrayList<Video> videos = new ArrayList<>();
                        if (response.has("videos")) {
                            JSONObject jVideos = response.getJSONObject("videos");
                            JSONArray jResults = jVideos.getJSONArray("results");
                            for (int i = 0; i < jResults.length(); i++) {
                                Video video = new Video.Builder().withJSONObject(jResults.getJSONObject(i)).build();
                                if (video.getSite() != null && video.getSite().equals("YouTube"))
                                    videos.add(video);
                            }
                        }

                        ArrayList<TVShow> recommendations = new ArrayList<>();
                        if (response.has("recommendations")) {
                            JSONObject jRecommendations = response.getJSONObject("recommendations");
                            JSONArray jResults = jRecommendations.getJSONArray("results");
                            for (int i = 0; i < jResults.length(); i++) {
                                recommendations.add(new TVShow.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                            }
                        }

                        ArrayList<TVShow> similar = new ArrayList<>();
                        if (response.has("similar")) {
                            JSONObject jSimilar = response.getJSONObject("similar");
                            JSONArray jResults = jSimilar.getJSONArray("results");
                            for (int i = 0; i < jResults.length(); i++) {
                                similar.add(new TVShow.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                            }
                        }

                        listener.onResponse(tvShow, casts, videos, similar, recommendations);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }

                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);


        }

        public static void getAiringToday(int page, final Listener<TVShow> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/tv/airing_today" +
                    "?api_key=" + API_KEY +
                    "&page=" + page +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<TVShow> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new TVShow.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }

        public static void getOnTheAir(int page, final Listener<TVShow> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/tv/on_the_air" +
                    "?api_key=" + API_KEY +
                    "&page=" + page +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<TVShow> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new TVShow.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }

        public static void getPopular(int page, final Listener<TVShow> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/tv/popular" +
                    "?api_key=" + API_KEY +
                    "&page=" + page +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<TVShow> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new TVShow.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }

        public static void getTopRated(int page, final Listener<TVShow> listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/tv/top_rated" +
                    "?api_key=" + API_KEY +
                    "&page=" + page +
                    "&language=" + locale.getLanguage()+"-"+locale.getCountry().toUpperCase();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int page = response.getInt("page");
                        int totalPages = response.getInt("total_pages");
                        List<TVShow> data = new ArrayList<>();
                        JSONArray jResults = response.getJSONArray("results");
                        for (int i = 0; i < jResults.length(); i++) {
                            data.add(new TVShow.Builder().withJSONObject(jResults.getJSONObject(i)).build());
                        }
                        listener.onResponse(data, page, totalPages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.Movies", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }
    }

    public static class TVSeasons {

        public static void getDetails(int tvId, int seasonId, final TVSeasonDetailsListener listener) {
            Locale locale = Locale.getDefault();
            String url = API_URL + "/tv/" + tvId + "/season/" + seasonId +
                    "?api_key=" + API_KEY +
                    "&language=" + locale.getLanguage();

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    TVSeason tvSeason = new TVSeason.Builder().withJSONObject(response).build();
                    listener.onResponse(tvSeason);
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TMDb.TVSeasons", error.toString());
                    listener.onError();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
            Application.getRequestQueue().add(jsonObjectRequest);
        }
    }

    public interface Listener<T extends BaseModel> {
        void onResponse(List<T> data, int page, int totalPages);
        void onError();
    }

    public interface ActorDetailsListener {
        void onResponse(Person actor, ArrayList<String> images, ArrayList<String> taggedImages, ArrayList<CastCredit> movieCast, ArrayList<CrewCredit> movieCrew, ArrayList<TVCastCredit> tvCast, ArrayList<TVCrewCredit> tvCrew);
        void onError();
    }

    public interface MovieDetailsListener {
        void onResponse(Movie movie, ArrayList<Cast> casts, ArrayList<Video> videos, ArrayList<Movie> similar, ArrayList<Movie> recommendations);
        void onError();
    }

    public interface TVShowDetailsListener {
        void onResponse(TVShow tvShow, ArrayList<Cast> movieCast, ArrayList<Video> videos, ArrayList<TVShow> similar, ArrayList<TVShow> recommendations);
        void onError();
    }

    public interface TVSeasonDetailsListener {
        void onResponse(TVSeason tvSeason);
        void onError();
    }
}
