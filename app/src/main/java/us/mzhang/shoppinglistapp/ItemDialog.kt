package us.mzhang.shoppinglistapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.new_item_dialog.view.*
import us.mzhang.shoppinglistapp.data.Item
import java.util.*

class ItemDialog : DialogFragment() {

    interface ItemHandler {
        fun itemCreated(item: Item)
        fun itemUpdated(item: Item)
    }

    private lateinit var itemHandler: ItemHandler

    private lateinit var etItemText: EditText
    private lateinit var etItemDescrip: EditText
    private lateinit var etItemPrice: EditText

    private lateinit var spCategory: Spinner

    var isEditMode = false


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.item_handler_error)
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.new_item))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_item_dialog, null
        )

        etItemDescrip = rootView.etDescrip
        etItemPrice = rootView.etPrice
        etItemText = rootView.etItemText
        spCategory = rootView.spCategory
        builder.setView(rootView)

        ArrayAdapter.createFromResource(
            context as ScrollingActivity,
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spCategory.adapter = adapter
        }

        handleEditMode(builder)

        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
            // empty
        }


        return builder.create()
    }

    private fun handleEditMode(builder: AlertDialog.Builder) {
        isEditMode = (arguments != null) && arguments!!.containsKey(ScrollingActivity.KEY_ITEM)

        if (isEditMode) {
            builder.setTitle(getString(R.string.edit_item))
            var item = arguments?.getSerializable(ScrollingActivity.KEY_ITEM) as Item

            etItemDescrip.setText(item.itemDescrip)
            etItemPrice.setText(item.price.toString())
            etItemText.setText(item.itemName)
            spCategory.setSelection(item.category)
        }
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setTextColor(getResources().getColor(R.color.background))
        positiveButton.setOnClickListener {
            if (etItemText.text.isNotEmpty() && etItemPrice.text.isNotEmpty()) {
                if (isEditMode) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                (dialog as AlertDialog).dismiss()
            } else {
                if (etItemText.text.isEmpty()) {
                    etItemText.error = getString(R.string.empty_field_error)
                }
                if (etItemPrice.text.isEmpty()) {
                    etItemPrice.error = getString(R.string.empty_field_error)
                }

            }
        }
    }

    private fun handleItemCreate() {
        itemHandler.itemCreated(
            Item(
                null,
                Date(System.currentTimeMillis()).toString(),
                etItemText.text.toString(),
                etItemDescrip.text.toString(),
                false,
                etItemPrice.text.toString().toInt(),
                categoryNameToInt(spCategory.selectedItem.toString())
            )
        )
    }

    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
            ScrollingActivity.KEY_ITEM
        ) as Item
        itemToEdit.itemName = etItemText.text.toString()
        itemToEdit.itemDescrip = etItemDescrip.text.toString()
        itemToEdit.price = etItemPrice.text.toString().toInt()
        itemToEdit.category = categoryNameToInt(spCategory.selectedItem.toString())

        itemHandler.itemUpdated(itemToEdit)
    }

    private fun categoryNameToInt(category: String) : Int {
        return when(category) {
            getString(R.string.food) -> 0
            getString(R.string.clothes) -> 1
            getString(R.string.electronics) -> 2
            else -> 3
        }
    }

}