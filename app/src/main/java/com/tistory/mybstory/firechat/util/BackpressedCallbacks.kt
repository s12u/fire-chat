package com.tistory.mybstory.firechat.util

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.tistory.mybstory.firechat.R
import java.util.concurrent.TimeUnit

class BackPressedCallbacks {

    companion object {
        private var lastPressedTime = 0L

        val doubleBackpressToExit: ((Fragment) -> OnBackPressedCallback.() -> Unit) = { f ->
            {
                if (lastPressedTime + TimeUnit.SECONDS.toMillis(2) > System.currentTimeMillis()) {
                    f.requireActivity().finishAffinity()
                } else {
                    Toast.makeText(
                        f.requireContext(),
                        f.requireContext().getString(R.string.tap_twice_to_exit),
                        Toast.LENGTH_SHORT
                    ).show()
                    lastPressedTime = System.currentTimeMillis()
                }
            }
        }
    }

}
