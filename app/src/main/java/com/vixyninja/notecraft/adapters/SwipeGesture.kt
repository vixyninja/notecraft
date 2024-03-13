package com.vixyninja.notecraft.adapters

import android.graphics.Canvas
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.vixyninja.notecraft.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeGesture :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        RecyclerViewSwipeDecorator.Builder(
            c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
        ).addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 16)
            .addPadding(TypedValue.COMPLEX_UNIT_DIP, 8f, 0f, 8f)
            .addSwipeLeftActionIcon(android.R.drawable.ic_menu_delete).addSwipeLeftLabel("Delete")
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(recyclerView.context, R.color.red))
            .addSwipeRightActionIcon(android.R.drawable.ic_menu_edit).addSwipeRightBackgroundColor(
                ContextCompat.getColor(
                    recyclerView.context, R.color.blue
                )
            ).addSwipeRightLabel("Edit")
            .setSwipeRightLabelColor(ContextCompat.getColor(recyclerView.context, R.color.white))
            .setSwipeLeftLabelColor(ContextCompat.getColor(recyclerView.context, R.color.white))
            .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            .setSwipeRightLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 18f).create().decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return .5f
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 4f
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return super.getSwipeDirs(recyclerView, viewHolder)
    }
}