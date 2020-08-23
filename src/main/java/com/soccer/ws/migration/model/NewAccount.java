package com.soccer.ws.migration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soccer.ws.data.SocialMediaEnum;
import com.soccer.ws.model.Role;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "account")
public class NewAccount extends NewBaseClass implements Comparable<com.soccer.ws.model.Account> {
    private String firstName;
    private String lastName;
    private String username;
    private DateTime passwordLastSet = DateTime.now();
    private Role role;
    private SocialMediaEnum signInProvider;
    private String pwdRecovery;
    private boolean active = false;
    private NewAccountSettings newAccountSettings;

    private NewAccountProfile newAccountProfile;

    private String password;

    public NewAccount(String firstName, String lastName, String username) {
        //Default values: null as password
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public NewAccount() {
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

    @NotNull
    @JsonIgnore
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    public NewAccountSettings getAccountSettings() {
        if (newAccountSettings == null) newAccountSettings = new NewAccountSettings();
        return newAccountSettings;
    }

    public void setAccountSettings(NewAccountSettings newAccountSettings) {
        this.newAccountSettings = newAccountSettings;
    }

    @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    @JoinColumn(name = "profile_id", insertable = true, updatable = true, nullable = true)
    public NewAccountProfile getAccountProfile() {
        if (newAccountProfile == null) newAccountProfile = new NewAccountProfile();
        return newAccountProfile;
    }

    public void setAccountProfile(NewAccountProfile newAccountProfile) {
        this.newAccountProfile = newAccountProfile;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NewAccount)) return false;
        final NewAccount other = (NewAccount) obj;
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
