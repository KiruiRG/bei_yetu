package com.example.projectdraft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MyAccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ProjectdraftTheme {
                    AccountScreen(
                        userName = "Jane Doe",
                        onLogoutClicked = { },
                        onUserProfileClicked = {
                            startActivity(
                                Intent(requireContext(), EditProfileActivity::class.java)
                            )
                        },
                        isPushEnabled = true,
                        onPushToggle = {}
                    )
                }
            }
        }
    }
}
