package com.indian.states.capitals.indianstates;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    View profileFragement;
    View v;
    private Button logOutBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView profilename;
    private TextView contactno;
    private TextView useremailid;
    private TextView reset;
    private ImageButton profileImageButton;
    private DatabaseReference databaseReference;
    private Uri imageUri;
    private ProgressBar mProgressBar;

    private static final int GALLERY_PICK = 1;

    private StorageReference mImageStorage;
    private CircleImageView profileImage;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileFragement = inflater.inflate(R.layout.fragment_profile,container,false);
        profilename= profileFragement.findViewById(R.id.profname_id);
        contactno= profileFragement.findViewById(R.id.profcontact_id);
        useremailid= profileFragement.findViewById(R.id.profemail_id);
        reset= profileFragement.findViewById(R.id.reset_pass_id);
        profileImageButton = profileFragement.findViewById(R.id.profile_image_button);
        mProgressBar = profileFragement.findViewById(R.id.image_progress);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        profileImage = profileFragement.findViewById(R.id.profile_image);

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent =  new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"),GALLERY_PICK);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ResetPasswordActivity.class));
            }
        });



        logOutBtn = profileFragement.findViewById(R.id.btn_logout);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                getActivity().finish();
            }
        });
        return profileFragement;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=profileFragement;
        loaddata();
    }

    private void loaddata() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child("name").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String cont = dataSnapshot.child("contact").getValue().toString();
                    if(dataSnapshot.child("profilePicUrl").exists()) {
                        String image = dataSnapshot.child("profilePicUrl").getValue().toString();
                        if (image != null) {
                            Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImage);
                        }
                    }
                    profilename.setText(username);
                    useremailid.setText(email);
                    contactno.setText(cont);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null && data.getData() != null ) {
            imageUri = data.getData();
            Toast.makeText(getActivity(), imageUri.toString(), Toast.LENGTH_SHORT).show();
            profileImage.setImageURI(imageUri);
            uploadImageToFirebaseStorage();
        }
    }

    private void uploadImageToFirebaseStorage() {

        if (imageUri != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            mImageStorage = FirebaseStorage.getInstance().getReference("profile pics/"+"/"+user.getUid()+".jpg");
            mImageStorage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("profilePicUrl",profileImageUrl);

                    databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_LONG).show();
                                mProgressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getActivity(), "Failed to update profile, try again", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

