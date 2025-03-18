package com.example.weatherapp

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.weatherapp.model.City
import com.example.weatherapp.ui.nav.BottomNavItem.HomeButton.icon
//import com.google.android.libraries.mapsplatform.transportation.consumer.model.Route
import com.example.weatherapp.ui.nav.Route
//@Preview(showBackground = true)
@Composable
fun ListPage(modifier: Modifier = Modifier, viewModel: MainViewModel) {

    val activity = LocalContext.current as? Activity
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .wrapContentSize(Alignment.Center)
    ) {
        /*
        Text(
            text = "Favoritas",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        */

    }
    val cityList = viewModel.cities
    //val cityList = remember { getCities().toMutableStateList() }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        items(cityList) { city ->
            if (city.weather == null) {
                viewModel.loadWeather(city)
            }
            /*
            Icon(
                imageVector = icon, contentDescription = "Monitorada?",
                modifier = Modifier.size(32.dp).clickable(enabled=viewModel.city != null){
                   // val isMonitored = true
                    viewModel.update(viewModel.city!!
                        .copy(isMonitored = true))
                }
            )

             */
            Icon(
                imageVector = if (city.isMonitored)
                    Icons.Filled.Notifications
                else
                    Icons.Outlined.Notifications,
                contentDescription = "Monitorada?",
                modifier = Modifier.size(32.dp)
            )
            CityItem(city = city,  onClick = {

                viewModel.city = city
                viewModel.page = Route.Home
/* TO DO */      Toast.makeText(activity, "Cidade OK!", Toast.LENGTH_LONG).show()
            }, onClose = {
                viewModel.remove(city)
                Toast.makeText(activity, "Fechado!", Toast.LENGTH_LONG).show()
/* TO DO */
            })
        }

    }
}



@Composable
fun CityItem(
    city: City,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        /*
        Icon(
            Icons.Rounded.FavoriteBorder,
            contentDescription = ""
        )
         */
        AsyncImage(
            model = city.weather?.imgUrl,
            modifier = Modifier.size(75.dp),
            error = painterResource(id = R.drawable.loading),
            contentDescription = "Imagem"
        )
        //modifier = Modifier.background(Color.White)
        Spacer(modifier = Modifier.size(12.dp))
        Column(modifier = modifier.weight(1f)) {

            Text(modifier = Modifier,
                text = city.name,
                fontSize = 24.sp)
            Text(modifier = Modifier,
                text = city.weather?.desc?:"carregando...",
                fontSize = 16.sp)

        }
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}

