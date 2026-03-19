package com.example.belsidriver.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.belsidriver.domain.model.UserRole
import com.example.belsidriver.presentation.auth.LoginScreen
import com.example.belsidriver.presentation.driver.home.DriverHomeScreen
import com.example.belsidriver.presentation.driver.route.ActiveRouteScreen
import com.example.belsidriver.presentation.driver.route.DeliveryPointDetailScreen
import com.example.belsidriver.presentation.driver.route.PointCameraScreen
import com.example.belsidriver.presentation.driver.shift.ShiftCameraScreen
import com.example.belsidriver.presentation.logistician.driver.DriverDetailScreen
import com.example.belsidriver.presentation.logistician.home.LogisticianHomeScreen
import com.example.belsidriver.presentation.logistician.route.CreateRouteScreen
import com.example.belsidriver.presentation.common.PhotoViewerScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Auth
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination = when (role) {
                        UserRole.DRIVER -> Screen.DriverHome.route
                        UserRole.LOGISTICIAN -> Screen.LogisticianHome.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Driver: Home
        composable(Screen.DriverHome.route) {
            DriverHomeScreen(
                onStartShift = {
                    navController.navigate(Screen.ShiftCamera.createRoute("start"))
                },
                onEndShift = { shiftId ->
                    navController.navigate(Screen.ShiftCamera.createRoute("end_$shiftId"))
                },
                onOpenRoute = { routeId ->
                    navController.navigate(Screen.ActiveRoute.createRoute(routeId))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Driver: Shift Camera
        composable(
            route = Screen.ShiftCamera.route,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val typeArg = backStackEntry.arguments?.getString("type") ?: "start"
            val isEnd = typeArg.startsWith("end_")
            val shiftId = if (isEnd) typeArg.removePrefix("end_") else null

            ShiftCameraScreen(
                type = if (isEnd) "end" else "start",
                shiftId = shiftId,
                onSuccess = {
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        // Driver: Active Route
        composable(
            route = Screen.ActiveRoute.route,
            arguments = listOf(navArgument("routeId") { type = NavType.StringType })
        ) {
            ActiveRouteScreen(
                onBack = { navController.popBackStack() },
                onPointArrival = { routeId, pointId ->
                    navController.navigate(
                        Screen.PointCamera.createRoute(routeId, pointId, "arrive")
                    )
                },
                onPointDelivery = { routeId, pointId ->
                    navController.navigate(
                        Screen.PointCamera.createRoute(routeId, pointId, "deliver")
                    )
                },
                onPointDetail = { routeId, pointId ->
                    navController.navigate(
                        Screen.DeliveryPointDetail.createRoute(routeId, pointId)
                    )
                }
            )
        }

        // Driver: Delivery Point Detail
        composable(
            route = Screen.DeliveryPointDetail.route,
            arguments = listOf(
                navArgument("routeId") { type = NavType.StringType },
                navArgument("pointId") { type = NavType.StringType }
            )
        ) {
            DeliveryPointDetailScreen(
                onBack = { navController.popBackStack() },
                onPhotoClick = { photoUrl ->
                    navController.navigate(
                        Screen.PhotoViewer.createRoute(photoUrl)
                    )
                }
            )
        }

        // Driver: Point Camera
        composable(
            route = Screen.PointCamera.route,
            arguments = listOf(
                navArgument("routeId") { type = NavType.StringType },
                navArgument("pointId") { type = NavType.StringType },
                navArgument("action") { type = NavType.StringType }
            )
        ) {
            PointCameraScreen(
                onSuccess = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        // Logistician: Home
        composable(Screen.LogisticianHome.route) {
            LogisticianHomeScreen(
                onDriverClick = { driverId ->
                    navController.navigate(Screen.DriverDetail.createRoute(driverId))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Logistician: Driver Detail
        composable(
            route = Screen.DriverDetail.route,
            arguments = listOf(navArgument("driverId") { type = NavType.StringType })
        ) {
            DriverDetailScreen(
                onBack = { navController.popBackStack() },
                onCreateRoute = { driverId ->
                    navController.navigate(Screen.CreateRoute.createRoute(driverId))
                },
                onRouteClick = { routeId ->
                    navController.navigate(Screen.RouteDetail.createRoute(routeId))
                }
            )
        }

        // Logistician: Create Route
        composable(
            route = Screen.CreateRoute.route,
            arguments = listOf(navArgument("driverId") { type = NavType.StringType })
        ) {
            CreateRouteScreen(
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        // Logistician: Route Detail (reusing ActiveRouteScreen in read-only mode)
        composable(
            route = Screen.RouteDetail.route,
            arguments = listOf(navArgument("routeId") { type = NavType.StringType })
        ) {
            ActiveRouteScreen(
                onBack = { navController.popBackStack() },
                onPointArrival = { _, _ -> },
                onPointDelivery = { _, _ -> },
                onPointDetail = { _, _ -> }
            )
        }

        // Photo Viewer
        composable(
            route = Screen.PhotoViewer.route,
            arguments = listOf(navArgument("photoUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val photoUrl = backStackEntry.arguments?.getString("photoUrl") ?: ""
            PhotoViewerScreen(
                photoUrl = photoUrl,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
