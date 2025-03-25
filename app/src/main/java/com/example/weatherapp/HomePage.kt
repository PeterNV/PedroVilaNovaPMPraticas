package com.example.weatherapp


import android.icu.text.DecimalFormat
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.ui.nav.BottomNavItem.HomeButton.icon

//@Preview(showBackground = true)
@Composable
fun HomePage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    /*

        Column(
            modifier = Modifier.fillMaxSize()
                .background(colorResource(id = R.color.teal_700))
                .wrapContentSize(Alignment.Center)
        ) {
            Text(
                text = "Home",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    */
    Column {
        if (viewModel.city == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.teal_700))
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = "Selecione uma cidade na lista de favoritas.",
                    fontWeight = FontWeight.Bold, color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center, fontSize = 20.sp
                )
            }
            return
        }

        Row {
            /*
            Icon(
                imageVector = Icons.Filled.AccountBox,
                contentDescription = "Localized description",
                modifier = Modifier.size(150.dp)
            )
             */
            AsyncImage( // Substitui o Icon
                model = viewModel.city?.weather?.imgUrl,
                modifier = Modifier.size(100.dp),
                error = painterResource(id = R.drawable.loading),
                contentDescription = "Imagem"
            )
            Column {
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = viewModel.city?.name ?: "Selecione uma cidade...",
                    fontSize = 28.sp
                )
                /*
                Icon(
                    imageVector = if (viewModel.city?.isMonitored == true)
                        Icons.Filled.Notifications
                    else
                        Icons.Outlined.Notifications,
                    contentDescription = "Monitorada?",
                    modifier = Modifier.size(32.dp).clickable(enabled = viewModel.city != null) {
                        viewModel.update(viewModel.city!!.copy(isMonitored = !viewModel.city!!.isMonitored))
                    }
                )

                 */
                Icon(
                    imageVector = if (viewModel.city?.isMonitored == true)
                        Icons.Filled.Notifications
                    else
                        Icons.Outlined.Notifications,
                    contentDescription = "Monitorada?",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            viewModel.city?.let { currentCity ->
                                val updatedCity = currentCity.copy(isMonitored = !currentCity.isMonitored)
                                viewModel.update(updatedCity)  // Atualiza o estado corretamente
                                viewModel.city = updatedCity   // Garante que a UI recombine
                            }
                        }
                )

                /*
                Icon(
                    imageVector = if (viewModel.city?.isMonitored == true) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                    contentDescription = "Monitorada?",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            val updatedCity = viewModel.city!!.copy(isMonitored = !viewModel.city!!.isMonitored)
                            viewModel.update(updatedCity)  // Chama o método correto
                        }
                )
*/
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = viewModel.city?.weather?.desc ?: "...",
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = "Temp: " + viewModel.city?.weather?.temp + "℃",
                    fontSize = 22.sp
                )
            }
        }
        viewModel.city?.let {
            if (viewModel.city!!.weather == null) {
                viewModel.loadWeather(viewModel.city!!)
            }
            if (viewModel.city!!.forecast == null) {
                viewModel.loadForecast(viewModel.city!!); return
            }
            /*
        LazyColumn {
            items(viewModel.city!!.forecast!!) { forecast ->
                ForecastItem(forecast, onClick = { })
            }
        }
         */
            viewModel.city!!.forecast?.let { list ->
                LazyColumn {
                    items(list) { forecast ->
                        ForecastItem(forecast, onClick = { })
                    }
                }
            }
        }

    }
}

@Composable
fun ForecastItem(forecast: Forecast, onClick: (Forecast) -> Unit, modifier: Modifier = Modifier) {
    val format = DecimalFormat("#.0")
    val tempMin = format.format(forecast.tempMin)
    val tempMax = format.format(forecast.tempMax)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = { onClick(forecast) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /*
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "Localized description",
            modifier = Modifier.size(48.dp)
        )

         */
        AsyncImage( // Substitui o Icon
            model = forecast.imgUrl,
            modifier = Modifier.size(40.dp),
            error = painterResource(id = R.drawable.loading),
            contentDescription = "Imagem"
        )
        Spacer(modifier = Modifier.size(16.dp))
        Column {
            Text(modifier = Modifier, text = forecast.weather, fontSize = 24.sp)
            Row {
                Text(modifier = Modifier, text = forecast.date, fontSize = 20.sp)
                Spacer(modifier = Modifier.size(12.dp))
                Text(modifier = Modifier, text = "Min: $tempMin℃", fontSize = 16.sp)
                Spacer(modifier = Modifier.size(12.dp))
                Text(modifier = Modifier, text = "Max: $tempMax℃", fontSize = 16.sp)
            }
        }

    }
}