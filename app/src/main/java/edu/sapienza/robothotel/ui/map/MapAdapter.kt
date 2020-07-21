package edu.sapienza.robothotel.ui.map

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.RobotHotelApplication
import edu.sapienza.robothotel.utils.LogHelp
import edu.sapienza.robothotel.vo.Room
import edu.sapienza.robothotel.vo.RoomType


class CheckinAdapter(private val myRoom: Long): PagedListAdapter<Room,
        CheckinAdapter.RoomViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return RoomViewHolder(v)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = getItem(position)
        holder.bind(room, myRoom)
    }

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.room_name)!!
        private val type: TextView = itemView.findViewById(R.id.room_type)!!

        fun bind(room: Room?, myRoom: Long) {
            name.text = room!!.name
            type.text = when(room.type) {
                RoomType.SINGLE -> "S"
                RoomType.DOUBLE -> "D"
                RoomType.DELUXE -> String(Character.toChars(128081))
            }

            if (room.id == myRoom) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorRed))
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Room>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldRoom: Room,
                                         newRoom: Room) = oldRoom.id == newRoom.id

            override fun areContentsTheSame(oldRoom: Room,
                                            newRoom: Room) = oldRoom == newRoom
        }
    }
}
