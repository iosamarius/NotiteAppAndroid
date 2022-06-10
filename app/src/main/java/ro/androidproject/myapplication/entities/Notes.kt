package ro.androidproject.myapplication.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
// tabela propriu-zisa pentru baza de date ROOM

@Entity(tableName = "Notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,

    @ColumnInfo(name="title")
    var title:String?=null,

    @ColumnInfo(name="sub_title")
    var subTitle:String?=null,

    @ColumnInfo(name="date_time")
    var dateTime:String?=null,

    @ColumnInfo(name="note_text")
    var noteText:String?=null,

    @ColumnInfo(name="img_path")
    var imgPath:String?=null,

    @ColumnInfo(name="color")
    var color:String?=null,

    @ColumnInfo(name="imagine")
    var image: Bitmap?=null

) : Serializable
{
    override fun toString(): String {
        return "$title : $dateTime"
    }
}