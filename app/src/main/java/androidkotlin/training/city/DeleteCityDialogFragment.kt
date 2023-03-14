package androidkotlin.training.city

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidkotlin.training.R
import androidx.fragment.app.DialogFragment

class DeleteCityDialogFragment : DialogFragment() {

    interface DeleteCityDialogListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    companion object {

        private const val EXTRA_CITY_NAME = "androidkotlin.training.extras.EXTRA_CITY_NAME"

        fun mewInstance(cityName: String) : DeleteCityDialogFragment {
            val fragment = DeleteCityDialogFragment()
            fragment.arguments = Bundle().apply {
                putString(EXTRA_CITY_NAME, cityName)
            }
            return fragment
        }
    }

    var listener: DeleteCityDialogListener? = null
    private lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cityName = arguments?.getString(EXTRA_CITY_NAME)!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Do you really want to delete $cityName?")
            .setPositiveButton(getString(R.string.deletecity_positive),
                        DialogInterface.OnClickListener
                                { _, _ -> listener?.onDialogPositiveClick() })
            .setNegativeButton(getString(R.string.deletecity_negative)
            ) { _, _ -> listener?.onDialogNegativeClick() }

        return builder.create()
    }
}