package top.andnux.chain.eos.client.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import top.andnux.chain.eos.api.result.error.ApiError;

/**
 * 
 * @author wangyan
 *
 */
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private ApiError error;

	public ApiError getError() {
		return error;
	}

	public void setError(ApiError error) {
		this.error = error;
	}

	public ApiException(ApiError apiError) {
		this.error = apiError;
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		if (error != null) {
			try {
				return new ObjectMapper().writeValueAsString(error);
			} catch (JsonProcessingException e) {
				return error.getMessage();
			}
		}
		return super.getMessage();
	}
}
