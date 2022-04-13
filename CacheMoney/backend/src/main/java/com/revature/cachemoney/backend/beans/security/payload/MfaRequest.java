package com.revature.cachemoney.backend.beans.security.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that represent a request for verifyCode endpoint.
 *
 * @author Phillip Vo, Josue Rodriguez, Prakash, Maikel Vera
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MfaRequest {
    private Integer userId;
    private String code;
}
