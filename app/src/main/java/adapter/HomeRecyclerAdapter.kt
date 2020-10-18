package adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Activity.DownLoadPhotoActivity
import com.example.Activity.R
import modal.Upload

class HomeRecyclerAdapter(val context: Context?, private val itemList: ArrayList<Upload>): RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
      return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
    val photodata=itemList[position]

        Glide.with(context).load(photodata.imageUrl).into(holder.img)
        holder.llcontent.setOnClickListener{
            val intent=Intent(context, DownLoadPhotoActivity::class.java)
            intent.putExtra("url",photodata.imageUrl)
            intent.putExtra("title",photodata.name)
            context?.startActivity(intent)
        }

    }

    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view) {
   //    val title:TextureView=view.findViewById(R.id.title)
        val img:ImageView=view.findViewById(R.id.imgphoto)
        val llcontent:RelativeLayout=view.findViewById(R.id.llContent)



    }


}