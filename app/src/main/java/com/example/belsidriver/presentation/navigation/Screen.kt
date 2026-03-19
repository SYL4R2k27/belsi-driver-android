package com.example.belsidriver.presentation.navigation

sealed class Screen(val route: String) {
    // Auth
    data object Login : Screen("login")

    // Driver
    data object DriverHome : Screen("driver_home")
    data object ShiftCamera : Screen("shift_camera/{type}") {
        fun createRoute(type: String) = "shift_camera/$type"
    }
    data object ActiveRoute : Screen("active_route/{routeId}") {
        fun createRoute(routeId: String) = "active_route/$routeId"
    }
    data object PointCamera : Screen("point_camera/{routeId}/{pointId}/{action}") {
        fun createRoute(routeId: String, pointId: String, action: String) =
            "point_camera/$routeId/$pointId/$action"
    }
    data object DeliveryPointDetail : Screen("delivery_point/{routeId}/{pointId}") {
        fun createRoute(routeId: String, pointId: String) =
            "delivery_point/$routeId/$pointId"
    }

    // Logistician
    data object LogisticianHome : Screen("logistician_home")
    data object DriverDetail : Screen("driver_detail/{driverId}") {
        fun createRoute(driverId: String) = "driver_detail/$driverId"
    }
    data object CreateRoute : Screen("create_route/{driverId}") {
        fun createRoute(driverId: String) = "create_route/$driverId"
    }
    data object RouteDetail : Screen("route_detail/{routeId}") {
        fun createRoute(routeId: String) = "route_detail/$routeId"
    }
    data object PhotoViewer : Screen("photo_viewer/{photoUrl}") {
        fun createRoute(photoUrl: String) = "photo_viewer/$photoUrl"
    }
}
