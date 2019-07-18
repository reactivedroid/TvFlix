package com.android.ashwiask.tvmaze.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(private val space: Int, private val noOfColumns: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        outRect.top = space
        when {
            parent.getChildLayoutPosition(view) % noOfColumns == 0 -> {
                outRect.left = 0
                outRect.right = space
            }
            parent.getChildLayoutPosition(view) % noOfColumns == noOfColumns - 1 -> {
                outRect.left = space
                outRect.right = 0
            }
            else -> {
                //do nothing
            }
        }
    }
}
