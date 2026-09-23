package jp.adlantis.android;

import android.content.Context;
import android.text.TextPaint;
import android.widget.TextView;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class SizeFitTextView extends TextView {
    private float _maxTextSize;
    private float _minTextSize;

    public SizeFitTextView(Context context) {
        super(context);
        this._minTextSize = 9.0f;
        this._maxTextSize = 20.0f;
    }

    private void refitText(String str, int i) {
        if (i > 0) {
            int paddingLeft = (i - getPaddingLeft()) - getPaddingRight();
            float f = this._maxTextSize;
            TextPaint paint = getPaint();
            while (true) {
                if (f <= this._minTextSize || paint.measureText(str) <= paddingLeft) {
                    break;
                }
                f -= 1.0f;
                if (f <= this._minTextSize) {
                    f = this._minTextSize;
                    break;
                }
                setTextSize(f);
            }
            setTextSize(f);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        if (i != i3) {
            refitText(getText().toString(), i);
        }
    }

    public void setTextAndSize(String str) {
        setTextSize(this._maxTextSize);
        super.setText(str);
        refitText(str, getWidth());
    }
}
