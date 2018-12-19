package com.metropolitan.nemanja.projekat_nmd.Activities.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

import com.metropolitan.nemanja.projekat_nmd.Activities.Database.DatabaseHelper;
import com.metropolitan.nemanja.projekat_nmd.Activities.MainActivity;
import com.metropolitan.nemanja.projekat_nmd.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private AppCompatTextView name;
    private AppCompatTextView mail;
    private AppCompatButton selectPic;
    private AppCompatButton uploadPic;

    DatabaseHelper databaseHelper;



    private CircleImageView profileImageView;

    private static final int MY_CAMERA_REQUEST_CODE = 100;


    static final int SELECT_PHOTO = 1;


    Bitmap thumbnail;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseHelper = new DatabaseHelper(getContext());


        Bundle bundle = getArguments();

        String firstname = bundle.getString("FIRSTNAME");
        String lastname = bundle.getString("LASTNAME");
        final String email = bundle.getString("EMAIL");

        String fullname = firstname + " " + lastname;


        name = (AppCompatTextView) view.findViewById(R.id.name);
        name.setText(fullname);

        mail = (AppCompatTextView) view.findViewById(R.id.mail);
        mail.setText(email);

        profileImageView = (CircleImageView) view.findViewById(R.id.profile_image);

        byte[] photo = databaseHelper.getImage(email);

        if(photo != null){
            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage= BitmapFactory.decodeStream(imageStream);
            profileImageView.setImageBitmap(theImage);

        }



        selectPic = (AppCompatButton) view.findViewById(R.id.selectBtn);
        uploadPic = (AppCompatButton) view.findViewById(R.id.uploadBtn);


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //profileImageView.setEnabled(false);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
        } else {
            //profileImageView.setEnabled(true);
        }

        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);


            }

        });

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileImageView.setDrawingCacheEnabled(true);
                profileImageView.buildDrawingCache();
                Bitmap bitmap = profileImageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                databaseHelper.updateImage(email, data);
                Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                getActivity().recreate();

            }

        });







        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_PHOTO) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getApplicationContext().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    profileImageView.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        }
    }




}
