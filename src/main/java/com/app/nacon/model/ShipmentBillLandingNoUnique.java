package com.app.nacon.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import com.app.nacon.service.ShipmentService;
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
 * Validate that the billLandingNo value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = ShipmentBillLandingNoUnique.ShipmentBillLandingNoUniqueValidator.class
)
public @interface ShipmentBillLandingNoUnique {

    String message() default "{Exists.shipment.billLandingNo}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ShipmentBillLandingNoUniqueValidator implements ConstraintValidator<ShipmentBillLandingNoUnique, String> {

        private final ShipmentService shipmentService;
        private final HttpServletRequest request;

        public ShipmentBillLandingNoUniqueValidator(final ShipmentService shipmentService,
                final HttpServletRequest request) {
            this.shipmentService = shipmentService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(shipmentService.get(Long.parseLong(currentId)).getBillLandingNo())) {
                // value hasn't changed
                return true;
            }
            return !shipmentService.billLandingNoExists(value);
        }

    }

}
