package com.demo.jolindubbo.client.loadbalance;

import java.util.List;

public interface LoadBalance {
    String select(List<String> repos);
}