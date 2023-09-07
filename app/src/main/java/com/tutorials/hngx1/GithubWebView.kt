package com.tutorials.hngx1

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tutorials.hngx1.databinding.FragmentGithubWebViewBinding


class GithubWebView : Fragment() {
    private lateinit var binding: FragmentGithubWebViewBinding
    private val gitUrl by lazy { requireContext().getString(R.string.git_link) }
    private var isChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGithubWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpWebView()
        if (isChecked) {
            binding.webView.settings.userAgentString = DESKTOP_USER_AGENT//"eliboM"
        } else {
            binding.webView.settings.userAgentString = MOBILE_USER_AGENT//"Mobile"
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.moreBtn.setOnClickListener {
            setUpPopUpMenu(it)
        }



    }

    private fun setUpWebView() {
        val chromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progressBar.progress = newProgress
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                binding.resumeText.text = title ?: "Unable to get title"
            }
        }
        val webClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.isVisible = false
                binding.resumeText.text = view?.title ?: "Unable to get title"
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding.progressBar.isVisible = true
                binding.resumeText.text = url ?: "Unable to get title"
            }


        }

        binding.webView.apply {
            webViewClient = webClient
            // Enable responsive layout
            settings.useWideViewPort = true
            // Zoom out if the content width is greater than the width of the viewport
            settings.loadWithOverviewMode = true


            loadUrl(gitUrl)
            webChromeClient = chromeClient
            settings.javaScriptEnabled = true
            canGoBack()


            setOnKeyListener { view, i, keyEvent ->
                val webView = binding.webView
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.action == MotionEvent.ACTION_UP && webView.canGoBack()) {
                    webView.goBack()
                    return@setOnKeyListener true
                } else return@setOnKeyListener false
            }
        }
    }

    private fun setUpPopUpMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.pop_delete_menu, popupMenu.menu)
        val viewMode = popupMenu.menu.findItem(R.id.view_option)
        viewMode.isChecked = isChecked
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.reload_option -> {
                    binding.webView.stopLoading()
                    binding.webView.reload()
                    true
                }
                R.id.view_option -> {
                    isChecked = !isChecked
                    true
                }
                R.id.chrome_option -> {
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(gitUrl)
                        startActivity(this)
                    }
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        popupMenu.show()
    }

}