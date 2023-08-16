package com.shopme.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Currency;

public interface CurrencyRepositroy extends CrudRepository<Currency, Integer> {

       public List<Currency> findAllByOrderByNameAsc();

}
