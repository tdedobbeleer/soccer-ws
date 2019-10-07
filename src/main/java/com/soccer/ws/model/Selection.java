package com.soccer.ws.model;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by u0090265 on 10/1/14.
 */
@Entity
@Table(name = "selections", uniqueConstraints = @UniqueConstraint(columnNames = {"doodle_id", "account_id"}))
public class Selection extends BaseClass {
    private Account account;
    private boolean selected;
    private Map<String, Short> data;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Column(name = "selected")
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "selection_data")
    @MapKeyColumn(name = "description")
    @Column(name="VALUE")

    @ElementCollection
    @JoinTable(name="selection_data", joinColumns=@JoinColumn(name="id"))
    @MapKeyColumn (name="selection_id")
    @Column(name="VALUE")
    public Map<String, Short> getData() {
        return data;
    }

    public void setData(Map<String, Short> data) {
        this.data = data;
    }
}
