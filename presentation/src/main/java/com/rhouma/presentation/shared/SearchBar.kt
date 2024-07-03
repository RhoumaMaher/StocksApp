package com.rhouma.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhouma.presentation.R
import com.rhouma.presentation.ui.theme.MarketPrimaryColor

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(MarketPrimaryColor, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                letterSpacing = 2.sp,
                fontFamily = FontFamily(Font(R.font.poppins))
            ),
            placeholder = {
                Text(
                    text = "Search by title",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily(Font(R.font.poppins))
                    )
                )
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }

}

@Preview(showBackground = true, backgroundColor = 0xFFEFEFEF)
@Composable
fun SearchBarPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        SearchBar(query = "") {

        }
    }
}