package ru.amplicode.rp.api.filter;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public record PetFilter(
        String q,
        String ownerId,
        LocalDate birthDateGreaterThan,
        LocalDate birthDateLessThan
) {

    public Criteria toCriteria() {
        var criteria = new Criteria();
        if (StringUtils.hasText(q)) {
//            LookupOperation lookupOperation = LookupOperation.newLookup()
//                    .from("petType")
//                    .localField("typeId")
//                    .foreignField("_id")
//                    .as("types");

            criteria.orOperator(
                    where("name").regex(containsPattern(q))
            );
        }
        if (ownerId != null) {
            criteria.and("ownerId").is(ownerId);
        }
        if (birthDateGreaterThan != null || birthDateLessThan != null) {
            criteria.and("birthDate");
            if (birthDateGreaterThan != null) {
                criteria.gte(birthDateGreaterThan);
            }
            if (birthDateLessThan != null) {
                criteria.lte(birthDateLessThan);
            }
        }
        return criteria;
    }

    private Pattern containsPattern(String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
}
