package com.metropolitan.nemanja.projekat_nmd.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metropolitan.nemanja.projekat_nmd.Activities.Database.DatabaseHelper;
import com.metropolitan.nemanja.projekat_nmd.Activities.Fragments.HelpFragment;
import com.metropolitan.nemanja.projekat_nmd.Activities.Fragments.NowPlayingFragment;
import com.metropolitan.nemanja.projekat_nmd.Activities.Fragments.PopularFragment;
import com.metropolitan.nemanja.projekat_nmd.Activities.Fragments.ProfileFragment;
import com.metropolitan.nemanja.projekat_nmd.Activities.Fragments.TopRatedFragment;
import com.metropolitan.nemanja.projekat_nmd.Activities.Fragments.UpcomingFragment;
import com.metropolitan.nemanja.projekat_nmd.R;

import java.io.ByteArrayInputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView tw;
    private ImageView headerImageView;
    private TextView fullName;
    private TextView fulLEmail;

    private DatabaseHelper databaseHelper = new DatabaseHelper(this);


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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Contact Via")
                        .setItems(R.array.ar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                if(which == 0){
                                    Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: +381649854155"));
                                    startActivity(intentCall);
                                }else if(which == 1){
                                    String[] to =
                                            {"nemanjakuzmanovic1994@gmail.com"};
                                    String[] cc = null;
                                    sendEmail(to,null,"HELP - [YOUR SUBJECT PROBLEM HERE]", "Dear Nemanja, \n[DESCRIBE PROBLEM HERE] \n Regards,\n" + fullName.getText().toString());
                                }
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        if(savedInstanceState == null){
            Fragment fragment = null;
            Class fragmentClass;
            fragmentClass = HelpFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();

            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        String firstnameFromIntent = getIntent().getStringExtra("FIRSTNAME");
        String lastnameFromIntent = getIntent().getStringExtra("LASTNAME");
        String fullname = firstnameFromIntent + " " + lastnameFromIntent;

        View hView =  navigationView.getHeaderView(0);
        CircleImageView imgvw = (CircleImageView)hView.findViewById(R.id.headerImageView);
        byte[] photo = databaseHelper.getImage(emailFromIntent);
        if(photo != null){
            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage= BitmapFactory.decodeStream(imageStream);
            imgvw.setImageBitmap(theImage);
        }
        fullName = (TextView)hView.findViewById(R.id.headerNameTextView);
        fullName.setText(fullname);

        fulLEmail = (TextView)hView.findViewById(R.id.headerEmailTextView);
        fulLEmail.setText(emailFromIntent);










//        fullName = (TextView) findViewById(R.id.headerNameTextView);
//        fullName.setText(firstnameFromIntent);
//
//        fulLEmail = (TextView) findViewById(R.id.headerEmailTextView);
//        fulLEmail.setText(emailFromIntent);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(!drawer.isDrawerOpen(GravityCompat.START)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();
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

    public void call(View v){
        Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: +381649854155"));
        startActivity(intentCall);
    }

    public void visit(View v){
        Intent mapsIntent = new Intent(this, MapsActivity.class);
        startActivity(mapsIntent);
    }

    public void mail(View v){
        String[] to =
                {"nemanjakuzmanovic1994@gmail.com"};
        String[] cc = null;
        sendEmail(to,null,"HELP - [YOUR SUBJECT PROBLEM HERE]", "Dear Nemanja, \n[DESCRIBE PROBLEM HERE] \n Regards,\n" + fullName.getText().toString());

    }

    private void sendEmail(String[] emailAddresses, String[] carbonCopies, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String[] to = emailAddresses;
        String[] cc = carbonCopies;
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email"));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_popular_movies) {

            Fragment fragment = null;
            Class fragmentClass;
            fragmentClass = PopularFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();


        } else if (id == R.id.nav_top_rated) {
            Fragment fragment = null;
            Class fragmentClass;
            fragmentClass = TopRatedFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();

        } else if (id == R.id.nav_upcoming) {
            Fragment fragment = null;
            Class fragmentClass;
            fragmentClass = UpcomingFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
        } else if (id == R.id.nav_now) {
            Fragment fragment = null;
            Class fragmentClass;
            fragmentClass = NowPlayingFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();

            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();

        } else if (id == R.id.help) {

            Fragment fragment = null;
            Class fragmentClass;
            fragmentClass = HelpFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();

            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();


        } else if (id == R.id.profile){

            String emailFromIntent = getIntent().getStringExtra("EMAIL");
            String firstnameFromIntent = getIntent().getStringExtra("FIRSTNAME");
            String lastnameFromIntent = getIntent().getStringExtra("LASTNAME");


            Fragment fragment = null;
            Class fragmentClass;
            fragmentClass = ProfileFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("EMAIL", emailFromIntent);
                bundle.putString("FIRSTNAME", firstnameFromIntent);
                bundle.putString("LASTNAME", lastnameFromIntent);
                fragment.setArguments(bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();



        } else if (id == R.id.logout){
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
