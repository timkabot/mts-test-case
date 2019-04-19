package com.example.mtstestcase

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient

import org.jsoup.Jsoup
import org.jsoup.select.Elements

import java.util.ArrayList

class ListOfStreamActivity : AppCompatActivity() {
    val url = Constants.WEB_SITE_URL
    lateinit var browser: WebView
    lateinit var streams: MutableList<StreamInfo>
    lateinit var rv: RecyclerView
    lateinit var adapter: ListOfStreamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_of_streams)
        browser = findViewById(R.id.webView)
        rv = findViewById(R.id.rv)
        streams = ArrayList()

        browser.settings.javaScriptEnabled = true
        browser.addJavascriptInterface(InfoExtractInterface(), Constants.HTMLOUT)
        browser.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                browser.loadUrl(Constants.JS_TEMPLATE)
            }
        }
        browser.loadUrl(url)
    }

    internal inner class InfoExtractInterface {
        @JavascriptInterface
        fun processHTML(html: String) {
            val rows = Jsoup.parse(html).select(Constants.ROWS_FILTER)
            for (i in 1 until rows.size) { //first row is the col names so skip it.
                val row = rows[i]
                val cols = row.select(Constants.TD)
                val hrefs = cols[8].select(Constants.LINK)
                for (j in hrefs) {
                    val href = j.attr(Constants.HREF)
                    if (j.text() == Constants.DASH)
                        addToList(cols, href)
                }
            }
            if (streams.size > 0)
                runOnUiThread { this@ListOfStreamActivity.initRecyclerView() }
        }
    }

    fun initRecyclerView() {
        rv.layoutManager = GridLayoutManager(this, 2)
        adapter = ListOfStreamAdapter(streams)
        adapter.setOnItemClickListener { url ->
            val intent = Intent(this@ListOfStreamActivity, MainActivity::class.java)
            intent.putExtra(Constants.URL, url)
            startActivity(intent)
        }
        rv.adapter = adapter
        (rv.adapter as ListOfStreamAdapter).notifyDataSetChanged()

    }

    internal fun addToList(stream: Elements, link: String) {
        val streamInfo = StreamInfo().apply {
            content = stream[2].text()
            duration = stream[5].text()
            resolution = stream[6].text()
            this.link = link
        }
        streams.add(streamInfo)
    }
}

