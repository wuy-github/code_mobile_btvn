package com.example.tuan_4_bai_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tuan_4_bai_1.ui.theme.Tuan_4_bai_1Theme // Đảm bảo import theme của bạn khớp!

// ... Phần còn lại của code (data classes, ViewModel, Composables, MainActivity)

data class Student(
    val id: String,
    val name: String
)

data class Book(
    val id: String,
    val title: String
)

// --- ViewModel ---
class LibraryViewModel : ViewModel() {

    val students = mutableStateListOf(
        Student("s001", "Nguyen Van A"),
        Student("s002", "Nguyen Thi B"),
        Student("s003", "Nguyen Van C")
    )

    val availableBooks = mutableStateListOf(
        Book("b001", "Sách 01"),
        Book("b002", "Sách 02"),
        Book("b003", "Sách 03"),
        Book("b004", "Sách 04"),
        Book("b005", "Sách 05")
    )

    var currentStudent by mutableStateOf<Student?>(null)
        private set

    private val _borrowedBooksMap = mutableStateMapOf<String, MutableList<Book>>()
    val borrowedBooksMap: Map<String, List<Book>> get() = _borrowedBooksMap

    var showStudentSelectionDialog by mutableStateOf(false)
    var showAddBookDialog by mutableStateOf(false)
    val selectedBooksToAdd = mutableStateListOf<Book>()

    init {
        _borrowedBooksMap["s001"] = mutableListOf(availableBooks[0], availableBooks[1])
        _borrowedBooksMap["s002"] = mutableListOf(availableBooks[0])
        currentStudent = students.firstOrNull()
    }

    fun selectStudent(student: Student) {
        currentStudent = student
        showStudentSelectionDialog = false
    }

    fun addSelectedBooksToCurrentStudent() {
        currentStudent?.let { student ->
            val currentStudentBorrowedBooks = _borrowedBooksMap.getOrPut(student.id) { mutableListOf() }
            selectedBooksToAdd.forEach { newBook ->
                if (currentStudentBorrowedBooks.none { it.id == newBook.id }) {
                    currentStudentBorrowedBooks.add(newBook)
                }
            }
            currentStudentBorrowedBooks.sortBy { it.title }
        }
        selectedBooksToAdd.clear()
        showAddBookDialog = false
    }

    fun getBorrowedBooksForCurrentStudent(): List<Book> {
        return currentStudent?.let { _borrowedBooksMap[it.id] } ?: emptyList()
    }

    fun isBookBorrowedByCurrentStudent(book: Book): Boolean {
        return currentStudent?.let { student ->
            _borrowedBooksMap[student.id]?.any { it.id == book.id } ?: false
        } ?: false
    }
}


