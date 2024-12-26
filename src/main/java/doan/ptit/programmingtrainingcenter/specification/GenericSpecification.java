package doan.ptit.programmingtrainingcenter.specification;

import jakarta.persistence.criteria.*;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;



public class GenericSpecification<T> implements Specification<T> {
    private final SearchCriteria criteria;

    public GenericSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder builder) {
        // Xử lý trường hợp join (khi key chứa dấu chấm)
        if (criteria.getKey().contains(".")) {
            String[] keys = criteria.getKey().split("\\.");
            Join<Object, Object> join = root.join(keys[0]);

            Path<?> path = join.get(keys[1]);
            Class<?> type = path.getJavaType();

            if (type == Boolean.class || type == boolean.class) {
                boolean boolValue = Boolean.parseBoolean(criteria.getValue());
                return builder.equal(path, boolValue);
            }
            // Xử lý các operation tương tự như trước
            switch (criteria.getOperation()) {
                case ">":
                    return builder.greaterThan(join.get(keys[1]), criteria.getValue());
                case ">=":
                    return builder.greaterThanOrEqualTo(join.get(keys[1]), criteria.getValue());
                case "<":
                    return builder.lessThan(join.get(keys[1]), criteria.getValue());
                case "<=":
                    return builder.lessThanOrEqualTo(join.get(keys[1]), criteria.getValue());
                case ":":
                    if (join.get(keys[1]).getJavaType() == String.class) {
                        return builder.like(join.get(keys[1]), "%" + criteria.getValue() + "%");
                    } else {
                        return builder.equal(join.get(keys[1]), criteria.getValue());
                    }
                case "=":
                    return builder.equal(join.get(keys[1]), criteria.getValue());
                default:
                    return null;
            }
        } else {
            // Xử lý các trường hợp không có join (code cũ của bạn)
            Path<?> path = root.get(criteria.getKey());
            Class<?> type = path.getJavaType();

            // Xử lý đặc biệt cho Boolean
            if (type == Boolean.class || type == boolean.class) {
                boolean boolValue = Boolean.parseBoolean(criteria.getValue());
                return builder.equal(path, boolValue);
            }
            switch (criteria.getOperation()) {
                case ">":
                    return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue());
                case ">=":
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue());
                case "<":
                    return builder.lessThan(root.get(criteria.getKey()), criteria.getValue());
                case "<=":
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue());
                case ":":
                    if (root.get(criteria.getKey()).getJavaType() == String.class) {
                        return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
                    } else {
                        return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                    }
                case "=":
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                default:
                    return null;
            }
        }
    }
}