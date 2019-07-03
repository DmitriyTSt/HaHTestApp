package ru.dmitriyt.hahtestapp.data.system

import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher

/**
 * Created by Dmitriy Tomilov on 02/07/2019.
 */

fun TextInputEditText.hideErrorOnTextChange(textInputLayout: TextInputLayout) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            textInputLayout.error = ""
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    })
}