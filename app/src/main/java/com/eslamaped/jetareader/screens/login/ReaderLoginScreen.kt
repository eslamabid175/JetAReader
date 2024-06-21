package com.eslamaped.jetareader.screens.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eslamaped.jetareader.components.EmailInput
import com.eslamaped.jetareader.components.PasswordInput
import com.eslamaped.jetareader.components.ReaderLogo
import com.eslamaped.jetareader.navigation.ReaderScreens

@Composable
fun ReaderLoginScreen(navController: NavController,
                      viewModel: LoginScreenViewModel= androidx.lifecycle.viewmodel.compose.viewModel()) {
    val showloginForm= rememberSaveable { mutableStateOf(true) }
Surface(modifier = Modifier.fillMaxSize().padding(top = 130.dp)) {
Column(horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top, modifier = Modifier.padding(top = 35.dp)) {
    ReaderLogo()
    if (showloginForm.value) UserForm(loading = false, isCreateAccount = false) { email, pwd ->
        viewModel.signInWithEmailAndPassword(email, pwd) {
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
        }
    }
    else {UserForm(loading = false, isCreateAccount = true) { email, pwd ->
        viewModel.createUserWithEmailAndPassword(email, pwd) {
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
        }
    }
}
}
    Spacer(modifier = Modifier.height(15.dp))
    Row (
        modifier = Modifier.padding(15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically

    ){
val text=if (showloginForm.value) "Sign Up" else "Login"
        Text(text = "New User?")
Text(text,
    modifier = Modifier
        .clickable {
            showloginForm.value = !showloginForm.value
        }
        .padding(start = 5.dp),
    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
    color = MaterialTheme.colorScheme.secondary)
    }
}
}

@Composable
fun UserForm(
    loading:Boolean=false,
    isCreateAccount:Boolean=false,
    onDone:(String,String)->Unit={email,pwd -> }
){
    val email=rememberSaveable { mutableStateOf("") }
val password=rememberSaveable { mutableStateOf("") }
    val PasswordVisibilty=rememberSaveable { mutableStateOf(false) }
    val passwordfocus=FocusRequester.Default
    val keyboardController= LocalSoftwareKeyboardController.current
    val valid= remember(email.value,password.value) {
email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

val modifier= Modifier
    .height(250.dp)
    .background(MaterialTheme.colorScheme.surface)
    .padding(3.dp)
    .verticalScroll(rememberScrollState())
Column(modifier=modifier,horizontalAlignment = Alignment.CenterHorizontally) {
    if (isCreateAccount) Text(text = "Please Enter A valid Email and Password that is at least 6 characters", modifier = Modifier.padding(4.dp))
    else Text("")

EmailInput(emailState = email, enabled = !loading,
    onAction = KeyboardActions{
        passwordfocus.requestFocus()
    },
    )
PasswordInput(
    modifier=Modifier.focusRequester(passwordfocus),
    passwordState = password,
    labelId = "Password",
    enabled = !loading,
    passwordVisibility = PasswordVisibilty,
    onAction = KeyboardActions{
        if (!valid) return@KeyboardActions
        onDone(email.value.trim(),password.value.trim())
    }
)
    SubmitButton(
        textId= if (isCreateAccount) "Create Account" else "Login",
        loading=loading,
        validInputs=valid
    ){
        onDone(email.value.trim(),password.value.trim())
        keyboardController?.hide()
    }
}

}

@Composable
fun SubmitButton(textId: String, loading: Boolean, validInputs: Boolean,onClick: () -> Unit) {
Button(onClick = onClick,
    modifier= Modifier
        .padding(3.dp)
        .fillMaxWidth(),
    enabled = !loading && validInputs,
    shape = CircleShape

) {
if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
    else Text(text = textId, modifier = Modifier.padding(5.dp))
}
}
