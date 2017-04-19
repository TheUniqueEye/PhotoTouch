package edu.ucsb.cs.cs185.JingYan.phototouch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GridView gridview;
    private ImageAdapter imageAdapter = new ImageAdapter(this);
    private ImageAdapter imageAdapter_empty = new ImageAdapter(this);
    private Context mainC = this;
    private AdapterView.OnItemClickListener myOnItemClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                final int PICK_IMAGE_REQUEST = 9876;

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                // launch an image picking activity
                Intent imgPickingIntent = new Intent();
                imgPickingIntent.setType("image/*");
                imgPickingIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imgPickingIntent, PICK_IMAGE_REQUEST);

            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // initiate to list view
        gridview = (GridView) findViewById(R.id.list_view);
        //gridview = (GridView) findViewById(R.id.grid_view);


        // register onItemClickListener
        myOnItemClickListener
                = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                // get uri
                Uri item = (Uri) parent.getItemAtPosition(position);

                // pass uri through intent to imageActivity
                Intent openImage = new Intent(mainC, ImageActivity.class);
                openImage.putExtra("UriFromMain", item.toString());
                startActivity(openImage);
            }
        };
    }


    // get uri back from image picking activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        final int PICK_IMAGE_REQUEST = 9876;
        final Uri uri;

        if (requestCode == PICK_IMAGE_REQUEST) {

            if(intent!=null) { // if an image is chosen, pass it's uri through intent
                uri = intent.getData();

                try {
                    Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    Bitmap bb = Bitmap.createScaledBitmap(b, (int) ((b.getWidth() * 1.0f / b.getHeight()) * 800), 800, false);
                    imageAdapter.addBitmapToList(bb, uri);
                    gridview.setAdapter(imageAdapter);
                    gridview.setOnItemClickListener(myOnItemClickListener);

                } catch (Exception e) {
                    Log.e("Error Message", e.getMessage());
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.e("Clear Items", "clear all items");
        imageAdapter.clearStorage();
        gridview.setAdapter(imageAdapter);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_grid) { /////// set to Grid View


            gridview.setAdapter(imageAdapter_empty);

            ViewGroup.LayoutParams params = gridview.getLayoutParams();
            params.width = 0;
            params.height = 0;
            gridview.setLayoutParams(params);

            //above: clear the list_view
            //==========================
            //below: set to grid_view

            imageAdapter.setListMode(false);
            gridview = (GridView) findViewById(R.id.grid_view);
            gridview.setAdapter(imageAdapter);
            gridview.setOnItemClickListener(myOnItemClickListener);


        } else if (id == R.id.nav_list) { /////// set to List View


            gridview.setAdapter(imageAdapter_empty);

            //above: clear the grid_view
            //=============
            //below: set to list_view

            imageAdapter.setListMode(true);
            gridview = (GridView) findViewById(R.id.list_view);

            ViewGroup.LayoutParams params = gridview.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            gridview.setLayoutParams(params);

            gridview.setAdapter(imageAdapter);
            gridview.setOnItemClickListener(myOnItemClickListener);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
