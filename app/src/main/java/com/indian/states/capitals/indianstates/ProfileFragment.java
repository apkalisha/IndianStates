package com.indian.states.capitals.indianstates;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
    View profileFragment;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView profilename;
    private TextView contactno;
    private TextView useremailid;
    private DatabaseReference databaseReference;
    private Uri imageUri;
    private ProgressBar mProgressBar;
    private ValueEventListener valueEventListener;

    private static final int GALLERY_PICK = 1;

    private CircleImageView profileImage;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileFragment = inflater.inflate(R.layout.fragment_profile,container,false);
        profilename= profileFragment.findViewById(R.id.profname_id);
        contactno= profileFragment.findViewById(R.id.profcontact_id);
        useremailid= profileFragment.findViewById(R.id.profemail_id);
        TextView reset = profileFragment.findViewById(R.id.reset_pass_id);
        ImageButton profileImageButton = profileFragment.findViewById(R.id.profile_image_button);
        mProgressBar = profileFragment.findViewById(R.id.image_progress);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        profileImage = profileFragment.findViewById(R.id.profile_image);
        ImageButton editNameBtn = profileFragment.findViewById(R.id.edit_name_btn);
        valueEventListener = new ValueEventListener() {
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
        };

        loadData();

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent =  new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"),GALLERY_PICK);
            }
        });

        editNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName(v);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword(view);
            }
        });


        Button logOutBtn = profileFragment.findViewById(R.id.btn_logout);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        return profileFragment;
    }

    private void logOut() {
        mAuth.signOut();
        databaseReference.removeEventListener(valueEventListener);
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        getActivity().finish();
    }

    private void loadData() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        databaseReference.addValueEventListener(valueEventListener);

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
            StorageReference mImageStorage = FirebaseStorage.getInstance().getReference("profile pics/" + "/" + user.getUid() + ".jpg");
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
    public void updateName(View view) {
        final Dialog myDialog = new Dialog(getActivity());
        TextView txtclose;
        Button btnSave;
        final EditText updatedName;
        myDialog.setContentView(R.layout.change_name_popup);
        myDialog.setTitle("Update Name");
        txtclose = myDialog.findViewById(R.id.txt_close);
        txtclose.setText("X");
        btnSave =  myDialog.findViewById(R.id.save_btn);
        updatedName = myDialog.findViewById(R.id.edit_name);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(updatedName.getText().toString()))
                    updatedName.setError("Required");
                else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Update Name")
                            .setMessage("Do you really want to change your name?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    myDialog.dismiss();
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("name",updatedName.getText().toString().trim());

                                    databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getActivity(), "Failed to update profile, try again", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                                }})
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myDialog.dismiss();
                                }
                            }).show();

                }
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
    public void updatePassword(View view) {
        final Dialog myDialog = new Dialog(getActivity());
        TextView txtclose;
        Button btnSave;
        myDialog.setContentView(R.layout.change_password_popup);
        myDialog.setTitle("Change Password");
        txtclose = myDialog.findViewById(R.id.txt_close);
        txtclose.setText("X");
        btnSave =  myDialog.findViewById(R.id.save_btn);
        final EditText oldPassword =  myDialog.findViewById(R.id.old_password);
        final EditText newPassword =  myDialog.findViewById(R.id.new_password);
        final EditText confirmPassword =  myDialog.findViewById(R.id.confirm_password);
        final ProgressBar progressBar = myDialog.findViewById(R.id.progressBar);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(oldPassword.getText().toString()))
                    oldPassword.setError("Required");
                if(TextUtils.isEmpty(newPassword.getText().toString()))
                    newPassword.setError("Required");
                if(TextUtils.isEmpty(confirmPassword.getText().toString()))
                    confirmPassword.setError("Required");
                if(newPassword.getText().toString().length()<6)
                    newPassword.setError("Too Short");
                else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Change Password")
                            .setMessage("Do you really want to change your password?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {

                                        progressBar.setVisibility(View.VISIBLE);
                                        String email = user.getEmail();
                                        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword.getText().toString());
                                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    user.updatePassword(newPassword.getText().toString())
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()) {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        myDialog.dismiss();
                                                                        Toast.makeText(getActivity(),"Password Updated Successfully",Toast.LENGTH_SHORT).show();

                                                                    } else {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        myDialog.dismiss();
                                                                        Toast.makeText(getActivity(), "Failed to change password", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    oldPassword.setError("Wrong Old Password");
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });

                                    } else {
                                        confirmPassword.setError("Does not Match");
                                    }
                                }})
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myDialog.dismiss();
                                }
                            }).show();

                }
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
}

