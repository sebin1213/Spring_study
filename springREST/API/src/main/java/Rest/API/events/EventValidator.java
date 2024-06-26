package Rest.API.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            // 필드에러
            errors.rejectValue("basePrice","wrongPrices", "Values fo prices are wrong"); // 에러 발생
            errors.rejectValue("maxPrice","wrongPrices", "Values fo prices are wrong"); // 에러 발생
            // 글로벌 에러
            errors.reject("wrongPrices", "Values fo prices are wrong"); // 에러 발생
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong"); // 에러발생
        }

        // TODO BeginEventDateTime
        // TODO CloseEnrollmentDateTime
    }

}
