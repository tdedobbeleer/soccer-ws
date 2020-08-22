package com.soccer.ws.model.migration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soccer.ws.data.SocialMediaEnum;
import com.soccer.ws.model.AccountProfile;
import com.soccer.ws.model.AccountSettings;
import com.soccer.ws.model.BaseClass;
import com.soccer.ws.model.Role;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "account")
public class Account extends BaseClass implements Comparable<com.soccer.ws.model.Account> {
    private String firstName;
    private String lastName;
    private String username;
    private DateTime passwordLastSet = DateTime.now();
    private Role role;
    private SocialMediaEnum signInProvider;
    private String pwdRecovery;
    private boolean active = false;
    private AccountSettings accountSettings;

    private AccountProfile accountProfile;

    public Account(String firstName, String lastName, String username) {
        //Default values: null as password
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public Account() {
    }

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    @Column(name = "sign_in_provider", length = 20)
    public SocialMediaEnum getSignInProvider() {
        return signInProvider;
    }

    public void setSignInProvider(SocialMediaEnum signInProvider) {
        this.signInProvider = signInProvider;
    }

    @NotNull
    @JsonIgnore
    @Size(min = 1, max = 50)
    @Column(name = "username")
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull
    @JsonIgnore
    @Size(min = 1, max = 50)
    @Column(name = "firstname")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonIgnore
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "lastname")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Transient
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    @Transient
    public String toString() {
        return firstName + " " + lastName;
    }

    @Column(name = "role")
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    public Role getRole() {
        if (role == null)
            return Role.USER;
        else
            return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @NotNull
    @JsonIgnore
    @Column(name = "activated")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Column(name = "password_recovery")
    @JsonIgnore
    public String getPwdRecovery() {
        return pwdRecovery;
    }

    public void setPwdRecovery(String pwdRecovery) {
        this.pwdRecovery = pwdRecovery;
    }

    @JsonIgnore
    @Embedded
    public AccountSettings getAccountSettings() {
        if (accountSettings == null) accountSettings = new AccountSettings();
        return accountSettings;
    }

    public void setAccountSettings(AccountSettings accountSettings) {
        this.accountSettings = accountSettings;
    }

    @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    @JoinColumn(name = "profile_id", insertable = true, updatable = true, nullable = true)
    public AccountProfile getAccountProfile() {
        if (accountProfile == null) accountProfile = new AccountProfile();
        return accountProfile;
    }

    public void setAccountProfile(AccountProfile accountProfile) {
        this.accountProfile = accountProfile;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof com.soccer.ws.model.Account)) return false;
        final com.soccer.ws.model.Account other = (com.soccer.ws.model.Account) obj;
        return id != null && other.id != null && id.equals(other.id);
    }

    @Override
    public int compareTo(com.soccer.ws.model.Account o) {
        return this.getFullName().compareTo(o.getFullName());
    }

    @Column(name = "password_last_set")
    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getPasswordLastSet() {
        return passwordLastSet;
    }

    public void setPasswordLastSet(DateTime passwordLastSet) {
        this.passwordLastSet = passwordLastSet;
    }

    public static class Builder {

        private String username;

        private String firstName;

        private String lastName;

        private Role role = Role.USER;

        private boolean active = false;

        private SocialMediaEnum socialSignInProvider;

        public Builder() {
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder signInProvider(SocialMediaEnum socialSignInProvider) {
            this.socialSignInProvider = socialSignInProvider;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public com.soccer.ws.model.Account build() {
            com.soccer.ws.model.Account account = new com.soccer.ws.model.Account(firstName, lastName, username);
            account.setSignInProvider(socialSignInProvider);
            account.setRole(role);
            account.setActive(active);
            return account;
        }
    }
}
