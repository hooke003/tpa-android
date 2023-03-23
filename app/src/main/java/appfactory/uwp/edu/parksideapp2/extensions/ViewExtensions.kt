package appfactory.uwp.edu.parksideapp2.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(message: String, lengthLong: Boolean = false) {
    Snackbar.make(this, message, if (lengthLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT).show()
}