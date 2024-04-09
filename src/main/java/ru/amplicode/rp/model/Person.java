package ru.amplicode.rp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public abstract class Person extends BaseDocument {
    @Field("firstName")
    private String firstName;

    @Field("lastName")
    private String lastName;

}