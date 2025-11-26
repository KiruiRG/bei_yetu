package com.example.projectdraft

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    /*The parameter application is the whole application context. There are two different
    views, ViewModel and an AndroidViewModel. They both hold UI-related data but
    AndroidViewModel gives you access to the application context which Room needs
    to be able to create a database instance(next line)*/

    private val _products = MutableStateFlow<List<ProductWithCategoryAndSubcategory>>(emptyList())
    /*This line creates a variable that holds a list of whatever you will access using a
    * dao method. We write emptyList to initialize it. The list contains nothing yet
    * MutableStateFlow makes the list contents change if changes are made. This is a kind
    * of state but it is not a composable state. You'll see it converted to a Composable state
    * in the Fragment. This acts just like a state but Compose doesn't know about its changes directly
    * Sth you need to know is that MutableStateFlow only contains one value at a time
    * meaning only one list at a time. If we use getAllProducts() method, it will contain
    * only the list of all products. If you change the method to getAllCategories, that
    * is what _products will now contain. I know you are probably wondering why we have a somewhat
    * state that yes is observable but is private so can't be accessed outside this ViewModel. That
    * is what the next line is for*/
    val products: StateFlow<List<ProductWithCategoryAndSubcategory>> get() = _products
    /*So this is a public list that is now available to everything outside this view model.
    * This uses StateFlow instead of MutableStateFlow to make it read-only. Since this variable
    * is accessible to outsiders, we want to ensure that no accidental changes can be made by
    * outsiders. The first _products was private, right? So that the only thing that can change it
    * is this view model. Ok so what's the point of this then? This variable gets its data from
    * _products. When a change occurs, MutableStateFlow has the new list. StateFlow usually
    * contains only the updates so once MutableStateFlow changes, this changes too.
    * So using a rough explanation: MutableStateFlow in _products tells products that there are
    * updates then StateFlow in products tells Compose(after it hase been converted to a Composable
    * state) that there are updates. But to be safe, Compose elements outside can't edit the database,
    * only View Model can do it here.*/

    private val db = DatabaseProvider.getDatabase(application)
    //Now we access the database
    private val productDao = db.productDao()
    private val categoriesDao = db.categoriesDao()
    private val subCategoryDao = db.subCategoryDao()

    //Then we access the dao method we want using the database


    init {
        // init block populates database and loads initial data
        viewModelScope.launch {
            //viewModelScope.launch runs database operations in a background thread.
            withContext(Dispatchers.IO) {
                val categoriesCount = categoriesDao.countCategories()
                if (categoriesCount == 0) {
                    // If DB is empty, insert defaults
                    insertDefaults()
                }
                // Always load products after setup
                loadAllProducts()
                /*The above if statement means that if the table is empty, insert the default products
                * so they'll be the first products. If it isn't, then just load the items in the
                * products table. It ensures that the default items are not inserted over and over again*/
            }
        }
    }

    suspend fun insertDefaults() {

        val electronicsId = categoriesDao.insertCategory(CategoriesEntity(name = "Electronics")).toInt()
        val pastriesId = categoriesDao.insertCategory(CategoriesEntity(name = "Pastries")).toInt()
        val detergentsId = categoriesDao.insertCategory(CategoriesEntity(name = "Detergents")).toInt()
        val drinksId = categoriesDao.insertCategory(CategoriesEntity(name = "Drinks")).toInt()
        val beautyId = categoriesDao.insertCategory(CategoriesEntity(name = "Beauty")).toInt()
        val organicId = categoriesDao.insertCategory(CategoriesEntity(name = "Organic")).toInt()
        val cerealsId = categoriesDao.insertCategory(CategoriesEntity(name = "Cereals")).toInt()

        //Electronics
        val tvId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Televisions", categoryId = electronicsId)).toInt()
        val fridgeId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Fridges", categoryId = electronicsId)).toInt()
        val blenderId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Blenders", categoryId = electronicsId)).toInt()
        val washmachineId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Washing Machines", categoryId = electronicsId)).toInt()

        //Pastries
        val breadId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Bread", categoryId = pastriesId)).toInt()
        val cakeId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Cake", categoryId = pastriesId)).toInt()

        //Detergents
        val laundryId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Laundry", categoryId = detergentsId)).toInt()
        val dishsoapId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Dish Soap", categoryId = detergentsId)).toInt()
        val bleachingId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Bleaching Agents", categoryId = detergentsId)).toInt()

        //Drinks
        val milkId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Milk", categoryId = drinksId)).toInt()
        val sodaId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Soda", categoryId = drinksId)).toInt()
        val waterId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Water", categoryId = drinksId)).toInt()

        //Beauty
        val skincareId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Skin Care", categoryId = beautyId)).toInt()
        val makeupId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Make Up", categoryId = beautyId)).toInt()

        //Organic
        val fruitsId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Fruits", categoryId = organicId)).toInt()
        val vegetablesId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Vegetables", categoryId = organicId)).toInt()

        //Cereals
        val riceId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Rice", categoryId = cerealsId)).toInt()
        val maizeId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Maize", categoryId = cerealsId)).toInt()
        val wheatId = subCategoryDao.insertSubcategory(SubcategoryEntity(name = "Rice", categoryId = cerealsId)).toInt()

        //Actual Insertions
        productDao.insertProduct(ProductEntity(name = "Samsung 55\" TV", subcategoryId = tvId, price = 599.99, imageRes = R.drawable.test_tv))
        productDao.insertProduct(ProductEntity(name = "Samsung Fridge", subcategoryId = fridgeId, price = 799.99, imageRes = R.drawable.test_fridge))
        productDao.insertProduct(ProductEntity(name = "Ramtons Blender", subcategoryId = blenderId, price = 49.99, imageRes = R.drawable.test_blender))
        productDao.insertProduct(ProductEntity(name = "Hisense 10.5 kgs", subcategoryId = washmachineId, price = 949.99, imageRes = R.drawable.test_washm))

        productDao.insertProduct(ProductEntity(name = "Festive Bread", subcategoryId = breadId, price = 5.99, imageRes = R.drawable.test_bread))
        productDao.insertProduct(ProductEntity(name = "Chocolate Cake", subcategoryId = cakeId, price = 12.99, imageRes = R.drawable.test_cake))

        productDao.insertProduct(ProductEntity(name = "Ultra Concentrated Laundry Soap", subcategoryId = laundryId, price = 30.99, imageRes = R.drawable.test_laundry))
        productDao.insertProduct(ProductEntity(name = "Cadia dish soap", subcategoryId = dishsoapId, price = 25.99, imageRes = R.drawable.test_dish))
        productDao.insertProduct(ProductEntity(name = "Concentrated Bleach", subcategoryId = bleachingId, price = 40.99, imageRes = R.drawable.test_bleach))

        productDao.insertProduct(ProductEntity(name = "Brookside Milk", subcategoryId = sodaId, price = 15.99, imageRes = R.drawable.test_milk))
        productDao.insertProduct(ProductEntity(name = "Canned Soda", subcategoryId = sodaId, price = 15.99, imageRes = R.drawable.test_soda))
        productDao.insertProduct(ProductEntity(name = "Water", subcategoryId = waterId, price = 10.99, imageRes = R.drawable.test_water))

        productDao.insertProduct(ProductEntity(name = "Eucerin Sunscreen", subcategoryId = skincareId, price = 60.99, imageRes = R.drawable.test_skin))
        productDao.insertProduct(ProductEntity(name = "Fenti Lipstick", subcategoryId = makeupId, price = 85.99, imageRes = R.drawable.test_makeup))

        productDao.insertProduct(ProductEntity(name = "Apples", subcategoryId = fruitsId, price = 15.99, imageRes = R.drawable.test_fruit))
        productDao.insertProduct(ProductEntity(name = "Clustered Veggies", subcategoryId = vegetablesId, price = 13.99, imageRes = R.drawable.test_veggies))

        productDao.insertProduct(ProductEntity(name = "Dawaat Basmati Rice", subcategoryId = riceId, price = 100.00, imageRes = R.drawable.test_rice))
        productDao.insertProduct(ProductEntity(name = "Pembe 2kg Maize Flour", subcategoryId = maizeId, price = 80.00, imageRes = R.drawable.test_maize))
        productDao.insertProduct(ProductEntity(name = "EXE 2kgs All-purpose Flour", subcategoryId = wheatId, price = 150.00, imageRes = R.drawable.test_wheat))
    }


    fun loadAllProducts(){
        viewModelScope.launch {
            val allProducts = withContext(Dispatchers.IO) {
                productDao.getAllProductsWithCategoryAndSubcategory()
            }

            //A log to see if there's a problem with loading the products
            Log.d("HomeViewModel", "loadAllProducts: fetched ${allProducts.size} items")

            // Load all products into stateflow
            _products.value = allProducts
            /*We already ensured that products gets the values from _products so we don't need to
            repeat that*/
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            val allProducts = productDao.getAllProductsWithCategoryAndSubcategory()
            _products.value = allProducts.filter {
                it.name.contains(query, ignoreCase = true)
                /*it → refers to each individual ProductEntity in the allProducts list.
                * it.name → the name of that product.If we wanted it to be compared with eg
                  id, we would've put it.id
                * .contains(query, ignoreCase = true) → checks if the product name contains the search
                  text typed by the user.
                * ignoreCase = true → matches regardless of upper/lowercase letters*/
            }
        }
    }
}
