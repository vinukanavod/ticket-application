package io.bootify.ticket_app.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import io.bootify.ticket_app.service.VendorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the id value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = VendorSystemConfigIdUnique.VendorSystemConfigIdUniqueValidator.class
)
public @interface VendorSystemConfigIdUnique {

    String message() default "{Exists.vendor.systemConfigId}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class VendorSystemConfigIdUniqueValidator implements ConstraintValidator<VendorSystemConfigIdUnique, Long> {

        private final VendorService vendorService;
        private final HttpServletRequest request;

        public VendorSystemConfigIdUniqueValidator(final VendorService vendorService,
                final HttpServletRequest request) {
            this.vendorService = vendorService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Long value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(vendorService.get(Long.parseLong(currentId)).getSystemConfigId())) {
                // value hasn't changed
                return true;
            }
            return !vendorService.systemConfigIdExists(value);
        }

    }

}
