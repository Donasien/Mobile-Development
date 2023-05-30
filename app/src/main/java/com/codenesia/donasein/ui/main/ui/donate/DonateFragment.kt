package com.codenesia.donasein.ui.main.ui.donate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.databinding.FragmentDonateBinding
import com.codenesia.donasein.ui.ViewModelFactory
import com.codenesia.donasein.ui.detailDonate.DetailDonateActivity
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

class DonateFragment : Fragment() {

    private var _binding: FragmentDonateBinding? = null
    private val binding get() = _binding!!
    private val donateViewModel: DonateViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }

    private val newsViewModel: NewsViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>() // Create image list

        showNewsData(imageList)
        showDonateData()

    }

    private fun showDonateData() {
        val donateAdapter = DonateAdapter { donate ->
            val intent = Intent(activity, DetailDonateActivity::class.java)
            startActivity(intent)
        }
        donateViewModel.getAllDonate().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Results.Loading -> {

                    }

                    is Results.Success -> {
                        val dataDonate = result.data
                        val listData = dataDonate.data
                        donateAdapter.submitList(listData)
                        binding.donateRvDonate.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = donateAdapter
                        }
                    }

                    is Results.Error -> {
                        Log.e("Donate Adapter", result.error.toString())
                    }
                }
            }
        }
    }

    private fun showNewsData(imageList: ArrayList<SlideModel>) {
        newsViewModel.getNewsHealth().observe(viewLifecycleOwner) { result ->
            if (result!=null) {
                val imageDefaultUrl = "https://storage.googleapis.com/proudcity/mebanenc/uploads/2021/03/placeholder-image.png"
                when(result) {
                    is Results.Success -> {
                        val data = result.data.articles
                        if (data != null) {
                            for ( i in 0 until 8) {
                                imageList.add(SlideModel(data[i].urlToImage ?: imageDefaultUrl, data[i].title, ScaleTypes.CENTER_CROP))
                            }
                        }
                    }
                    is Results.Loading -> {

                    }

                    is Results.Error -> {

                    }
                }
                binding.donateImageSlider.setImageList(imageList)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDonateBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}