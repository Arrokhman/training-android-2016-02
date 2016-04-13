package com.artivisi.pembayaran.service;

import com.artivisi.pembayaran.dao.AntrianGcmDao;
import com.artivisi.pembayaran.dao.ProdukDao;
import com.artivisi.pembayaran.dao.UserDao;
import com.artivisi.pembayaran.dao.UserGcmTokenDao;
import com.artivisi.pembayaran.entity.Produk;
import com.artivisi.pembayaran.entity.User;
import com.artivisi.pembayaran.entity.UserGcmToken;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class PembayaranService {
    @Autowired private ProdukDao produkDao;
    @Autowired private GcmService gcmService;
    @Autowired private UserDao userDao;
    @Autowired private UserGcmTokenDao userGcmTokenDao;
    
    public void simpan(Produk p){
        produkDao.save(p);
        Map<String, Object> data = new HashMap<>();
        data.put("action", "update");
        data.put("content", "produk");
        gcmService.kirimGcmMessage("/topics/produk", data);
    }

    public Page<Produk> semuaProduk(Pageable page) {
        return produkDao.findAll(page);
    }

    public void updateToken(String email, String token) {
        User u = userDao.findByEmail(email);
        if(u != null){
            UserGcmToken userToken = new UserGcmToken();
            userToken.setUser(u);
            userToken.setGcmToken(token);
            userGcmTokenDao.save(userToken);
        }
    }
}
