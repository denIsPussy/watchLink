package com.example.watchlinkapp.ComposeUI.User

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.watchlinkapp.ComposeUI.APIStatus
import com.example.watchlinkapp.ComposeUI.AppViewModelProvider
import com.example.watchlinkapp.ComposeUI.Navigation.Screen
import com.example.watchlinkapp.ComposeUI.Network.ErrorPlaceholder
import com.example.watchlinkapp.ComposeUI.Network.LoadingPlaceholder
import com.example.watchlinkapp.Entities.Model.User.User
import com.example.watchlinkapp.R

@Composable
fun Signup(
    navController: NavController,
    viewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    if (viewModel.apiStatus == APIStatus.ERROR) {
        ErrorPlaceholder(
            message = viewModel.apiError,
            onBack = { navController.navigate(Screen.Signup.route) }
        )
        return
    }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }
    var dateOfBirthError by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun validateInput(): Boolean {
        var isValid = true

        if (username.isBlank()) {
            usernameError = "Введите имя пользователя"
            isValid = false
        } else {
            usernameError = null
        }

        if (password.isBlank()) {
            passwordError = "Введите пароль"
            isValid = false
        } else {
            passwordError = null
        }

        if (phoneNumber.isBlank()) {
            phoneNumberError = "Введите номер телефона"
            isValid = false
        } else {
            phoneNumberError = null
        }

        if (dateOfBirth.isBlank()) {
            dateOfBirthError = "Введите дату рождения"
            isValid = false
        } else {
            dateOfBirthError = null
        }

        return isValid
    }

    @SuppressLint("SuspiciousIndentation")
    fun registration() {
        if (validateInput()) {
            val user = User(
                userName = username,
                dateOfBirth = dateOfBirth,
                phoneNumber = phoneNumber,
                password = password
            )
            viewModel.registrationUser(user)
            navController.navigate(Screen.Login.route)
        }
    }
    when (viewModel.apiStatus) {
        APIStatus.DONE -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.backgroundColor)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .padding(bottom = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.signup_title),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Номер телефона") },
                        isError = phoneNumberError != null,
                        colors = TextFieldDefaults.colors(
                            unfocusedLabelColor = Color.LightGray,
                            focusedLabelColor = Color.LightGray,
                            unfocusedTextColor = colorResource(id = R.color.button),
                            focusedTextColor = colorResource(id = R.color.button),
                            unfocusedContainerColor = colorResource(id = R.color.backgroundNavBarColor),
                            focusedContainerColor = colorResource(id = R.color.backgroundNavBarColor)
                        )
                    )
                    if (phoneNumberError != null) {
                        Text(text = phoneNumberError!!, color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Имя пользователя") },
                        isError = usernameError != null,
                        colors = TextFieldDefaults.colors(
                            unfocusedLabelColor = Color.LightGray,
                            focusedLabelColor = Color.LightGray,
                            unfocusedTextColor = colorResource(id = R.color.button),
                            focusedTextColor = colorResource(id = R.color.button),
                            unfocusedContainerColor = colorResource(id = R.color.backgroundNavBarColor),
                            focusedContainerColor = colorResource(id = R.color.backgroundNavBarColor)
                        )
                    )
                    if (usernameError != null) {
                        Text(text = usernameError!!, color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Пароль") },
                        isError = passwordError != null,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.colors(
                            unfocusedLabelColor = Color.LightGray,
                            focusedLabelColor = Color.LightGray,
                            unfocusedTextColor = colorResource(id = R.color.button),
                            focusedTextColor = colorResource(id = R.color.button),
                            unfocusedContainerColor = colorResource(id = R.color.backgroundNavBarColor),
                            focusedContainerColor = colorResource(id = R.color.backgroundNavBarColor)
                        )
                    )
                    if (passwordError != null) {
                        Text(text = passwordError!!, color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = dateOfBirth,
                        onValueChange = { dateOfBirth = it },
                        isError = dateOfBirthError != null,
                        label = { Text("Дата рождения") },
                        colors = TextFieldDefaults.colors(
                            unfocusedLabelColor = Color.LightGray,
                            focusedLabelColor = Color.LightGray,
                            unfocusedTextColor = colorResource(id = R.color.button),
                            focusedTextColor = colorResource(id = R.color.button),
                            unfocusedContainerColor = colorResource(id = R.color.backgroundNavBarColor),
                            focusedContainerColor = colorResource(id = R.color.backgroundNavBarColor)
                        )
                    )
                    if (dateOfBirthError != null) {
                        Text(text = dateOfBirthError!!, color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        shape = RoundedCornerShape(5.dp), onClick = { registration() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.backgroundNavBarColor)
                        )
                    ) {
                        Text("Зарегистрироваться")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                        Text(text = "У меня есть учетная запись")
                    }
                }
            }
        }

        APIStatus.LOADING -> LoadingPlaceholder()
        else -> ErrorPlaceholder(
            message = viewModel.apiError,
            onBack = { navController.navigate(Screen.Signup.route) }
        )
    }
}