package com.cameronchristiansen.headstoneindexer;

public class HIResult {
	private String id;
	private Long duration;
	private String originalPath;
	private String binarizedNormalPath;
	private String binarizedInvertedPath;
	
	public HIResult(String id){
		this.id = id;
	}
	
	/**
	 * @return the duration
	 */
	public Long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	/**
	 * @return the binarizedNormalPath
	 */
	public String getBinarizedNormalPath() {
		return binarizedNormalPath;
	}
	/**
	 * @param binarizedNormalPath the binarizedNormalPath to set
	 */
	public void setBinarizedNormalPath(String binarizedNormalPath) {
		this.binarizedNormalPath = binarizedNormalPath;
	}
	/**
	 * @return the binarizedInvertedPath
	 */
	public String getBinarizedInvertedPath() {
		return binarizedInvertedPath;
	}
	/**
	 * @param binarizedInvertedPath the binarizedInvertedPath to set
	 */
	public void setBinarizedInvertedPath(String binarizedInvertedPath) {
		this.binarizedInvertedPath = binarizedInvertedPath;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the originalPath
	 */
	public String getOriginalPath() {
		return originalPath;
	}

	/**
	 * @param originalPath the originalPath to set
	 */
	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}
}
