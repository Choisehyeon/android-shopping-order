package woowacourse.shopping.data.repository

import android.os.Looper
import com.example.domain.model.Product
import com.example.domain.repository.ProductDetailRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import woowacourse.shopping.data.datasource.remote.productdetail.ProductDetailSource
import java.io.IOException

class ProductDetailRepositoryImpl(
    private val productDetailSource: ProductDetailSource,
) : ProductDetailRepository {

    override fun getById(id: Long, onSuccess: (Product) -> Unit, onFailure: (Exception) -> Unit) {
        productDetailSource.getById(id).enqueue(createResponseCallback(onSuccess, onFailure))
    }

    private fun createResponseCallback(
        onSuccess: (Product) -> Unit,
        onFailure: (Exception) -> Unit,
    ) = object : Callback {
        val handler = android.os.Handler(Looper.getMainLooper())
        override fun onResponse(call: Call, response: Response) {
            // 콜백 수정
            if (response.isSuccessful) {
                Thread {
                    val product = parseToObject(response.body?.string())
                    handler.post {
                        onSuccess(product)
                    }
                }.start()
                return
            }
            handler.post {
                onFailure(Exception("Response unsuccessful"))
            }
        }

        override fun onFailure(call: Call, e: IOException) {
            handler.post {
                onFailure(e)
            }
        }
    }

    private fun parseToObject(responseBody: String?): Product {
        val listType = object : TypeToken<Product>() {}.type
        return Gson().fromJson(responseBody, listType)
    }
}
