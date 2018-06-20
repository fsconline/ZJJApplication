
package hawk.com.zjjapplication.utils;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title CustomGallery.java
 * @Description: 自定义Gallery控件
 * @date 2016-3-17 下午2:16:02
 */
public class CustomGallery extends Gallery {

    private Camera mCamera = new Camera();

    /**
     * 最大旋转角度
     */
    private int mMaxRotationAngle = 45;
    private int mMaxZoom = -120;
    private int mCoveflowCenter;

    public CustomGallery(Context context) {
        super(context);
        this.setStaticTransformationsEnabled(true);
    }

    public CustomGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setStaticTransformationsEnabled(true);
    }

    public CustomGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setStaticTransformationsEnabled(true);
    }

    private static int getCenterOfView(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }

    public int getMaxRotationAngle() {
        return mMaxRotationAngle;
    }

    /**
     * 设置偏转角度
     *
     * @param maxRotationAngle
     */
    public void setMaxRotationAngle(int maxRotationAngle) {
        mMaxRotationAngle = maxRotationAngle;
    }

    public int getMaxZoom() {
        return mMaxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        mMaxZoom = maxZoom;
    }

    private int getCenterOfCoverflow() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
    }

    protected boolean getChildStaticTransformation(View child, Transformation t) {
        final int childCenter = getCenterOfView(child);
        final int childWidth = child.getWidth();
        int rotationAngle = 0;

        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);

        if (childCenter == mCoveflowCenter) {
            transformImageBitmap((ImageView) child, t, 0);
        } else {
            rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
            if (Math.abs(rotationAngle) > mMaxRotationAngle) {
                rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;
            }
            transformImageBitmap((ImageView) child, t, rotationAngle);
        }

        return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCoveflowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void transformImageBitmap(ImageView child, Transformation t, int rotationAngle) {
        mCamera.save();
        final Matrix imageMatrix = t.getMatrix();
        final int imageHeight = child.getLayoutParams().height;
        final int imageWidth = child.getLayoutParams().width;
        final int rotation = Math.abs(rotationAngle);

        // 在Z轴上正向移动camera的视角，实际效果为放大图片。
        // 如果在Y轴上移动，则图片上下移动；X轴上对应图片左右移动。
        mCamera.translate(0.0f, 0.0f, 100.0f);

        // As the angle of the view gets less, zoom in
        if (rotation < mMaxRotationAngle) {
            float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
            mCamera.translate(0.0f, 0.0f, zoomAmount);
        }

        // 在Y轴上旋转，对应图片竖向向里翻转。
        // 如果在X轴上旋转，则对应图片横向向里翻转。
        mCamera.rotateY(rotationAngle);
        mCamera.getMatrix(imageMatrix);
        imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
        imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
        mCamera.restore();
    }


}
