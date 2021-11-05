package com.demo.jolindubbo.api.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class DemoBean implements Serializable, Callable {
    private String text;
    private Haha haha;

    public String getText() {
        return text;
    }

    public Haha getHaha() {
        return haha;
    }

    public void setHaha(Haha haha) {
        this.haha = haha;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "DemoBean{" +
                "text='" + text + '\'' +
                '}';
    }

    public static void main(String[] args) {

        DemoBean demoBean = new DemoBean();

        ExecutorService executor = new ThreadPoolExecutor(10, 100, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
        Future submit = executor.submit(demoBean);

        if (submit.isDone()) {
            System.out.println("莫了");
        }else {
            demoBean.setHaha(new Haha() {
                @Override
                public void say() {
                    System.out.println("哈哈哈哈");
                }
            });
        }

        System.out.println(111111);

        executor.shutdown();

        new Thread(()->{
            hassss();
        }).start();

        List<String> list = Arrays.asList("j", "a", "c");

        Collections.sort(list, (a, b) -> {
            return a.compareTo(b) > 1 ? 1 : -1;
        });

        System.out.println(list.toString());

        boolean equals = Objects.equals(null, null);
        System.out.println(equals);
    }

    public static void hassss(){
        System.out.println("sss");
    }

    @Override
    public Object call() throws Exception {

        Thread.sleep(5000);
        haha.say();
        return null;
    }
}
