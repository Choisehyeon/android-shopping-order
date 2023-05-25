package woowacourse.shopping.data.datasource.remote.product

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

class ProductDataSourceImpl : ProductDataSource {
    private val okHttpClient = OkHttpClient()

    override fun getAllProducts(): Call {
        val request = Request.Builder().url("http://3.34.134.115:8080/products").build()
        return okHttpClient.newCall(request)
    }

    companion object {
        private const val GET_PRODUCT_PATH = "/products"
    }
}
