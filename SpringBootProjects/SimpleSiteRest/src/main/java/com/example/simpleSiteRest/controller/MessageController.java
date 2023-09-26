package com.example.simpleSiteRest.controller;

import com.example.simpleSiteRest.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("message")
public class MessageController {


    private int counter = 4;

    private final List<Map<String, String>> messages = new ArrayList<>() {{
        add(new HashMap<>() {{
            put("id", "1");
            put("text", "first");
        }});
        add(new HashMap<>() {{
            put("id", "2");
            put("text", "second");
        }});
        add(new HashMap<>() {{
            put("id", "3");
            put("text", "third");
        }});
    }};


    @GetMapping
    public List<Map<String, String>> index() {
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id) {
        return getFromDB(id);
    }


    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(counter++));
        messages.add(message);
        return message;
    }


    @PutMapping("{id}")
    public Map<String, String> update(@RequestBody Map<String, String> message) {
        Map<String, String> fromDB = getFromDB(message.get("id"));
        fromDB.putAll(message);
        return fromDB;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Map<String, String> fromDB = getFromDB(id);
        messages.remove(fromDB);
    }


    private Map<String, String> getFromDB(String id) {
        return messages.stream()
                .filter(message -> message.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
