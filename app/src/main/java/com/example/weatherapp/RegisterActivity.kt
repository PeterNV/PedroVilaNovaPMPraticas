package com.example.weatherapp

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.platform.LocalContext
import com.example.weatherapp.db.fb.FBDatabase
import com.example.weatherapp.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterPage(modifier = Modifier.padding(innerPadding))
                    Spacer(modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable

fun RegisterPage(modifier: Modifier = Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var cpassword by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current // Obtenha o contexto aqui

    val activity = LocalContext.current as? Activity

    Column(
        modifier = modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = "Registro!",
            fontSize = 24.sp,
        )
            OutlinedTextField(
                value = name,
                label = { Text(text = "Digite seu e-mail") },
                modifier = modifier.fillMaxWidth(),
                onValueChange = { name = it }
            )
        OutlinedTextField(
            value = email,
            label = { Text(text = "Digite seu e-mail") },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { email = it }
        )
        OutlinedTextField(
            value = password,
            label = { Text(text = "Digite sua senha") },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),

            )
        OutlinedTextField(
            value = cpassword,
            label = { Text(text = "Confirme sua senha") },
            modifier = modifier.fillMaxWidth(),
            onValueChange = { cpassword = it },
            visualTransformation = PasswordVisualTransformation(),

            )
        Row(modifier = modifier) {

            Button(
                onClick = { email = ""; password = "" }
            ) {
                Text("Limpar")
            }
            Button(
                enabled = email.isNotEmpty() && password.isNotEmpty() && password == cpassword,
                onClick = {

                    Firebase.auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity!!) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity,"Registro OK!", Toast.LENGTH_LONG).show()
                                activity.startActivity(
                                    Intent(activity, MainActivity::class.java).setFlags(
                                        FLAG_ACTIVITY_SINGLE_TOP )
                                )
                                FBDatabase().register(User(name, email))
                            } else {
                                Toast.makeText(activity,
                                    "Registro FALHOU!", Toast.LENGTH_LONG).show()
                            }
                        }

                }

            ) {Text("Registrar") }
        }
    }
}