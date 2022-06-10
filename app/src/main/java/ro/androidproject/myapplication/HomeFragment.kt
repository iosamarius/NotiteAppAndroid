package ro.androidproject.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import ro.androidproject.myapplication.adapter.NotesAdapter

import ro.androidproject.myapplication.database.NotesDatabase
import ro.androidproject.myapplication.databinding.ActivityMainBinding
import ro.androidproject.myapplication.entities.Notes
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment(){

    val notesAdapter:NotesAdapter?= NotesAdapter()
    var arrNotes = ArrayList<Notes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                var notes=NotesDatabase.getDatabase(it).noteDao().allNotes()
                notesAdapter!!.setData(notes)
                arrNotes = notes as ArrayList<Notes>
                recycler_view?.let{
                    recycler_view.adapter=notesAdapter
                }
            }

        }

        notesAdapter!!.setOnClickListener(onClicked)

        btnCreateNote.setOnClickListener {
            replacefragment(CreateNoteFragment.newInstance(),false)
        }

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val tempArr = ArrayList<Notes>()
                for ( arr in arrNotes){
                    if ( arr.title?.toLowerCase(locale = Locale.getDefault())!!.contains(newText.toString())){
                        tempArr.add(arr)
                    }
                }
                notesAdapter.setData(tempArr)
                notesAdapter.notifyDataSetChanged()
                return true
            }

        })

    }

    fun replacefragment(fragment: Fragment, istransition:Boolean){
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
        if(istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName)
        fragmentTransition.commit()
    }


    private val onClicked = object :NotesAdapter.OnNoteItemClickListener {
        override fun onNoteClick(position: Int) {
            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("noteId", position)
            fragment = CreateNoteFragment.newInstance()
            fragment.arguments = bundle
            replacefragment(fragment, false)
        }
    }

}