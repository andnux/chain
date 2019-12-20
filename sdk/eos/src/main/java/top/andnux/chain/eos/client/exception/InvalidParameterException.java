package top.andnux.chain.eos.client.exception;

public class InvalidParameterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidParameterException(String msg) {
		super(msg);
	}
}
