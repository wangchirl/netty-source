package com.shadow.netty.chapter09.demo03;

/**
 * @author shadow
 * @create 2020-10-07
 * @description
 *
 */
public class MessageProtocol {
	private int length;

	private byte[] content;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
