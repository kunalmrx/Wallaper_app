package com.example.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso
import modal.Upload


class UplaodActivity : AppCompatActivity() {
    private final val PICK_IMAGE_REQUEST = 1
    lateinit var mButtonChooseImage: Button

    lateinit var mTextViewShowUploads: TextView
    lateinit var mEditTextFileName: EditText
    lateinit var mImageView: ImageView
    lateinit var mProgressBar: ProgressBar
    lateinit var mImageUri: Uri
    lateinit var mStorageRef: StorageReference
    lateinit var mDatabaseRef: DatabaseReference

    // private lateinit var mUploadTask: StorageTask<*>
    private var mUploadTask: StorageTask<*>? = null
    lateinit var mButtonUpload: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uplaod)
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().reference;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");



        //
        mButtonChooseImage.setOnClickListener()
        {
            openFileChooser()
        }
        mButtonUpload.setOnClickListener()
        {
            if (mUploadTask != null && mUploadTask!!.isInProgress()) {
                Toast.makeText(this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
            }
        }

        mTextViewShowUploads.setOnClickListener()
        {
            openImagesActivity();
        }


    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {
            mImageUri = data.data!!
            Picasso.get().load(mImageUri).into(mImageView)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadFile() {
        if (mImageUri != null) {
            val fileReference = mStorageRef.child(
                System.currentTimeMillis()
                    .toString() + "." + getFileExtension(mImageUri)
            )
            mUploadTask = fileReference.putFile(mImageUri)
                .addOnSuccessListener { taskSnapshot ->
                    val handler = Handler()
                    handler.postDelayed(Runnable { mProgressBar.progress = 0 }, 500)
                    Toast.makeText(this, "Upload successful", Toast.LENGTH_LONG)
                        .show()
                    fileReference.downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                           print("kunal"+downloadUri)
                            val upload = Upload(
                                mEditTextFileName.text.toString().trim { it <= ' ' },
                                downloadUri.toString()
                            )
                            val uploadId = mDatabaseRef.push().key
                            mDatabaseRef.child(uploadId!!).setValue(upload)
                        } else {
                            // Handle failures
                        }
                    }

                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    mProgressBar.progress = progress.toInt()
                }
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }



        }
    private fun openImagesActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
    }



