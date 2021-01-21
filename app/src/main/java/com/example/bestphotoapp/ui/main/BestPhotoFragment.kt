package com.example.bestphotoapp.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.example.bestphotoapp.MainActivity
import com.example.bestphotoapp.R
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.main_fragment.*

class BestPhotoFragment : Fragment() {


    companion object {
        fun newInstance() = BestPhotoFragment()
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: BestPhotoViewModel by lazy {
        ViewModelProviders.of(this).get(BestPhotoViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData()
                .observe(this@BestPhotoFragment, Observer<BestPhotoData> { renderData(it) })
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://https://yandex.ru/${input_edit_text.text.toString()}")
            })
        }

        bottomSheetBehavior.addBottomSheetCallback(object:
                BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> bottom_sheet_description_header
                            .text.toString()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                TODO("Not yet implemented")
            }

        })

    }


    private fun renderData(data: BestPhotoData) {
        when (data) {
            is BestPhotoData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Пустая ссылка")
                } else {
                    //showSuccess()
                    image_view.load(url) {
                        lifecycle(this@BestPhotoFragment)
                        error(R.drawable.ic_error_24px)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
            is BestPhotoData.Loading -> {
                // Отображение загрузки
                //showLoading()
            }
            is BestPhotoData.Error -> {
                // Отображение ошибки
                //showError(data.error.message)
                toast(data.error.message)
            }
        }
    }


    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

}