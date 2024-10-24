package com.kinal.libreria_online.model;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@NoArgsConstructor
@Document(collection = "roles")
public class Role {

    @Id
    private String id;
    private String roleName;

    public Role(String roleName){

        this.roleName = roleName;

    }

}
