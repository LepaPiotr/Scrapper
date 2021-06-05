package com.MongoApp.app.service;

import com.MongoApp.app.entity.Product;
import com.MongoApp.app.entity.ProductPriceList;
import com.MongoApp.app.mongoRepos.ProductPriceListRepository;
import com.MongoApp.app.mongoRepos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class ProductService {
    @Autowired
    ProductPriceListRepository productPriceListRepository;

    @Autowired
    ProductRepository productRepository;

//dodaje pozycje listy cen produtu weryfikująć czy znajduje się już w bazie jeśli tak i cena nie została zmieniona jest aktualizowana data, jeśli nie dodaje nowy rekord do bazy
    public void addWitchCheck(ProductPriceList productPriceList, Product product, Date newDate){
        ProductPriceList lastProd = productPriceListRepository.findTop1ByNameAndShopIgnoreCaseOrderByDateOfAddDesc(productPriceList.getName(), productPriceList.getShop());
        Calendar calLastProd = new GregorianCalendar();
        Calendar calNewProd = new GregorianCalendar();
        calNewProd.setTime(newDate);
        if(lastProd != null) {
            calLastProd.setTime(lastProd.getDateOfAdd());
            if (!lastProd.getPrice().equals(productPriceList.getPrice()) ) {
                productPriceListRepository.save(productPriceList);
                product.setDateOfActualization(newDate);
                product.setLastKnowPrice(productPriceList.getPrice());
                productRepository.save(product);
            }  if (lastProd.getPrice().equals(productPriceList.getPrice()) &&
                    ((calLastProd.get(Calendar.YEAR) != calNewProd.get(Calendar.YEAR))
                    || (calLastProd.get(Calendar.DAY_OF_YEAR) != calNewProd.get(Calendar.DAY_OF_YEAR)))) {
                lastProd.setDateOfAdd(newDate);
                productPriceListRepository.save(lastProd);
                product.setDateOfActualization(newDate);
                productRepository.save(product);
            }
        }
        else{
            productPriceListRepository.save(productPriceList);
        }
    }



}
