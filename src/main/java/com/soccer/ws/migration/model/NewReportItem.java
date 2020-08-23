package com.soccer.ws.migration.model;

/**
 * User: Tom De Dobbeleer
 * Date: 12/20/13
 * Time: 8:48 AM
 * Remarks: none
 */
public class NewReportItem extends NewNews {
    public NewReportItem(String header, String content, NewAccount account) {
        super(header, content, account);
    }
}
