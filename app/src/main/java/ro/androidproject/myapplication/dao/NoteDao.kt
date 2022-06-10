package ro.androidproject.myapplication.dao

import androidx.room.*
import ro.androidproject.myapplication.entities.Notes

@Dao
// query-urile pentru baza de date
interface NoteDao{

    @Query("SELECT * from Notes order by id DESC")
    suspend fun allNotes(): List<Notes>

    @Query("SELECT * from Notes where id=:id")
    suspend fun getNote(id:Int): Notes

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note:Notes)

    @Query("Delete from Notes where id=:id")
    suspend fun deleteNote(id:Int)


}