package com.parthjpatel.cleanarchitecture.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.parthjpatel.cleanarchitecture.presentation.ui.theme.CleanArchitectureTheme

// Load User List
// URL: https://jsonplaceholder.typicode.com/users

class MainActivity : ComponentActivity() {

    private val viewModel: ImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CleanArchitectureTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                var query by rememberSaveable { mutableStateOf("") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.LightGray,
                    topBar = {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .statusBarsPadding(),
                            placeholder = { Text("Search...") },
                            value = query,
                            onValueChange = {
                                query = it
                                viewModel.updateQuery(it)
                            },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedTextColor = Color.Transparent,
                            ),
                        )
                    }
                ) { innerPadding ->
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    if (uiState.error.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(uiState.error)
                        }
                    }

                    uiState.data?.let { list ->
                        LazyColumn(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            items(list) {
                                AsyncImage(
                                    model = it.imageUrl,
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .fillMaxWidth()
                                        .height(300.dp),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}


