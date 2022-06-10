package ro.androidproject.myapplication

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notes_bottom_sheet.*
import kotlinx.android.synthetic.main.item_rv_notes.view.*
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import ro.androidproject.myapplication.database.NotesDatabase
import ro.androidproject.myapplication.entities.Notes
import ro.androidproject.myapplication.util.BottomSheetFragment
import java.lang.Long.parseLong

import java.text.SimpleDateFormat
import java.util.*
import android.os.Bundle as Bundle1


 class CreateNoteFragment : BaseFragment()  {


    var currentDate:String?=null
    var selectedColor = "#ffffff"
    var Image:Bitmap?=null
    var notes:Notes?=null
    private val CAMERA_RQ=123
    private val IMAGE_CAPTURE_CODE:Int ?=1001
    private var noteID=0


    override fun onCreate(savedInstanceState: Bundle1?) {
        super.onCreate(savedInstanceState)
        noteID=requireArguments().getInt("noteId")
        arguments?.let {
        }
        setFragmentResultListener("requestKey"){requestKey, bundle ->
            selectedColor = bundle.getString("boundleKey").toString()
            noteView.setBackgroundColor(Color.parseColor(selectedColor))

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle1?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    companion object {
        var note_view:View?= null

        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle1().apply {
                }
            }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle1?) {
        super.onViewCreated(view, savedInstanceState)
        // pentru data
        val sdf = SimpleDateFormat("dd/mm/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        datetime.text = currentDate

        if(noteID!=0){
            launch {
                context?.let {
                    notes=NotesDatabase.getDatabase(it).noteDao().getNote(noteID)
                    val  auxiliar=notes
                    edit_note_content.setText(auxiliar?.noteText)
                    edit_note_subtitle.setText(auxiliar?.subTitle)
                    edit_note_title.setText(auxiliar?.title)
                    if(auxiliar?.image !=null){
                        Image=auxiliar.image
                        imgContent.setImageBitmap(Image)
                        imgContent.visibility=View.VISIBLE
                    }
                    //if (notes.color != null) {
                  //      System.out.println(notes.color)
                  //     view.cardView.setCardBackgroundColor(Color.parseColor(notes.color))
                  //  } else {
                 //       view.cardView.setCardBackgroundColor(Color.LTGRAY)
                  //  }
                  //  if (arrList[position].image != null) {
                   //     holder.itemView.imagenoteitem.setImageBitmap(arrList[position].image)
                 //       holder.itemView.imagenoteitem.visibility = View.VISIBLE
                 //   } else {
               //         holder.itemView.imagenoteitem.visibility = View.GONE
                //    }
                }

            }
        }
        imgDone.setOnClickListener {
            saveNote()
        }

        // Listener pentru butonul de share
        shareNoteBtn.setOnClickListener{
            val storage = "NOTE: "+ "Title:"+ (notes?.title) +" Content:"+ (notes?.noteText)
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT,storage)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"subject")
            startActivity(Intent.createChooser(shareIntent,"Share text via"))
        }

        imgBack.setOnClickListener {
            replacefragment(HomeFragment.newInstance(), false)
        }

        // Listener pentru butonul de delete
        deleteNoteBtn.setOnClickListener{
            deleteNote()
            replacefragment(HomeFragment.newInstance(), false)
        }
        // Listener pentru butonul de adaugat imagine
        addImageBtn.setOnClickListener {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if(context?.checkSelfPermission(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(android.Manifest.permission.CAMERA)
                    requestPermissions(permission,CAMERA_RQ)
                }else{
                    openCamera()
                }
            }else{
                openCamera()
            }
        }

        img_more.setOnClickListener {
            val sheet = BottomSheetFragment()
            sheet.show(requireActivity().supportFragmentManager, "BottomSheetFragment")
        }
    }

     // Pentru camera
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode== Activity.RESULT_OK){
                Image= data?.extras?.get("data") as Bitmap
                imgContent.setImageBitmap(Image)
                imgContent.visibility=View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_RQ->{
                if(grantResults.size>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    openCamera()
                }else{
                    Toast.makeText(context,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


     // Salvare notite
    private fun saveNote(){

        // Verific daca utilizatorul a introdus ceva in campuri
        if(edit_note_title.text.isNullOrEmpty()){
            Toast.makeText(context,"Title Required",Toast.LENGTH_SHORT).show()
        }
        if(edit_note_subtitle.text.isNullOrEmpty()){
            Toast.makeText(context,"Sub Title Required",Toast.LENGTH_SHORT).show()
        }
        if(edit_note_content.text.isNullOrEmpty()){
            Toast.makeText(context,"Content Required",Toast.LENGTH_SHORT).show()
        }

        launch {
            var notes= Notes()
            notes.title=edit_note_title.text.toString()
            notes.subTitle=edit_note_subtitle.text.toString()
            notes.dateTime=currentDate.toString()
            notes.color=selectedColor
            notes.noteText=edit_note_content.text.toString()
            if(Image!=null){
                notes.image=Image
            }

            context?.let {
                NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                edit_note_content.setText("")
                edit_note_subtitle.setText("")
                edit_note_title.setText("")
                imgContent.visibility=View.GONE
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

     private fun deleteNote(){
        launch{
            context?.let{
                NotesDatabase.getDatabase(it).noteDao().deleteNote(noteID)

            }
        }
     }

    fun replacefragment(fragment: Fragment, istransition:Boolean){
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
        if(istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName)
        fragmentTransition.commit()
    }
}



