package com.oam.model;

import com.oam.model.converter.ApartmentCodeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID id;

    @Column(name = "number")
    private String number;

    @Column(name = "number_of_persons")
    private Integer numberOfPersons;

    @Column(name = "code")
    @Convert(converter = ApartmentCodeConverter.class)
    private String code;

    @Column(name = "surface")
    private Double surface;

    @OneToMany(mappedBy = "apartment",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<AssociationMember> associationMembers = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "association_id", referencedColumnName = "id")
    private Association association;

    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<InvoiceDistribution> invoiceDistributions = new ArrayList<>();

    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Index> indexes = new ArrayList<>();
}
