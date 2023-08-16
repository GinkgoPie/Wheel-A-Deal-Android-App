package nz.ac.uclive.jba214.wheeladeal


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nz.ac.uclive.jba214.wheeladeal.ui.theme.WheelADealTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.commandiron.spin_wheel_compose.SpinWheel
import com.commandiron.spin_wheel_compose.SpinWheelDefaults
import com.commandiron.spin_wheel_compose.state.SpinWheelState
import com.commandiron.spin_wheel_compose.state.rememberSpinWheelState
import kotlinx.coroutines.launch
import kotlin.random.Random


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
    }

    @Composable
    fun MainScreen(navController: NavController) {
        WheelADealTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.fillMaxHeight()) {
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .padding(all = 25.dp)
                            .fillMaxWidth()
                    ) {
                        Greeting(modifier = Modifier.align(Alignment.Center))
                    }

                    Box(
                        modifier = Modifier
                            .weight(2f)
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
                text = "Wheel I",
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier
            )
            Text(
                text = "or",
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier
            )
            Text(
                text = "wheel I not?",
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
        ) {
            Text(
                text = label,
            )
        }
    }



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WheelNameScreen(navController: NavController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Enter a name for your wheel:",
                color = Color.Black,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            var textValue by remember { mutableStateOf(TextFieldValue()) }
            TextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text("Wheel Name") },
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate(Screen.AddChoicesScreen.route + "/${textValue.text}") },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Arrow Right")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddChoicesScreen(navController: NavController, wheelName: String?) {

        val choicesStringList: MutableList<String> = remember { mutableStateListOf() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (wheelName != null) {
                Text(
                    text = "Add choices for $wheelName",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            var textValue by remember { mutableStateOf(TextFieldValue()) }
            TextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text("Add a choice here...") },
                singleLine = true
            )

            Button(
                onClick = {
                    choicesStringList.add(textValue.text);
                    textValue = TextFieldValue("") },
                modifier = Modifier
                    .align(Alignment.Start)
            ) {
                Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "Add Circle")
            }

            Spacer(modifier = Modifier.height(16.dp))

            choicesStringList.forEach { choice ->
                var selected by remember { mutableStateOf(true) }
                InputChip(
                    selected = selected,
                    onClick = { selected = !selected
                              if (!selected) {
                                  choicesStringList.remove(choice)
                              }},
                    label = { Text(choice) },
                    avatar = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Choice chip",
                            Modifier.size(InputChipDefaults.AvatarSize)
                        )
                    }
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate(Screen.WheelSpinScreen.route + "/$wheelName" +"/${choicesStringList.joinToString(", ")}") },

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

                SpinningWheel(choicesList)

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




    @Composable
    fun SpinningWheel(items: List<String>) {

        val iconList = List(items.size) { Icons.Default.Star }


        val state = rememberSpinWheelState(
            pieCount = items.size,
            durationMillis = 20000,
            delayMillis = 200,
            rotationPerSecond = 2f,
            easing = LinearOutSlowInEasing,
            startDegree = 90f,
            resultDegree = 212f)

        val scope = rememberCoroutineScope()
        val chosen = Random(items.size)
        val textList by remember {
            mutableStateOf(items)}
        var selectedChoice: String? = null
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF3000B3)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SpinWheel(
                onClick = { scope.launch { state.animate {pieIndex ->
                    Log.d("Here", pieIndex.toString())
                    selectedChoice = "Selected choice: " + items[pieIndex]
                    Log.d("Here", selectedChoice!!)
                }}},
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
                        Color(0xFFdabfff),
                        Color(0xFF907ad6),
                        Color(0xFF4f518c),
                        Color(0xFF2c2a4a),
                        Color(0xFFdabfff),
                        Color(0xFF907ad6),
                        Color(0xFF4f518c),
                        Color(0xFF2c2a4a),
                        Color(0xFFdabfff),
                        Color(0xFF907ad6),
                        Color(0xFF4f518c),
                        Color(0xFF2c2a4a),
                        Color(0xFFdabfff),
                        Color(0xFF907ad6),
                        Color(0xFF4f518c),
                        Color(0xFF2c2a4a),
                    )
                )
            ){ pieIndex ->
                Icon(
                    imageVector = iconList[pieIndex],
                    tint = Color.White,
                    contentDescription = null
                )
                Modifier.align(Alignment.Center)
                Text (text = textList[pieIndex])

            }
        }

        selectedChoice?.let { Text(text = it) }
        Spacer(modifier = Modifier.height(32.dp))


    }

    @Composable
    fun lottieCatAnimation () {
        val composition by rememberLottieComposition(spec =  LottieCompositionSpec.Url("https://lottie.host/dd17a01e-7918-4b5c-bf8a-0db8ddfadc47/a2dgeaC9fV.lottie"))
        LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever )
    }


}


