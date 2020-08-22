package com.soccer.ws.model.migration;

import com.soccer.ws.model.Account;
import com.soccer.ws.model.News;

/**
 * User: Tom De Dobbeleer
 * Date: 12/20/13
 * Time: 8:48 AM
 * Remarks: none
 */
public class ReportItem extends News {
    public ReportItem(String header, String content, Account account) {
        super(header, content, account);
    }
}
