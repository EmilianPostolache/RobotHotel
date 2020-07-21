package edu.sapienza.robothotel.ui.book

import android.content.Context
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.BaseQiChatExecutor
import com.aldebaran.qi.sdk.builder.SayBuilder
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.ui.action.ActionActivity
import edu.sapienza.robothotel.ui.action.BookingState

class DeluxeChatExecutor(qiContext: QiContext,
                          private var context: Context
) : BaseQiChatExecutor(qiContext) {

    override fun runWith(params: List<String>) {
        // This is called when execute is reached in the topic
//        val context: ActionActivity = (context as ActionActivity)
//        if (bookingState == BookingState.CHECKIN) {
//            context.onCheckinClicked(null)
//        } else {
//            SayBuilder.with(qiContext).withText(
//                context.getString(R.string.pepper_checkin_fail)).build().run()
//        }
    }

    override fun stop() {
    }
}

class SingleChatExecutor(qiContext: QiContext,
                           private var context: Context
) : BaseQiChatExecutor(qiContext) {

    override fun runWith(params: List<String>) {
        // This is called when execute is reached in the topic
//        val context: ActionActivity = (context as ActionActivity)
//        if (bookingState == BookingState.CHECKOUT) {
//            context.onCheckoutClicked(null)
//        } else {
//            SayBuilder.with(qiContext).withText(
//                context.getString(R.string.pepper_checkout_fail)).build().run()
//        }
    }

    override fun stop() {
    }
}

class DoubleChatExecutor(qiContext: QiContext,
                        private var context: Context
) : BaseQiChatExecutor(qiContext) {

    override fun runWith(params: List<String>) {
        // This is called when execute is reached in the topic
//        val context: ActionActivity = (context as ActionActivity)
//        if (bookingState == BookingState.BOOK) {
//            context.onBookClicked(null)
//        } else {
//            SayBuilder.with(qiContext).withText(
//                context.getString(R.string.pepper_book_fail)).build().run()
//        }
    }

    override fun stop() {
    }
}

class DaysChatExecutor(qiContext: QiContext,
                         private var context: Context
) : BaseQiChatExecutor(qiContext) {

    override fun runWith(params: List<String>) {
        // This is called when execute is reached in the topic
//        val context: ActionActivity = (context as ActionActivity)
//        if (bookingState == BookingState.BOOK) {
//            context.onBookClicked(null)
//        } else {
//            SayBuilder.with(qiContext).withText(
//                context.getString(R.string.pepper_book_fail)).build().run()
//        }
    }

    override fun stop() {
    }
}

class OkChatExecutor(qiContext: QiContext,
                         private var context: Context
) : BaseQiChatExecutor(qiContext) {

    override fun runWith(params: List<String>) {
        // This is called when execute is reached in the topic
//        val context: ActionActivity = (context as ActionActivity)
//        if (bookingState == BookingState.BOOK) {
//            context.onBookClicked(null)
//        } else {
//            SayBuilder.with(qiContext).withText(
//                context.getString(R.string.pepper_book_fail)).build().run()
//        }
    }

    override fun stop() {
    }
}

class NoChatExecutor(qiContext: QiContext,
                     private var context: Context
) : BaseQiChatExecutor(qiContext) {

    override fun runWith(params: List<String>) {
        // This is called when execute is reached in the topic
//        val context: ActionActivity = (context as ActionActivity)
//        if (bookingState == BookingState.BOOK) {
//            context.onBookClicked(null)
//        } else {
//            SayBuilder.with(qiContext).withText(
//                context.getString(R.string.pepper_book_fail)).build().run()
//        }
    }

    override fun stop() {
    }
}