package com.draculavenom.company;

import com.draculavenom.security.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "company_name",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id")
    }
)
public class CompanyName {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nameCompany", nullable = false)
    private String nameCompany;
    @Column(name = "company_number", nullable = true)  
    private String companyNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public CompanyName(String nameCompany, User user){
        this.nameCompany = nameCompany;
        this.user = user;
    }
}
