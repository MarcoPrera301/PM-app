package plat.lab1.composelab4.lab7.data

data class UiState<T>(
    val isLoading: Boolean = true,
    val data: T? = null,
    val hasError: Boolean = false
)