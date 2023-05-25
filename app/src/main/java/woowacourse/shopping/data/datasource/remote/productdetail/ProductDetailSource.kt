package woowacourse.shopping.data.datasource.remote.productdetail

import okhttp3.Call

interface ProductDetailSource {

    fun getById(id: Long): Call
}
