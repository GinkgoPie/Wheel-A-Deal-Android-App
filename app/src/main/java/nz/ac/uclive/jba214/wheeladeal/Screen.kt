package nz.ac.uclive.jba214.wheeladeal

sealed class Screen(val route: String) {
    object MainScreen :  Screen("main_screen")
    object WheelNameScreen : Screen("wheel_name_screen")
    object WheelSpinScreen: Screen("wheel_spin_screen")
    object AddChoicesScreen: Screen("add_choices_screen")


}
