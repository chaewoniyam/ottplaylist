package com.example.testapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.example.testapp.databinding.FragmentSearchBinding

class Search : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private var movieItems = ""
    private var searchString: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val searchEditText = view.findViewById<EditText>(R.id.input)
        val searchButton = view.findViewById<Button>(R.id.seach_click)
      //  val webView = view.findViewById<WebView>(R.id.webView)

        searchButton.setOnClickListener {
            val searchString = searchEditText.text.toString()
            println("Search string is $searchString") // 변수에 사용자가 입력한 값 들어 왔는지 확인

            // 키보드 내리기
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)

            val bundle = Bundle()
            bundle.putString("searchString", searchString)

            val childFragment = MovieProfileListFragment()
            childFragment.arguments = bundle

            childFragmentManager.beginTransaction()
                .replace(R.id.child_fragment_container, childFragment)
                .addToBackStack(null)
                .commit()

        }


        return view
    }
}


