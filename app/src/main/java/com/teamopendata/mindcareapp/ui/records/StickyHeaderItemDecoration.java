package com.teamopendata.mindcareapp.ui.records;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class StickyHeaderItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "StickyHeaderItemDecoration";

    private final StickyHeaderInterface mListener;
    private int mStickyHeaderHeight;

    private boolean currentHeaderToShow;

    public interface OnHeaderClickListener {
        void onHeaderClick();
    }

    public StickyHeaderItemDecoration(StickyHeaderInterface stickyHeaderInterface) {
        mListener = stickyHeaderInterface;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        View topChild = parent.getChildAt(0);
        if (topChild == null) return;

        int topChildPosition = parent.getChildAdapterPosition(topChild);
        if (topChildPosition == RecyclerView.NO_POSITION) return;

        int headerPos = mListener.getHeaderPositionForItem(topChildPosition);
        View currentHeader = mListener.getHeaderLayoutView(parent, topChildPosition);

        fixLayoutSize(parent, currentHeader);
        currentHeaderToShow = true;

        int contactPoint = currentHeader.getBottom();
        View childInContact = getChildInContact(parent, contactPoint, headerPos);
        if (childInContact != null && mListener.isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(c, currentHeader, childInContact, topChildPosition);
            return;
        }

        drawHeader(c, currentHeader);
    }

    private void moveHeader(Canvas c, View currentHeader, View nextHeader, int topChildPosition) {
        //  Log.d(TAG, "moveHeader: " + currentHeader + nextHeader);
        c.save();
        if (topChildPosition == 0)
            c.translate(0f, nextHeader.getTop() - currentHeader.getHeight());
        currentHeader.draw(c);
        c.restore();
    }

    private void drawHeader(Canvas c, View currentHeader) {
        // Log.d(TAG, "drawHeader: " + currentHeader);
        c.save();
        c.translate(0f, 0f);
        currentHeader.draw(c);
        c.restore();
    }

    private void fixLayoutSize(ViewGroup parent, View view) {
        // Specs for parent (RecyclerView)
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

        // Specs for children (headers)
        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

        view.measure(childWidthSpec, childHeightSpec);
        view.layout(0, 0, view.getMeasuredWidth(), mStickyHeaderHeight = view.getMeasuredHeight());
    }

    private View getChildInContact(RecyclerView parent, int contactPoint, int currentHeaderPos) {
        View childInContact = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            int heightTolerance = 0;
            View child = parent.getChildAt(i);

            //measure height tolerance with child if child is another header
            if (currentHeaderPos != i) {
                boolean isChildHeader = mListener.isHeader(parent.getChildAdapterPosition(child));
                if (isChildHeader) {
                    heightTolerance = mStickyHeaderHeight - child.getHeight();
                }
            }

            // add heightTolerance if child top be in display area
            int childBottomPosition;
            if (child.getTop() > 0) {
                childBottomPosition = child.getBottom() + heightTolerance;
            } else {
                childBottomPosition = child.getBottom();
            }

            if (childBottomPosition > contactPoint) {
                if (child.getTop() <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child;
                    break;
                }
            }
        }
        return childInContact;
    }

    public interface StickyHeaderInterface {
        boolean isHeader(int position);

        View getHeaderLayoutView(RecyclerView parent, int position);

        int getHeaderPositionForItem(int itemPosition);

        void bindHeaderData(View header, int headerPosition);

        int getHeaderLayout(int headerPosition);

    }
}
