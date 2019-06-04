package com.soccer.ws.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CSVServiceImpl implements CSVService {

    @Override
    public String write(final List<List<String>> lines) {
        if (lines != null) {
            return lines.stream()
                    .map(this::convertToCSV)
                    .collect(Collectors.joining("\n"));
        }
        return "";
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private String convertToCSV(List<String> data) {
        return data.stream()
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }
}
