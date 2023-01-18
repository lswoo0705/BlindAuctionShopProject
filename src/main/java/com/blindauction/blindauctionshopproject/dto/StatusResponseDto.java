package com.blindauction.blindauctionshopproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusResponseDto {
    private final int statusCode; // 예시 200

    private final String message; // 예시 회원가입완료

    //	/**
    //	 * Create a {@code ResponseEntity} with a body, headers, and a status code.
    //	 * @param 매개변수
    //	           body the entity body
    //	 * @param headers the entity headers
    //	 * @param status the status code
    //	 */
    //	public ResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
    //		this(body, headers, (Object) status);
    // i.e ResponseEntity = HttpEntity + StatusCode


}
