package us.mzhang.shoppinglistapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.activity_scrolling.*
import us.mzhang.shoppinglistapp.adapter.ItemAdapter
import us.mzhang.shoppinglistapp.data.AppDatabase
import us.mzhang.shoppinglistapp.data.Item
import us.mzhang.shoppinglistapp.touch.ItemRecyclerTouchCallback

class ScrollingActivity : AppCompatActivity(), ItemDialog.ItemHandler {

    companion object {
        const val KEY_ITEM = "KEY_ITEM"
        const val TAG_ITEM_DIALOG = "TAG_ITEM_DIALOG"
        const val TAG_ITEM_EDIT = "TAG_ITEM_EDIT"
        const val TAG_ITEM_DETAILS = "TAG_ITEM_DETAILS"
    }

    lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false)


        initRecyclerView()

        fab.setOnClickListener {
            showAddItemDialog()
        }

        fabDeleteAll.setOnClickListener {
            itemAdapter.deleteAllItems()
        }
    }

    private fun initRecyclerView() {
        Thread {
            var itemList = AppDatabase.getInstance(this@ScrollingActivity).itemDao().getAllItem()

            runOnUiThread {
                itemAdapter = ItemAdapter(this, itemList)
                recyclerItem.adapter = itemAdapter
                var itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
                recyclerItem.addItemDecoration(itemDecoration)

                val callback = ItemRecyclerTouchCallback(itemAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerItem)
            }
        }.start()
    }

    fun showAddItemDialog() {
        ItemDialog().show(supportFragmentManager, TAG_ITEM_DIALOG)
    }

    var editIndex: Int = -1

    fun showEditItemDialog(itemToEdit: Item, idx: Int) {
        editIndex = idx

        val editDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM, itemToEdit)

        editDialog.arguments = bundle

        editDialog.show(supportFragmentManager, TAG_ITEM_EDIT)

    }

    fun showItemDetails(itemToShow: Item) {
        val detailsDialog = DetailsDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM, itemToShow)

        detailsDialog.arguments = bundle

        detailsDialog.show(supportFragmentManager, TAG_ITEM_DETAILS)

    }

    fun saveItem(item: Item) {
        Thread {
            var newId = AppDatabase.getInstance(this).itemDao().insertItem(
                item
            )

            item.itemId = newId

            runOnUiThread {
                itemAdapter.addItem(item)
            }
        }.start()

    }

    override fun itemCreated(item:Item) {
        saveItem(item)
    }

    override fun itemUpdated(item: Item) {
        Thread {
            AppDatabase.getInstance(this@ScrollingActivity).itemDao().updateItem(item)

            runOnUiThread {
                itemAdapter.updateItemOnPosition(item, editIndex)
            }
        }.start()
    }

}
