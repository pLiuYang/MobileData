package com.assignment.mobiledata.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.mobiledata.R
import com.assignment.mobiledata.util.ViewModelFactory
import kotlinx.android.synthetic.main.main_fragment.loadingView
import kotlinx.android.synthetic.main.main_fragment.recyclerView

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MobileDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MobileDataAdapter { showToast() }
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = this@MainFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(activity!!.applicationContext))
            .get(MainViewModel::class.java)

        viewModel.dataList.observe(viewLifecycleOwner, Observer {
            adapter.setItems(it)
            loadingView.visibility = View.GONE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            loadingView.visibility = View.GONE
        })

        loadingView.visibility = View.VISIBLE
        viewModel.loadData()
    }

    private fun showToast() {
        Toast.makeText(context, getString(R.string.down_trending_message), Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}
