package io.bootify.ticket_app.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import io.bootify.ticket_app.service.TicketService;
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
 * Validate that the number value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = TicketNumberUnique.TicketNumberUniqueValidator.class
)
public @interface TicketNumberUnique {

    String message() default "{Exists.ticket.number}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class TicketNumberUniqueValidator implements ConstraintValidator<TicketNumberUnique, Integer> {

        private final TicketService ticketService;
        private final HttpServletRequest request;

        public TicketNumberUniqueValidator(final TicketService ticketService,
                final HttpServletRequest request) {
            this.ticketService = ticketService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Integer value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(ticketService.get(Long.parseLong(currentId)).getNumber())) {
                // value hasn't changed
                return true;
            }
            return !ticketService.numberExists(value);
        }

    }

}
