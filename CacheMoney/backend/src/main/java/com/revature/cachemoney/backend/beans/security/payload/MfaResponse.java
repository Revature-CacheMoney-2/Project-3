package com.revature.cachemoney.backend.beans.security.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that represent a response for postUser endpoint.
 *
 * @author Phillip Vo, Josue Rodriguez, Prakash, Maikel Vera
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MfaResponse {
    private boolean mfa;
    private String secretImageUri;
}
