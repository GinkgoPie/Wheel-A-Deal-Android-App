package nz.ac.uclive.jba214.wheeladeal

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.commandiron.spin_wheel_compose.SpinWheel
import com.commandiron.spin_wheel_compose.SpinWheelDefaults
import com.commandiron.spin_wheel_compose.state.rememberSpinWheelState
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import nz.ac.uclive.jba214.wheeladeal.ui.theme.WheelADealTheme
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    private lateinit var shareScreenshotButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
    }

    @Composable
    fun MainScreen(navController: NavController) {
        WheelADealTheme {

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.fillMaxHeight()) {
                    Box(
                        modifier = Modifier
                            .weight(3f)
                            .padding(all = 25.dp)
                            .fillMaxWidth()
                    ) {
                        Greeting(modifier = Modifier.align(Alignment.Center))
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(all = 2.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        StartButton(getString(R.string.start_button_text)) {
                            navController.navigate(Screen.WheelNameScreen.route)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Greeting(modifier: Modifier = Modifier) {
        Column (modifier = modifier){
            Text(
                text = getString(R.string.app_name_text1),
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier
            )
            Text(
                text = getString(R.string.app_name_text2),
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier
            )
            Text(
                text = getString(R.string.app_name_text3),
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier
            )
        }
        lottieCatAnimation()
    }

    @Composable
    fun StartButton(label: String, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(Color(0xFF4f518c))
        ) {
            Text(
                text = label,
            )
        }
    }



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WheelNameScreen(navController: NavController) {
        var valueNotEmpty by remember { mutableStateOf(true) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(45.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = getString(R.string.wheel_name_request_text),
                color = Color.Black,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            var textValue by remember { mutableStateOf(TextFieldValue()) }
            TextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text(getString(R.string.wheel_name_label_text)) },
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))



            if (!valueNotEmpty) {
                Toast.makeText(LocalContext.current, "Empty value is not allowed.", Toast.LENGTH_LONG).show()
            }

            Button(
                onClick = {
                    if (textValue.text.trim() == "") {
                        valueNotEmpty = !valueNotEmpty
                    } else {
                        navController.navigate(Screen.AddChoicesScreen.route + "/${textValue.text}")
                    }
                     },
                modifier = Modifier
                    .align(Alignment.End),
                colors = ButtonDefaults.buttonColors(Color(0xFF4f518c))
            ) {
                Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Arrow Right")
            }


        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddChoicesScreen(navController: NavController, wheelName: String?) {
        var valueNotEmpty by remember { mutableStateOf(true) }
        val choicesStringList: MutableList<String> = remember { mutableStateListOf() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (wheelName != null) {
                Text(
                    text = getString(R.string.add_choices_request_text) +' '+ wheelName,
                    color = Color.Black,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            var textValue by remember { mutableStateOf(TextFieldValue()) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                TextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    label = { Text(getString(R.string.add_choices_label_text)) },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    colors = ButtonDefaults.buttonColors(Color(0xFF4f518c)),
                    onClick = {
                        if (textValue.text.trim() == "") {
                            valueNotEmpty = !valueNotEmpty
                        } else {
                        choicesStringList.add(textValue.text)
                        textValue = TextFieldValue("")}
                    },
                    modifier = Modifier
                        .padding(start = 8.dp)
                ) {
                    Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "Add Circle")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!valueNotEmpty) {
                Toast.makeText(LocalContext.current, "Empty value is not allowed.", Toast.LENGTH_LONG).show()
            }
            LazyVerticalGrid(
                modifier = Modifier.weight(2f),
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(choicesStringList.size) { index ->
                    val choice = choicesStringList[index]
                    var selected by remember { mutableStateOf(true) }
                    InputChip(
                        modifier = Modifier.padding(8.dp),
                        selected = selected,
                        onClick = {
                            selected = !selected
                            if (!selected) {
                                choicesStringList.remove(choice)
                            }
                        },
                        label = { Text(choice) },
                        avatar = {
                            Icon(
                                Icons.TwoTone.Clear,
                                contentDescription = "Choice chip",
                                Modifier.size(InputChipDefaults.AvatarSize)
                            )
                        }

                    )
                }
                }



            Spacer(modifier = Modifier.weight(1f))

            Button(
                colors = ButtonDefaults.buttonColors(Color(0xFF4f518c)),
                onClick = { navController.navigate(Screen.WheelSpinScreen.route + "/$wheelName" + "/${choicesStringList.joinToString(", ")}") },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Arrow Right")
            }
        }
    }

    @Composable
    fun WheelSpinScreen(navController: NavController, wheelName: String?, choices: String?) {
        var choicesList :List<String> = choices?.split(", ") ?: emptyList()
        if (choicesList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .padding(all = 2.dp)
                    .fillMaxWidth()
            ) {

                SpinningWheel(choicesList, wheelName = wheelName)

            }



        }
    }

    @Composable
    fun Navigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.MainScreen.route ) {

            composable(route = Screen.MainScreen.route){
                MainScreen(navController)
            }
            composable(route = Screen.WheelNameScreen.route){
                WheelNameScreen(navController)
            }

            composable(route = Screen.AddChoicesScreen.route + "/{wheelName}", arguments = listOf(
                navArgument("wheelName") {
                    type = NavType.StringType
                    nullable = false
                }
            )){ entry ->
                AddChoicesScreen(navController, wheelName = entry.arguments?.getString("wheelName"))
            }
            composable(route = Screen.WheelSpinScreen.route + "/{wheelName}" + "/{choicesStringList}",
                arguments = listOf(
                    navArgument("wheelName") {
                        type = NavType.StringType
                        nullable = false
                    },
                    navArgument("choicesStringList") {
                        type = NavType.StringType
                        nullable = false
                    },
                )){
                entry ->
                WheelSpinScreen(navController, wheelName = entry.arguments?.getString("wheelName"),
                    choices = entry.arguments?.getString("choicesStringList"))
            }

        }
    }




    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SpinningWheel(items: List<String>, wheelName: String?) {
        var view = LocalView.current
        val iconList = List(items.size) { Icons.Filled.Favorite }
        var pausedState = remember { mutableStateOf(false) }

        val state = rememberSpinWheelState(
            pieCount = items.size,
            durationMillis = 20000,
            delayMillis = 200,
            rotationPerSecond = 2f,
            easing = LinearOutSlowInEasing,
            startDegree = 90f,
            resultDegree = 212f
        )

        val scope = rememberCoroutineScope()
        val textList by remember {
            mutableStateOf(items)
        }
        var selectedChoice = remember { mutableStateOf(getString(R.string.spin_wheel_notification_text)) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF676F9B)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (wheelName != null) {
                Text(
                    text = wheelName,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF2c2a4a),
                    modifier = Modifier.padding(15.dp)
                )
            }
            SpinWheel(
                onClick = {
                    scope.launch {
                        state.animate { pieIndex ->
                            selectedChoice.value = getString(R.string.selected_choices_text) + " "+ items[pieIndex]
                        }
                    }
                },
                state = state,
                dimensions = SpinWheelDefaults.spinWheelDimensions(
                    spinWheelSize = 250.dp,
                    frameWidth = 20.dp,
                    selectorWidth = 15.dp
                ),
                colors = SpinWheelDefaults.spinWheelColors(
                    frameColor = Color(0xFF403d39),
                    dividerColor = Color(0xFFfffcf2),
                    selectorColor = Color(0xFFdc0073),
                    pieColors = listOf(
                        Color(0xFFdabfff),
                        Color(0xFF907ad6),
                        Color(0xFF4f518c),
                        Color(0xFF2c2a4a),
                        Color(0xFFffd8be),
                        Color(0xFFffeedd),
                        Color(0xFFf8f7ff),
                        Color(0xFFb8b8ff),
                        Color(0xFF9381ff),
                        Color(0xFF8a716a),
                        Color(0xFFc2b8b2),
                        Color(0xFF197bbd),
                        Color(0xFF125e8a),
                        Color(0xFF204b57),
                        Color(0xFF4f518c),
                        Color(0xFF2c2a4a),
                        Color(0xFF9381ff),
                        Color(0xFF907ad6),
                        Color(0xFF4f518c),
                        Color(0xFF2c2a4a),
                    )
                )
            ) { pieIndex ->
                Icon(
                    imageVector = iconList[pieIndex],
                    tint = Color.White,
                    contentDescription = null,
                )

                val angle = (360 / textList.size) * pieIndex
                val angleInRadians = Math.toRadians(angle.toDouble())
                val offsetModifier = Modifier.offset(
                    x = (30).dp * cos(angleInRadians).toFloat(),
                    y = (30).dp * sin(angleInRadians).toFloat()
                )
                Text(
                    text = textList[pieIndex],
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .then(offsetModifier), // Apply the offset modifier
                    color = Color(0XFFA2678A), // Set text color to red
                )

            }


            val context = LocalContext.current
            LaunchedEffect( selectedChoice.value, pausedState) {
                showMessage(context, message = selectedChoice!!.value, view)
            }



        }


        Spacer(modifier = Modifier.height(32.dp))



    }


    @Composable
    fun lottieCatAnimation() {
        val composition by rememberLottieComposition(spec =  LottieCompositionSpec.Url("https://lottie.host/dd17a01e-7918-4b5c-bf8a-0db8ddfadc47/a2dgeaC9fV.lottie"))
        LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever )
    }

    private fun showMessage(context: Context, message: String, view: View){
        if(message == getString(R.string.spin_wheel_notification_text)){
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)
            builder.setNeutralButton(getString(R.string.OK_text), null)
            val dialog = builder.create()
            dialog.show()
        } else {
            val bottomSheetDialog = BottomSheetDialog(context)
            val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null)
            val messageTextView = bottomSheetView.findViewById<TextView>(R.id.messageTextView)
            messageTextView.text = message
            shareScreenshotButton = bottomSheetView.findViewById<Button>(R.id.shareButton)
            shareScreenshotButton.setOnClickListener(View.OnClickListener { takeScreenShot(view) })
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

    }

    private fun takeScreenShot(view: View) {
        val date = Date()
        val format: CharSequence = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date)
        try {
            val mainDir = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FilShare"
            )
            if (!mainDir.exists()) {
                val mkdir: Boolean = mainDir.mkdir()
            }
            val path: String = mainDir.toString() + "/" + "TrendOceans" + "-" + format + ".jpeg"
            view.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            val imageFile = File(path)
            val fileOutputStream: FileOutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            shareScreenShot(imageFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //Share ScreenShot
    private fun shareScreenShot(imageFile: File) {
        val uri = FileProvider.getUriForFile(
            this,
            "nz.ac.uclive.jba214.wheeladeal.MainActivity.provider",
            imageFile
        )
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_TEXT, "Download Application from Instagram")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        try {
            this.startActivity(Intent.createChooser(intent, "Share With"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show()
        }
    }

}

