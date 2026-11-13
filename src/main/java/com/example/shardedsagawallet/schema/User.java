package com.example.shardedsagawallet.schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table (name = "user")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class User extends BaseEntity {
    
    private String name;

    private String email;
}
