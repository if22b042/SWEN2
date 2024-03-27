        package com.example.jpademo.persistence.entities;

        import jakarta.persistence.*;
        import lombok.*;

        @Entity
        @Table(name = "ADDRESS")
        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public class AddressEntity {

            @Id
            @GeneratedValue(strategy = GenerationType.AUTO)
            private Long id;

            private int postcode;
            private String city;
            private String street;

            @ManyToOne(cascade = CascadeType.PERSIST)
            @JoinColumn(name = "FK_PERSON")
            private PersonEntity person;

        }
