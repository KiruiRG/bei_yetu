package com.example.projectdraft

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectdraft.ui.theme.ProjectdraftTheme
import androidx.lifecycle.ViewModelProvider


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

                        /*We create a shared ViewModel using requireActivity().
                        This means:
                        - ViewModel lives as long as the Activity lives.
                        - It is shared with other fragments if needed.
                        - Database loads only once.
                        */
                        val viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

                        /*Finally we pass the ViewModel to the composable screen*/
                        HomePageScreen(viewModel)
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
fun HomePageScreen(viewModel: HomeViewModel) {

    /*collectAsState() converts StateFlow into a Compose State.
      This means anytime _products changes in ViewModel,
      the UI recomposes automatically.*/
    val productList = viewModel.products.collectAsState().value

    /*Copilot says that in Compose, every UI element is a function so eg the logo, greeting etc, all of them will be functions I'm adding here*/
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        TopBar()

        /*Passing the viewModel's search function*/
        HomepageSearchBar(onSearch = { query ->
            viewModel.searchProducts(query)
        })

        topCategories()

        /*Use dynamic products from database instead of hardcoded Suggested() items*/
        Suggested(products = productList)
    }
}

@Composable
fun TopBar(){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp, vertical = 12.dp),/*This is 16 dp for left and right each and 12dp for top and bottom each*/
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
        style = MaterialTheme.typography.titleMedium,
        /*The line above controls the size and weight. By weight here, I mean the thickness or boldness
        * There is a difference between font weight(this one here) and the weight of the layouts(weight = 1f)
        * One is for thickness, the other one is applied on rows, columns etc to determine how much space they should occupy
        * The h5 above stands for heading 5*/
        color = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
fun HomepageSearchBar(onSearch: (String) -> Unit){
    var searchWord by remember { mutableStateOf("") }
    /*Ok so above, I know it's a bit confusing why the value is not false like we it was in our other app so apparently,
    * what we put in the brackets is usually what we want our initial value of the variable to be. In this case, we want it to be
    * a black string so that's why we are putting ""
    * Reminder:
    * mutableStateOf makes it a state. A state is a component that is observed by Compose so that it can recompose in case it changes
    * remember ensures that the new value of the variable is not forgotten when Compose recomposes. I was confused by this before. I didn't see
    * the point of creating a state cz why can't we just store the variable then use it. But here is the thing, you want change to occur every time
    * you are typing sth. So eg typing f will have an effect and typing fr will have an effect so that means we need a state. what do states need?
    * Variables that are remembered.
    * by allows me to access the value like this: value = searchQuery instead of this: value = searchQuery.value*/



    TextField(
        value = searchWord,
        onValueChange = {
            /*onValueChange just means,"If the value changes, do the following:"*/
            searchWord = it /*Ok so here is what "it" is for, when the value of the searchWord changes, the variable is a state, right? So the change is detected.
            What we know now is that there has been change but then we need to actually assign that new value and that is what it does. It says that, "You see that
            new value entered, that is what searchWord is now equal to*/

            onSearch(it) // calling the viewModel search function
        },


        placeholder = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ){
                Text(
                    text = "Search",
                    color = Color.Gray,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        },

        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(25.dp)
            )
        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 15.dp)//This is padding outside. In compose there are no margins
            .height(53.dp)
            .border(1.5.dp, Color.Gray, MaterialTheme.shapes.medium),


        singleLine = false, /*This means that the user can only enter a single line of words. If they press enter, it won't work*/

        shape = MaterialTheme.shapes.small,//Rounded corners
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,/*Background color if search bar is not selected. I'm removing the default grey*/
            focusedContainerColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,/*This is how you remove the default grey line on the bottom border*/
            focusedIndicatorColor = Color.Transparent
            )

    )
}

@Composable
fun topCategories(){
    Column (
        modifier = Modifier
            .padding(horizontal = 30.dp)
    ){
        Text(
            text = "Top Categories",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(bottom = 10.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            IconAndName(R.drawable.ic_electronics, "Electronics")
            IconAndName(R.drawable.ic_pastries, "Pastries")
            IconAndName(R.drawable.ic_detergents, "Detergents")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            IconAndName(R.drawable.ic_drinks, "Drinks")
            IconAndName(R.drawable.ic_beauty, "Beauty")
            IconAndName(R.drawable.ic_organic, "Organic")
        }
    }
}

@Composable
fun IconAndName(icon: Int, name : String){
    Column(
        modifier = Modifier
            .size(width = 85.dp, height = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(icon),
            contentDescription = "Category Icon",
            modifier = Modifier.size(40.dp)
        )

        Text(
            text = "$name"
        )
    }
}

@Composable
fun Suggested(products: List<ProductWithName>){
    Column (
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 15.dp)
    ){
        Text(
            text = "Suggested for you",
            style = MaterialTheme.typography.titleSmall
        )

        /*Loop through products in rows of 3*/
        val chunkedProducts = products.chunked(3)
        chunkedProducts.forEach { rowProducts ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowProducts.forEach { product ->
                    SuggestionAndName(
                        icon = product.imageRes,
                        name = product.name
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SuggestionAndName(icon: Int, name : String){
    Column(
        modifier = Modifier
            .size(width = 90.dp, height = 170.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(icon),
            contentDescription = "Category Icon",
            modifier = Modifier.size(100.dp)
        )

        Text(
            text = "$name",
            textAlign = TextAlign.Center
        )
    }
}

/*A fake view model to help me see the preview
class FakeHomeViewModel : ViewModel() {
    val products = MutableStateFlow(
        listOf(
            ProductWithName(1, "Festive Bread", 5.99, 2, "Pastries", R.drawable.test_bread),
            ProductWithName(2, "Samsung 55\" TV", 599.99, 1, "Electronics", R.drawable.test_tv),
            ProductWithName(3, "Hisense Washing Machine", 399.99, 3, "Detergents", R.drawable.test_washm)
        )
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
    @Suppress("ViewModelConstructorInComposable")
    val fakeViewModel = FakeHomeViewModel()
    ProjectdraftTheme {
        Surface {
            /*Purpose: Surface is like a background container that applies the theme’s colors, elevation, and shapes.
              It ensures your UI respects the theme’s background color and avoids drawing directly on the raw can*/
            HomePageScreen(viewModel = fakeViewModel)
        }

    }
}
*/
