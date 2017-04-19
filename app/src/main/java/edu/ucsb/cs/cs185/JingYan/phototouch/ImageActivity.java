package edu.ucsb.cs.cs185.JingYan.phototouch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.almeros.android.multitouch.RotateGestureDetector;

public class ImageActivity extends AppCompatActivity implements View.OnTouchListener {
    ImageView imgV;
    Bitmap b,bb;
    ScaleGestureDetector mScaleDetector;
    RotateGestureDetector mRotateDetector;
    MoveGestureDetector mMoveDetector;

    float mScaleFactor = 1.0f;
    float mScaleFactor_original = 1.0f;
    float mRotationDegrees = 0.f;
    float mFocusX = 0.f;
    float mFocusY = 0.f;
    Matrix mMatrix = new Matrix();
    int mImageHeight, mImageWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent imgIntent = getIntent();
        String uri_s = imgIntent.getStringExtra("UriFromMain");
        Uri uri = Uri.parse(uri_s);
        imgV = (ImageView) findViewById(R.id.imageView);

        Display display = getWindowManager().getDefaultDisplay();

        imgV.getLayoutParams().height = display.getHeight();
        imgV.getLayoutParams().width = display.getWidth();

        try {
            b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            bb=Bitmap.createScaledBitmap(b, (int)((b.getWidth()*1.0f/b.getHeight())*800), 800, false);
            imgV.setImageBitmap(b);

        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
        }


        // set onTouch to the image view !
        imgV.setOnTouchListener(this);

        // [ init ]

        // Center of screen as focus point

        mFocusX = display.getWidth() / 2f;
        mFocusY = display.getHeight() / 2f - 56 -64; // minus the size of app bar(54) and margin(64)

        // original image width and height
        mImageWidth = b.getWidth();
        mImageHeight = b.getHeight();
        Log.d("b.getWidth() ", "b.getWidth() " + b.getWidth());

        // initialize the original scaleFactor
        mScaleFactor_original = (display.getWidth())*1.0f/b.getWidth();
        mScaleFactor = mScaleFactor_original;
        Log.d("mScaleFactor_original", "mScaleFactor_original " + mScaleFactor_original);

        // scaled image center
        float scaledImageCenterX = (mImageWidth * mScaleFactor) / 2f;
        float scaledImageCenterY = (mImageHeight * mScaleFactor) / 2f;

        // set image to the center of content view by transformation matrix
        imgV.setScaleType(ImageView.ScaleType.MATRIX); // remember to write this line!
        mMatrix.postScale(mScaleFactor, mScaleFactor);
        mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);
        imgV.setImageMatrix(mMatrix);

        // Setup Gesture Detectors
        mScaleDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
        mRotateDetector = new RotateGestureDetector(getApplicationContext(), new RotateListener());
        mMoveDetector = new MoveGestureDetector(getApplicationContext(), new MoveListener());

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        mRotateDetector.onTouchEvent(event);
        mMoveDetector.onTouchEvent(event);

        //mScaleFactor, mRotationDegrees, mFocusX and mFocusY
        //Log.d("mScaleDetector", "mScaleFactor " + mScaleFactor);
        //Log.d("mRotateDetector", "mRotationDegrees " + mRotationDegrees);
        //Log.d("mMoveDetector", "mFocusX,Y " + mFocusX + ", " + mFocusY);

        float scaledImageCenterX = (mImageWidth * mScaleFactor) / 2f;
        float scaledImageCenterY = (mImageHeight * mScaleFactor) / 2f;

        // set multi touch rotation, translation, and scale to image
        mMatrix.reset();
        mMatrix.postScale(mScaleFactor, mScaleFactor);
        mMatrix.postRotate(mRotationDegrees, scaledImageCenterX, scaledImageCenterY);
        mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);

        ImageView view = (ImageView) v;
        Log.d("mMatrix", "mMatrix " + mMatrix.toString());
        view.setScaleType(ImageView.ScaleType.MATRIX);
        view.setImageMatrix(mMatrix);

        return true;
    }


    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor(); // scale change since previous event
            return true;
        }
    }

    public class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            mRotationDegrees -= detector.getRotationDegreesDelta();
            return true;
        }
    }

    public class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            mFocusX += d.x;
            mFocusY += d.y;
            return true;
        }
    }
}


