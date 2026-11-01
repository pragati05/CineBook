package com.cinebook.catalog.theatre;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("theatres")
public record Theatre(
    @Id String id, String cityId, String name, String address, List<Screen> screens) {}
