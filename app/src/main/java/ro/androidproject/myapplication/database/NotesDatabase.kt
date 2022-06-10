package ro.androidproject.myapplication.database

import android.content.Context
import androidx.room.*
import ro.androidproject.myapplication.dao.NoteDao
import ro.androidproject.myapplication.entities.Notes
import ro.androidproject.myapplication.util.Convertor

// declararea bazei de date
@Database(entities = [Notes::class],version = 1,exportSchema = false)
@TypeConverters(Convertor::class)
abstract class NotesDatabase: RoomDatabase() {

    companion object{
        var notesDatabase:NotesDatabase?=null
        //tot din acest bloc va fi protejat in accesul concurential
        @Synchronized
        fun getDatabase(context: Context):NotesDatabase{
            if(notesDatabase==null){
                notesDatabase= Room.databaseBuilder(
                    context,
                    NotesDatabase::class.java,
                    "notes.db"
                ).build()
            }
            //cand pun !! fac un assert si verific daca nu e null.
            return notesDatabase!!
        }
    }

    abstract fun noteDao():NoteDao
}