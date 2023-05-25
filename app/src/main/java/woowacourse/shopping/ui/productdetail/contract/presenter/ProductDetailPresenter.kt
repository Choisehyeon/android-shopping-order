package woowacourse.shopping.ui.productdetail.contract.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.CartProduct
import com.example.domain.repository.CartRepository
import com.example.domain.repository.ProductDetailRepository
import com.example.domain.repository.RecentRepository
import woowacourse.shopping.mapper.toUIModel
import woowacourse.shopping.model.ProductUIModel
import woowacourse.shopping.ui.productdetail.contract.ProductDetailContract

class ProductDetailPresenter(
    private val view: ProductDetailContract.View,
    private val id: Long,
    private val productDetailRepository: ProductDetailRepository,
    private val cartRepository: CartRepository,
    private val recentRepository: RecentRepository,
) : ProductDetailContract.Presenter {
    private val _count: MutableLiveData<Int> = MutableLiveData(1)
    val count: LiveData<Int> get() = _count
    private var latestProduct: ProductUIModel? = null

    init {
        setUpProductDetail()
        setLatestProduct()
        addProductToRecent()
    }

    override fun setUpProductDetail() {
        productDetailRepository.getById(id, onSuccess = { product ->
            view.setProductDetail(product.toUIModel())
        }, onFailure = { exception ->
            Log.e("ProductDetailPresenter", exception.message.toString())
        })
    }

    override fun addProductToCart() {
        count.value?.let {
            productDetailRepository.getById(id, onSuccess = { product ->
                cartRepository.insert(CartProduct(product, it, true))
            }, onFailure = { exception ->
                Log.e("ProductDetailPresenter", exception.message.toString())
            })
        }
    }

    override fun addProductToRecent() {
        productDetailRepository.getById(id, onSuccess = { product ->
            recentRepository.findById(product.id)?.let {
                recentRepository.delete(it.id)
            }
            recentRepository.insert(product)
        }, onFailure = { exception ->
            Log.e("ProductDetailPresenter", exception.message.toString())
        })
    }

    override fun setProductCountDialog() {
        productDetailRepository.getById(id, onSuccess = { product ->
            view.showProductCountDialog(product.toUIModel())
        }, onFailure = { exception ->
            Log.e("ProductDetailPresenter", exception.message.toString())
        })
    }

    override fun setLatestProduct() {
        recentRepository.getRecent(1).firstOrNull()?.let { recent ->
            latestProduct = recent.toUIModel()
            view.showLatestProduct(latestProduct!!)
        }
    }

    override fun clickLatestProduct() {
        latestProduct?.let { view.navigateToDetail(it.id) }
    }

    override fun addProductCount(id: Long) {
        _count.value = _count.value?.plus(1)
    }

    override fun subtractProductCount(id: Long) {
        _count.value = _count.value?.minus(1)
    }
}
