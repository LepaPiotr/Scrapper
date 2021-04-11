package com.MongoApp.app.service;

import com.MongoApp.app.entity.Product;
import com.MongoApp.app.mongoRepos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<Product> doSomeActions(){
        productRepository.deleteAll();
//        productRepository.save(new Product("Redmi 7", "Xiaomi", new Date(), 799.99f));
//        productRepository.save(new Product("S21", "Samsung", new Date() , 5699.99f));
        return null;
    }

    public void addWitchCheck(Product product){
        Product lastProd = productRepository.findTop1ByNameAndShopIgnoreCaseOrderByDateOfAddDesc(product.getName(), product.getShop());
        Calendar calLastProd = new GregorianCalendar();
        Calendar calNewProd = new GregorianCalendar();
        if(lastProd != null) {
            if (!lastProd.getPrice().equals(product.getPrice()) ) {
                productRepository.save(product);
            } else if ((calLastProd.get(Calendar.YEAR) != calNewProd.get(Calendar.YEAR))
                    || (calLastProd.get(Calendar.DAY_OF_YEAR) != calNewProd.get(Calendar.DAY_OF_YEAR))) {
                lastProd.setDateOfAdd(new Date());
                productRepository.save(lastProd);
            }
        }
        else{
            productRepository.save(product);
        }
    }



}
