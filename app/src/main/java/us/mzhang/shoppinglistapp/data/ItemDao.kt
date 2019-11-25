package us.mzhang.shoppinglistapp.data

import androidx.room.*

@Dao
interface itemDao {

    @Query("SELECT * FROM item")
    fun getAllItem() : List<Item>

    @Insert
    fun insertItem(item: Item) : Long

    @Delete
    fun deleteItem(item: Item)

    @Update
    fun updateItem(item: Item)

    @Query("DELETE FROM item")
    fun deleteAllItem()
}