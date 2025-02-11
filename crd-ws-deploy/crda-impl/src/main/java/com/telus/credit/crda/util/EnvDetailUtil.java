package com.telus.credit.crda.util;

public class EnvDetailUtil {
	private static EnvDetailUtil single_instance = null;
	private String m_envId;

	public static EnvDetailUtil getInstance() {
		if (single_instance == null)
			single_instance = new EnvDetailUtil();
		return single_instance;
	}

	public void setEnvId(String mEnvId) {
		m_envId = mEnvId;
	}

	public String getEnvId() {
		return m_envId;
	}

}
