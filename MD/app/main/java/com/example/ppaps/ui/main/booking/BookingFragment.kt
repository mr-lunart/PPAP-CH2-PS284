package com.example.ppaps.ui.main.booking

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import com.example.ppaps.R
import com.google.android.material.textfield.TextInputEditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.ppaps.databinding.FragmentAccountBinding
import com.example.ppaps.databinding.FragmentBookingBinding
import com.example.ppaps.ui.ViewModelFactory
import com.example.ppaps.ui.main.account.AccountViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingFragment : Fragment(R.layout.fragment_booking) {

    private lateinit var nameEditText: TextInputEditText
    private lateinit var addressEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var datePickerEditText: TextInputEditText
    private lateinit var longEditText: TextInputEditText
    private lateinit var btnPesan: Button

    private val ambulanceService: AmbulanceService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api-cc-a56t3srpta-uc.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AmbulanceService::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText = view.findViewById(R.id.nameEditText)
        addressEditText = view.findViewById(R.id.addressEditText)
        phoneEditText = view.findViewById(R.id.phoneEditText)
        datePickerEditText = view.findViewById(R.id.datePickerEditText)
        longEditText = view.findViewById(R.id.longEditText)
        btnPesan = view.findViewById(R.id.btn_pesan)

        datePickerEditText.setOnClickListener {
            showDatePickerDialog()
        }

        btnPesan.setOnClickListener{
            val name = nameEditText.text.toString()
            val address = addressEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val date = datePickerEditText.text.toString()
            val longText = longEditText.text.toString()

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || date.isEmpty() || longText.isEmpty()) {
                Toast.makeText(requireContext(), "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bookingRequest = AmbulanceBookingRequest(name, address, phone, date, longText)

            ambulanceService.bookAmbulance(bookingRequest).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Simulasi pesanan berhasil
                        showSuccessMessage()
                    } else {
                        val statusCode = response.code()
                        Log.e("API_REQUEST", "Failed with status code: $statusCode")
                        Toast.makeText(
                            requireContext(),
                            "Gagal melakukan pesanan ambulance. Kode status: $statusCode",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Gagal melakukan pesanan ambulance",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            }
    }
    private fun showSuccessMessage() {
        Toast.makeText(requireContext(), "Pesanan ambulance berhasil", Toast.LENGTH_SHORT).show()

        // Clear form atau lakukan tindakan lain yang diperlukan
        nameEditText.text = null
        addressEditText.text = null
        phoneEditText.text = null
        datePickerEditText.text = null
        longEditText.text = null
    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, day: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                datePickerEditText.setText(
                    SimpleDateFormat("dd/MM/yyyy", Locale.US).format(selectedDate.time)
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api-cc-a56t3srpta-uc.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
