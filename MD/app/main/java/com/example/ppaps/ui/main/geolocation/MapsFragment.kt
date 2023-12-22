package com.example.ppaps.ui.main.geolocation

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ppaps.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    val RsBaladhikaHusada = LatLng(-8.161774010396654, 113.70622433252021)
    val RsCitraHusadaJember = LatLng(-8.166520390807419, 113.6806551655391)
    val RsudDrSoebandi = LatLng(-8.150644898619014, 113.71547929437509)
    val RsParuJember = LatLng(-8.159716168375407, 113.70551349252128)
    val RsUmumKaliwates = LatLng(-8.18169870105207, 113.6750952655394)
    val RsSiloamHospitalsJember = LatLng(-8.173934355394616, 113.68754099437555)
    val RsPerkebunanJemberKlinik = LatLng(-8.167420971596444, 113.70558293670278)
    val RsudkalisatKabupatenJember = LatLng(-8.133634735552755, 113.82137261368464)
    val RsUtamaHusada = LatLng(-8.344124616977405, 113.59835285205115)
    val RsDaerahBalung = LatLng(-8.271014308721401, 113.53992700008737)
    val RsBinaSehatJember = LatLng(-8.180201021386857, 113.68492819437556)
    val RsLippoJember = LatLng(-8.170839217138159, 113.68765950028921)
    val RsuSrikandiIbiJember = LatLng(-8.185659462255847, 113.69024970601315)
    val RsPtpnXiiKaliwates = LatLng(-8.181432922337603, 113.67514395204812)
    val RsUmumUmj = LatLng(-8.208735744374138, 113.70194724724834)
    val RsUmumTniAngkatanDarat = LatLng(-8.161324346035935, 113.70500533241737)

    private var locationArrayList: ArrayList<LatLng>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        locationArrayList = ArrayList()

        locationArrayList!!.add(RsBaladhikaHusada)
        locationArrayList!!.add(RsCitraHusadaJember)
        locationArrayList!!.add(RsudDrSoebandi)
        locationArrayList!!.add(RsParuJember)
        locationArrayList!!.add(RsUmumKaliwates)
        locationArrayList!!.add(RsSiloamHospitalsJember)
        locationArrayList!!.add(RsPerkebunanJemberKlinik)
        locationArrayList!!.add(RsudkalisatKabupatenJember)
        locationArrayList!!.add(RsUtamaHusada)
        locationArrayList!!.add(RsDaerahBalung)
        locationArrayList!!.add(RsBinaSehatJember)
        locationArrayList!!.add(RsUmumKaliwates)
        locationArrayList!!.add(RsLippoJember)
        locationArrayList!!.add(RsuSrikandiIbiJember)
        locationArrayList!!.add(RsUmumUmj)
        locationArrayList!!.add(RsUmumTniAngkatanDarat)

//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync(this@MapsFragment)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        for (i in locationArrayList!!.indices) {
            val hospitalName: String = getHospitalName(i)
            mMap.addMarker(
                MarkerOptions()
                    .position(locationArrayList!![i])
                    .title(hospitalName)
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationArrayList!!.get(i), (15.0f)))
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList!!.get(i)))
        }
    }

    // Function to get the hospital name based on the index
    private fun getHospitalName(index: Int): String {
        return when (index) {
            0 -> "Rs. Baladhika Husada"
            1 -> "Rs. Citra Husada Jember"
            2 -> "Rsud Dr. Soebandi"
            3 -> "Rs. Paru Jember"
            4 -> "Rs. Umum Kaliwates"
            5 -> "Rs. Siloam Hospitals Jember"
            6 -> "Rs. Perkebunan Jember Klinik"
            7 -> "Rsud Kalisat Kabupaten Jember"
            8 -> "Rs. Utama Husada"
            9 -> "Rs. Daerah Balung"
            10 -> "Rs. Bina Sehat Jember"
            11 -> "Rs. Umum Kaliwates"
            12 -> "Rs. Lippo Jember"
            13 -> "Rsu Srikandi Ibi Jember"
            14 -> "Rs. Umum Universitas Muhammadiyhah Jember"
            15 -> "Rs. Umum TNI Angkatan Darat"
            else -> "Unknown Hospital"
        }
    }
}