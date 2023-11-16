package com.example.appchat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.appchat.databinding.FragmentProfileBinding;
import com.example.appchat.model.UserModel;
import com.example.appchat.utils.AndroidUtils;
import com.example.appchat.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding _binding;
    UserModel userModel;
    ActivityResultLauncher<Intent> imagenPickLauncher;
    Uri selectedImageUrl;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagenPickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUrl = data.getData();
                            AndroidUtils.setProfilePic(getContext(), selectedImageUrl, _binding.profileImage);
                        }

                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = _binding.getRoot();


        guardarDatos();

        _binding.btnSeguir.setOnClickListener(v -> {
            updatebtnClick();
        });
        _binding.Logout.setOnClickListener(v -> {
            FirebaseUtil.logout();
            Intent intent = new Intent(getContext(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });


        _binding.profileImage.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512).createIntent(new Function1<Intent, Unit>() {
                @Override
                public Unit invoke(Intent intent) {
                    imagenPickLauncher.launch(intent);
                    return null;
                }
            });
        });
        return root;
    }


    private void updatebtnClick() {
        String newUsername = _binding.editUsername.getText().toString();
        if (newUsername.isEmpty() || newUsername.length() < 3) {
            _binding.editUsername.setError("Nombre invalido");
            return;
        }
        userModel.setUsername(newUsername);
        setinProgress(true);


        if (selectedImageUrl != null) {
            FirebaseUtil.getCurrentProfileStorage().putFile(selectedImageUrl).addOnCompleteListener(task -> {
                updateFirebase();
            });
        } else {
            updateFirebase();
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }

    private void updateFirebase() {
        FirebaseUtil.userDocumentReference().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setinProgress(false);
                if (task.isSuccessful()) {
                    AndroidUtils.showToast(getContext(), "Actualizacion exitosa");
                } else {
                    AndroidUtils.showToast(getContext(), "Fallo");
                }
            }
        });
    }

    void guardarDatos() {

        FirebaseUtil.getCurrentProfileStorage().getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri uri = task.getResult();
                if (_binding != null && _binding.profileImage != null) {
                    AndroidUtils.setProfilePic(getContext(),uri,_binding.profileImage);
                }

            }
        });
        setinProgress(true);
        FirebaseUtil.userDocumentReference().get().addOnCompleteListener(task -> {
            setinProgress(false);
            userModel = task.getResult().toObject(UserModel.class);
            _binding.editUsername.setText(userModel.getUsername());
            _binding.editTelefono.setText(userModel.getTelefono());

        });

    }

    void setinProgress(boolean inProgress) {
        if (inProgress) {
            _binding.progressBar.setVisibility(View.VISIBLE);
            _binding.btnSeguir.setVisibility(View.GONE);

        } else {

            _binding.progressBar.setVisibility(View.GONE);
            _binding.btnSeguir.setVisibility(View.VISIBLE);
        }

    }
}
