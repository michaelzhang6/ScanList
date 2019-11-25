package us.mzhang.shoppinglistapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*
import us.mzhang.shoppinglistapp.DetailsDialog
import us.mzhang.shoppinglistapp.R
import us.mzhang.shoppinglistapp.ScrollingActivity
import us.mzhang.shoppinglistapp.data.AppDatabase
import us.mzhang.shoppinglistapp.data.Item
import us.mzhang.shoppinglistapp.touch.ItemTouchHelperCallback
import java.util.*

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>, ItemTouchHelperCallback {

    var itemList = mutableListOf<Item>()

    val context: Context

    constructor(context: Context, listItem: List<Item>) {
        this.context = context
        itemList.addAll(listItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbItem = itemView.cbItem
        val tvDate = itemView.tvDate
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
        val categoryIcon = itemView.categoryIcon
        val tvPrice = itemView.tvPrice
        val btnDescrip = itemView.btnDescrip
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemRow = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false)

        return ViewHolder(itemRow)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = itemList.get(holder.adapterPosition)

        populateView(holder, item)

        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        holder.cbItem.setOnClickListener {
            item.done = holder.cbItem.isChecked
            updateItem(item)
        }

        holder.btnDescrip.setOnClickListener {
            (context as ScrollingActivity).showItemDetails(item)
        }

        holder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditItemDialog(item, holder.adapterPosition)
        }
    }

    private fun populateView(holder: ViewHolder, item: Item) {
        holder.cbItem.text = item.itemName
        holder.cbItem.isChecked = item.done
        holder.tvDate.text = item.createDate
        holder.tvPrice.text = "$${item.price.toString()}"

        when (item.category) {
            0 -> holder.categoryIcon.setImageResource(R.drawable.food)
            1 -> holder.categoryIcon.setImageResource(R.drawable.hanger)
            2 -> holder.categoryIcon.setImageResource(R.drawable.power_plug)
            else -> holder.categoryIcon.setImageResource(R.drawable.cart)
        }
    }

    fun addItem(item: Item) {
        itemList.add(item)

        notifyItemInserted(itemList.lastIndex)
    }

    fun deleteItem(index: Int) {
        Thread {
            AppDatabase.getInstance(context).itemDao().deleteItem(itemList[index])

            (context as ScrollingActivity).runOnUiThread {
                itemList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun deleteAllItems() {
        Thread {
            AppDatabase.getInstance(context).itemDao().deleteAllItem()

            (context as ScrollingActivity).runOnUiThread {
                itemList.clear()
                notifyDataSetChanged()
            }

        }.start()

    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(itemList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun updateItem(todo: Item) {
        Thread {
            AppDatabase.getInstance(context).itemDao().updateItem(todo)
        }.start()
    }

    fun updateItemOnPosition(item: Item, idx: Int) {
        itemList.set(idx, item)
        notifyItemChanged(idx)
    }
}