package ro.androidproject.myapplication.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import kotlinx.android.synthetic.main.item_rv_notes.view.*
import kotlinx.coroutines.currentCoroutineContext
import ro.androidproject.myapplication.R
import ro.androidproject.myapplication.entities.Notes
import kotlin.coroutines.coroutineContext

// RecyclerView
class NotesAdapter () : RecyclerView.Adapter<NotesAdapter.NotesAdapterViewHolder>() {

    private var arrList= ArrayList<Notes>()
    var clickListener:OnNoteItemClickListener?=null

    class NotesAdapterViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
    }

    fun setOnClickListener(listener : OnNoteItemClickListener){
        clickListener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapterViewHolder {
       return NotesAdapterViewHolder(
           LayoutInflater.from(parent.context).inflate(R.layout.item_rv_notes,parent,false)
       )
    }

    override fun getItemCount(): Int {
        return arrList.count()
    }

    fun setData( arrNotesList: List<Notes>){
        arrList=arrNotesList as ArrayList<Notes>
    }

    override fun onBindViewHolder(holder: NotesAdapterViewHolder, position: Int) {
        holder.itemView.tvtitle.text = arrList[position].title
        holder.itemView.tvcontent.text = arrList[position].subTitle
        holder.itemView.tvdatetime.text = arrList[position].dateTime

        if (arrList[position].color != null) {
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(arrList[position].color))
        } else {
            holder.itemView.cardView.setCardBackgroundColor(Color.LTGRAY)
        }

        if (arrList[position].image != null) {
            holder.itemView.imagenoteitem.setImageBitmap(arrList[position].image)
            holder.itemView.imagenoteitem.visibility = View.VISIBLE
        } else {
            holder.itemView.imagenoteitem.visibility = View.GONE
        }

        holder.itemView.cardView.setOnClickListener{
            clickListener!!.onNoteClick(arrList[position].id!!)
        }
    }

    interface OnNoteItemClickListener{
        fun onNoteClick(position:Int)
    }
}

