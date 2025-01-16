package com.migros_one.courier_tracking.courier.entity;

import com.migros_one.courier_tracking.store.entity.StoreEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courier_store_entrance_log",
    indexes = {
        @Index(name = "ix_courier_store_entrance", columnList = "courier_id, store_name, entrance"),
        @Index(name = "ix_courier_store", columnList = "courier_id, store_name"),
    }
)
public class CourierStoreEntranceLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", nullable = false, foreignKey = @ForeignKey(name = "FK_COURIER_ID"))
    private CourierEntity courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_name", referencedColumnName = "name", nullable = false, foreignKey = @ForeignKey(name = "FK_STORE_NAME"))
    private StoreEntity store;

    @NotBlank
    @Column(name = "entrance", nullable = false)
    private LocalDateTime entrance;

}
