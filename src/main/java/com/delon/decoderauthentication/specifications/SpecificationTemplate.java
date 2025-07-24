package com.delon.decoderauthentication.specifications;

import com.delon.decoderauthentication.entities.UserCourseEntity;
import com.delon.decoderauthentication.entities.UserEntity;
import jakarta.persistence.criteria.Join;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class SpecificationTemplate {

    public static Specification<UserEntity> userCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<UserEntity, UserCourseEntity> joined = root.join("userCourses");
            return cb.equal(joined.get("courseId"), courseId);
        };
    }

    @And({@Spec(path = "userType", spec = Equal.class),
          @Spec(path = "userStatus", spec = Equal.class),
          @Spec(path = "email", spec = Like.class),
          @Spec(path = "fullName", spec = Like.class)})
    public interface UserSpec extends Specification<UserEntity> {
    }
}
