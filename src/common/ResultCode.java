package common;

public final class ResultCode {
	public static final int ok = 0;
	
	//Errors
	public static final int wrongCredentials = -1;
	public static final int wrongUsernameFormat = -2;
	public static final int wrongPasswordFormat = -3;
	public static final int wrongUserNameAndPasswordFormat = -4;
	public static final int userNameAlreadyTaken = -5;
    public static final int serverDown = -6; //Krav 01
}
