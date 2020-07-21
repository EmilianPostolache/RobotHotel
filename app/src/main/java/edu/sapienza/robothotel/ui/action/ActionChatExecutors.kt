package edu.sapienza.robothotel.ui.action

import android.content.Context
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.BaseQiChatExecutor
import com.aldebaran.qi.sdk.builder.SayBuilder
import edu.sapienza.robothotel.R

class CheckinChatExecutor(qiContext: QiContext, private var bookingState: BookingState,
                          private var context: Context) : BaseQiChatExecutor(qiContext) {

    override fun runWith(params: List<String>) {
        // This is called when execute is reached in the topic
        val context: ActionActivity = (context as ActionActivity)
        if (bookingState == BookingState.CHECKIN) {
            context.onCheckinClicked(null)
        } else {
            SayBuilder.with(qiContext).withText(
                context.getString(R.string.pepper_checkin_fail)).build().run()
        }
    }

    override fun stop() {
    }
}

class CheckoutChatExecutor(qiContext: QiContext, private var bookingState: BookingState,
                           private var context: Context) : BaseQiChatExecutor(qiContext) {

    override fun runWith(params: List<String>) {
        // This is called when execute is reached in the topic
        val context: ActionActivity = (context as ActionActivity)
        if (bookingState == BookingState.CHECKOUT) {
            context.onCheckoutClicked(null)
        } else {
            SayBuilder.with(qiContext).withText(
                context.getString(R.string.pepper_checkout_fail)).build().run()
        }
    }

    override fun stop() {
    }
}

class BookChatExecutor(qiContext: QiContext, private var bookingState: BookingState,
                       private var context: Context) : BaseQiChatExecutor(qiContext) {

    override fun runWith(params: List<String>) {
        // This is called when execute is reached in the topic
        val context: ActionActivity = (context as ActionActivity)
        if (bookingState == BookingState.BOOK) {
            context.onBookClicked(null)
        } else {
            SayBuilder.with(qiContext).withText(
                context.getString(R.string.pepper_book_fail)).build().run()
        }
    }

    override fun stop() {
    }
}

class MapChatExecutor(qiContext: QiContext, private var context: Context) : BaseQiChatExecutor(qiContext) {

    override fun runWith(params: List<String>) {
        // This is called when execute is reached in the topic
        val context: ActionActivity = (context as ActionActivity)
        context.onMapClicked(null)
    }

    override fun stop() {
    }
}
