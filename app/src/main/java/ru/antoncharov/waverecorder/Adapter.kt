package ru.antoncharov.waverecorder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

private class ViewHolder(view : View){
    var fileName: TextView? = view.findViewById(R.id.item_textView)
}
class Adapter(list : ArrayList<String?>) : BaseAdapter() {
    private val listOfFiles = list

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val viewHolder : ViewHolder
        var view = view

        if(view == null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else{
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.fileName?.text = listOfFiles[position]

        return view!!
    }

    override fun getItem(position: Int): Any? {
        return listOfFiles[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listOfFiles.count()
    }
}