package org.osndevs.liteorm;

import org.osndevs.liteorm.models.ActiveRecord;
import org.osndevs.liteorm.models.Column;
import org.osndevs.liteorm.models.Model;
import org.osndevs.liteorm.models.PrimaryKey;

@Model
public class User extends ActiveRecord {
    @Column
    @PrimaryKey
    private Long id;

    @Column
    private String name;

    @Column
    private String username;

    @Column
    private String password;

    @Column(actualName = "is_admin")
    private Boolean isAdministrator;

    public User(Long id, String name, String username, String password, Boolean isAdministrator) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdministrator = isAdministrator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(Boolean administrator) {
        isAdministrator = administrator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
