package woowacourse.shopping.data.datasource.remote.producdetail

import woowacourse.shopping.data.datasource.remote.retrofit.RetrofitClient
import woowacourse.shopping.data.remote.request.ProductDTO
import java.util.concurrent.Executors

class ProductDetailRemoteSourceImpl : ProductDetailRemoteSource {
    override fun getById(id: Long): Result<ProductDTO> {
        val executor = Executors.newSingleThreadExecutor()
        val result = executor.submit<Result<ProductDTO>> {
            val response =
                RetrofitClient.getInstance().productDataService.getProductById(id).execute()
            if (response.isSuccessful) {
                Result.success(response.body() ?: throw IllegalArgumentException())
            } else {
                Result.failure(Throwable(response.message()))
            }
        }.get()
        executor.shutdown()
        return result
    }
}
