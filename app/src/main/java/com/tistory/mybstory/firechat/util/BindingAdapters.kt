package com.tistory.mybstory.firechat.util

import android.graphics.drawable.Drawable
import android.text.Editable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.internal.TextWatcherAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:url", "app:errorDrawable", requireAll = false)
fun bindImageView(view: ImageView, url: String?, error: Drawable?) {
    val crossFadeTransition = DrawableTransitionOptions.withCrossFade(300)

    Glide.with(view)
        .load(url)
        .transition(crossFadeTransition)
        .error(error)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(view)
}

@BindingAdapter("app:nickNameValidator")
fun validateNickname(textInputLayout: TextInputLayout, data: String) {
    val regex = "^\$|^(?=.*[a-z])[a-z0-9]{0,15}\$".toRegex()
    val result = regex.matches(data)
    textInputLayout.error =
        if (!result) "User name must contain only letters, numbers or the underscore character." else ""
}

@BindingAdapter("app:queryHandler")
fun bindQueryOnEditText(view: TextInputEditText, handler: (String) -> Unit) {
    view.addTextChangedListener(object : TextWatcherAdapter() {
        override fun afterTextChanged(text: Editable) {
            handler.invoke(text.toString())
        }
    })
}
