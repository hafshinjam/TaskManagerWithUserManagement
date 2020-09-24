package com.example.taskmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskDBRepository;

import java.io.File;
import java.util.List;

import static com.example.taskmanager.control.fragment.TaskListFragment.FILEPROVIDER_AUTHORITY;


public class PictureSourceChooseFragmentDialog extends DialogFragment {
    private static final String CURRENT_TASK_TAKE_PICTURE = "CurrentTaskTakePicture";
    private Button mCaptureButton;
    private Button mGalleryButton;
    private Task mCurrentTask;
    private File mPhotoFile;
    private TaskDBRepository mTaskDBRepository;
    private final int SELECT_FROM_GALLERY_REQUEST_CODE = 2;
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public PictureSourceChooseFragmentDialog() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PictureSourceChooseFragmentDialog newInstance(Task task) {
        PictureSourceChooseFragmentDialog chooseImageFragment =
                new PictureSourceChooseFragmentDialog();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_TASK_TAKE_PICTURE, task);
        chooseImageFragment.setArguments(args);
        return chooseImageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            mTaskDBRepository = TaskDBRepository.getInstance(getActivity());
            if (getArguments() != null) {
                mCurrentTask = (Task) getArguments().getSerializable(CURRENT_TASK_TAKE_PICTURE);
                mPhotoFile = mTaskDBRepository.generatePhotoFilesDir(getActivity(), mCurrentTask);
            }
        }
    }

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture_source_choose_dialog, container, false);
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_picture_source_choose_dialog, null);
        findViews(view);
        setListeners();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    private void setListeners() {
        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(fromGallery, SELECT_FROM_GALLERY_REQUEST_CODE);
            }
        });
        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                PackageManager packageManager = getActivity().getPackageManager();
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        if (mPhotoFile == null)
                            return;
                        Uri uri = FileProvider.getUriForFile(getActivity(),
                                FILEPROVIDER_AUTHORITY,
                                mPhotoFile);
                        grantTemPermissionForTakePicture(takePictureIntent, uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
            }
        });
    }

    private void findViews(View view) {
        mCaptureButton = view.findViewById(R.id.capture_button);
        mGalleryButton = view.findViewById(R.id.gallery_button);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK)
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                mCurrentTask.setTaskPicturePath(mPhotoFile.getPath());
                mTaskDBRepository.update(mCurrentTask);
                Uri photoUri = FileProvider.getUriForFile(
                        getActivity(),
                        FILEPROVIDER_AUTHORITY,
                        mPhotoFile);
                getActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        if (requestCode == SELECT_FROM_GALLERY_REQUEST_CODE) {
            Uri selectImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectImage, filePath,
                    null, null, null);
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(filePath[0]);
            String path = cursor.getString(index);
            mCurrentTask.setTaskPicturePath(path);
            mTaskDBRepository.update(mCurrentTask);
            cursor.close();
        }
    }

    private void grantTemPermissionForTakePicture(Intent takePictureIntent, Uri photoURI) {
        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                takePictureIntent,
                PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : activities) {
            getActivity().grantUriPermission(activity.activityInfo.packageName,
                    photoURI,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }
}