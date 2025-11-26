package com.example.projectdraft

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    private val storeDao = db.storeDao()
    private val listingDao = db.listingDao()


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
        val samsungTvId = productDao.insertProduct(ProductEntity(name = "Samsung 55\" TV", subcategoryId = tvId, price = 59999.99, imageRes = R.drawable.test_tv, description = "Experience stunning  4K clarity with the Samsung UHD TV. Powered by the Crystal Processor 4K Engine, every scene looks vibrant and lifelike.")).toInt()
        val samsungFridgeId = productDao.insertProduct(ProductEntity(name = "Samsung Fridge", subcategoryId = fridgeId, price = 90999.00, imageRes = R.drawable.test_fridge, description = "Samsung fridges are described as modern appliances featuring innovative technology, energy efficiency, and various storage options. Common features include the Digital Inverter Compressor for quiet and efficient operation, Twin Cooling Plus to maintain optimal humidity and prevent odor mixing, and No Frost technology to eliminate manual defrosting. They are also known for their stylish designs, durable tempered glass shelves, and convenient features like adjustable shelving, LED lighting, and specific drawers for fruits and vegetables. ")).toInt()
        val ramtonsBlenderId = productDao.insertProduct(ProductEntity(name = "Ramtons Blender", subcategoryId = blenderId, price = 30000.00, imageRes = R.drawable.test_blender, description = "Ramtons blenders are designed to be functional and reliable, featuring powerful motors and durable stainless-steel blades for blending, chopping, and pureeing various ingredients for everything from smoothies to sauces. They typically include features like multi-speed settings, safety locks, and large-capacity jugs (often 1.5 liters), with many models also coming with additional attachments like a mill or chopper. ")).toInt()
        val HisensewsId = productDao.insertProduct(ProductEntity(name = "Hisense 10.5 kgs", subcategoryId = washmachineId, price = 85000.00, imageRes = R.drawable.test_washm, description = "Hisense washing machines are described as energy-efficient and reliable appliances that come in various types, including front-load, top-load, and wash-and-dry models, with capacities ranging from small (6kg) to large (13.5kg).")).toInt()

        val festiveId = productDao.insertProduct(ProductEntity(name = "Festive Bread", subcategoryId = breadId, price = 60.00, imageRes = R.drawable.test_bread, description = "The brand, DPL Festive, offers a white bread that is soft and light, suitable for sandwiches and toast, as well as brown and sugar-free options. In a more traditional sense, festive bread is a naturally fermented, often dark, round loaf, flavored with caraway seeds and malt, and is sometimes baked into decorative shapes for holidays like Christmas. ")).toInt()
        val chococakeId = productDao.insertProduct(ProductEntity(name = "Chocolate Cake", subcategoryId = cakeId, price = 200.00, imageRes = R.drawable.test_cake, description = "This is a cake flavored with melted chocolate, cocoa powder, or both, known for its rich, deep chocolate taste and generally moist, tender, and soft texture. The exact description can vary depending on the specific ingredients and type of preparation. ")).toInt()

        val ultralaundryId = productDao.insertProduct(ProductEntity(name = "Ultra Concentrated Laundry Soap", subcategoryId = laundryId, price = 700.00, imageRes = R.drawable.test_laundry, description = "This is a powerful, highly effective detergent with a higher percentage of active cleaning ingredients and less water compared to traditional formulas. This means a smaller amount is needed for each wash, making it cost-effective, logistically efficient, and often more eco-friendly due to reduced plastic packaging.")).toInt()
        val cadiadishId = productDao.insertProduct(ProductEntity(name = "Cadia dish soap", subcategoryId = dishsoapId, price = 200.00, imageRes = R.drawable.test_dish, description = "Cadia dish soaps are plant-based, biodegradable, and hypoallergenic cleaning products that are formulated to be gentle yet effective against grease and grime.")).toInt()
        val concBleachId = productDao.insertProduct(ProductEntity(name = "Concentrated Bleach", subcategoryId = bleachingId, price = 900.00, imageRes = R.drawable.test_bleach, description = "Concentrated bleach is a powerful cleaning solution, typically a pale yellow liquid with a characteristic odor, used for whitening, disinfecting, and deodorizing. It is a highly effective, multi-purpose product for both laundry and hard surfaces, capable of killing germs, removing tough stains, and eliminating mold and mildew.")).toInt()

        val BrooksideId = productDao.insertProduct(ProductEntity(name = "Brookside Milk", subcategoryId = milkId, price = 190.00, imageRes = R.drawable.test_milk, description = "Brookside milk includes a variety of products like long-life UHT whole milk, which is creamy, rich, and has an extended shelf life without refrigeration until opened. The company also offer dairy-free alternatives like soy, almond, and oat milks, which are naturally lactose-free. ")).toInt()
        val canSodaId = productDao.insertProduct(ProductEntity(name = "Canned Soda", subcategoryId = sodaId, price = 99.99, imageRes = R.drawable.test_soda, description = "This is a sealed, single-serving metal container holding a carbonated beverage that typically includes water, sweeteners (sugar or artificial sweeteners), and flavorings.")).toInt()
        val waterBottleId = productDao.insertProduct(ProductEntity(name = "Water", subcategoryId = waterId, price = 40.00, imageRes = R.drawable.test_water, description = "This is water packaged in sealed containers for human consumption, sourced from various places like springs, wells, or municipal supplies, and is subject to safety standards.")).toInt()

        val eucerinId = productDao.insertProduct(ProductEntity(name = "Eucerin Sunscreen", subcategoryId = skincareId, price = 1000.00, imageRes = R.drawable.test_skin, description = "Eucerin sunscreen is a line of sun protection products that offers high levels of UVA/UVB protection and is formulated for various skin types.Specific product descriptions include the Oil Control Gel-Cream for oily/acne-prone skin with an anti-shine effect and the Hydro Protect Ultra-Light Fluid, which provides deep hydration and a weightless feel for all skin types.")).toInt()
        val fentiId = productDao.insertProduct(ProductEntity(name = "Fenti Lipstick", subcategoryId = makeupId, price = 1000.00, imageRes = R.drawable.test_makeup, description = "Fenty lipsticks are known for their wide range of inclusive shades, high-impact color, and comfortable formulas that provide either a velvet-matte or a sheer, shiny finish. For example, the Icon Velvet Liquid Lipstick is a non-drying, whipped formula that delivers rich, matte color with a plush, comfortable feel.")).toInt()

        val applesId = productDao.insertProduct(ProductEntity(name = "Apples", subcategoryId = fruitsId, price = 199.99, imageRes = R.drawable.test_fruit, description = "Packaged apples are whole or sliced apples that are washed, sorted by size and quality, and protected in containers such as bags, trays, or boxes to prevent bruising and extend shelf life.")).toInt()
        val veggiesId = productDao.insertProduct(ProductEntity(name = "Clustered Veggies", subcategoryId = vegetablesId, price = 299.99, imageRes = R.drawable.test_veggies, description = "Packaged vegetables are fresh or frozen produce that has been cleaned, cut, and sealed in a container for sale and consumption. The packaging extends shelf life, protects against damage, and provides consumer convenience by offering pre-portioned, pre-washed, or pre-cut items.")).toInt()

        val dawaatId = productDao.insertProduct(ProductEntity(name = "Dawaat Basmati Rice", subcategoryId = riceId, price = 300.00, imageRes = R.drawable.test_rice, description = "Daawat Basmati rice is a premium long-grain rice known for its authentic aroma, fluffy texture, and superior quality, with grains that elongate up to 19 mm or more when cooked. It is aged to perfection to enhance its flavor and is often grown in the Himalayan foothills.")).toInt()
        val pembeId = productDao.insertProduct(ProductEntity(name = "Pembe 2kg Maize Flour", subcategoryId = maizeId, price = 100.00, imageRes = R.drawable.test_maize, description = "Pembe maize flour is a leading Kenyan brand of sifted maize flour that is rich in carbohydrates, proteins, vitamins, and minerals, and is fortified to ensure it's a nutritious staple for families. It is a versatile ingredient used for making traditional dishes like ugali, as well as other baked goods, and is milled from fine-quality maize. ")).toInt()
        val exWheatId = productDao.insertProduct(ProductEntity(name = "EXE 2kgs All-purpose Flour", subcategoryId = wheatId, price = 200.00, imageRes = R.drawable.test_wheat, description = "EXE wheat flour is a versatile brand, with a flagship \"All-Purpose\" flour suitable for a wide range of uses, including baking, pancakes, chapatis, and thickening sauces. Other variations like \"Self Raising\" flour make baking easier, while \"Atta Mark 1\" is ideal for soft, whole-wheat chapatis.")).toInt()


        //Store Insertions
        val naivasId = storeDao.insertStore(StoreEntity(name = "Naivas", websiteUrl = "https://naivas.online/")).toInt()
        val quickMartId = storeDao.insertStore(StoreEntity(name = "Quick Mart", websiteUrl = "https://www.quickmart.co.ke/?srsltid=AfmBOooK8PrYL4s-w2u87tlsXuqqC-2BM3VsfgEgtdDa8z7FW7rSpUO4")).toInt()
        val carrefourId = storeDao.insertStore(StoreEntity(name = "Carrefour", websiteUrl = "https://www.carrefour.ke/mafken/en?utm_source=google&utm_medium=cpc&utm_campaign=ke_en_s_fnf_kw_web_brand&gad_source=1&gad_campaignid=13528771373&gbraid=0AAAAADP90D-tE7Ib-m7wOhSS5KMnp06WJ&gclid=Cj0KCQiAxJXJBhD_ARIsAH_JGjiPdJoJmzSIPg9jqgcaSPRF2tShPDX_caeSIZVqpwnEAeDCx83wdDIaAkPhEALw_wcB")).toInt()


        //I'm only gonna demonstrate the first 4 products bcz of time
        listingDao.insertListing(ProductListingEntity(productId = samsungTvId, storeId = naivasId, price = 57999.00))
        listingDao.insertListing(ProductListingEntity(productId = samsungTvId, storeId = quickMartId, price = 58500.00))
        listingDao.insertListing(ProductListingEntity(productId = samsungTvId, storeId = carrefourId, price = 60999.00))

        listingDao.insertListing(ProductListingEntity(productId = samsungFridgeId, storeId = naivasId, price = 98999.00))
        listingDao.insertListing(ProductListingEntity(productId = samsungFridgeId, storeId = quickMartId, price = 97500.00))
        listingDao.insertListing(ProductListingEntity(productId = samsungFridgeId, storeId = carrefourId, price = 100999.00))

        listingDao.insertListing(ProductListingEntity(productId = ramtonsBlenderId, storeId = naivasId, price = 27999.00))
        listingDao.insertListing(ProductListingEntity(productId = ramtonsBlenderId, storeId = quickMartId, price = 28500.00))
        listingDao.insertListing(ProductListingEntity(productId = ramtonsBlenderId, storeId = carrefourId, price = 26999.00))

        listingDao.insertListing(ProductListingEntity(productId = HisensewsId, storeId = naivasId, price = 85999.00))
        listingDao.insertListing(ProductListingEntity(productId = HisensewsId, storeId = quickMartId, price = 88500.00))
        listingDao.insertListing(ProductListingEntity(productId = HisensewsId, storeId = carrefourId, price = 86999.00))

        listingDao.insertListing(ProductListingEntity(productId = festiveId, storeId = carrefourId, price = 65.00))
        listingDao.insertListing(ProductListingEntity(productId = festiveId, storeId = naivasId, price = 67.00))
        listingDao.insertListing(ProductListingEntity(productId = festiveId, storeId = quickMartId, price = 70.00))

        listingDao.insertListing(ProductListingEntity(productId = chococakeId, storeId = carrefourId, price = 210.00))
        listingDao.insertListing(ProductListingEntity(productId = chococakeId, storeId = naivasId, price = 215.00))
        listingDao.insertListing(ProductListingEntity(productId = chococakeId, storeId = quickMartId, price = 200.00))

        listingDao.insertListing(ProductListingEntity(productId = ultralaundryId, storeId = carrefourId, price = 700.00))
        listingDao.insertListing(ProductListingEntity(productId = ultralaundryId, storeId = naivasId, price = 750.00))
        listingDao.insertListing(ProductListingEntity(productId = ultralaundryId, storeId = quickMartId, price = 710.00))

        listingDao.insertListing(ProductListingEntity(productId = cadiadishId, storeId = carrefourId, price = 250.00))
        listingDao.insertListing(ProductListingEntity(productId = cadiadishId, storeId = naivasId, price = 240.00))
        listingDao.insertListing(ProductListingEntity(productId = cadiadishId, storeId = quickMartId, price = 230.00))

        listingDao.insertListing(ProductListingEntity(productId = concBleachId, storeId = carrefourId, price = 850.00))
        listingDao.insertListing(ProductListingEntity(productId = concBleachId, storeId = naivasId, price = 815.00))
        listingDao.insertListing(ProductListingEntity(productId = concBleachId, storeId = quickMartId, price = 900.00))

        listingDao.insertListing(ProductListingEntity(productId = BrooksideId, storeId = carrefourId, price = 150.00))
        listingDao.insertListing(ProductListingEntity(productId = BrooksideId, storeId = naivasId, price = 160.00))
        listingDao.insertListing(ProductListingEntity(productId = BrooksideId, storeId = quickMartId, price = 170.00))

        listingDao.insertListing(ProductListingEntity(productId = canSodaId, storeId = carrefourId, price = 170.00))
        listingDao.insertListing(ProductListingEntity(productId = canSodaId, storeId = naivasId, price = 160.00))
        listingDao.insertListing(ProductListingEntity(productId = canSodaId, storeId = quickMartId, price = 150.00))

        listingDao.insertListing(ProductListingEntity(productId = waterBottleId, storeId = carrefourId, price = 60.00))
        listingDao.insertListing(ProductListingEntity(productId = waterBottleId, storeId = naivasId, price = 40.00))
        listingDao.insertListing(ProductListingEntity(productId = waterBottleId, storeId = quickMartId, price = 50.00))

        listingDao.insertListing(ProductListingEntity(productId = eucerinId, storeId = carrefourId, price = 1250.00))
        listingDao.insertListing(ProductListingEntity(productId = eucerinId, storeId = naivasId, price = 1000.00))
        listingDao.insertListing(ProductListingEntity(productId = eucerinId, storeId = quickMartId, price = 1200.00))

        listingDao.insertListing(ProductListingEntity(productId = fentiId, storeId = carrefourId, price = 950.00))
        listingDao.insertListing(ProductListingEntity(productId = fentiId, storeId = naivasId, price = 1050.00))
        listingDao.insertListing(ProductListingEntity(productId = fentiId, storeId = quickMartId, price = 999.99))

        listingDao.insertListing(ProductListingEntity(productId = applesId, storeId = carrefourId, price = 300.00))
        listingDao.insertListing(ProductListingEntity(productId = applesId, storeId = naivasId, price = 290.00))
        listingDao.insertListing(ProductListingEntity(productId = applesId, storeId = quickMartId, price = 280.00))

        listingDao.insertListing(ProductListingEntity(productId = veggiesId, storeId = carrefourId, price = 170.00))
        listingDao.insertListing(ProductListingEntity(productId = veggiesId, storeId = naivasId, price = 180.00))
        listingDao.insertListing(ProductListingEntity(productId = veggiesId, storeId = quickMartId, price = 190.00))

        listingDao.insertListing(ProductListingEntity(productId = dawaatId, storeId = carrefourId, price = 350.00))
        listingDao.insertListing(ProductListingEntity(productId = dawaatId, storeId = naivasId, price = 400.00))
        listingDao.insertListing(ProductListingEntity(productId = dawaatId, storeId = quickMartId, price = 370.00))

        listingDao.insertListing(ProductListingEntity(productId = pembeId, storeId = carrefourId, price = 130.00))
        listingDao.insertListing(ProductListingEntity(productId = pembeId, storeId = naivasId, price = 110.00))
        listingDao.insertListing(ProductListingEntity(productId = pembeId, storeId = quickMartId, price = 125.00))

        listingDao.insertListing(ProductListingEntity(productId = exWheatId, storeId = carrefourId, price = 210.00))
        listingDao.insertListing(ProductListingEntity(productId = exWheatId, storeId = naivasId, price = 200.00))
        listingDao.insertListing(ProductListingEntity(productId = exWheatId, storeId = quickMartId, price = 195.00))
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

    suspend fun getBestPrice(productId: Int): StorePriceListing? {
        return listingDao.getSortedListingsForProduct(productId).firstOrNull()
    }

    private val _productDetails =
        MutableStateFlow<Pair<ProductWithCategoryAndSubcategory, List<StorePriceListing>>?>(null)
    val productDetails: StateFlow<Pair<ProductWithCategoryAndSubcategory, List<StorePriceListing>>?> =
        _productDetails

    fun loadProductDetails(productId: Int) {
        viewModelScope.launch {
            val product = productDao.getProductById(productId) // suspend call
            val listings = listingDao.getSortedListingsForProduct(productId) // suspend call
            _productDetails.value = product to listings
        }
    }

}
