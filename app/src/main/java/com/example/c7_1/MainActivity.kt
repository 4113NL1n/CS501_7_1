package com.example.c7_1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.c7_1.ui.theme.C7_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            C7_1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RecipeScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(modifier: Modifier,viewModel: MealViewModel = viewModel()) {
    var mealName by remember { mutableStateOf("") }
    var mealResponse by remember { mutableStateOf<MealResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val mealState by viewModel.mealState.collectAsState()



    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = mealName,
            onValueChange = { mealName = it },
            label = { Text(text = "Enter Food", fontSize = 20.sp) },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
        Button(
            onClick = {
                if (mealName.isNotEmpty()) {
                    viewModel.fetchMeal(mealName)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Get Recipe", fontSize = 20.sp)
        }
        when(mealState){
            MealViewModel.MealState.Initial -> {}
            MealViewModel.MealState.Loading -> {
                CircularProgressIndicator()
            }
            is MealViewModel.MealState.Success -> {
                val mealResponse = (mealState as MealViewModel.MealState.Success).mealResponse.meals ?: emptyList()

                if(mealResponse.isEmpty()){
                    Text("No meals found", fontSize = 20.sp)

                }else{
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(mealResponse) { meal ->
                            MealItemView(meal)
                        }
                    }
                }


            }
            is MealViewModel.MealState.Error -> {

            }
        }
    }
}

@Composable
fun MealItemView(item: MealItem) {
    Column(modifier = Modifier.padding(8.dp)) {
        AsyncImage(
            model = item.image,
            contentDescription = item.name,
            modifier = Modifier
                .size(200.dp)
                .border(2.dp, Color.Gray),
            contentScale = ContentScale.Crop
        )
        Text(text = item.name, fontSize = 20.sp)
        Text(text = item.instructions, fontSize = 14.sp)
    }
}

