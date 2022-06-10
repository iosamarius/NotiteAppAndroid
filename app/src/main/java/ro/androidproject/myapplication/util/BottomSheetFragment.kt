package ro.androidproject.myapplication.util


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.zubair.permissionmanager.PermissionManager
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.fragment_notes_bottom_sheet.*
import ro.androidproject.myapplication.CreateNoteFragment
import ro.androidproject.myapplication.R


//BottomsheedDialogFragment este un mic layer peste fragment ( gen cand dai share si iti apare de jos ceva )
//e ca un Dialog.Cand atingi alta parte din afara, se duce in jos

/*
* The lifecycle of BottomSheetDialogFragment is the same as Fragment.

This is quite easy to understand since, BottomSheetDialogFragment extends AppCompatDialogFragment
*  (and adds just onCreateDialog() functions), which in turn extends DialogFragment (and add onCreateDialog() & setupDialog() functions),
*  which in turn extends Fragment.

DialogFragment has the same lifecycle as Fragment (reference).
*  Since, none of the lifecycle methods were touched, AppCompatDialogFragment and BottomSheetDialogFragment will have the same lifecycle as Fragment.
* */

class BottomSheetFragment:SuperBottomSheetFragment(){

    private var selectedColor: String ="#ffffff"
    private lateinit var mPermissionManager: PermissionManager
    companion object{
        fun newInstance():BottomSheetFragment{
            val args=Bundle()
            val fragment=BottomSheetFragment()
            fragment.arguments=args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mPermissionManager = PermissionManager()
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_notes_bottom_sheet,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    override fun getPeekHeight(): Int = requireContext().resources.getDimension(R.dimen._85sdp).toInt()

    private fun setListener(){
        fNote1.setOnClickListener{
            imgcolornote1.setImageResource(R.drawable.ic_check)
            imgcolornote2.setImageResource(0)
            selectedColor="#4e33ff"
            setFragmentResult("requestKey", bundleOf("boundleKey" to selectedColor))

        }
        fNote2.setOnClickListener{
            imgcolornote1.setImageResource(0)
            imgcolornote2.setImageResource(R.drawable.ic_check)
            selectedColor="#ffd633"
            setFragmentResult("requestKey", bundleOf("boundleKey" to selectedColor))
        }

    }







}


