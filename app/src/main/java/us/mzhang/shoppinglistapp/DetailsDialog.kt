package us.mzhang.shoppinglistapp

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.item_description_dialog.view.*
import us.mzhang.shoppinglistapp.data.Item

class DetailsDialog : DialogFragment() {

    private lateinit var tvItemDescrip: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.item_description))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.item_description_dialog, null
        )

        tvItemDescrip = rootView.tvItemDescription

        builder.setView(rootView)

        var item = arguments?.getSerializable(ScrollingActivity.KEY_ITEM) as Item

        tvItemDescrip.setText(item.itemDescrip)

        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
            // empty
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setTextColor(getResources().getColor(R.color.background))
        positiveButton.setOnClickListener {
            (dialog as AlertDialog).dismiss()
        }
    }

}