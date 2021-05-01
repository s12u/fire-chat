package com.tistory.mybstory.firechat.ui.chat

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.databinding.FragmentChatListBinding
import com.tistory.mybstory.firechat.util.BackPressedCallbacks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment: BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override var backPressedCallback: (OnBackPressedCallback.() -> Unit)? = BackPressedCallbacks.doubleBackpressToExit(this)
}
