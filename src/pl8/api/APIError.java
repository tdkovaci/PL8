package pl8.api;

public class APIError {
	public enum APIErrorCode {
		UsernameAlreadyTaken,
		InvalidUsername,
		InvalidPassword,
		InvalidEmail,
		UnhandledException,
		TooManyResultsFound,
		CombinationNotFound,
		InvalidName,
		InvalidIngredient
	}
	
	public APIErrorCode code;
	public String message;
	
	public APIError(APIErrorCode c, String message) {
		code = c;
		this.message = message;
	}
}
