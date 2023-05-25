package woowacourse.shopping.data.datasource.remote.productdetail

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

class ProductDetailSourceImpl : ProductDetailSource {
    private val okHttpClient = OkHttpClient()
    override fun getById(id: Long): Call {
        val request = Request.Builder().url("http://3.34.134.115:8080/products/$id").build()
        return okHttpClient.newCall(request)
    }

    companion object {
        private const val GET_PRODUCT_PATH = "/products"
    }
}
