package com.example.projectdraft

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment

class ProductDetailsFragments : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply{
            setContent {
                ProjectdraftTheme {
                    Surface {
                        ProductDetailsScreen()
                    }
                }
            }
        }
    }
}


@Composable
fun ProductDetailsScreen(){
    TopBar()
}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Night Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun previewProductDetailScreen(){
    ProjectdraftTheme {
        Surface {
            ProductDetailsScreen()
        }
    }
}