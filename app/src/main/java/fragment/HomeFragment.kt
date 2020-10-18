package fragment

import adapter.HomeRecyclerAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Activity.R
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import modal.Upload
import util.ConnectionManager


class HomeFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: HomeRecyclerAdapter

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    private var mUploads: List<Upload>? = null
    var list= arrayListOf<Upload>()

   lateinit var mDatabaseRef: DatabaseReference
    lateinit var mStorageRef: StorageReference
  //  var mUploads = arrayListOf<Upload>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_home, container, false)

      //  setHasOptionsMenu(true)
        mUploads = ArrayList()

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        progressLayout = view.findViewById(R.id.progressLayout)

        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        layoutManager = GridLayoutManager(activity,2)



        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads")
        mDatabaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val upload = postSnapshot.getValue(Upload::class.java)!!
                   list.add(upload)
                }

                progressLayout.visibility = View.GONE

                recyclerAdapter = HomeRecyclerAdapter(activity as Context, this@HomeFragment.list)

                recyclerDashboard.adapter = recyclerAdapter

                recyclerDashboard.layoutManager = layoutManager


               // mProgressCircle.setVisibility(View.INVISIBLE)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, databaseError.message, Toast.LENGTH_SHORT)
                    .show()
              //  mProgressCircle.setVisibility(View.INVISIBLE)
            }
        })






        if (ConnectionManager().checkConnectivity(activity as Context))
        {


        }else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }

            dialog.setNegativeButton("Exit") {text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }





        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_drawer, menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//
//        val id = item?.itemId
//        if (id == R.id.action_sort){
//            Collections.sort(bookInfoList, ratingComparator)
//            bookInfoList.reverse()
//        }
//
//        recyclerAdapter.notifyDataSetChanged()
//
//        return super.onOptionsItemSelected(item)
//    }
}
