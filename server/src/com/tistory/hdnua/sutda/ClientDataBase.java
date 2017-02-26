package com.tistory.hdnua.sutda;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 클라이언트 데이터베이스입니다.
 */
public class ClientDataBase implements Iterable<ClientInfo> {
    //region 필드를 정의합니다.
    private HashSet<ClientInfo> clientSet;

    public String toString() {
        return clientSet.toString();
    }

    //endregion



    //region 생성자를 정의합니다.
    /**
     * 클라이언트 데이터베이스입니다.
     */
    public ClientDataBase() {
        clientSet = new HashSet<>();
    }

    //endregion



    //region 메서드를 정의합니다.
    /**
     * 클라이언트를 데이터베이스에 추가합니다.
     */
    public void add(ClientInfo client) {
        clientSet.add(client);
    }
    /**
     * 데이터베이스에서 클라이언트를 제거합니다.
     */
    public void remove(ClientInfo client) {
        clientSet.remove(client);
    }
    /**
     * 데이터베이스에 클라이언트가 존재하는지 확인합니다.
     */
    public boolean exist(ClientInfo client) {
        return clientSet.contains(client);
    }

    public int size() {
        return clientSet.size();
    }

    @Override
    public Iterator iterator() {
        return clientSet.iterator();
    }

    public void print() {
        System.out.printf("(%d) %s \n", size(), this);
    }

    //endregion
}
