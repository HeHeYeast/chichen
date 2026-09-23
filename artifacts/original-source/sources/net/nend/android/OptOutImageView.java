package net.nend.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import net.nend.android.NendConstants;
import net.nend.android.NendHelper;

@SuppressLint({"ViewConstructor"})
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class OptOutImageView extends ImageView implements View.OnClickListener {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final float mDensity;
    private Bitmap mOptOutImage;
    private final String mOptOutUrl;

    static {
        $assertionsDisabled = !OptOutImageView.class.desiredAssertionStatus();
    }

    private static final class TapMargin {
        private static final int BOTTOM = 18;
        private static final int LEFT = 18;

        private TapMargin() {
        }
    }

    OptOutImageView(Context context, String uid, int spotId) throws IOException {
        super(context);
        this.mDensity = getContext().getResources().getDisplayMetrics().density;
        this.mOptOutUrl = String.valueOf(NendHelper.MetaDataHelper.getStringValue(context, NendConstants.MetaData.OPT_OUT_URL.getName(), "http://nend.net/privacy/optsdkgate")) + "?uid=" + uid + "&spot=" + spotId;
        setPadding(18, 0, 0, 18);
        setOnClickListener(this);
        try {
            InputStream is = getResources().getAssets().open("nend_information_icon.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            this.mOptOutImage = resizeBitmap(bitmap);
            setImageBitmap(this.mOptOutImage);
        } catch (IOException e) {
            this.mOptOutImage = null;
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap) {
        if (!$assertionsDisabled && bitmap == null) {
            throw new AssertionError();
        }
        Matrix matrix = new Matrix();
        matrix.setScale(this.mDensity / 2.0f, this.mDensity / 2.0f);
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != resizeBitmap) {
            bitmap.recycle();
        }
        return resizeBitmap;
    }

    boolean hasDrawable() {
        return getDrawable() != null;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        NendHelper.startBrowser(getContext(), this.mOptOutUrl);
    }

    public void deallocateImage() {
        if (this.mOptOutImage != null) {
            if (!this.mOptOutImage.isRecycled()) {
                this.mOptOutImage.recycle();
            }
            this.mOptOutImage = null;
        }
    }
}
