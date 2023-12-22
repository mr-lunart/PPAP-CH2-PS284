package com.example.ppaps.ui.main.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ppaps.R

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyList: List<HistoryItem> // Gantilah dengan model data yang sesuai

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyList = createDummyData() // Inisialisasi historyList dengan data dummy
        recyclerView = view.findViewById(R.id.recyclerViewHistory)
        historyAdapter = HistoryAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = historyAdapter
        recyclerView.setHasFixedSize(true)

        historyAdapter.submitList(historyList)

    }
    private fun createDummyData(): List<HistoryItem> {
        return listOf(
            HistoryItem(R.drawable.ambulance, "Rs. Baladika Husada", "22/12/2023", "Dalam perjalanan"),
            HistoryItem(R.drawable.ambulance, "Rs. Citra Husada Jember", "25/12/2023", "Dipesan"),
            HistoryItem(R.drawable.ambulance, "Rs. Umum Kaliwates", "10/12/2023", "Selesai")

        )
    }
}