package ru.svod.evotorkkt

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.svod.evotorkkt.ui.theme.EvotorKktTheme


private  const val TAG = "ru.svod.evotorkkt"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mActivity = this

        var jlog = LogcatCapture()

        setContent {
            EvotorKktTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //Greeting(
                    //    name = "Android",
                    //    modifier = Modifier.padding(innerPadding)
                    //)
                    PageSettings(
                        modifier = Modifier.padding(innerPadding),
                        mActivity
                    )
                }
            }
        }

        jlog.start(this)

    }
}


@Composable
fun PageSettings(modifier: Modifier = Modifier, mActivity: Activity) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(1.dp)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(56.dp))

        // Buttons "Add position" and "Make payment"
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        {

            // Add new position
            Button(
                onClick = {
                    Log.e(TAG, "Button onClick 1422")
                    Toast.makeText(mActivity, "Button onClick 1422", Toast.LENGTH_LONG).show()
                }
            ) { Text(text = "Обновить") }

        }
    }

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EvotorKktTheme {
        Greeting("Android")
    }
}