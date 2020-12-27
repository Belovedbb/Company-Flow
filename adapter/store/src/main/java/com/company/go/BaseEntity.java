package com.company.go;

import com.company.go.global.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    protected UserEntity companyRegisterer;

    @CreatedBy
    @Column(name = "registerer_name", updatable = false, nullable = false)
    protected String registererName;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", updatable = false, nullable = false)
    protected Date dateCreated;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_changed", nullable = false)
    protected Date lastChanged;

}
