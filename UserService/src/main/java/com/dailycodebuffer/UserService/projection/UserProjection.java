package com.dailycodebuffer.UserService.projection;

import com.dailycodebuffer.CommonService.model.CardDetails;
import com.dailycodebuffer.CommonService.model.User;
import com.dailycodebuffer.CommonService.queries.GetUserPaymentDetailQuery;
import org.springframework.stereotype.Component;

@Component
public class UserProjection {
    public User getUserPaymentDetails(GetUserPaymentDetailQuery query) {
        CardDetails cardDetails = CardDetails.builder()
                .name("CMCMCM JJJ")
                .validUntilYear(2030)
                .validUntilMonth(10)
                .cardNumber("1234123412341234")
                .cvv(123)
                .build();
        return User.builder()
                .userId(query.getUserId())
                .firstName("CMCMCM")
                .lastName("JJJ")
                .cardDetails(cardDetails)
                .build();
    }
}
