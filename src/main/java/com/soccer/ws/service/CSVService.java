package com.soccer.ws.service;

import java.util.List;

public interface CSVService {
    String write(List<List<String>> lines);
}
