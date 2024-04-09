package ru.amplicode.rp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public abstract class NamedDocument extends BaseDocument {
    @Field("name")
    private String name;

}