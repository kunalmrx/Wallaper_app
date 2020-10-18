package fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.Activity.R


class FaqFragment : Fragment() {

lateinit var faqtext:EditText
    lateinit var post:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_faq, container, false)
        faqtext=view.findViewById(R.id.faqtext)
        post=view.findViewById(R.id.btnpost)

        post.setOnClickListener()
        { val text=faqtext.text
            if (text.isEmpty())
            {
                Toast.makeText(context,"Please write something", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(context,"Your post added suceesfully", Toast.LENGTH_SHORT).show()
                faqtext.setText("")
            }
        }


        return view
    }


}