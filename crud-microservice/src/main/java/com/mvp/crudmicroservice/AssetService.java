package com.mvp.crudmicroservice;

public interface AssetService<E> {

    E buyAsset(E asset, Long userId, Long lots);
}
