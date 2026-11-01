package com.cinebook.catalog.city;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("cities")
public class City{
    @Id
    String id;

    String name;
    String timezone;-
}