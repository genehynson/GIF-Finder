package com.giffinder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.gifanimation.GifAnimationDrawable;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit.RestAdapter;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Developer key for gify
     */
    private String key = "dc6zaTOxFJmzC";
    RestAdapter restAdapter;

    private ListView listView;
    private SearchView searchView;
    private ImageView imageView;
    private Drawable[] drawables;
    private InputStream[] images;
    private int imageCounter = 0;
    private static int MAX_IMAGES = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        listView = new ListView(this);
        searchView = new SearchView(this);
        imageView = new ImageView(this);
        imageView.findViewById(R.id.imageView);
        listView.findViewById(R.id.listView);
        searchView.findViewById(R.id.searchView);


        GifService giphyFetcher = new GifService("http://api.giphy.com/v1/gifs");
        giphyFetcher.getTrending(this);
        //giphyFetcher.searchGifs("minions", 1);
    }

    public void parseImages(String[] urls) {
        drawables = new Drawable[urls.length+1];
        images = new InputStream[urls.length+1];
        for (int i = 0; i < urls.length; i++) {
            new DownloadImageTask().execute(urls[i]);
            drawables[i] = imageView.getDrawable();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void populateListView(String s) {
        TextView t = new TextView(this);
        t.append(s);
        listView.addView(t);
        //TODO: edit this
    }

    public void errorNoRecivingData(String s) {
        TextView t = new TextView(this);
        t.append(s);
        listView.addView(t);

    }

    private void addImage(InputStream image) {
        images[imageCounter++] = image;
        if (imageCounter == MAX_IMAGES) {
            GridView gridView = (GridView)findViewById(R.id.gridView);
            gridView.setAdapter(new ImageAdapter(this));
        }
    }



    /*
     * This class takes the list of urls and downloads each .gif as an inputstream
     */
    private class DownloadImageTask extends AsyncTask<String, Void, InputStream> {

        protected InputStream doInBackground(String... urls) {
            String urldisplay = urls[0];
            InputStream in = null;
            try {
                in = new java.net.URL(urldisplay).openStream();
            } catch (Exception e) {
                //TODO: sometimes null?
                //Log.e("Error", e.getMessage());
                //e.printStackTrace();
            }
            return in;
        }

        protected void onPostExecute(InputStream result) {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(result));
            ByteArrayBuffer baf;
            try {
                int b = bufferedreader.read();
                baf = new ByteArrayBuffer(result.available());
                while (b != -1) {
                    baf.append((byte) b);
                }
            } catch (IOException e) {
                Log.e("hello", "");
            }
            /*byte[] byteArray = baf.buffer();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
            Canvas canvas = new Canvas(bmp);
            */
            try {
                result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            addImage(result);
        }
    }



    /*
     * This class is an adapter for the GridView. It uses a special GifParser to put the gifs on
     * the GridView so they'll play
     */
    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter (Context c) {
            mContext = c;
        }
        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            AnimationDrawable drawable = null;
            try {
                drawable = new GifAnimationDrawable(images[position]);
            } catch (IOException e) {
                Log.e("IO Error", e.getMessage());
            }
            if (drawable != null) {
                imageView.setImageDrawable(drawable);
            }
            return imageView;

        }
    }
}
