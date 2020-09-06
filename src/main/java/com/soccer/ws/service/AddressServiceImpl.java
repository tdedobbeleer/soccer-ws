package com.soccer.ws.service;

import com.google.common.collect.Lists;
import com.soccer.ws.model.Address;
import com.soccer.ws.persistence.AddressDao;
import com.soccer.ws.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by u0090265 on 9/11/14.
 */
@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    private final AddressDao addressDao;

    @Autowired
    public AddressServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    public List<Address> getAllAddresses() {
        return Lists.newArrayList(addressDao.findAll());
    }

    @Override
    public Address getAddressById(String id) {
        return addressDao.findById(GeneralUtils.convertToUUID(id)).orElse(null);
    }
}
