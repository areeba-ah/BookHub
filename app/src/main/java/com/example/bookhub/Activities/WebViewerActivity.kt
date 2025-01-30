package com.example.bookhub.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.bookhub.R


class WebViewerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Retrieve the PDF URI from the Intent
        val pdfUriString = intent.getStringExtra("Web_URI")
        val url = Uri.parse(pdfUriString)

        val finalUrl = "${url}&printsec=frontcover&source=gbs_ge_summary_r&cad=0#v=onepage&q&f=true"

        setContent {
            WebViewScreen(finalUrl, ::shareLink)
        }
    }

    private fun shareLink(url: String) {
        // Check if the URL is empty or invalid
        if (url.isEmpty()) {
            Toast.makeText(this, "URL is empty or invalid", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare the share intent with the URL
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Check out this link: $url")  // The message to share
        }

        // Use the Intent.createChooser method to show the share options dialog
        try {
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        } catch (e: Exception) {
            // Log the exception to understand the error
            e.printStackTrace()
            Toast.makeText(this, "Error while sharing the link", Toast.LENGTH_SHORT).show()
        }
    }
}


@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(url: String, shareLink: (String) -> Unit) {
    val context = LocalContext.current

    // Show a Toast if URL is null or empty
    if (url.isEmpty()) {
        Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show()
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Web Viewer") }, // You can change this title as per your requirement
                navigationIcon = {
                    IconButton(onClick = {
                        (context as? Activity)?.finish() // This will finish the current activity
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // Restart the activity to refresh
                        val intent = (context as ComponentActivity).intent
                        context.finish() // Finish the current activity
                        context.startActivity(intent) // Restart the activity
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }

                    IconButton(onClick = {
                        shareLink(url)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_share_24),
                            contentDescription = "Save Book"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            AndroidView(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                factory = {
                    WebView(context).apply {
                        // Set up the WebView with required settings
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                                // Handle the URL loading logic if needed
                                Log.d("WebView", "Loading URL: ${request.url}")
                                return super.shouldOverrideUrlLoading(view, request)
                            }
                        }

                        settings.apply {
                            javaScriptEnabled = true // Enable JavaScript
                            domStorageEnabled = true // Enable DOM storage
                            useWideViewPort = true // Enable wide viewport for better scaling
                            loadWithOverviewMode = true // Scale the content to fit the screen
                            setSupportZoom(true) // Enable zoom controls
                            builtInZoomControls = true // Display zoom controls
                            displayZoomControls = false // Hide default zoom controls
                            allowFileAccess = true // Allow access to files if needed
                            javaScriptCanOpenWindowsAutomatically = true // Allow JavaScript to open popups
                        }

                        loadUrl(url) // Load the PDF URL
                    }
                }
            )
        }
    )
}




