package com.example.projectdraft

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projectdraft.ui.theme.ProjectdraftTheme
import com.google.firebase.firestore.core.ComponentProvider

class HomePageFragment : Fragment() {
    /*You're creating a Fragment called HomePageFragment.
    : Fragment() means it inherits from Android's Fragment class.*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        /*For the above 3 lines:
        * This is the lifecycle method that tells Android how to create the view for this Fragment.
        * It’s called when the Fragment’s UI is being created.
        * Normally, you'd inflate an XML layout here — but with Compose, you return a ComposeView
        * Inflating means taking an XML Layout file which is just text that describes UI elements and turning it to an actual View object that can be displayed on screen
        * A view in simple words is a GUI component so eg text, button etc
        * In XML we would've had to actually inflate using the inflate method so the next line after this would be sth like this:
        * return inflater.inflate(R.layout.fragment_home_page, container, false)
        * But this is Compose, we don't have XML files so we directly declare UI in Kotlin Code that's why the next line is returning a Compose View*/
        return ComposeView(requireContext()).apply {
            /*ComposeView is a special view that lets you use Jetpack Compose inside a Fragment.
            requireContext() gives the current context (needed to create the view).
            A context is like a backstage pass that lets you access system services, resources, and app-level info.
            The line ComposeView(requireContext()) tells Android: “Hey, I want to build this view, and here’s the environment info you need.”
            apply { ... } lets you configure the ComposeView right after creating it.*/
            setContent {
                ProjectdraftTheme {
                    Surface {
                        /*Purpose: Surface is like a background container that applies the theme’s colors, elevation, and shapes.
                        It ensures your UI respects the theme’s background color and avoids drawing directly on the raw can*/
                        HomePageScreen()
                        /*This is where you set the Compose UI.
                        setContent { ... } tells the ComposeView what to display.
                        HomePageScreen() is your Composable function — the actual UI code written in Compose.*/
                    }
                }

            }
        }
    }
}

@Composable
fun HomePageScreen(){
    /*Copilot says that in Compose, every UI element is a function so eg the logo, greeting etc, all of them will be functions I'm adding here*/
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        TopBar()
    }
}

@Composable
fun TopBar(){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Logo()
        GreetingSection()
    }
}

@Composable
fun Logo(){
    Image(
        painter = painterResource(R.drawable.beiyetu_logo),
        contentDescription = "App Logo",
        /*The purpose of contentDescription above is to provide a textual description of a visual element (like an image or icon).
        Screen readers (used by people with visual impairments) read this description aloud so users know what the image represents.
        In short, it makes ur app more inclusive.
        If you have an image that u think doesn't need a description cz its very unnecessary eg a background pattern, you can say contentDescription = null
        This makes the screen readers skip the image when reading to a visually impaired person*/
        modifier = Modifier
            .size(50.dp)
    )
}

var userName : String = "User";
@Composable
fun GreetingSection(){
    Text(
        text = "Hello, " + userName,
        style = MaterialTheme.typography.titleSmall,
        /*The line above controls the size and weight. By weight here, I mean the thickness or boldness
        * There is a difference between font weight(this one here) and the weight of the layouts(weight = 1f)
        * One is for thickness, the other one is applied on rows, columns etc to determine how much space they should occupy
        * The h5 above stands for heading 5*/
        color = MaterialTheme.colorScheme.onPrimary
    )
}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun previewHomeScreen(){
    ProjectdraftTheme {
        Surface {
            /*Purpose: Surface is like a background container that applies the theme’s colors, elevation, and shapes.
              It ensures your UI respects the theme’s background color and avoids drawing directly on the raw can*/
            HomePageScreen()
        }

    }
}
