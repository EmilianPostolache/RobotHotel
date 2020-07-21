package edu.sapienza.robothotel.ui.map

import android.content.Context
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.BaseQiChatExecutor
import com.aldebaran.qi.sdk.builder.SayBuilder
import edu.sapienza.robothotel.R

class OkChatExecutor(qiContext: QiContext, private var context: Context) : BaseQiChatExecutor(qiContext) {

    override fun runWith(params: List<String>) {
        // This is called when execute is reached in the topic
        val context: MapActivity = (context as MapActivity)
        context.action()
    }

    override fun stop() {
    }
}

