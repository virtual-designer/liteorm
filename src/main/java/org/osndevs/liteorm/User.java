package org.osndevs.liteorm;

import org.osndevs.liteorm.models.ActiveRecord;
import org.osndevs.liteorm.models.Column;
import org.osndevs.liteorm.models.Model;
import org.osndevs.liteorm.models.PrimaryKey;

@Model
public class User extends ActiveRecord {
    @Column
    @PrimaryKey
    private long id;

    @Column
    private String name;

    @Column
    private String username;

    @Column
    private String password;

    @Column(actualName = "is_admin")
    private boolean isAdministrator;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean administrator) {
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