// --- Composable cho màn hình chính (Quản lý) ---
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManagementScreen(viewModel: LibraryViewModel = viewModel()) {
    val currentStudent = viewModel.currentStudent
    val borrowedBooks = viewModel.getBorrowedBooksForCurrentStudent()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Sinh viên", fontSize = 18.sp, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = currentStudent?.name ?: "Chưa chọn sinh viên",
                onValueChange = { /* Không cho phép sửa trực tiếp */ },
                readOnly = true,
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledBorderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                    disabledLabelColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                    disabledTextColor = MaterialTheme.colors.onSurface
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.showStudentSelectionDialog = true }) {
                Text("Thay đổi")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Danh sách sách", fontSize = 18.sp, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.LightGray),
            elevation = 2.dp
        ) {
            if (borrowedBooks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Bạn chưa mượn quyển sách nào\nNhấn 'Thêm' để bắt đầu hành trình đọc sách!",
                        style = MaterialTheme.typography.body1,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(borrowedBooks) { book ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = true,
                                onCheckedChange = { /* Không làm gì, chỉ để hiển thị */ },
                                enabled = false
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = book.title, fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (currentStudent != null) {
                    viewModel.showAddBookDialog = true
                    viewModel.selectedBooksToAdd.clear()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = currentStudent != null
        ) {
            Text("Thêm")
        }

        if (viewModel.showStudentSelectionDialog) {
            StudentSelectionDialog(
                students = viewModel.students,
                onStudentSelected = { student -> viewModel.selectStudent(student) },
                onDismiss = { viewModel.showStudentSelectionDialog = false }
            )
        }

        if (viewModel.showAddBookDialog) {
            AddBookDialog(
                availableBooks = viewModel.availableBooks,
                selectedBooks = viewModel.selectedBooksToAdd,
                onAddBooks = { viewModel.addSelectedBooksToCurrentStudent() },
                onDismiss = { viewModel.showAddBookDialog = false },
                isBookBorrowed = { book -> viewModel.isBookBorrowedByCurrentStudent(book) }
            )
        }
    }
}

// --- Composable cho Dialog chọn sinh viên ---
@Composable
fun StudentSelectionDialog(
    students: List<Student>,
    onStudentSelected: (Student) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chọn Sinh viên") },
        text = {
            LazyColumn {
                items(students) { student ->
                    Text(
                        text = student.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onStudentSelected(student)
                            }
                            .padding(vertical = 8.dp),
                        fontSize = 18.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}

// --- Composable cho Dialog thêm sách ---
@Composable
fun AddBookDialog(
    availableBooks: List<Book>,
    selectedBooks: MutableList<Book>,
    onAddBooks: () -> Unit,
    onDismiss: () -> Unit,
    isBookBorrowed: (Book) -> Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Sách") },
        text = {
            LazyColumn {
                val borrowedBooks = availableBooks.filter { isBookBorrowed(it) }
                val unborrowedBooks = availableBooks.filter { !isBookBorrowed(it) }

                items(borrowedBooks.sortedBy { it.title }) { book ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = true,
                            onCheckedChange = { /* Không làm gì */ },
                            enabled = false
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = book.title,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "(Đã mượn)",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                items(unborrowedBooks.sortedBy { it.title }) { book ->
                    val isChecked = remember { mutableStateOf(selectedBooks.contains(book)) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isChecked.value = !isChecked.value
                                if (isChecked.value) {
                                    selectedBooks.add(book)
                                } else {
                                    selectedBooks.remove(book)
                                }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked.value,
                            onCheckedChange = { newChecked ->
                                isChecked.value = newChecked
                                if (newChecked) {
                                    selectedBooks.add(book)
                                } else {
                                    selectedBooks.remove(book)
                                }
                            },
                            enabled = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = book.title,
                            fontSize = 16.sp,
                            color = Color.Unspecified
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onAddBooks,
                enabled = selectedBooks.isNotEmpty()
            ) {
                Text("Thêm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

// --- Composable cho màn hình DS Sách ---
@Composable
fun BookListScreen(viewModel: LibraryViewModel = viewModel()) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(text = "Tất cả Sách", fontSize = 24.sp, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(viewModel.availableBooks.sortedBy { it.title }) { book ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = 2.dp,
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = book.title, fontSize = 18.sp)
                }
            }
        }
    }
}

// --- Composable cho màn hình Sinh viên ---
@Composable
fun StudentListScreen(viewModel: LibraryViewModel = viewModel()) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(text = "Tất cả Sinh viên", fontSize = 24.sp, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(viewModel.students.sortedBy { it.name }) { student ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = 2.dp,
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = student.name, fontSize = 18.sp)
                }
            }
        }
    }
}

// --- Composable cho Bottom Navigation Bar ---
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Management,
        BottomNavItem.BookList,
        BottomNavItem.StudentList
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationRoute!!) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String) {
    object Management : BottomNavItem("management", Icons.Default.Home, "Quản lý")
    object BookList : BottomNavItem("booklist", Icons.Default.List, "DS Sách")
    object StudentList : BottomNavItem("studentlist", Icons.Default.Person, "Sinh viên")
}

// --- Main Activity ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Thay đổi tên Theme nếu cần (ví dụ: LibraryManagementAppTheme -> Tuan_4_bai_1Theme)
            Tuan_4_bai_1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        topBar = {
                            TopAppBar(title = { Text("Hệ thống Quản lý Thư viện") })
                        },
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }
                    ) { innerPadding ->
                        NavHost(navController, startDestination = BottomNavItem.Management.route, Modifier.padding(innerPadding)) {
                            composable(BottomNavItem.Management.route) { ManagementScreen() }
                            composable(BottomNavItem.BookList.route) { BookListScreen() }
                            composable(BottomNavItem.StudentList.route) { StudentListScreen() }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // Thay đổi tên Theme nếu cần
    Tuan_4_bai_1Theme {
        Text("Preview của ứng dụng")
    }
}
