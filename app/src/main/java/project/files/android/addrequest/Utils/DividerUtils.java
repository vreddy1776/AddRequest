package project.files.android.addrequest.Utils;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Divider Utils
 *
 * Draw blank dividers on RecyclerView
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class DividerUtils extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public DividerUtils(Drawable divider) {
        mDivider = divider;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i <= childCount - 2; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(0, 0, 0, 0);
            mDivider.draw(canvas);
        }
    }
}
