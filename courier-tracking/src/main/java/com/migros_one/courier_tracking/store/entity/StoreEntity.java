package com.migros_one.courier_tracking.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store",
    indexes = {
        @Index(name = "ix_store_name", columnList = "name")
    }
)
public class StoreEntity {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    private Double latitude;

    private Double longitude;
}
