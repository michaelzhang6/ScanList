package us.mzhang.shoppinglistapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true) var itemId: Long?,
    @ColumnInfo(name = "createDate") var createDate: String,
    @ColumnInfo(name = "itemName") var itemName: String,
    @ColumnInfo(name = "itemDescrip") var itemDescrip: String,
    @ColumnInfo(name = "done") var done: Boolean,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "category") var category: Int
) : Serializable