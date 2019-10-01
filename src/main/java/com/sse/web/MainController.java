package com.sse.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/listen-sse")
    public SseEmitter sseEmitter() {
        SseEmitter emitter = new SseEmitter(0L);
        emitter.onError(e -> System.out.println("OnError"));

        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                    emitter.send("Hello");
                } catch (IOException | InterruptedException e) {
                    System.out.println("Exception");
                    break;
                }
            }
        });

        service.shutdown();
        return emitter;
    }

}
